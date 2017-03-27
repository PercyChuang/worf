package orj.worf.jms.config;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import orj.worf.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientIdConfig {
    private static final Logger logger = LoggerFactory.getLogger(ClientIdConfig.class);
    private static final Properties props = new Properties();
    private final String clientId;
    private final String durableSubscriptionName;

    public ClientIdConfig(String $clientId) {
        this.clientId = new StringBuilder().append($clientId).append(getMachinePiece())
                .append(props.getProperty("stop.container.listen.port")).toString();
        this.durableSubscriptionName = new StringBuilder().append(this.clientId).append("_X").toString();
        logger.info("clientId:{},durableSubscriptionName:{}", this.clientId, this.durableSubscriptionName);
    }

    private int getMachinePiece() {
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            StringBuilder sb = new StringBuilder(64);
            while (e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e.nextElement();
                sb.append(ni.toString());
            }
            return (sb.toString().hashCode() << 16);
        } catch (Throwable ex) {
            try {
                return (InetAddress.getLocalHost().getHostName().hashCode() << 16);
            } catch (Throwable ex1) {
                logger.warn("Can not get the machine piece from NetworkInterface or InetAddress.");
            }
        }
        return (new Random().nextInt() << 16);
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getDurableSubscriptionName() {
        return this.durableSubscriptionName;
    }

    static {
        try {
            InputStream in = ResourceUtils.getResourceAsStream("container.properties");
            Throwable localThrowable2 = null;
            try {
                props.load(in);
            } catch (Throwable localThrowable1) {
            } finally {
                if (in != null)
                    if (localThrowable2 != null)
                        try {
                            in.close();
                        } catch (Throwable x2) {
                            localThrowable2.addSuppressed(x2);
                        }
                    else
                        in.close();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage());
        }
    }
}
