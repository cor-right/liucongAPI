package edu.nefu.webapp.biz.service;

import edu.nefu.webapp.core.po.User;

import java.util.Map;

/**
 * 学生学分绩点成绩相关模块的业务逻辑
 */
public interface ScoreService {

    /**
     * 获取当前用户绑定的教务处信息的学分绩点等信息
     * @param user
     * @return
     */
    Map getCurrentStudentScoreData(User user);

}
