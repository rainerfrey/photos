package de.mrfrey.photos.store.photo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.hateoas.core.Relation;

import java.util.List;
import java.util.Map;

@Relation(value = "photo", collectionRelation = "photos")
public class Photo {
    private ObjectId id;
    private ObjectId fileId;
    private ObjectId scaledFileId;
    private ObjectId thumbnailFileId;
    private String fileName;
    private String title;
    private String caption;
    private String contentType;
    private Map metadata;
    private String owner;
    private List<Comment> comments;
    private ObjectId collectionId;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public ObjectId getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(ObjectId collectionId) {
        this.collectionId = collectionId;
    }

    public static enum Size {
        original,
        scaled,
        thumbnail
    }

    public static class Comment {
        private String user;
        private String comment;

        @PersistenceConstructor
        public Comment(String user, String comment) {
            this.user = user;
            this.comment = comment;
        }

        public String getUser() {
            return user;
        }

        public String getComment() {
            return comment;
        }
    }
}
