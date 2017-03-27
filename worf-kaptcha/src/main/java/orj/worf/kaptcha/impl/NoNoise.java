package orj.worf.kaptcha.impl;

import java.awt.image.BufferedImage;
import orj.worf.kaptcha.NoiseProducer;
import orj.worf.kaptcha.util.Configurable;

public class NoNoise extends Configurable implements NoiseProducer {
    public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
    }
}
