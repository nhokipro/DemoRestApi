package com.DemoRestApi.ConnectRedis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectionPoolRedisTest {
    @Test
    public void checkConnections() {
        JedisPool pool = ConnectionPoolRedis.getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.set("foo", "bar");
            assertEquals("bar", jedis.get("foo"));
        }
        pool.close();
        assertTrue(pool.isClosed());
    }
}