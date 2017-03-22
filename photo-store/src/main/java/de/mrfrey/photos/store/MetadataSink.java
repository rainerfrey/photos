package de.mrfrey.photos.store;

import de.mrfrey.photos.store.photo.PhotoStorageService;
import org.slf4j.Logger;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MetadataSink {
    private final Logger logger;
    private final PhotoStorageService photoStorageService;

    public MetadataSink(PhotoStorageService photoStorageService, Logger logger) {
        this.photoStorageService = photoStorageService;
        this.logger = logger;
    }

    @StreamListener("input")
    public void receiveMetadata(@Header("photo-id") String id, @Payload Map metadata) {
        logger.info("Metadata received for {}: {}", id, metadata);
        photoStorageService.addMetadata(id, metadata);
    }
}
