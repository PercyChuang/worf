package orj.worf.jms.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

public class DurableSubscriber extends DefaultMessageListenerContainer {
    private static final Logger logger = LoggerFactory.getLogger(DurableSubscriber.class);

    public void setConcurrentConsumers(int concurrentConsumers) {
        if (concurrentConsumers > 1)
            logger.warn("Do not raise the number of concurrent consumers of durable subscription for a topic, use default '1'");
        else
            super.setConcurrentConsumers(concurrentConsumers);
    }

    public void setMaxConcurrentConsumers(int maxConcurrentConsumers) {
        if (maxConcurrentConsumers > 1)
            logger.warn("Do not raise the number of max concurrent consumers of durable subscription for a topic, use default '1'");
        else
            super.setMaxConcurrentConsumers(maxConcurrentConsumers);
    }

    public void setConcurrency(String concurrency) {
        super.setConcurrency("1");
    }
}
