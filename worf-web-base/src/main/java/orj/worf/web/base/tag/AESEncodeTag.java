package orj.worf.web.base.tag;

import javax.servlet.ServletRequest;

import orj.worf.security.util.EncryptionUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

public class AESEncodeTag extends RequestContextAwareTag {
    private static final long serialVersionUID = 2117928298215851655L;

    private String paramName;
    private String textName;

    @Override
    protected int doStartTagInternal() throws Exception {
        ServletRequest request = pageContext.getRequest();
        String value = request.getParameter(paramName);
        if (StringUtils.isBlank(value)) {
            value = (String) request.getAttribute(paramName);
        }
        if (StringUtils.isBlank(value)) {
            value = paramName;
        }
        String hidden = "<input type=\"hidden\" name=\"%s\" value=\"%s\" />";
        pageContext.getOut().print(String.format(hidden, textName, EncryptionUtils.encryptByAES(value)));
        return SKIP_BODY;
    }

    public void setParamName(final String paramName) {
        this.paramName = paramName;
    }

    public void setTextName(final String textName) {
        this.textName = textName;
    }

}
