package orj.worf.kaptcha.impl;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.WaterFilter;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import orj.worf.kaptcha.GimpyEngine;
import orj.worf.kaptcha.NoiseProducer;
import orj.worf.kaptcha.util.Config;
import orj.worf.kaptcha.util.Configurable;

public class WaterRipple extends Configurable implements GimpyEngine {
    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        NoiseProducer noiseProducer = getConfig().getNoiseImpl();
        BufferedImage distortedImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), 2);

        Graphics2D graphics = (Graphics2D) distortedImage.getGraphics();
        RippleFilter rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(0);
        rippleFilter.setXAmplitude(2.6F);
        rippleFilter.setYAmplitude(1.7F);
        rippleFilter.setXWavelength(15.0F);
        rippleFilter.setYWavelength(5.0F);
        rippleFilter.setEdgeAction(0);
        WaterFilter waterFilter = new WaterFilter();
        waterFilter.setAmplitude(1.5F);
        waterFilter.setPhase(10.0F);
        waterFilter.setWavelength(2.0F);
        BufferedImage effectImage = waterFilter.filter(baseImage, null);
        effectImage = rippleFilter.filter(effectImage, null);
        graphics.drawImage(effectImage, 0, 0, null, null);
        graphics.dispose();
        noiseProducer.makeNoise(distortedImage, 0.1F, 0.1F, 0.25F, 0.25F);
        noiseProducer.makeNoise(distortedImage, 0.1F, 0.25F, 0.5F, 0.9F);
        return distortedImage;
    }
}