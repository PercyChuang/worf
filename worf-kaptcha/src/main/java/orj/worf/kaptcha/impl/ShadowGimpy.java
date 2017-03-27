package orj.worf.kaptcha.impl;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShadowFilter;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import orj.worf.kaptcha.GimpyEngine;
import orj.worf.kaptcha.NoiseProducer;
import orj.worf.kaptcha.util.Config;
import orj.worf.kaptcha.util.Configurable;

public class ShadowGimpy extends Configurable implements GimpyEngine {
    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        NoiseProducer noiseProducer = getConfig().getNoiseImpl();
        BufferedImage distortedImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), 2);

        Graphics2D graph = (Graphics2D) distortedImage.getGraphics();
        ShadowFilter shadowFilter = new ShadowFilter();
        shadowFilter.setRadius(10.0F);
        shadowFilter.setDistance(5.0F);
        shadowFilter.setOpacity(1.0F);
        Random rand = new Random();
        RippleFilter rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(0);
        rippleFilter.setXAmplitude(7.6F);
        rippleFilter.setYAmplitude(rand.nextFloat() + 1.0F);
        rippleFilter.setXWavelength(rand.nextInt(7) + 8);
        rippleFilter.setYWavelength(rand.nextInt(3) + 2);
        rippleFilter.setEdgeAction(1);
        BufferedImage effectImage = rippleFilter.filter(baseImage, null);
        effectImage = shadowFilter.filter(effectImage, null);
        graph.drawImage(effectImage, 0, 0, null, null);
        graph.dispose();

        noiseProducer.makeNoise(distortedImage, 0.1F, 0.1F, 0.25F, 0.25F);
        noiseProducer.makeNoise(distortedImage, 0.1F, 0.25F, 0.5F, 0.9F);
        return distortedImage;
    }
}
