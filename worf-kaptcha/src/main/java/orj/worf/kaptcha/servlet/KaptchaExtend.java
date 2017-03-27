package orj.worf.kaptcha.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import orj.worf.kaptcha.Producer;
import orj.worf.kaptcha.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KaptchaExtend {
    private static final Logger logger;
    private final Properties props = new Properties();
    private final Producer kaptchaProducer;
    private final Config config;

    public KaptchaExtend() {
        ImageIO.setUseCache(false);
        this.props.put("kaptcha.border", "yes");
        this.props.put("kaptcha.textproducer.char.space", "5");
        this.config = new Config(this.props);
        this.kaptchaProducer = this.config.getProducerImpl();
    }

    public void captcha(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setDateHeader("Expires", 0L);

        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");

        resp.setHeader("Pragma", "no-cache");

        resp.setContentType("image/jpeg");

        String capText = this.kaptchaProducer.createText();
        String sessionKeyValue = this.config.getSessionKey();

        req.getSession().setAttribute(sessionKeyValue, capText);

        String sessionKeyDateValue = this.config.getSessionDate();
        req.getSession().setAttribute(sessionKeyDateValue, new Date());

        BufferedImage bi = this.kaptchaProducer.createImage(capText);
        ServletOutputStream out = resp.getOutputStream();

        ImageIO.write(bi, "jpg", out);

        req.getSession().setAttribute(sessionKeyValue, capText);

        req.getSession().setAttribute(sessionKeyDateValue, new Date());
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "请求[{}@{}]缓存的验证码为:{}, User-Agent为：{}",
                    new Object[] { req.getSession().getId(), req.getRequestURI(),
                            req.getSession().getAttribute(sessionKeyValue), req.getHeader("User-Agent") });
        }
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    public final String getGeneratedKey(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return ((String) session.getAttribute(this.config.getSessionKey()));
    }

    public final Properties getProperties() {
        return this.config.getProperties();
    }

    static {
        System.setProperty("java.awt.headless", "true");

        logger = LoggerFactory.getLogger(KaptchaExtend.class);
    }
}