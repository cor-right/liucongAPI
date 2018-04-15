package edu.nefu.webapp.core.mapper;

import edu.nefu.webapp.core.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    /**
     * 查询是否存在用户名和密码匹配的用户
     */
    @Select("SELECT * FROM `t_user` WHERE `username`=#{username} AND `password`=MD5(#{password})")
    User postLogin(User user);

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM `t_user` WHERE `username`=#{username}")
    User selectUserByUsername(String username);

    /**
     * 插入新用户记录
     */
    @Insert("INSERT INTO `t_user` (`uid`, `username`, `password`) VALUES (MD5(#{username}), #{username}, MD5(#{password}))")
    int insertNewUser(User user);

    /**
     * 更新用户的Token并返回
     */
    @Update("UPDATE `t_user` SET `token`=#{token} WHERE `uid`=#{uid}")
    int updateToken(User usre);

    /**
     * 根据token获取用户信息
     */
    @Select("SELECT * FROM `t_user` WHERE `token`=#{token}")
    User getUserByToken(String token);

    /**
     * 修改用户的密码
     */
    @Update("UPDATE `t_user` SET `password`=MD5(#{password}) WHERE `uid`=#{uid}")
    int updatePassword(User user);
}
