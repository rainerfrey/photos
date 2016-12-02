package de.mrfrey.photos.store;

import org.bson.types.ObjectId;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoStorageService photoStorageService;

    public PhotoController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Resource<Photo> get(@PathVariable("id") ObjectId id) {
        Link self = linkTo(methodOn(PhotoController.class).get(id)).withSelfRel();
        Link original = linkTo(methodOn(ImageController.class).getImage(id, Photo.Size.original)).withRel("image:original");
        Link scaled = linkTo(methodOn(ImageController.class).getImage(id, Photo.Size.scaled)).withRel("image:scaled");
        return new Resource<>(photoStorageService.getPhoto(id), self, original, scaled);
    }

    @PostMapping
    public String upload(@RequestPart("image-file") MultipartFile imageFile) {
        photoStorageService.storePhoto(imageFile);
        return "redirect:upload.html";
    }
}
