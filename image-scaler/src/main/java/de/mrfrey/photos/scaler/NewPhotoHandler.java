package de.mrfrey.photos.scaler;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.slf4j.Logger;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class NewPhotoHandler {
    private final RestTemplate imageService;
    private final Scaler scaler;
    private final Logger logger;
    private Traverson traverson;

    public NewPhotoHandler(RestTemplate imageService, Scaler scaler, Logger logger) {
        this.imageService = imageService;
        this.scaler = scaler;
        this.logger = logger;
    }


    @PostConstruct
    protected void init() {
        this.traverson = new Traverson(URI.create("http://photo-store/"), MediaType.APPLICATION_JSON, MediaTypes.HAL_JSON).setRestOperations(imageService);
    }

    @StreamListener(Processor.INPUT)
    public void handleNewPhoto(String photoId) throws IOException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ResponseEntity<ByteArrayResource> original = fetchOriginalImage(photoId);
        int orientation = 1;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(original.getBody().getInputStream());
            ExifIFD0Directory ifd0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            orientation = ifd0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (MetadataException e) {
            e.printStackTrace();
        }
        scale(photoId, original, "scaled", scaler::scaleImage, orientation);
        scale(photoId, original, "thumbnail", scaler::scaleThumbnail, orientation);
    }

    private void scale(String photoId, ResponseEntity<ByteArrayResource> original, String imageSize, BiFunction<InputStream, Integer, BufferedImage> scaling, int orientation) throws IOException {
        BufferedImage bufferedImage = scaling.apply(original.getBody().getInputStream(), orientation);
        String upload = traverson.follow("image:upload:" + imageSize).withHeaders(getHttpHeaders()).asTemplatedLink().expand(photoId).getHref();
        RequestEntity<BufferedImage> requestEntity = RequestEntity.post(URI.create(upload)).contentType(original.getHeaders().getContentType()).body(bufferedImage);
        ResponseEntity<Void> uploadResponse = imageService.exchange(requestEntity, Void.class);
        logger.info("Uploading scaled image: {}", uploadResponse);
    }

    private ResponseEntity<ByteArrayResource> fetchOriginalImage(String photoId) {
        String original = this.traverson.follow("image:original").withHeaders(getHttpHeaders()).asTemplatedLink().expand(photoId).getHref();
        return imageService.getForEntity(original, ByteArrayResource.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Host", "photo-store");
        return httpHeaders;
    }
}
