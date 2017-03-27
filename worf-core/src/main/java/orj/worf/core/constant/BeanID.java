package orj.worf.core.constant;

public interface BeanID {

    /* Database */
    String dataSource = "dataSource";
    String jdbcTemplate = "jdbcTemplate";
    String transactionManager = "transactionManager";
    String ehcacheManager = "cacheManager";

    /* MyBatis */
    String simpleSqlSession = "simpleSqlSession";
    String reuseSqlSession = "reuseSqlSession";
    String batchSqlSession = "batchSqlSession";

    /* Redis */
    String redisTemplate = "redisTemplate";
    String jedisTemplate = "jedisTemplate";
    String shardedJedisTemplate = "shardedJedisTemplate";

    /* JMS */
    String abstractJmsTemplate = "abstractJmsTemplate";
    String abstractMessageListener = "abstractMessageListener";

    /* MongoDB */
    String mongoTemplateFactory = "mongoTemplateFactory";

}
