package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.Photo;
import org.bson.types.ObjectId;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static de.escalon.hypermedia.spring.AffordanceBuilder.linkTo;
import static de.escalon.hypermedia.spring.AffordanceBuilder.methodOn;

public class PhotoResourceAssembler extends ResourceAssemblerSupport<Photo, PhotoResource> {

    public PhotoResourceAssembler() {
        super(PhotoController.class, PhotoResource.class);
    }

    @Override
    public PhotoResource toResource(Photo photo) {
        ObjectId id = photo.getId();
        Link self = linkTo(methodOn(PhotoController.class).get(id)).withSelfRel();
        Link original = linkTo(methodOn(ImageController.class).getImage(id, Photo.Size.original)).withRel("image:original");
        Link scaled = linkTo(methodOn(ImageController.class).getImage(id, Photo.Size.scaled)).withRel("image:scaled");
        Link thumb = linkTo(methodOn(ImageController.class).getImage(id, Photo.Size.thumbnail)).withRel("image:thumbnail");
        return new PhotoResource(photo, self, original, scaled, thumb);
    }

    @Override
    protected PhotoResource instantiateResource(Photo photo) {
        return new PhotoResource(photo);
    }
}
