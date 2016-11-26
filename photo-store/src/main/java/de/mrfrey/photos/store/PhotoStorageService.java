package de.mrfrey.photos.store;

import com.mongodb.gridfs.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class PhotoStorageService {
    private final GridFsOperations gridfs;
    private final PhotoRepository photoRepository;
    private final MessageChannel newPhotoChannel;

    @Autowired
    public PhotoStorageService(GridFsOperations gridfs, PhotoRepository photoRepository, Processor processor) {
        this.gridfs = gridfs;
        this.photoRepository = photoRepository;
        this.newPhotoChannel = processor.output();
    }

    public Photo storePhoto(MultipartFile file) {
        try {
            GridFSFile storedFile = gridfs.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            Photo photo = new Photo();
            photo.setFileName(storedFile.getFilename());
            photo.setFileId((ObjectId) storedFile.getId());
            photo = photoRepository.insert(photo);
            sendNewPhotoNotification(photo);
            return photo;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void addMetadata(String photoId, Map metadata) {
        Photo photo = photoRepository.findOne(new ObjectId(photoId));
        if (photo != null) {
            photo.setMetadata(metadata);
            photoRepository.save(photo);
        }
    }

    private void sendNewPhotoNotification(Photo photo) {
        Message<String> message = MessageBuilder.withPayload(photo.getId().toString()).build();
        newPhotoChannel.send(message);
    }
}
