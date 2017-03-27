package orj.worf.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoaderUtils extends org.springframework.core.io.support.PropertiesLoaderUtils {

    public static Properties loadProperties(final String filename) throws IOException {
        Properties props = new Properties();
        try (InputStream is = ResourceUtils.getResourceAsStream(filename)) {
            props.load(is);
        }
        return props;
    }

}