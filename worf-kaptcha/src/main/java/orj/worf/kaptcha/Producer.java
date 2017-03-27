package orj.worf.kaptcha;

import java.awt.image.BufferedImage;

public abstract interface Producer {
    public abstract BufferedImage createImage(String paramString);

    public abstract String createText();
}