package de.mrfrey.photos.scaler;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@Service
public class Scaler {

    @PostConstruct
    protected void initialize() {
        ImageIO.setCacheDirectory(null);
        ImageIO.setUseCache(true);
    }

    public BufferedImage scaleImage(InputStream original, String format) throws IOException {
        BufferedImage image = ImageIO.read(original);
        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, 1200, null);
    }
}
