package de.mrfrey.photos.store.photo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends PhotoRepositoryCustom, MongoRepository<Photo, ObjectId> {
    int countByOwner(String owner);

    Iterable<Photo> findAllByCollectionId(ObjectId collectionId);
}
