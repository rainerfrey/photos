package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.photo.Photo;
import de.mrfrey.photos.store.photo.PhotoStorageService;
import org.bson.types.ObjectId;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Controller
@RequestMapping( "/photos" )
public class PhotoController {
    private final PhotoStorageService photoStorageService;

    public PhotoController( PhotoStorageService photoStorageService ) {
        this.photoStorageService = photoStorageService;
    }

    @GetMapping
    @ResponseBody
    public Resources allPhotos() {
        List<Photo> photos = photoStorageService.getPhotos();
        if ( photos.isEmpty() ) {
            List<Object> values = Collections.singletonList( new EmbeddedWrappers( false ).emptyCollectionOf( Photo.class ) );
            return new Resources( values );
        }
        return new Resources<>( new PhotoResourceAssembler().toResources( photos ) );
    }

    @GetMapping( "/{id}" )
    @ResponseBody
    public Resource<Photo> get( @PathVariable( "id" ) ObjectId id ) {
        Photo photo = photoStorageService.getPhoto( id ).orElseThrow( () -> new PhotoNotFound( id ) );
        return new PhotoResourceAssembler().toResource( photo );
    }

    @PostMapping
    @ResponseBody
    public Map<String, String> upload(
        @RequestParam( "image-file" ) MultipartFile imageFile,
        @RequestParam( name = "title", required = false ) String title,
        @RequestParam( name = "caption", required = false ) String caption,
        @RequestParam( name = "collectionId", required = false ) ObjectId collectionId,
        Principal user ) {
        Photo photo = photoStorageService.storePhoto( imageFile, title, caption, user != null ? user.getName() : "anonymous", collectionId );
        return Collections.singletonMap( "photoId", photo.getId().toString() );
    }

    @MessageMapping( "/reprocess/{id}" )
    public void reprocessPhoto( @DestinationVariable( "id" ) ObjectId photoId, ReprocessRequest reprocessRequest, Principal user ) {
        photoStorageService.reprocessPhoto( photoId, reprocessRequest.getCommand(), reprocessRequest.getSize(), user.getName() );
    }


    @DeleteMapping( "/{id}" )
    @ResponseStatus( NO_CONTENT )
    public void delete( @PathVariable( "id" ) ObjectId id, Principal user ) {
        photoStorageService.deletePhoto( id, user.getName() );
    }

    @SubscribeMapping( "/count" )
    public Map myPhotoCount( Principal user ) {
        return Collections.singletonMap( "count", photoStorageService.countForUser( user.getName() ) );
    }

    @ResponseStatus( NOT_FOUND )
    public static class PhotoNotFound extends RuntimeException {
        public PhotoNotFound( ObjectId id ) {
            super( String.format( "Photo %s not found", id ) );
        }
    }
}
