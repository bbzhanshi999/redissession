package com.neusoft.redissession.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截所有请求替换session
 */
public class RedisSessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RedisSessionRequestWrapper requestWrapper = new RedisSessionRequestWrapper((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        filterChain.doFilter(requestWrapper,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
