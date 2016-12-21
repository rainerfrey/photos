package de.mrfrey.photos.store;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;

@Controller
@RequestMapping("/photos/{photoId}/image")
public class ImageController {


    private final PhotoStorageService photoStorageService;

    public ImageController(PhotoStorageService photoStorageService) {
        this.photoStorageService = photoStorageService;
    }

    @GetMapping("/{imageSize}")
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> getImage(@PathVariable("photoId") ObjectId photoId, @PathVariable("imageSize") Photo.Size imageSize) {
        GridFsResource image = photoStorageService.getImageResource(photoId, imageSize);
        if (image == null) throw new ImageNotFound(photoId, imageSize);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(outputStream -> IOUtils.copy(image.getInputStream(), outputStream));
    }

    @PostMapping("/{imageSize}")
    @ResponseBody
    public ResponseEntity<Void> uploadScaledImage(@PathVariable("photoId") ObjectId photoId, InputStream content, @PathVariable("imageSize") Photo.Size imageSize) {
        photoStorageService.addAdditionalImage(photoId, content, imageSize);
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler(ImageNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ImageNotFound handleImageNotFound(ImageNotFound inf) {
        return inf;
    }

    protected static class ImageNotFound extends RuntimeException {
        public ImageNotFound(ObjectId photoId, Photo.Size size) {
            super(String.format("Image size %s not found for photo %s", size, photoId));
        }
    }

}
