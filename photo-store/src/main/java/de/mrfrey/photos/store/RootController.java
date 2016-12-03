package de.mrfrey.photos.store;

import de.escalon.hypermedia.affordance.Affordance;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static de.escalon.hypermedia.spring.AffordanceBuilder.linkTo;
import static de.escalon.hypermedia.spring.AffordanceBuilder.methodOn;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

@RequestMapping(value = "/", produces = HAL_JSON_VALUE)
@RestController
public class RootController {

    @GetMapping
    public ResourceSupport root() {
        Affordance self = linkTo(RootController.class).withSelfRel();
        Affordance photos = linkTo(methodOn(PhotoController.class).allPhotos()).withRel("photos");
        Affordance photo = linkTo(methodOn(PhotoController.class).get(null)).withRel("photo");
        Affordance uploadPhoto = linkTo(PhotoController.class).withRel("photo:upload");

        Affordance original = linkTo(methodOn(ImageController.class).getImage(null, Photo.Size.original)).withRel("image:original");
        Affordance scaled = linkTo(methodOn(ImageController.class).getImage(null, Photo.Size.scaled)).withRel("image:scaled");
        Affordance image = linkTo(methodOn(ImageController.class).getImage(null, null)).withRel("image");
        Affordance uploadImage = linkTo(methodOn(ImageController.class).uploadScaledImage(null, null)).withRel("image:upload:scaled");

        ResourceSupport resource = new ResourceSupport();
        resource.add(self, photos, photo, uploadPhoto, original, scaled, image, uploadImage);
        return resource;
    }
}
