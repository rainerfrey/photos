package de.mrfrey.photos.store;

import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@RequestMapping("/photos/{photoId}/image")
public class ImageController {

    private final GridFsOperations gridFs;
    private final PhotoRepository photoRepository;

    public ImageController(GridFsOperations gridFs, PhotoRepository photoRepository) {
        this.gridFs = gridFs;
        this.photoRepository = photoRepository;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> getImage(@PathVariable ObjectId photoId) {
        Photo photo = photoRepository.findOne(photoId);
        GridFSDBFile image = gridFs.findOne(Query.query(Criteria.where("_id").is(photo.getFileId())));
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(outputStream -> IOUtils.copy(image.getInputStream(), outputStream));
    }
}
