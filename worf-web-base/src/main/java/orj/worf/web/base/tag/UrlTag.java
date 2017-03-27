package orj.worf.web.base.tag;

import orj.worf.web.base.scanner.ClassPathUrlConstantAnnotationHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

public class UrlTag extends RequestContextAwareTag {
    private static final long serialVersionUID = 7268130231604573281L;
    private static Logger logger = LoggerFactory.getLogger(UrlTag.class);
    private String namespace;
    private String fieldName;

    public void setNamespace(final String namespace) {
        this.namespace = namespace.trim();
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName.trim();
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        String key = this.namespace + "." + this.fieldName;
        String constant = ClassPathUrlConstantAnnotationHandler.getConstants().get(key);
        if (constant == null) {
            logger.warn("find a null constant [key = {}]", key);
        }
        pageContext.getOut().print(pageContext.getServletContext().getContextPath() + constant);
        return SKIP_BODY;
    }

}
