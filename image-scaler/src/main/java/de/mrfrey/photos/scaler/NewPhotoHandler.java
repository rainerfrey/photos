package de.mrfrey.photos.scaler;

import org.slf4j.Logger;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
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
        scale(photoId, original, "scaled", scaler::scaleImage);
        scale(photoId, original, "thumbnail", scaler::scaleThumbnail);
    }

    private void scale(String photoId, ResponseEntity<ByteArrayResource> original, String imageSize, Function<InputStream, BufferedImage> scaling) throws IOException {
        BufferedImage bufferedImage = scaling.apply(original.getBody().getInputStream());
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
