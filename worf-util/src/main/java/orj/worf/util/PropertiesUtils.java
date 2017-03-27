package orj.worf.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtils extends PropertiesLoaderUtils {
    public static Properties loadProperties(String filename) {
        Properties props = new Properties();
        try {
            InputStream is = ResourceUtils.getResourceAsStream(filename);
            Throwable localThrowable2 = null;
            try {
                props.load(is);
            } catch (Throwable localThrowable1) {
            } finally {
                if (is != null)
                    if (localThrowable2 != null)
                        try {
                            is.close();
                        } catch (Throwable x2) {
                            localThrowable2.addSuppressed(x2);
                        }
                    else
                        is.close();
            }
        } catch (Exception e) {
        }
        return props;
    }

    public static Writer toWriter(Properties properties) {
        StringBuilderWriter writer = new StringBuilderWriter();
        try {
            properties.store(writer, null);
        } catch (IOException impossible) {
        } finally {
            IOUtils.closeQuietly(writer);
        }
        return writer;
    }

    public static String toString(Properties properties) {
        Writer writer = toWriter(properties);
        return writer.toString();
    }

    public static Reader toReader(Properties properties) {
        String content = toString(properties);
        StringReader stringReader = new StringReader(content);
        return stringReader;
    }
}
