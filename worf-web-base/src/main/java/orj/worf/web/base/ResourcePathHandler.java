package orj.worf.web.base;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class ResourcePathHandler implements ServletContextAware {
    private static final String JS_RESOURCES = "/js-";
    private static final String CSS_RESOURCES = "/css-";
    private static final String IMG_RESOURCES = "/img-";
    private ServletContext servletContext;
    private String scriptVersion;
    private String styleVersion;
    private String imageVersion;

    private String scriptLocation;
    private String styleLocation;
    private String imageLocation;

    public void init() {
        scriptLocation = JS_RESOURCES + scriptVersion;
        styleLocation = CSS_RESOURCES + styleVersion;
        imageLocation = IMG_RESOURCES + imageVersion;

        String contextPath = servletContext.getContextPath();
        servletContext.setAttribute("contextLocation", contextPath);
        servletContext.setAttribute("scriptLocation", contextPath + scriptLocation);
        servletContext.setAttribute("styleLocation", contextPath + styleLocation);
        servletContext.setAttribute("imageLocation", contextPath + imageLocation);
        servletContext.setAttribute("resourcesLocation", contextPath + "/resources");
    }

    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setStyleVersion(final String styleVersion) {
        this.styleVersion = styleVersion;
    }

    public void setImageVersion(final String imageVersion) {
        this.imageVersion = imageVersion;
    }

    public void setScriptVersion(final String scriptVersion) {
        this.scriptVersion = scriptVersion;
    }

    public String getScriptLocation() {
        return scriptLocation;
    }

    public String getStyleLocation() {
        return styleLocation;
    }

    public String getImageLocation() {
        return imageLocation;
    }
}