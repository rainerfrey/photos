package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.photo.Photo;
import org.bson.types.ObjectId;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static de.mrfrey.photos.store.web.Rels.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PhotoResourceAssembler extends ResourceAssemblerSupport<Photo, PhotoResource> {

    public PhotoResourceAssembler() {
        super(PhotoController.class, PhotoResource.class);
    }

    @Override
    public PhotoResource toResource(Photo photo) {
        ObjectId id = photo.getId();
        Link self = linkTo(methodOn(PhotoController.class).get(id)).withSelfRel();
        Link original = linkTo(methodOn(ImageController.class).getImage(id, Photo.Size.original)).withRel(IMAGE_ORIGINAL);
        Link scaled = linkTo(methodOn(ImageController.class).getImage(id, Photo.Size.scaled)).withRel(IMAGE_SCALED);
        Link thumb = linkTo(methodOn(ImageController.class).getImage(id, Photo.Size.thumbnail)).withRel(IMAGE_THUMBNAIL);
        return new PhotoResource(photo, self, original, scaled, thumb);
    }

    @Override
    protected PhotoResource instantiateResource(Photo photo) {
        return new PhotoResource(photo);
    }
}
