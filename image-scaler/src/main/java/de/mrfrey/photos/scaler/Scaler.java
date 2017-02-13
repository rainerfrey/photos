package de.mrfrey.photos.scaler;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.imgscalr.Scalr.Rotation.CW_180;
import static org.imgscalr.Scalr.Rotation.CW_270;
import static org.imgscalr.Scalr.Rotation.CW_90;
import static org.imgscalr.Scalr.Rotation.FLIP_HORZ;
import static org.imgscalr.Scalr.Rotation.FLIP_VERT;
import static org.imgscalr.Scalr.rotate;


@Service
public class Scaler {

    @PostConstruct
    protected void initialize() {
        ImageIO.setCacheDirectory(null);
        ImageIO.setUseCache(true);
    }

    public BufferedImage scaleImage(InputStream original, int orientation) {
        try {
            BufferedImage image = ImageIO.read(original);
            BufferedImage rotated = rotateIfNecessary(image, orientation);
            return Scalr.resize(rotated, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, 1200);
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
            return null;
        }
    }

    public BufferedImage scaleThumbnail(InputStream original, int orientation) {
        try {
            BufferedImage image = ImageIO.read(original);
            BufferedImage rotated = rotateIfNecessary(image, orientation);
            return Scalr.resize(rotated, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 320);
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
            return null;
        }
    }

    private BufferedImage rotateIfNecessary(BufferedImage image, int orientation) {
        switch (orientation) {
            case 2:
                return rotate(image, FLIP_HORZ);
            case 3:
                return rotate(image, CW_180);
            case 4:
                return rotate(image, FLIP_VERT);
            case 5:
                BufferedImage step1 = rotate(image, FLIP_HORZ);
                return rotate(step1, CW_270);
            case 6:
                return rotate(image, CW_90);
            case 7:
                BufferedImage step2 = rotate(image, FLIP_HORZ);
                return rotate(step2, CW_90);
            case 8:
                return rotate(image, CW_270);
            case 1:
            default:
                return image;

        }
    }
}
