package de.mrfrey.photos.store.collection;

import de.mrfrey.photos.store.GpsLocation;
import org.bson.types.ObjectId;
import org.springframework.hateoas.core.Relation;

@Relation(value = "photoCollection", collectionRelation = "photoCollections")
public class PhotoCollection {
    private ObjectId id;
    private String title;
    private String description;
    private String owner;
    private GpsLocation gpsLocation;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public GpsLocation getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(GpsLocation gpsLocation) {
        this.gpsLocation = gpsLocation;
    }
}
