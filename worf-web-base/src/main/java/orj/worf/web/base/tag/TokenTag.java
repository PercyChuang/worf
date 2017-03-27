package orj.worf.web.base.tag;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import orj.worf.web.base.token.Token;
import orj.worf.web.base.token.TokenHelper;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

public class TokenTag extends RequestContextAwareTag {

    private static final long serialVersionUID = 2422549476354099201L;

    @Override
    protected int doStartTagInternal() throws Exception {
        ServletRequest request = pageContext.getRequest();
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            Token token = TokenHelper.setToken(req);
            String tokenName = "<input type=\"hidden\" name=\"%s\" value=\"%s\" />\r\n";
            String tokenValue = "<input type=\"hidden\" name=\"%s\" value=\"%s\" />";
            pageContext.getOut().print(String.format(tokenName, TokenHelper.TOKEN_NAME_FIELD, token.getName()));
            pageContext.getOut().print(String.format(tokenValue, TokenHelper.TOKEN_VALUE_FIELD, token.getValue()));
        } else {
            throw new ServletException("Only Http request supported.");
        }
        return SKIP_BODY;
    }

}
