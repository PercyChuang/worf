package orj.worf.nosql.redis;

import org.junit.Test;

import orj.worf.redis.pool.JedisPool;
import orj.worf.redis.pool.JedisPoolBuilder;
import redis.clients.jedis.Jedis;

public class RedisTest {
    @Test
    public void getJedis() throws Exception {
        Jedis jedis = new Jedis("192.168.171.231",6379);
        jedis.set("foo", "bar庄濮向同学的测试哦。。哈哈哈，，，逼B");
        String value = jedis.get("foo");
        System.out.println(value);
    }
    //@Test
    public void jedisPool(){
	JedisPool pool = new JedisPoolBuilder().setUrl("direct://192.168.171.231:6379?poolName=test1&poolSize=5").buildPool();
	Jedis jedis = pool.getResource();
	String value = jedis.get("foo");
        System.out.println(value);
    }
}
