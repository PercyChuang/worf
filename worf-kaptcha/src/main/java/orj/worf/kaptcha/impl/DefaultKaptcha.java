package orj.worf.kaptcha.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.image.BufferedImage;
import orj.worf.kaptcha.BackgroundProducer;
import orj.worf.kaptcha.GimpyEngine;
import orj.worf.kaptcha.Producer;
import orj.worf.kaptcha.text.TextProducer;
import orj.worf.kaptcha.text.WordRenderer;
import orj.worf.kaptcha.util.Config;
import orj.worf.kaptcha.util.Configurable;

public class DefaultKaptcha extends Configurable implements Producer {
    private int width;
    private int height;

    public DefaultKaptcha() {
        this.width = 200;
        this.height = 50;
    }

    public BufferedImage createImage(String text) {
        WordRenderer wordRenderer = getConfig().getWordRendererImpl();
        GimpyEngine gimpyEngine = getConfig().getObscurificatorImpl();
        BackgroundProducer backgroundProducer = getConfig().getBackgroundImpl();
        boolean isBorderDrawn = getConfig().isBorderDrawn();
        this.width = getConfig().getWidth();
        this.height = getConfig().getHeight();
        BufferedImage bi = wordRenderer.renderWord(text, this.width, this.height);
        bi = gimpyEngine.getDistortedImage(bi);
        bi = backgroundProducer.addBackground(bi);
        Graphics2D graphics = bi.createGraphics();
        if (isBorderDrawn) {
            drawBox(graphics);
        }
        return bi;
    }

    private void drawBox(Graphics2D graphics) {
        Color borderColor = getConfig().getBorderColor();
        int borderThickness = getConfig().getBorderThickness();
        graphics.setColor(borderColor);
        if (borderThickness != 1) {
            BasicStroke stroke = new BasicStroke(borderThickness);
            graphics.setStroke(stroke);
        }
        Line2D line1 = new Line2D.Double(0.0D, 0.0D, 0.0D, this.width);
        graphics.draw(line1);
        Line2D line2 = new Line2D.Double(0.0D, 0.0D, this.width, 0.0D);
        graphics.draw(line2);
        line2 = new Line2D.Double(0.0D, this.height - 1, this.width, this.height - 1);
        graphics.draw(line2);
        line2 = new Line2D.Double(this.width - 1, this.height - 1, this.width - 1, 0.0D);
        graphics.draw(line2);
    }

    public String createText() {
        return getConfig().getTextProducerImpl().getText();
    }
}
