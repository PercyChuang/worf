package orj.worf.security.session;

import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SessionProtection {
    private static final Logger logger = LoggerFactory.getLogger(SessionProtection.class);

    private static boolean migrateSessionAttributes = true;

    public static HttpSession applySessionFixation(HttpServletRequest request) {
        HttpSession oldSession = request.getSession();
        String originalSessionId = oldSession.getId();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder().append("Invalidating session with Id '").append(originalSessionId)
                    .append("' ").append((migrateSessionAttributes) ? "and" : "without")
                    .append(" migrating attributes.").toString());
        }

        Map attributesToMigrate = extractAttributes(oldSession);
        oldSession.invalidate();
        HttpSession newSession = request.getSession(true);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuilder().append("Started new session: ").append(newSession.getId()).toString());
        }
        transferAttributes(attributesToMigrate, newSession);
        return newSession;
    }

    private static Map<String, Object> extractAttributes(HttpSession session) {
        Map attributesToMigrate = null;
        if (migrateSessionAttributes) {
            attributesToMigrate = new ConcurrentHashMap();
            Enumeration enumer = session.getAttributeNames();
            while ((enumer != null) && (enumer.hasMoreElements())) {
                String key = (String) enumer.nextElement();
                attributesToMigrate.put(key, session.getAttribute(key));
            }
        }
        return attributesToMigrate;
    }

    private static void transferAttributes(Map<String, Object> attributes, HttpSession newSession) {
        if (attributes != null)
            for (Map.Entry entry : attributes.entrySet())
                newSession.setAttribute((String) entry.getKey(), entry.getValue());
    }

    public static void setMigrateSessionAttributes(boolean migrateSessionAttributes) {
        migrateSessionAttributes = migrateSessionAttributes;
    }
}