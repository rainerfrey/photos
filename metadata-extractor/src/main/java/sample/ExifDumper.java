package sample;


import com.adobe.xmp.XMPMeta;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Descriptor;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.xmp.XmpDirectory;

import java.io.File;
import java.io.IOException;

public class ExifDumper {
    public static void main(String[] args) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(new File(args[0]));

        for (Directory directory : metadata.getDirectories()) {
            System.out.format("%s (%s)\n", directory.getName(), directory.getClass().getCanonicalName());
            System.out.println("----");
            for (Tag tag : directory.getTags()) {
                System.out.format("[%s] - %s = %s",
                        directory.getName(), tag.getTagName(), tag.getDescription());
                System.out.println();
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                    System.out.println();
                }
            }
        }

        ExifIFD0Directory ifd0Dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        ExifIFD0Descriptor ifd0 = new ExifIFD0Descriptor(ifd0Dir);
        System.out.println();
        System.out.println(ifd0Dir.getTagName(ExifIFD0Directory.TAG_APERTURE));
        System.out.println(ifd0.getApertureValueDescription());

    }
}
