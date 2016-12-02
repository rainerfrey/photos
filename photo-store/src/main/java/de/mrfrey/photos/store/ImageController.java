package de.mrfrey.photos.store;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;

@Controller
@RequestMapping("/photos/{photoId}/image")
public class ImageController {

    private final PhotoStorageService photoStorageService;

    public ImageController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> getImage(@PathVariable ObjectId photoId) {
        GridFsResource image = photoStorageService.getImageResource(photoId, false);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(outputStream -> IOUtils.copy(image.getInputStream(), outputStream));
    }
}
