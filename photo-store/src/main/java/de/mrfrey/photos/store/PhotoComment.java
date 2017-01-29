package de.mrfrey.photos.store;

import org.bson.types.ObjectId;

public class PhotoComment {
    private String user;
    private ObjectId photoId;
    private String comment;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ObjectId getPhotoId() {
        return photoId;
    }

    public void setPhotoId(ObjectId photoId) {
        this.photoId = photoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "PhotoComment{" +
                "user='" + user + '\'' +
                ", photoId=" + photoId +
                ", comment='" + comment + '\'' +
                '}';
    }
}
