package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.Photo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping(value = "/", produces = HAL_JSON_VALUE)
@RestController
public class RootController {

    @GetMapping
    public ResourceSupport root() {
        Link self = linkTo(RootController.class).withSelfRel();
        Link photos = linkTo(methodOn(PhotoController.class).allPhotos()).withRel("photos");
        Link photo = linkTo(methodOn(PhotoController.class).get(null)).withRel("photo");
        Link uploadPhoto = linkTo(PhotoController.class).withRel("photo:upload");

        Link original = linkTo(methodOn(ImageController.class).getImage(null, Photo.Size.original)).withRel("image:original");
        Link scaled = linkTo(methodOn(ImageController.class).getImage(null, Photo.Size.scaled)).withRel("image:scaled");
        Link thumb = linkTo(methodOn(ImageController.class).getImage(null, Photo.Size.thumbnail)).withRel("image:thumbnail");
        Link image = linkTo(methodOn(ImageController.class).getImage(null, null)).withRel("image");
        Link uploadImage = linkTo(methodOn(ImageController.class).uploadScaledImage(null, null, Photo.Size.scaled)).withRel("image:upload:scaled");
        Link uploadThumb = linkTo(methodOn(ImageController.class).uploadScaledImage(null, null, Photo.Size.thumbnail)).withRel("image:upload:thumbnail");

        ResourceSupport resource = new ResourceSupport();
        resource.add(self, photos, photo, uploadPhoto, original, scaled, thumb, image, uploadImage, uploadThumb);
        return resource;
    }

    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
