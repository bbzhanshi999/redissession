package com.neusoft.redissession.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


public class TokenUtil {
    public static final String TOKEN_HEADER_NAME = "x-auth-token";


    /**
     * 通过请求获得token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String token = null;
        for(int i  =0;cookies!=null&&i<cookies.length;i++){
            if(cookies[i].getName().equals(TOKEN_HEADER_NAME)){
                token = cookies[i].getValue();
            }
        }
        return token;
    }

    /**
     * 向response中设置token
     * @param response
     * @param token
     */
    public static void setToken(HttpServletResponse response, String token){
        response.addCookie(new Cookie(TOKEN_HEADER_NAME,token));
    }

    /**
     * 通知浏览器删除token
     * @param response
     * @param token
     */
    public static void delToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(TOKEN_HEADER_NAME,token);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String  createToken(){
        return UUID.randomUUID().toString();
    }

}
