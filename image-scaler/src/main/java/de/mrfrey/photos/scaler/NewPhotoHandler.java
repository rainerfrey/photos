package de.mrfrey.photos.scaler;

import org.slf4j.Logger;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
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
import java.net.URI;

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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Forwarded-Host", "photo-store");
        String original = this.traverson.follow("image:original").withHeaders(httpHeaders).asTemplatedLink().expand(photoId).getHref();
        ResponseEntity<InputStreamResource> entity = imageService.getForEntity(original, InputStreamResource.class);
        MediaType contentType = entity.getHeaders().getContentType();
        BufferedImage bufferedImage = scaler.scaleImage(entity.getBody().getInputStream(), contentType.getSubtype());
        String upload = traverson.follow("image:upload:scaled").withHeaders(httpHeaders).asTemplatedLink().expand(photoId).getHref();
        RequestEntity<BufferedImage> requestEntity = RequestEntity.post(URI.create(upload)).contentType(contentType).body(bufferedImage);
        ResponseEntity<Void> uploadResponse = imageService.exchange(requestEntity, Void.class);
        logger.info("Uploading scaled image: {}", uploadResponse);
    }
}
