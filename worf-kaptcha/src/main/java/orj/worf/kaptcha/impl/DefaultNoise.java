package orj.worf.kaptcha.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.CubicCurve2D.Float;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import orj.worf.kaptcha.NoiseProducer;
import orj.worf.kaptcha.util.Config;
import orj.worf.kaptcha.util.Configurable;

public class DefaultNoise extends Configurable implements NoiseProducer {
    public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
        Color color = getConfig().getNoiseColor();

        int width = image.getWidth();
        int height = image.getHeight();

        Point2D[] pts = null;
        Random rand = new Random();

        CubicCurve2D cc = new CubicCurve2D.Float(width * factorOne, height * rand.nextFloat(), width * factorTwo,
                height * rand.nextFloat(), width * factorThree, height * rand.nextFloat(), width * factorFour, height
                        * rand.nextFloat());

        PathIterator pi = cc.getPathIterator(null, 2.0D);
        Point2D[] tmp = new Point2D[200];
        int i = 0;

        while (!(pi.isDone())) {
            float[] coords = new float[6];
            switch (pi.currentSegment(coords)) {
                case 0:
                case 1:
                    tmp[i] = new Point2D.Float(coords[0], coords[1]);
            }
            ++i;
            pi.next();
        }
        pts = new Point2D[i];
        System.arraycopy(tmp, 0, pts, 0, i);
        Graphics2D graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        graph.setColor(color);

        for (i = 0; i < pts.length - 1; ++i) {
            if (i < 3) {
                graph.setStroke(new BasicStroke(1.5F));
            }
            graph.drawLine((int) pts[i].getX(), (int) pts[i].getY(), (int) pts[(i + 1)].getX(),
                    (int) pts[(i + 1)].getY());
        }
        graph.dispose();
    }
}