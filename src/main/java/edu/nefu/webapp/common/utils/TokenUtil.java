package edu.nefu.webapp.common.utils;


import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Util For Token
 */
public class TokenUtil {

    /**
     * 从Cookie中获取token
     * 如果请求中没有token则返回null
     *
     * @param request
     * @return token
     */
    public static String getToken(HttpServletRequest request) {
        System.out.println(request.getHeader("token"));
        return request.getHeader("token");
    }

    /**
     * 创建一个新的token
     * @return token
     */
    public static String newToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
