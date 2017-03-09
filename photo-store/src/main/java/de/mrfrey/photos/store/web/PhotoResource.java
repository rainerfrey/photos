package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.photo.Photo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class PhotoResource extends Resource<Photo> {
    public PhotoResource(Photo content, Link... links) {
        super(content, links);
    }
}
