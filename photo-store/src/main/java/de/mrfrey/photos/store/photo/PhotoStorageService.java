package de.mrfrey.photos.store.photo;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import de.mrfrey.photos.store.notify.BackendNotifier;
import de.mrfrey.photos.store.notify.FrontendNotifier;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class PhotoStorageService {
    private static final Logger logger = LoggerFactory.getLogger(PhotoStorageService.class);

    private final GridFsOperations gridfs;
    private final PhotoRepository photoRepository;
    private final BackendNotifier backendNotifier;
    private final FrontendNotifier frontendNotifier;

    @Autowired
    public PhotoStorageService(GridFsOperations gridfs, PhotoRepository photoRepository, BackendNotifier backendNotifier, FrontendNotifier frontendNotifier) {
        this.gridfs = gridfs;
        this.photoRepository = photoRepository;
        this.backendNotifier = backendNotifier;
        this.frontendNotifier = frontendNotifier;
    }

    public Photo storePhoto(MultipartFile file, String title, String caption, String username, ObjectId collectionId) {
        try {
            GridFSFile storedFile = gridfs.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            Photo photo = new Photo();
            photo.setFileName(storedFile.getFilename());
            photo.setFileId((ObjectId) storedFile.getId());
            photo.setContentType(file.getContentType());
            photo.setOwner(username);
            photo.setTitle(StringUtils.trimToNull(title));
            photo.setCaption(StringUtils.trimToNull(caption));
            photo.setCollectionId(collectionId);
            photo = photoRepository.insert(photo);
            backendNotifier.newPhoto(photo);
            logger.info("New photo {} uploaded, with title {}, saved as {}", photo.getFileName(), photo.getTitle(), photo.getId());
//            frontendNotifier.newPhoto(photo);
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
            frontendNotifier.photoUpdated(photo, "metadata added");
            photoCompleted(photo);
        }
    }

    public void addComment(ObjectId photoId, String user, String comment) {
        photoRepository.addComment(photoId, new Photo.Comment(user, comment));
    }

    public GridFsResource getImageResource(ObjectId photoId, Photo.Size size) {
        Photo photo = photoRepository.findOne(photoId);
        if (photo == null)
            return null;
        GridFSDBFile file = gridfs.findOne(Query.query(Criteria.where("_id").is(getImageId(photo, size))));
        if (file != null)
            return new GridFsResource(file);
        return null;
    }

    private ObjectId getImageId(Photo photo, Photo.Size size) {
        switch (size) {
            case original:
                return photo.getFileId();
            case scaled:
                return photo.getScaledFileId();
            case thumbnail:
                return photo.getThumbnailFileId();
            default:
                throw new IllegalArgumentException("Unsupported file size " + size);
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
            case original:
                photo.setFileId((ObjectId) scaled.getId());
                break;
            case scaled:
                photo.setScaledFileId((ObjectId) scaled.getId());
                break;
            case thumbnail:
                photo.setThumbnailFileId((ObjectId) scaled.getId());
                break;
        }
        photoRepository.save(photo);
        logger.info("Image of size {} received for {}", size, photoId);
        frontendNotifier.photoUpdated(photo, String.format("%s image added", size));
        photoCompleted(photo);
    }

    private String getAdditionalFileName(String originalFileName, Photo.Size size) {
        int dot = originalFileName.lastIndexOf('.');
        String basename = originalFileName.substring(0, dot);
        String extension = originalFileName.substring(dot + 1);
        String suffix = "";
        switch (size) {
            case scaled:
                suffix = "_s";
                break;
            case thumbnail:
                suffix = "_t";
                break;
        }
        return basename + suffix + "." + extension;
    }

    private boolean isPhotoComplete(Photo photo) {
        return photo.getFileId() != null && photo.getScaledFileId() != null && photo.getThumbnailFileId() != null && photo.getMetadata() != null;
    }

    private void photoCompleted(Photo photo) {
        if (isPhotoComplete(photo))
            frontendNotifier.newPhoto(photo);
    }

    public List<Photo> getPhotos() {
        return photoRepository.findAll();
    }

    public Iterable<Photo> getPhotosForCollection(ObjectId collectionId) {
        return photoRepository.findAllByCollectionId(collectionId);
    }

    public int countForUser(String name) {
        return photoRepository.countByOwner(name);
    }
}
