package orj.worf.kaptcha;

import java.awt.image.BufferedImage;

public abstract interface GimpyEngine {
    public abstract BufferedImage getDistortedImage(BufferedImage paramBufferedImage);
}