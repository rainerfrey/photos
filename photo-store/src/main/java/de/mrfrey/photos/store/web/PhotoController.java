package de.mrfrey.photos.store.web;

import de.mrfrey.photos.store.Photo;
import de.mrfrey.photos.store.PhotoStorageService;
import org.bson.types.ObjectId;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoStorageService photoStorageService;

    public PhotoController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @GetMapping
    @ResponseBody
    public Resources allPhotos() {
        List<Photo> photos = photoStorageService.getPhotos();
        if (photos.isEmpty()) {
            List<Object> values = Collections.singletonList(new EmbeddedWrappers(false).emptyCollectionOf(Photo.class));
            return new Resources(values);
        }
        return new Resources<>(new PhotoResourceAssembler().toResources(photos));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Resource<Photo> get(@PathVariable("id") ObjectId id) {
        Photo photo = photoStorageService.getPhoto(id);
        return new PhotoResourceAssembler().toResource(photo);
    }

    @PostMapping
    @ResponseBody
    public Map<String, String> upload(@RequestParam("image-file") MultipartFile imageFile, @RequestParam("title") String title, @RequestParam("caption") String caption, Principal user) {
        Photo photo = photoStorageService.storePhoto(imageFile, title, caption, user != null ? user.getName() : "anonymous");
        return Collections.singletonMap("photoId", photo.getId().toString());
    }

    @SubscribeMapping("/count")
    public Map myPhotoCount(Principal user) {
        return Collections.singletonMap("count", photoStorageService.countForUser(user.getName()));
    }
}
