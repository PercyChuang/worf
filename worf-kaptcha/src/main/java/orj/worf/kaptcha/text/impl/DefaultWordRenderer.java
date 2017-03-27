package orj.worf.kaptcha.text.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import orj.worf.kaptcha.text.WordRenderer;
import orj.worf.kaptcha.util.Config;
import orj.worf.kaptcha.util.Configurable;

public class DefaultWordRenderer extends Configurable implements WordRenderer {
    public BufferedImage renderWord(String word, int width, int height) {
        int fontSize = getConfig().getTextProducerFontSize();
        Font[] fonts = getConfig().getTextProducerFonts(fontSize);
        Color color = getConfig().getTextProducerFontColor();
        int charSpace = getConfig().getTextProducerCharSpace();
        BufferedImage image = new BufferedImage(width, height, 2);
        Graphics2D g2D = image.createGraphics();
        g2D.setColor(color);
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2D.setRenderingHints(hints);
        FontRenderContext frc = g2D.getFontRenderContext();
        Random random = new Random();
        int startPosY = (height - fontSize) / 5 + fontSize;
        char[] wordChars = word.toCharArray();
        Font[] chosenFonts = new Font[wordChars.length];
        int[] charWidths = new int[wordChars.length];
        int widthNeeded = 0;
        for (int i = 0; i < wordChars.length; ++i) {
            chosenFonts[i] = fonts[random.nextInt(fonts.length)];
            char[] charToDraw = { wordChars[i] };
            GlyphVector gv = chosenFonts[i].createGlyphVector(frc, charToDraw);
            charWidths[i] = (int) gv.getVisualBounds().getWidth();
            if (i > 0) {
                widthNeeded += 2;
            }
            widthNeeded += charWidths[i];
        }
        int startPosX = (width - widthNeeded) / 2 + 3;

        for (int i = 0; i < wordChars.length; ++i) {
            g2D.setFont(chosenFonts[i]);
            char[] charToDraw = { wordChars[i] };
            g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
            startPosX = startPosX + charWidths[i] + charSpace;
        }
        return image;
    }
}