package edu.nefu.webapp.web;

import edu.nefu.webapp.biz.service.UserService;
import edu.nefu.webapp.common.RestData;
import edu.nefu.webapp.common.utils.JsonUtil;
import edu.nefu.webapp.common.utils.TokenUtil;
import edu.nefu.webapp.core.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户模块
 */
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("api/user")
@RestController
public class UserAPI {

    // service
    private final UserService userService;

    // logger
    private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);

    @Autowired
    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登陆
     *
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "login")
    public RestData userLogin(@RequestBody  User user, HttpServletRequest request) {
        logger.info("POST userLogin : " + JsonUtil.getJsonString(user));
        String token = TokenUtil.getToken(request);
        // 拒绝用户的重复登陆
        if (token != null)
            return new RestData(1, "请先退出登陆再尝试重新登陆");

        // 尝试登陆
        Map data = userService.postLogin(user);

        return data != null ? new RestData(data)
                : new RestData(1, "用户名或密码不匹配");
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "register")
    public RestData userRegister(@RequestBody  User user) {
        logger.info("POST register : " + JsonUtil.getJsonString(user));
        if (user.getUsername().matches("[0-9a-zA-Z]+") == false || user.getUsername().length() < 5) {
            return new RestData(1, "用户名不能包含英文和数字外的字符且长度不小于5");
        }
        if (user.getPassword().matches("[0-9a-zA-Z]+") == false || user.getPassword().length() < 6) {
            return new RestData(1, "密码不能包含英文和数字外的字符且长度不小于6");
        }
        // 注册
        return userService.postRegister(user) == true ? new RestData()
                : new RestData(1, "用户名已存在，请重新选择用户名");
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "logout")
    public RestData userLogout(HttpServletRequest request) {
        String token = TokenUtil.getToken(request);
        if (token == null)
            return new RestData(1, "当前处于未登录状态");
        // 注销用户
        return userService.postLogout(token) ? new RestData() : new RestData(1, "请检查您的登陆状态在执行注销操作");
    }

    /**
     * 用户修改密码
     * @param request
     * @param tempUser
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "info")
    public RestData putPasswd(HttpServletRequest request,@RequestBody User  tempUser) {
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null || tempUser.getPassword() == null)
            return new RestData(1, "请检查您的新密码");
        logger.info("PUT UserPassworrd : " + JsonUtil.getJsonString(user));
        // 进行修改
        return userService.putPasswd(user, tempUser.getPassword()) == true ? new RestData() :
                new RestData(1, "密码修改失败");
    }



}
