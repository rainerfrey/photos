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
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.drew.metadata.exif.ExifDirectoryBase.TAG_APERTURE;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_DATETIME;
import static com.drew.metadata.exif.ExifDirectoryBase.TAG_LENS_MODEL;

@Service
public class MetadataExtractor {

    private final RestTemplate imageService;

    public MetadataExtractor(RestTemplate imageService) {
        this.imageService = imageService;
    }

    public Map<String, Object> extractMetadata(String photoId) {
        ResponseEntity<InputStreamResource> entity = imageService.getForEntity("http://photo-store/photos/{photoId}/image/original", InputStreamResource.class, photoId);
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

        info.put(ifd0Dir.getTagName(ExifIFD0Directory.TAG_MODEL), ifd0Dir.getString(ExifIFD0Directory.TAG_MODEL));
        info.put(ifd0Dir.getTagName(TAG_DATETIME), ifd0Dir.getDate(TAG_DATETIME));
        info.put(subIFDDir.getTagName(TAG_LENS_MODEL), subIFDDir.getString(TAG_LENS_MODEL));
        info.put(subIFDDir.getTagName(TAG_APERTURE), subIFD.getFNumberDescription());

        GeoLocation geoLocation = gpsDir.getGeoLocation();
        if(geoLocation!=null) {
            info.put("GPS Location", geoLocation);
            info.put("GPS Position", String.format("%s, %s", gps.getGpsLatitudeDescription(), gps.getGpsLongitudeDescription()));
            info.put(gpsDir.getTagName(GpsDirectory.TAG_ALTITUDE), gps.getGpsAltitudeDescription());
        }
        return info;
    }
}
