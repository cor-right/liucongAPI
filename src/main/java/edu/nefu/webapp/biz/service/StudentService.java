package edu.nefu.webapp.biz.service;

import edu.nefu.webapp.core.po.Student;
import edu.nefu.webapp.core.po.User;

import java.util.Map;

/**
 * 学生教务处相关的业务逻辑
 */
public interface StudentService {


    /**
     * 学生教务处信息与用户绑定的业务逻辑
     * @param student
     * @param user
     * @return
     */
    Map postInfo(Student student, User user);

    /**
     * 获取用户绑定的教务处信息的业务逻辑
     * @param user
     * @return
     */
    Map getInfo(User user);


}
