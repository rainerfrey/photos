package de.mrfrey.photos.store;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.Relation;

public class PhotoResource extends Resource<Photo> {
    public PhotoResource(Photo content, Link... links) {
        super(content, links);
    }
}
