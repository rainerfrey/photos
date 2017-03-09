package de.mrfrey.photos.store.collection;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoCollectionRepository extends MongoRepository<PhotoCollection, ObjectId> {
}
