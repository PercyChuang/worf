package orj.worf.kaptcha.text;

import java.awt.image.BufferedImage;

public abstract interface WordRenderer {
    public abstract BufferedImage renderWord(String paramString, int paramInt1, int paramInt2);
}