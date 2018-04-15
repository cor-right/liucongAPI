package edu.nefu.webapp.web;

import edu.nefu.webapp.biz.service.StudentService;
import edu.nefu.webapp.biz.service.TableService;
import edu.nefu.webapp.biz.service.UserService;
import edu.nefu.webapp.common.RestData;
import edu.nefu.webapp.common.utils.JsonUtil;
import edu.nefu.webapp.common.utils.TokenUtil;
import edu.nefu.webapp.core.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 课程表模块
 */
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("api/table")
@RestController
public class TableAPI {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);

    // service
    private final UserService userService;
    private final StudentService studentService;
    private final TableService tableService;

    public TableAPI(UserService userService, StudentService studentService, TableService tableService) {
        this.userService = userService;
        this.studentService = studentService;
        this.tableService = tableService;
    }

    /**
     * 更新当前用户绑定的学生的课表信息
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "content")
    public RestData refreshClassTableContent(HttpServletRequest request) {
        // 获取用户
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查当前用户登陆状态");
        logger.info("PUT RefreshClassTableContent User : " + JsonUtil.getJsonString(user));
        // 查看用户是否绑定了教务处信息
        if(studentService.getInfo(user) == null)
            return new RestData(1, "请在操作前绑定您的教务处信息");
        // 更新并返回结果
        boolean returnValue = tableService.refreshClassTable(user);
        return returnValue == false ? new RestData(1, "请检查您的教务处信息")
                : new RestData();
    }


    /**
     * 获取课表
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "content")
    public RestData getCurrentWeekClassTable(HttpServletRequest request) {
        // 获取用户
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查当前用户登陆状态");
        logger.info("GET GetCurrentWeekClassTable User : " + JsonUtil.getJsonString(user));
        // 查看用户是否绑定了教务处信息
        if(studentService.getInfo(user) == null)
            return new RestData(1, "请在操作前绑定您的教务处信息");
        // 执行业务逻辑并返回
        Map data = tableService.getCurrentWeekClassTable(user);
        return data == null ? new RestData(1, "请在个人中心中手动刷新课表之后再执行操作")
                : new RestData(data);
    }

    /**
     * 获取课程的详细信息
     * @param classid
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "detail")
    public RestData getClassDetailData(String classid, HttpServletRequest request) {
        // 查看classid
        if (classid == null || classid.trim().equals(""))
            return new RestData(1, "请输入有效的CLASSID");
        // 获取用户
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查当前用户登陆状态");
        logger.info("GET GetCurrentWeekClassTable User : " + JsonUtil.getJsonString(user));
        // 查看用户是否绑定了教务处信息
        if(studentService.getInfo(user) == null)
            return new RestData(1, "请在操作前绑定您的教务处信息");
        // 进行查询
        Map data = tableService.getClassDetailData(classid);
        // 返回
        return data == null ? new RestData(1, "请检查您的教务处信息和登陆状态")
                : new RestData(data);
    }


    /**
     * 查询指定周的课程
     * @param weeknum
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "week")
    public RestData getSpecificWeekClassTable(int weeknum, HttpServletRequest request) {
        if (weeknum < 0 )
            return new RestData(1, "请检查您输入的周目");
        // 获取用户
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查当前用户登陆状态");
        logger.info("GET getSpecificWeekClassTable User : " + JsonUtil.getJsonString(user));
        // 查看用户是否绑定了教务处信息
        if(studentService.getInfo(user) == null)
            return new RestData(1, "请在操作前绑定您的教务处信息");
        // 执行业务逻辑并返回
        Map data = tableService.getSpecificWeekClassTable(user, weeknum);
        return data == null ? new RestData(1, "请在个人中心中手动刷新课表之后再执行操作")
                : new RestData(data);
    }

    /**
     * 修改当前周
     * @param weeknum
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "week")
    public RestData putCurrentWeek(@RequestBody String weeknum, HttpServletRequest request) {
        int curweek = 1;
        System.out.println(weeknum);
        try {
            curweek = Integer.parseInt((String)JsonUtil.getMapFromJson(weeknum).get("weeknum"));
            if (curweek < 0 )
                return new RestData(1, "请检查您输入的周目");
        } catch(Exception ee) {
            return new RestData(1, "请输入整数");
        }

        // 获取用户
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查当前用户登陆状态");
        logger.info("GET getSpecificWeekClassTable User : " + JsonUtil.getJsonString(user));
        // 查看用户是否绑定了教务处信息
        if(studentService.getInfo(user) == null)
            return new RestData(1, "请在操作前绑定您的教务处信息");
        // 执行业务逻辑并返回
        return tableService.putCurrentWeek(user, curweek) ? new RestData() :
                new RestData(1, "请假差您的教务处信息绑定情况");
    }

}
