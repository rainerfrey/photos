package de.mrfrey.photos.store;

import org.bson.types.ObjectId;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.Relation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoStorageService photoStorageService;

    public PhotoController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @GetMapping
    @ResponseBody
    public Resources<PhotoResource> allPhotos() {
        List<Photo> photos = photoStorageService.getPhotos();
        return new Resources<>(new PhotoResourceAssembler().toResources(photos));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Resource<Photo> get(@PathVariable("id") ObjectId id) {
        Photo photo = photoStorageService.getPhoto(id);
        return new PhotoResourceAssembler().toResource(photo);
    }

    @PostMapping
    public String upload(@RequestPart("image-file") MultipartFile imageFile) {
        photoStorageService.storePhoto(imageFile);
        return "redirect:upload.html";
    }
}
