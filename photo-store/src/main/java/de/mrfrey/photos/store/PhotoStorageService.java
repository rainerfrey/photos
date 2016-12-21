package de.mrfrey.photos.store;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
            photo.setContentType(file.getContentType());
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

    public GridFsResource getImageResource(ObjectId photoId, Photo.Size size) {
        Photo photo = photoRepository.findOne(photoId);
        GridFSDBFile file = gridfs.findOne(Query.query(Criteria.where("_id").is(getImageId(photo, size))));
        if (file != null)
            return new GridFsResource(file);
        return null;
    }

    private ObjectId getImageId(Photo photo, Photo.Size size) {
        switch (size) {
            case original: return photo.getFileId();
            case scaled: return photo.getScaledFileId();
            case thumbnail: return photo.getThumbnailFileId();
            default: throw new IllegalArgumentException("Unsupported file size " + size);
        }
    }

    public Photo getPhoto(ObjectId photoId) {
        return photoRepository.findOne(photoId);
    }

    public void addAdditionalImage(ObjectId photoId, InputStream image, Photo.Size size) {
        Photo photo = photoRepository.findOne(photoId);
        String scaledFileName = getAdditionalFileName(photo.getFileName(), size);
        GridFSFile scaled = gridfs.store(image, scaledFileName, photo.getContentType());
        switch (size) {
            case original: photo.setFileId((ObjectId) scaled.getId());break;
            case scaled: photo.setScaledFileId((ObjectId) scaled.getId());break;
            case thumbnail: photo.setThumbnailFileId((ObjectId) scaled.getId());break;
        }
        photoRepository.save(photo);
    }

    private String getAdditionalFileName(String originalFileName, Photo.Size size) {
        int dot = originalFileName.lastIndexOf('.');
        String basename = originalFileName.substring(0, dot);
        String extension = originalFileName.substring(dot + 1);
        String suffix = "";
        switch (size) {
            case scaled: suffix = "_s";break;
            case thumbnail: suffix = "_t";break;
        }
        return basename + suffix + "." + extension;
    }

    private void sendNewPhotoNotification(Photo photo) {
        Message<String> message = MessageBuilder.withPayload(photo.getId().toString()).build();
        newPhotoChannel.send(message);
    }

    public List<Photo> getPhotos() {
        return photoRepository.findAll();
    }
}
