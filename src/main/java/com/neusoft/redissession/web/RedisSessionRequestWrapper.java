package com.neusoft.redissession.web;

import com.neusoft.redissession.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * request增强类
 */
public class RedisSessionRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private String token;
    private HttpSession session;

    public RedisSessionRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(request);
        this.request = request;
        this.response = response;
        token = TokenUtil.getToken(request);
    }


    @Override
    public HttpSession getSession(boolean create) {
        if (session != null) {
            return session;
        }

        if (create) {
            //如果request cookie中没有
            if (StringUtils.isNotBlank(token)) {

                //如果redis中存在此token，则直接创建一个已有的redisSession
                if (RedisManager.getInstance().getConnection().exists(RedisHttpSession.KEY_PREFIX + token)) {
                    session = RedisHttpSession.createExist(this.getServletContext(), RedisManager.getInstance().getConnection(), token, response);
                    return session;
                    //如果redis中此token已失效，则直接创建新的token新的redisSession（模仿tomcat实现方式）
                } else {
                    session = RedisHttpSession.createNew(this.getServletContext(), RedisManager.getInstance().getConnection(), response);
                    TokenUtil.setToken(response, session.getId());
                    return session;
                }
                //如果request cookie中没有 token，那么直接创建
            } else {
                session = RedisHttpSession.createNew(this.getServletContext(), RedisManager.getInstance().getConnection(), response);
                TokenUtil.setToken(response, session.getId());
                return session;
            }
            //如果create=false
        } else {
            //如果request cookie 含有token
            if (StringUtils.isNotBlank(token)) {
                //如果redis中没有token，就设置cookie中token过期，并且返回null;
                if (!RedisManager.getInstance().getConnection().exists(RedisHttpSession.KEY_PREFIX+token)) {
                    TokenUtil.delToken(response, token);
                    return null;
                }else{
                    //如果redis中有token，就
                    return RedisHttpSession.createExist(this.getServletContext(), RedisManager.getInstance().getConnection(),token, response);
                }
            } else {
                //如果request header中没有token，就直接返回null；
                return null;
            }
        }

    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public String getRequestedSessionId() {
        return token;
    }
}
