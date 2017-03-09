package de.mrfrey.photos.store.photo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class PhotoRepositoryImpl implements PhotoRepositoryCustom {
    private final MongoOperations template;

    public PhotoRepositoryImpl(MongoOperations template) {
        this.template = template;
    }

    @Override
    public void addComment(ObjectId id, Photo.Comment comment) {
        Update update = new Update().push("comments", comment);
        template.updateFirst(query(where("id").is(id)), update, Photo.class);
    }
}
