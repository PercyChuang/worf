package orj.worf.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.PathMatcher;

public final class ServletUtils {
    private static final Logger logger = LoggerFactory.getLogger(ServletUtils.class);
    private static final String HTTP_CONTENT_LENGTH_HEADER = "Content-Length";
    private static final String HTTP_LAST_MODIFIED_HEADER = "Last-Modified";
    private static final String HTTP_EXPIRES_HEADER = "Expires";
    private static final String HTTP_CACHE_CONTROL_HEADER = "Cache-Control";
    private static final int DEFAULT_CACHE_TIMEOUT = 31556926;
    private static final Map<String, String> defaultMimeTypes = new HashMap<String, String>(7, 1.0F);
    private static final Set<String> compressedMimeTypes = new HashSet<String>(1);
    static {
        defaultMimeTypes.put(".css", "text/css");
        defaultMimeTypes.put(".gif", "image/gif");
        defaultMimeTypes.put(".ico", "image/vnd.microsoft.icon");
        defaultMimeTypes.put(".jpeg", "image/jpeg");
        defaultMimeTypes.put(".jpg", "image/jpeg");
        defaultMimeTypes.put(".js", "text/javascript");
        defaultMimeTypes.put(".png", "image/png");
        compressedMimeTypes.add("text/*");
    }

    private ServletUtils() {
    }

    public static void writeBytesToResponse(final byte[] data, final boolean gzipEnabled,
            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        OutputStream out = null;
        try {
            out = selectOutputStream(request, response, gzipEnabled);
            FileCopyUtils.copy(data, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void writeResourcesToResponse(final Set<String> filePaths, final Integer cacheTimeout,
            final boolean gzipEnabled, final HttpServletRequest request, final HttpServletResponse response)
                    throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to GET resource...");
        }
        Resource[] resources = getRequestResources(filePaths);
        if (resources == null || resources.length == 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Resources not found..");
            }
            response.sendError(404);
            return;
        }
        writeResourcesToResponse(resources, cacheTimeout, gzipEnabled, request, response);
    }

    public static void writeResourcesToResponse(final Resource[] resources, final Integer cacheTimeout,
            final boolean gzipEnabled, final HttpServletRequest request, final HttpServletResponse response)
                    throws IOException {
        prepareResponse(request, response, resources, cacheTimeout);
        OutputStream out = null;
        try {
            out = selectOutputStream(request, response, gzipEnabled);
            for (Resource resource : resources) {
                FileCopyUtils.copy(resource.getInputStream(), out);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static Resource[] getRequestResources(final Set<String> filePaths) throws IOException {
        List<Resource> resources = new ArrayList<Resource>(filePaths.size());
        for (String path : filePaths) {
            Resource[] re = ResourceUtils.getResources(path);
            for (Resource r : re) {
                resources.add(r);
            }
        }
        return (Resource[]) resources.toArray();
    }

    private static void prepareResponse(final HttpServletRequest request, final HttpServletResponse response,
            final Resource[] resources, final Integer cacheTimeout) throws IOException {
        long lastModified = -1L;
        int contentLength = 0;
        String mimeType = null;
        String currentMimeType = null;
        for (Resource resource : resources) {
            if (resource.lastModified() > lastModified) {
                lastModified = resource.lastModified();
            }
            String path = resource.getURL().getPath();
            HttpSession session = request.getSession(false);
            if (session != null) {
                currentMimeType = session.getServletContext().getMimeType(path);
            }
            if (currentMimeType == null) {
                String extension = path.substring(path.lastIndexOf('.'));
                currentMimeType = defaultMimeTypes.get(extension);
            }
            if (mimeType == null) {
                mimeType = currentMimeType;
            } else if (!mimeType.equals(currentMimeType)) {
                throw new MalformedURLException("Can't combine resources. All resources must be of the same mime type.");
            }
            contentLength = (int) (contentLength + resource.contentLength());
        }
        response.setContentType(mimeType);
        response.setHeader(HTTP_CONTENT_LENGTH_HEADER, Long.toString(contentLength));
        response.setDateHeader(HTTP_LAST_MODIFIED_HEADER, lastModified);
        if ((cacheTimeout == null ? DEFAULT_CACHE_TIMEOUT : cacheTimeout.intValue()) > 0) {
            configureCaching(response, cacheTimeout.intValue());
        }
    }

    public static OutputStream selectOutputStream(final HttpServletRequest request, final HttpServletResponse response,
            final boolean gzipEnabled) throws IOException {
        String acceptEncoding = request.getHeader("Accept-Encoding");
        String mimeType = response.getContentType();
        if (gzipEnabled && StringUtils.isNotEmpty(acceptEncoding) && acceptEncoding.indexOf("gzip") > -1
                && matchesCompressedMimeTypes(mimeType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Enabling GZIP compression for the current response.");
            }
            return new GZIPResponseStream(response);
        }
        return response.getOutputStream();
    }

    public static boolean matchesCompressedMimeTypes(final String mimeType) {
        PathMatcher pathMatcher = new AntPathMatcher();
        Iterator<String> compressedMimeTypesIt = compressedMimeTypes.iterator();
        while (compressedMimeTypesIt.hasNext()) {
            String compressedMimeType = compressedMimeTypesIt.next();
            if (pathMatcher.match(compressedMimeType, mimeType)) {
                return true;
            }
        }
        return false;
    }

    private static void configureCaching(final HttpServletResponse response, final int seconds) {
        response.setDateHeader(HTTP_EXPIRES_HEADER, System.currentTimeMillis() + seconds * 1000L);
        response.setHeader(HTTP_CACHE_CONTROL_HEADER, "max-age=" + seconds);
    }
}
