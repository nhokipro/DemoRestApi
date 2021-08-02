package com.Producer.ConnectRedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class ConnectionPoolRedis {
    static JedisPool jedisPool;
    private static final String HOST = "127.0.0.1";

    public static JedisPool getJedisPool() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        //maximum number of connections
        poolConfig.setMaxTotal(8);
        //The maximum number of idle connections
        poolConfig.setMaxIdle(8);
        //minimum number of idle connections
        poolConfig.setMinIdle(2);
        //Tests whether connection is dead when connection
        poolConfig.setTestOnBorrow(true);
        //Tests whether connection is dead when returning a
        poolConfig.setTestOnReturn(true);
        //Tests whether connections are dead during idle periods
        poolConfig.setTestWhileIdle(true);
        //The minimum idle time of a resource
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        //Idle connection checking period
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        //Maximum number of connections to test in each idle check
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        jedisPool = new JedisPool(poolConfig, HOST, 6379);
        return jedisPool;
    }
}
