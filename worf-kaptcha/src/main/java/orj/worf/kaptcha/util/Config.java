package orj.worf.kaptcha.util;

import java.awt.Color;
import java.awt.Font;
import java.util.Properties;
import java.util.Random;
import orj.worf.kaptcha.BackgroundProducer;
import orj.worf.kaptcha.GimpyEngine;
import orj.worf.kaptcha.NoiseProducer;
import orj.worf.kaptcha.Producer;
import orj.worf.kaptcha.impl.BravoGimpy;
import orj.worf.kaptcha.impl.BravoNoise;
import orj.worf.kaptcha.impl.DefaultBackground;
import orj.worf.kaptcha.impl.DefaultKaptcha;
import orj.worf.kaptcha.text.TextProducer;
import orj.worf.kaptcha.text.WordRenderer;
import orj.worf.kaptcha.text.impl.DefaultTextCreator;
import orj.worf.kaptcha.text.impl.DefaultWordRenderer;

public class Config {
    private Properties properties;
    private ConfigHelper helper;

    public Config(Properties properties) {
        this.properties = properties;
        this.helper = new ConfigHelper();
    }

    public boolean isBorderDrawn() {
        String paramName = "kaptcha.border";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getBoolean(paramName, paramValue, true);
    }

    public Color getBorderColor() {
        String paramName = "kaptcha.border.color";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getColor(paramName, paramValue, Color.LIGHT_GRAY);
    }

    public int getBorderThickness() {
        String paramName = "kaptcha.border.thickness";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 1);
    }

    public Producer getProducerImpl() {
        String paramName = "kaptcha.producer.impl";
        String paramValue = this.properties.getProperty(paramName);
        Producer producer = (Producer) this.helper.getClassInstance(paramName, paramValue, new DefaultKaptcha(), this);
        return producer;
    }

    public TextProducer getTextProducerImpl() {
        String paramName = "kaptcha.textproducer.impl";
        String paramValue = this.properties.getProperty(paramName);
        TextProducer textProducer = (TextProducer) this.helper.getClassInstance(paramName, paramValue,
                new DefaultTextCreator(), this);

        return textProducer;
    }

    public char[] getTextProducerCharString() {
        String paramName = "kaptcha.textproducer.char.string";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getChars(paramName, paramValue, "61830936411443614623476295287590".toCharArray());
    }

    public int getTextProducerCharLength() {
        String paramName = "kaptcha.textproducer.char.length";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 4);
    }

    public Font[] getTextProducerFonts(int fontSize) {
        String paramName = "kaptcha.textproducer.font.names";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getFonts(paramName, paramValue, fontSize, new Font[] { new Font("Fixedsys", 1, fontSize),
                new Font("Arial", 1, fontSize) });
    }

    public int getTextProducerFontSize() {
        String paramName = "kaptcha.textproducer.font.size";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 20);
    }

    public Color getTextProducerFontColor() {
        String paramName = "kaptcha.textproducer.font.color";
        String paramValue = this.properties.getProperty(paramName);
        Random random = new Random();
        return this.helper.getColor(paramName, paramValue,
                new Color(random.nextInt(10), random.nextInt(11), random.nextInt(12)));
    }

    public int getTextProducerCharSpace() {
        String paramName = "kaptcha.textproducer.char.space";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 2);
    }

    public NoiseProducer getNoiseImpl() {
        String paramName = "kaptcha.noise.impl";
        String paramValue = this.properties.getProperty(paramName);
        NoiseProducer noiseProducer = (NoiseProducer) this.helper.getClassInstance(paramName, paramValue,
                new BravoNoise(), this);

        return noiseProducer;
    }

    public Color getNoiseColor() {
        String paramName = "kaptcha.noise.color";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getColor(paramName, paramValue, getRandomColor(110, 120));
    }

    public int getNoiseLines() {
        String paramName = "kaptcha.noise.lines";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 13);
    }

    public GimpyEngine getObscurificatorImpl() {
        String paramName = "kaptcha.obscurificator.impl";
        String paramValue = this.properties.getProperty(paramName);
        GimpyEngine gimpyEngine = (GimpyEngine) this.helper.getClassInstance(paramName, paramValue, new BravoGimpy(),
                this);

        return gimpyEngine;
    }

    public WordRenderer getWordRendererImpl() {
        String paramName = "kaptcha.word.impl";
        String paramValue = this.properties.getProperty(paramName);
        WordRenderer wordRenderer = (WordRenderer) this.helper.getClassInstance(paramName, paramValue,
                new DefaultWordRenderer(), this);

        return wordRenderer;
    }

    public BackgroundProducer getBackgroundImpl() {
        String paramName = "kaptcha.background.impl";
        String paramValue = this.properties.getProperty(paramName);
        BackgroundProducer backgroundProducer = (BackgroundProducer) this.helper.getClassInstance(paramName,
                paramValue, new DefaultBackground(), this);

        return backgroundProducer;
    }

    public Color getBackgroundColorFrom() {
        String paramName = "kaptcha.background.clear.from";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getColor(paramName, paramValue, getRandomColor(200, 255));
    }

    public Color getBackgroundColorTo() {
        String paramName = "kaptcha.background.clear.to";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getColor(paramName, paramValue, Color.WHITE);
    }

    public int getWidth() {
        String paramName = "kaptcha.image.width";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 80);
    }

    public int getHeight() {
        String paramName = "kaptcha.image.height";
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 25);
    }

    public String getSessionKey() {
        return this.properties.getProperty("kaptcha.session.key", "KAPTCHA_SESSION_KEY");
    }

    public String getSessionDate() {
        return this.properties.getProperty("kaptcha.session.date", "KAPTCHA_SESSION_DATE");
    }

    public Properties getProperties() {
        return this.properties;
    }

    public final Color getRandomColor(int fc, int bc) {
        Random random = new Random();
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}