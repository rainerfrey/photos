package de.mrfrey.photos.store.photo;

import org.bson.types.ObjectId;

public interface PhotoRepositoryCustom {
    void addComment(ObjectId id, Photo.Comment comment);
}
