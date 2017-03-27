package orj.worf.kaptcha.text.impl;

import java.util.Random;
import orj.worf.kaptcha.text.TextProducer;
import orj.worf.kaptcha.util.Config;
import orj.worf.kaptcha.util.Configurable;

public class DefaultTextCreator extends Configurable implements TextProducer {
    public String getText() {
        int length = getConfig().getTextProducerCharLength();
        char[] chars = getConfig().getTextProducerCharString();
        Random rand = new Random();
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            text.append(chars[rand.nextInt(chars.length)]);
        }
        return text.toString();
    }
}