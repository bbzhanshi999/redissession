package com.neusoft.redissession.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 管理redispool，通过配置文件组装redisPool
 */
public class RedisManager {

    private JedisPool jedisPool;

    /**
     * 声明单例redisManager
     */
    private static final RedisManager instance= new RedisManager();

    /**
     * 单例需要私有化构造函数
     */
    private RedisManager(){
        RedisPropertyUtil.RedisConfig redisConfig = RedisPropertyUtil.getRedisConfig();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
        jedisPoolConfig.setMaxIdle(redisConfig.getMaxIdle());

        //如果赋值为-1，则表示不限制；如果pool已经分配了maxTotal个jedis实例，则此时pool的状态为exhausted(耗尽)。
        jedisPoolConfig.setMaxTotal(redisConfig.getMaxTotal());
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getMaxWait());
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        jedisPoolConfig.setTestOnBorrow(true);

        jedisPool = new JedisPool(jedisPoolConfig,redisConfig.getHost(),redisConfig.getPort(),redisConfig.getTimeOut(),redisConfig.getPasword());

    }

    /**
     * 单例返回redisManager连接
     * @return
     */
    public static RedisManager  getInstance(){
        return instance;
    }

    /**
     * 通过jedisPool返回一个连接
     * @return
     */
    public RedisConnection getConnection(){
        return new SingleRedisConnection(jedisPool.getResource());
    }




}
