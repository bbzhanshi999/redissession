package com.neusoft.redissession.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

/**
 * response增强类
 */
public class RedisSessionResponseWrapper extends HttpServletResponseWrapper {


    public RedisSessionResponseWrapper(HttpServletResponse response,RedisSessionRequestWrapper requestWrapper) {
        super(response);
        //在这里创建responsewrapper时，不直接创建session，而是先看session里是否有token，有的话才根据id获得session
        HttpSession session = requestWrapper.getSession(false);
        if(session!=null){
            String id = session.getId();
            TokenUtil.setToken(response,id);
        }
    }
}
