package orj.worf.kaptcha;

import java.awt.image.BufferedImage;

public abstract interface NoiseProducer {
    public abstract void makeNoise(BufferedImage paramBufferedImage, float paramFloat1, float paramFloat2,
            float paramFloat3, float paramFloat4);
}