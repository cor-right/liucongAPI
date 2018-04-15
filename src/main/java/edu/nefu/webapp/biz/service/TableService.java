package edu.nefu.webapp.biz.service;

import edu.nefu.webapp.core.po.User;

import java.util.Map;

/**
 * 课表相关的业务逻辑
 */
public interface TableService {

    /**
     * 刷新已绑定教务处账号的用户的个人课表信息并保存到数据库的业务逻辑
     * @param user
     * @return success/fail
     */
    boolean refreshClassTable(User user);

    /**
     * 获取用户绑定学生的当前周的课表的业务逻辑
     * @param user
     * @return map
     */
    Map getCurrentWeekClassTable(User user);

    /**
     * 获取某个课程的详细信息
     * @param classid
     * @return
     */
    Map getClassDetailData(String classid);

    /**
     * 获取指定星期的课程
     * @param user
     * @param weeknum
     * @return
     */
    Map getSpecificWeekClassTable(User user, int weeknum);

    /**
     * 修改当前用户绑定的学生信息的当前周字段
     * @param user
     * @param weeknum
     * @return
     */
    boolean putCurrentWeek(User user, int weeknum);

}
