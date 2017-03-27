package orj.worf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public final class ResourceUtils {

    public static Resource[] getResources(final String locationPattern) throws IOException {
        return orj.worf.core.util.ResourceUtils.getResources(locationPattern);
    }

    public static FileSystemResource getFileSystemResource(final String locationPattern) throws IOException {
        return orj.worf.core.util.ResourceUtils.getFileSystemResource(locationPattern);
    }

    @Deprecated
    public static File getFileInJBoss6(final String resourceLocation) throws FileNotFoundException {
        return orj.worf.core.util.ResourceUtils.getFileInJBoss6(resourceLocation);
    }

    public static File getFile(final URL resourceUrl, final String description) throws FileNotFoundException {
        return orj.worf.core.util.ResourceUtils.getFile(resourceUrl, description);
    }

    public static InputStream getResourceAsStream(final String filename) throws IOException {
        return orj.worf.core.util.ResourceUtils.getResourceAsStream(filename);
    }
}
