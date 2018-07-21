package com.neusoft.redissession.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 加载配置文件同时给出配置
 */
public class RedisPropertyUtil {

    private static final String DEFAULT_HOST="127.0.0.1";
    private static final Integer DEFAULT_PORT = 6379;
    private static final String DEFAULT_PASSWORD = null;
    private static final Integer DEFAULT_MAXTOTAL = 10;
    private static final Integer DEFAULT_MAXIDLE = 10;
    private static final Integer DEFAULT_MAXWAIT = 10000;
    private static final Integer DEFAULT_TIMEOUT = 10000;
    private static final Integer DEFAULT_EXPIRE= 1800;

    private static RedisConfig redisConfig;

    public static RedisConfig getRedisConfig(){
        return redisConfig;
    }

    static{
        Properties properties = new Properties();
        try {
            properties.load(RedisPropertyUtil.class.getClassLoader().getResourceAsStream("redis.properties"));
            String port = properties.getProperty("redis.port");
            String host = properties.getProperty("redis.host");
            String password = properties.getProperty("redis.password");
            String maxTotal = properties.getProperty("redis.connectionConfig.maxTotal");
            String maxIdle = properties.getProperty("redis.connectionConfig.maxIdle");
            String maxWait = properties.getProperty("redis.connectionConfig.maxWait");
            String timeout = properties.getProperty("redis.connectionConfig.timeout");
            String expire = properties.getProperty("redis.expire");
            redisConfig = new RedisConfig();
            redisConfig.setHost(StringUtils.isNotBlank(host)?host:DEFAULT_HOST);
            redisConfig.setPort(NumberUtils.isNumber(port)?Integer.valueOf(port):DEFAULT_PORT);
            redisConfig.setPasword(StringUtils.isNotBlank(password)?host:DEFAULT_PASSWORD);
            redisConfig.setMaxTotal(NumberUtils.isNumber(maxTotal)?Integer.valueOf(maxTotal):DEFAULT_MAXTOTAL);
            redisConfig.setMaxIdle(NumberUtils.isNumber(maxIdle)?Integer.valueOf(maxIdle):DEFAULT_MAXIDLE);
            redisConfig.setMaxWait(NumberUtils.isNumber(maxWait)?Integer.valueOf(maxWait):DEFAULT_MAXWAIT);
            redisConfig.setTimeOut(NumberUtils.isNumber(timeout)?Integer.valueOf(timeout):DEFAULT_TIMEOUT);
            redisConfig.setExpire(NumberUtils.isNumber(expire)?Integer.valueOf(expire):DEFAULT_EXPIRE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static final class RedisConfig{
        private String host;
        private Integer port;
        private String pasword;
        private Integer maxTotal;
        private Integer maxIdle;
        private Integer maxWait;
        private Integer timeOut;
        private Integer expire;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getPasword() {
            return pasword;
        }

        public void setPasword(String pasword) {
            this.pasword = pasword;
        }

        public Integer getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(Integer maxTotal) {
            this.maxTotal = maxTotal;
        }

        public Integer getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(Integer maxIdle) {
            this.maxIdle = maxIdle;
        }

        public Integer getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Integer maxWait) {
            this.maxWait = maxWait;
        }

        public Integer getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(Integer timeOut) {
            this.timeOut = timeOut;
        }

        public Integer getExpire() {
            return expire;
        }

        public void setExpire(Integer expire) {
            this.expire = expire;
        }
    }
}
