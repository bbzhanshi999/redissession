package com.neusoft.redissession.web;

import com.neusoft.redissession.redis.RedisConnection;
import com.neusoft.redissession.redis.RedisPropertyUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.Serializable;
import java.util.*;

public class RedisHttpSession implements HttpSession {

    private static final int DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS = RedisPropertyUtil.getRedisConfig().getExpire();
    public static final String KEY_PREFIX = "session:";
    private static final String SESSION_ATTR_PREFIX = "sessionAttr:";
    private static final String CREATION_TIME = "creationTime";
    private static final String LAST_ACCESSED_TIME = "lastAccessedTime";
    private static final String MAX_INACTIVE_INTERVAL = "maxInactiveInterval";

    private String key;
    private String id;
    private long creationTime;
    private long lastAccessedTime;
    private int maxInactiveInterval;

    private ServletContext servletContext;

    private RedisConnection redisConnection;

    private HttpServletResponse response;


    public RedisConnection getRedisConnection() {
        return redisConnection;
    }

    public void setRedisConnection(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    private RedisHttpSession(ServletContext servletContext, RedisConnection redisConnection, String token, HttpServletResponse response){
        this.redisConnection = redisConnection;
        this.servletContext = servletContext;
        this.response = response;
        id = StringUtils.isNotBlank(token)?token:TokenUtil.createToken();
        key = KEY_PREFIX+id;
        creationTime = StringUtils.isNotBlank(token)? (long) redisConnection.hget(key,CREATION_TIME) :System.currentTimeMillis();
        lastAccessedTime = creationTime;
        maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;
        saveSession();
    }

    /**
     * 创建新的session
     * @param servletContext
     * @param redisConnection
     * @return
     */
    public static RedisHttpSession createNew(ServletContext servletContext, RedisConnection redisConnection, HttpServletResponse response){
        return new RedisHttpSession(servletContext,redisConnection,null,response);
    }

    /**
     * 创建一个已有token的session
     * @param servletContext
     * @param redisConnection
     * @param token
     * @return
     */
    public static RedisHttpSession createExist(ServletContext servletContext, RedisConnection redisConnection,String token,HttpServletResponse response){
        return new RedisHttpSession(servletContext,redisConnection,token,response);
    }


    /**
     * 按照key将session的相关内部属性一并保存到redis中
     */
    private void saveSession() {
        redisConnection.hset(key,CREATION_TIME,creationTime);
        redisConnection.hset(key,LAST_ACCESSED_TIME,lastAccessedTime);
        redisConnection.hset(key,MAX_INACTIVE_INTERVAL,maxInactiveInterval);
        refresh();
    }

    /**
     * 当session被访问或者调用任何方法时，重新设置他的过期时间，利用redis的过期策略模拟session过去策略
     */
    private void refresh() {
        redisConnection.expire(key,maxInactiveInterval);
    }


    public long getCreationTime() {
        return creationTime;
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public HttpSessionContext getSessionContext() {
        return null;
    }

    public Object getAttribute(String name) {
        return redisConnection.hget(key,SESSION_ATTR_PREFIX+name);
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(redisConnection.hkeys(key));
    }

    public String[] getValueNames() {
        return (String[])redisConnection.hkeys(key).toArray();
    }

    public void setAttribute(String name, Object o) {
        redisConnection.hset(key,SESSION_ATTR_PREFIX+name,(Serializable) o);
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        redisConnection.hdel(key,SESSION_ATTR_PREFIX+name);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {
        redisConnection.del(key);
        redisConnection.close();
        TokenUtil.delToken(response,id);
    }

    public boolean isNew() {
        return false;
    }
}
