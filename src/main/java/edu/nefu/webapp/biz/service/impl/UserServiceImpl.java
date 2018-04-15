package edu.nefu.webapp.biz.service.impl;

import edu.nefu.webapp.biz.service.UserService;
import edu.nefu.webapp.common.utils.TokenUtil;
import edu.nefu.webapp.core.mapper.UserMapper;
import edu.nefu.webapp.core.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Map postLogin(User user) {
        // 先查询用户名和密码是否匹配
        user = userMapper.postLogin(user);
        if (user == null)
            return null;
        // 设置用户的信息
        Map<String, Object> data = new HashMap<>(8);
        data.put("username", user.getUsername());
        data.put("head", user.getHeadUrl());
        data.put("token", TokenUtil.newToken());
        // 然后更新用户的token并返回
        user.setToken((String)(data.get("token")));
        return userMapper.updateToken(user) == 0 ? null : data;
    }

    /**
     * 先查找数据库中相同名字的用户，名字不重复就插
     * @param user
     * @return
     */
    @Override
    public boolean postRegister(User user) {
        //  判重
        if (userMapper.selectUserByUsername(user.getUsername()) != null)
            return false;
        // 插入
        return userMapper.insertNewUser(user) == 1 ? true : false;
    }

    @Override
    public boolean postLogout(String token) {
        // 获取当前已登陆的用户
        User user = getUserByToken(token);
        if (user == null)
            return false;
        // 清除用户的token即可达到注销用户的目的
        user.setToken(null);

        return userMapper.updateToken(user) != 0 ? true : false;

    }

    @Override
    public boolean putPasswd(User user, String passwd) {
        user.setPassword(passwd);
        return userMapper.updatePassword(user) == 0 ?
                false : true;
    }

    @Override
    public User getUserByToken(String token) {
        if (token == null || token.trim().equals("") == true)
            return null;
        return userMapper.getUserByToken(token);
    }
}
