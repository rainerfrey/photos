package de.mrfrey.photos.store;

import de.mrfrey.photos.store.photo.PhotoStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MetadataSink {
    private static final Logger logger = LoggerFactory.getLogger(MetadataSink.class);
    private final PhotoStorageService photoStorageService;

    public MetadataSink(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @StreamListener("input")
    public void receiveMetadata(@Header("photo-id") String id, @Payload Map metadata) {
        logger.info("Metadata received for {}: {}", id, metadata);
        photoStorageService.addMetadata(id, metadata);
    }
}
