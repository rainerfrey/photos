package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.collection.PhotoCollection;
import de.mrfrey.photos.store.collection.PhotoCollectionRepository;
import de.mrfrey.photos.store.photo.Photo;
import de.mrfrey.photos.store.photo.PhotoStorageService;
import org.bson.types.ObjectId;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/photo-collections")
public class PhotoCollectionController {
    private final PhotoStorageService photoStorageService;
    private final PhotoCollectionRepository photoCollectionRepository;

    public PhotoCollectionController(PhotoStorageService photoStorageService, PhotoCollectionRepository photoCollectionRepository) {
        this.photoStorageService = photoStorageService;
        this.photoCollectionRepository = photoCollectionRepository;
    }

    @GetMapping
    public Resources list() {
        List<PhotoCollection> collections = photoCollectionRepository.findAll();
        if (collections.isEmpty()) {
            List<Object> values = Collections.singletonList(new EmbeddedWrappers(false).emptyCollectionOf(PhotoCollection.class));
            return new Resources(values);
        }
        return new Resources<>(new PhotoCollectionResourceAssembler(photoStorageService).toResources(collections));

    }
    @GetMapping("/{id}")
    public PhotoCollectionResource get(@PathVariable("id") ObjectId id) {
        Optional<PhotoCollection> photoCollection = photoCollectionRepository.findById( id);
        if (photoCollection.isPresent()) {
            return new PhotoCollectionResourceAssembler(photoStorageService).toResource(photoCollection.get());
        } else
            throw new CollectionNotFound(id);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public PhotoCollectionResource create(@RequestBody PhotoCollection photoCollection, Principal user) {
        photoCollection.setOwner(user.getName());
        PhotoCollection created = photoCollectionRepository.insert(photoCollection);
        return new PhotoCollectionResourceAssembler(photoStorageService).toResource(created);
    }

    @PatchMapping("/{id}")
    public PhotoCollectionResource update(@PathVariable("id") ObjectId id, @RequestBody PhotoCollection update) {
         return new PhotoCollectionResourceAssembler(photoStorageService).toResource(photoCollectionRepository.save(update));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class CollectionNotFound extends RuntimeException {
        public CollectionNotFound(ObjectId id) {
            super(String.format("Photo collection %s not found", id));
        }
    }
}
