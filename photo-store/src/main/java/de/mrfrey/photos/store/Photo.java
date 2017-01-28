package de.mrfrey.photos.store;

import org.bson.types.ObjectId;
import org.springframework.hateoas.core.Relation;

import java.util.Map;

@Relation(value = "photo", collectionRelation = "photos")
public class Photo {
    private ObjectId id;
    private ObjectId fileId;
    private ObjectId scaledFileId;
    private ObjectId thumbnailFileId;
    private String fileName;
    private String caption;
    private String contentType;
    private Map metadata;
    private String owner;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getFileId() {
        return fileId;
    }

    public void setFileId(ObjectId fileId) {
        this.fileId = fileId;
    }

    public ObjectId getScaledFileId() {
        return scaledFileId;
    }

    public void setScaledFileId(ObjectId scaledFileId) {
        this.scaledFileId = scaledFileId;
    }

    public ObjectId getThumbnailFileId() {
        return thumbnailFileId;
    }

    public void setThumbnailFileId(ObjectId thumbnailFileId) {
        this.thumbnailFileId = thumbnailFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map getMetadata() {
        return metadata;
    }

    public void setMetadata(Map metadata) {
        this.metadata = metadata;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public static enum Size {
        original,
        scaled,
        thumbnail
    }
}
