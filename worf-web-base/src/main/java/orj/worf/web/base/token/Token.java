package orj.worf.web.base.token;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import orj.worf.util.ObjectId;
import orj.worf.util.RandomStringGenerator;

public class Token implements Serializable {
    private static final long serialVersionUID = -2620912281214255410L;
    private String name;
    private String value;

    private Token() {
    }

    public static Token build() {
        Token token = new Token();
        token.setName(ObjectId.getId() + RandomStringGenerator.randomString(6));
        token.setValue(generateGUID());
        return token;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    private static String generateGUID() {
        Random random = new Random();
        return new BigInteger(165, random).toString(36).toUpperCase();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(96);
        builder.append("Token [name=").append(name).append(", value=").append(value).append("]");
        return builder.toString();
    }
}
