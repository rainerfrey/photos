package de.mrfrey.photos.processor.metadata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDescriptor;
import com.drew.metadata.exif.GpsDirectory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.drew.metadata.exif.ExifDirectoryBase.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_APERTURE;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_DATETIME;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_EXPOSURE_TIME;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_FOCAL_LENGTH;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_ISO_EQUIVALENT;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_LENS_MODEL;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_MODEL;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_SHUTTER_SPEED;

@Service
public class MetadataExtractor {

    private final RestTemplate imageService;
    private Traverson traverson;

    public MetadataExtractor(RestTemplate imageService) {
        this.imageService = imageService;
    }

    @PostConstruct
    protected void init() {
        this.traverson = new Traverson(URI.create("http://photo-store/"), MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8, MediaTypes.HAL_JSON).setRestOperations(imageService);
    }

    public Map<String, Object> extractMetadata(String photoId) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Host", "photo-store");
        String original = this.traverson.follow("image:original").withHeaders(httpHeaders).asTemplatedLink().expand(photoId).getHref();
        ResponseEntity<InputStreamResource> entity = imageService.getForEntity(original, InputStreamResource.class);
        try {
            return extractMetadata(entity.getBody().getInputStream());
        } catch (IOException | ImageProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Map<String, Object> extractMetadata(InputStream inputStream) throws ImageProcessingException, IOException {
        Map<String, Object> info = new LinkedHashMap<>();
        Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
        ExifIFD0Directory ifd0Dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        ExifSubIFDDirectory subIFDDir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        ExifSubIFDDescriptor subIFD = new ExifSubIFDDescriptor(subIFDDir);

        GpsDirectory gpsDir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        GpsDescriptor gps = new GpsDescriptor(gpsDir);

        info.put(ifd0Dir.getTagName(TAG_MODEL), ifd0Dir.getString(TAG_MODEL));
        info.put(ifd0Dir.getTagName(TAG_DATETIME), ifd0Dir.getDate(TAG_DATETIME));
        info.put(subIFDDir.getTagName(TAG_LENS_MODEL), subIFDDir.getString(TAG_LENS_MODEL));
        info.put(subIFDDir.getTagName(TAG_APERTURE), subIFD.getFNumberDescription());
        info.put(subIFDDir.getTagName(TAG_SHUTTER_SPEED), subIFD.getShutterSpeedDescription());
        info.put(subIFDDir.getTagName(TAG_ISO_EQUIVALENT), subIFD.getIsoEquivalentDescription());
        info.put(subIFDDir.getTagName(TAG_FOCAL_LENGTH), subIFD.getFocalLengthDescription());
        info.put(subIFDDir.getTagName(TAG_35MM_FILM_EQUIV_FOCAL_LENGTH), subIFD.get35mmFilmEquivFocalLengthDescription());

        GeoLocation geoLocation = gpsDir.getGeoLocation();
        if (geoLocation != null) {
            info.put("GPS Location", geoLocation);
            info.put("GPS Position", String.format("%s, %s", gps.getGpsLatitudeDescription(), gps.getGpsLongitudeDescription()));
            info.put(gpsDir.getTagName(GpsDirectory.TAG_ALTITUDE), gps.getGpsAltitudeDescription());
        }
        return info;
    }
}
