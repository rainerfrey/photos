package de.mrfrey.photos.store;

import org.bson.types.ObjectId;

public interface PhotoRepositoryCustom {
    void addComment(ObjectId id, Photo.Comment comment);
}
