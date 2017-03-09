package de.mrfrey.photos.store.web;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.mrfrey.photos.store.collection.PhotoCollection;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

public class PhotoCollectionResource extends Resource<PhotoCollection> {
    @JsonUnwrapped
    private Resources photos;
    public PhotoCollectionResource(PhotoCollection content, Link... links) {
        super(content, links);
    }

    public Resources getPhotos() {
        return photos;
    }

    public void setPhotos(Resources photos) {
        this.photos = photos;
    }
}
