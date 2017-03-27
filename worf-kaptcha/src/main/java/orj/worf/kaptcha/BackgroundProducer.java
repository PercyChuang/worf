package orj.worf.kaptcha;

import java.awt.image.BufferedImage;

public abstract interface BackgroundProducer {
    public abstract BufferedImage addBackground(BufferedImage paramBufferedImage);
}