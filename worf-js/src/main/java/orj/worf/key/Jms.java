package orj.worf.key;

public abstract interface Jms
{
  public static final String queueJmsTemplate = "queueJmsTemplate";
  public static final String topicJmsTemplate = "topicJmsTemplate";
  public static final String queueJmsListenerContainer = "queueJmsListenerContainer";
  public static final String abstractQueueMessageListener = "abstractQueueMessageListener";
  public static final String topicJmsListenerContainer = "topicJmsListenerContainer";
  public static final String abstractTopicMessageListener = "abstractTopicMessageListener";
  public static final String kafkaProducer = "kafkaProducer";
}

