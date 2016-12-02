package de.mrfrey.photos.scaler;

import org.slf4j.Logger;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

@Component
public class NewPhotoHandler {
    private final RestTemplate imageService;
    private final Scaler scaler;
    private final Logger logger;

    public NewPhotoHandler(RestTemplate imageService, Scaler scaler, Logger logger) {
        this.imageService = imageService;
        this.scaler = scaler;
        this.logger = logger;
    }


    @StreamListener(Processor.INPUT)
    public void handleNewPhoto(String photoId) throws IOException {
        ResponseEntity<InputStreamResource> entity = imageService.getForEntity("http://photo-store/photos/{photoId}/image", InputStreamResource.class, photoId);
        MediaType contentType = entity.getHeaders().getContentType();
        BufferedImage bufferedImage = scaler.scaleImage(entity.getBody().getInputStream(), contentType.getSubtype());
        URI url = fromHttpUrl("http://photo-store/photos/{photoId}/image/scaled").buildAndExpand(photoId).toUri();
        RequestEntity<BufferedImage> requestEntity = RequestEntity.post(url).contentType(contentType).body(bufferedImage);
        ResponseEntity<Void> uploadResponse = imageService.exchange(requestEntity, Void.class);
        logger.info("Uploading scaled image: {}", uploadResponse);
    }
}
