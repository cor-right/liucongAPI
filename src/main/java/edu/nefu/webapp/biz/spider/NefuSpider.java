package edu.nefu.webapp.biz.spider;

import edu.nefu.webapp.core.po.Student;

import java.util.Map;

/**
 * 针对东北林业大学教务处的爬虫
 */
public interface NefuSpider {

    /**
     * 检查学生的教务处信息是否正确
     * 原理是：模拟登陆教务处，检查状态码，如果登陆成功就返回学生的姓名和学期
     * @param student
     * @return
     */
    Map checkStudentAcount(Student student);

    /**
     * 获取当前周空间教室的列表
     * 原理是：模拟登陆教务处，进入教室借用申请页面进行爬取，对数据进行筛选之后按格式放到map中
     * @param student
     * @return
     */
    Map getFreeClassRoom(Student student);

    /**
     * 获取教务处上的课程表并且按格式返回
     * @param student
     * @return
     */
    Map getClassTable(Student student);

    /**
     * 获取教务处上的修读情况审核报告并返回
     * @param student
     * @return
     */
    Map getStudentSchoolStatus(Student student);

}
