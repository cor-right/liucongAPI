package edu.nefu.webapp.biz.service;

import edu.nefu.webapp.core.po.User;

import java.util.Map;

/**
 * 用户相关的业务逻辑
 */
public interface UserService {

    /**
     * 用户登陆的业务逻辑
     * @param user
     * @return data
     */
    Map postLogin(User user);

    /**
     * 用户注册的业务逻辑
     * @param user
     * @return
     */
    boolean postRegister(User user);

    /**
     * 用户注销的业务逻辑
     * @param token
     * @return true/false
     */
    boolean postLogout(String token);

    /**
     * 用户修改密码时的业务逻辑
     * @param user
     * @param passwd
     * @return true/false
     */
    boolean putPasswd(User user, String passwd);


    //


    /**
     * 根据Token获取用户的业务逻辑
     * @param token
     * @return user
     */
    User getUserByToken(String token);

}
