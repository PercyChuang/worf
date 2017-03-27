package orj.worf.web.base.tag;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

public class RsaJSTag extends RequestContextAwareTag {

    private static final long serialVersionUID = 717376737844622758L;

    @Override
    protected int doStartTagInternal() throws Exception {
        StringBuilder buf = new StringBuilder(128);
        buf.append("<script type=\"text/javascript\" src=\"");
        buf.append(pageContext.getServletContext().getContextPath());
        buf.append("/resources/rsa/utility.js?v=1.0.1\"></script>");
        pageContext.getOut().print(buf.toString());
        return SKIP_BODY;
    }
}
