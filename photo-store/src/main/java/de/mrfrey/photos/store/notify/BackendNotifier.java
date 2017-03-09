package de.mrfrey.photos.store.notify;

import de.mrfrey.photos.store.photo.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class BackendNotifier {
    private final MessageChannel newPhotoChannel;

    @Autowired
    public BackendNotifier(Processor processor) {
        this.newPhotoChannel = processor.output();
    }

    public void newPhoto(Photo photo) {
        Message<String> message = MessageBuilder.withPayload(photo.getId().toString()).build();
        newPhotoChannel.send(message);
    }

}
