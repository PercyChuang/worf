package orj.worf.kaptcha.impl;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;
import orj.worf.kaptcha.NoiseProducer;
import orj.worf.kaptcha.util.Config;
import orj.worf.kaptcha.util.Configurable;

public class BravoNoise extends Configurable implements NoiseProducer {
    public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
        Graphics2D graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        Random random = new Random();
        int lines = getConfig().getNoiseLines();
        for (int i = 0; i < lines; ++i) {
            int x = random.nextInt(image.getWidth());
            int y = random.nextInt(image.getHeight());
            int x0 = random.nextInt(21);
            int y0 = random.nextInt(35);
            graph.setColor(getConfig().getRandomColor(40, 180));
            graph.drawLine(x, y, x + x0, y + y0);
        }
        graph.dispose();
    }
}
