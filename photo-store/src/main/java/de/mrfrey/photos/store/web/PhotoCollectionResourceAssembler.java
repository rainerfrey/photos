package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.photo.Photo;
import de.mrfrey.photos.store.photo.PhotoId;
import de.mrfrey.photos.store.photo.PhotoStorageService;
import de.mrfrey.photos.store.collection.PhotoCollection;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PhotoCollectionResourceAssembler extends ResourceAssemblerSupport<PhotoCollection, PhotoCollectionResource> {
    private final PhotoStorageService photoStorageService;
    private final PhotoResourceAssembler photoResourceAssembler;

    public PhotoCollectionResourceAssembler(PhotoStorageService photoStorageService) {
        super(PhotoCollectionController.class, PhotoCollectionResource.class);
        this.photoStorageService = photoStorageService;
        this.photoResourceAssembler = new PhotoResourceAssembler();
    }

    @Override
    public PhotoCollectionResource toResource(PhotoCollection photoCollection) {
        PhotoCollectionResource resource = new PhotoCollectionResource(photoCollection);
        Iterable<Photo> photosForCollection = photoStorageService.getPhotosForCollection(photoCollection.getId());
        List<PhotoResource> photoResources = photoResourceAssembler.toResources(photosForCollection);
        EmbeddedWrapper photos = new EmbeddedWrappers(true).wrap(photoResources, "photos");
        Resources resources = new Resources(Arrays.asList(photos));
        resource.setPhotos(resources);
        return resource;
    }
}
