package de.mrfrey.photos.scaler;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


@Service
public class Scaler {

    @PostConstruct
    protected void initialize() {
        ImageIO.setCacheDirectory(null);
        ImageIO.setUseCache(true);
    }

    public BufferedImage scaleImage(InputStream original) {
        try {
            BufferedImage image = ImageIO.read(original);
            return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, 1200);
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
            return null;
        }
    }

    public BufferedImage scaleThumbnail(InputStream original) {
        try {
            BufferedImage image = ImageIO.read(original);
            return Scalr.resize(image, Scalr.Method.BALANCED, Scalr.Mode.AUTOMATIC, 240);
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
            return null;
        }
    }
}
