package de.mrfrey.photos.store.notify;

import de.mrfrey.photos.store.Photo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FrontendNotifier {
    private final SimpMessageSendingOperations messaging;

    public FrontendNotifier(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    public void photoUpdated(Photo photo, String message) {
        Map<String, String> update = new HashMap<>();
        update.put("photoId", photo.getId().toString());
        update.put("message", message);
        messaging.convertAndSendToUser(photo.getOwner(), "/exchange/amq.direct/photo.update", update);
    }

    public void newPhoto(Photo photo) {
        Map<String, String> message =  new HashMap<>();
        message.put("photoId", photo.getId().toString());
        message.put("caption", photo.getCaption());
        message.put("fileName", photo.getFileName());
        message.put("message", "photo added");
        messaging.convertAndSend("/topic/photos", message);
    }
}
