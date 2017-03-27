package orj.worf.kaptcha.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import orj.worf.kaptcha.Producer;
import orj.worf.kaptcha.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KaptchaServlet extends HttpServlet implements Servlet {
    private static final long serialVersionUID = 3356133589691133207L;
    private static final Logger logger;
    private Properties props;
    private Producer kaptchaProducer;
    private String sessionKeyValue;
    private String sessionKeyDateValue;

    public KaptchaServlet() {
        this.props = new Properties();
        this.kaptchaProducer = null;
        this.sessionKeyValue = null;
        this.sessionKeyDateValue = null;
    }

    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);

        ImageIO.setUseCache(false);
        Enumeration initParams = conf.getInitParameterNames();
        while (initParams.hasMoreElements()) {
            String key = (String) initParams.nextElement();
            String value = conf.getInitParameter(key);
            this.props.put(key, value);
        }
        Config config = new Config(this.props);
        this.kaptchaProducer = config.getProducerImpl();
        this.sessionKeyValue = config.getSessionKey();
        this.sessionKeyDateValue = config.getSessionDate();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setDateHeader("Expires", 0L);

        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");

        resp.setHeader("Pragma", "no-cache");

        resp.setContentType("image/jpeg");

        String capText = this.kaptchaProducer.createText();

        req.getSession().setAttribute(this.sessionKeyValue, capText);

        req.getSession().setAttribute(this.sessionKeyDateValue, new Date());

        BufferedImage bi = this.kaptchaProducer.createImage(capText);
        ServletOutputStream out = resp.getOutputStream();

        ImageIO.write(bi, "jpg", out);

        req.getSession().setAttribute(this.sessionKeyValue, capText);

        req.getSession().setAttribute(this.sessionKeyDateValue, new Date());
        logger.info("请求[{}@{}]缓存的验证码为:{}, User-Agent为：{}", new Object[] { req.getSession().getId(),
                req.getRequestURI(), req.getSession().getAttribute(this.sessionKeyValue), req.getHeader("User-Agent") });
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    static {
        System.setProperty("java.awt.headless", "true");

        logger = LoggerFactory.getLogger(KaptchaServlet.class);
    }
}