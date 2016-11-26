package de.mrfrey.photos.store;

import org.bson.types.ObjectId;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoStorageService photoStorageService;

    public PhotoController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @PostMapping
    public String upload(@RequestPart("image-file")MultipartFile imageFile) {
        photoStorageService.storePhoto(imageFile);
        return "redirect:uploadSuccess";
    }
}
