package de.mrfrey.photos.store.notify;

import de.mrfrey.photos.store.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FrontendNotifier {
    private static final Logger logger = LoggerFactory.getLogger(FrontendNotifier.class);
    private final SimpMessageSendingOperations messaging;

    public FrontendNotifier(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    public void photoUpdated(Photo photo, String message) {
        Map<String, String> update = new HashMap<>();
        update.put("photoId", photo.getId().toString());
        update.put("message", message);
        logger.info("Sending photo update to {}: {}", photo.getOwner(), update);
        messaging.convertAndSendToUser(photo.getOwner(), "/exchange/amq.direct/photo.update", update);
    }

    public void newPhoto(Photo photo) {
        Map<String, String> message = new HashMap<>();
        message.put("photoId", photo.getId().toString());
        message.put("caption", photo.getCaption());
        message.put("fileName", photo.getFileName());
        message.put("thumbnailUrl", String.format("http://localhost:8080/photos/%s/image/thumbnail", photo.getId()));
        message.put("scaledUrl", String.format("http://localhost:8080/photos/%s/image/scaled", photo.getId()));
        message.put("owner", photo.getOwner());
        message.put("message", String.format("%s has added the photo %s", photo.getOwner(), photo.getFileName()));
        messaging.convertAndSend("/topic/photos", message);
    }
}
