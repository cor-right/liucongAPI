package edu.nefu.webapp.web;

import edu.nefu.webapp.biz.service.StudentService;
import edu.nefu.webapp.biz.service.UserService;
import edu.nefu.webapp.common.RestData;
import edu.nefu.webapp.common.utils.JsonUtil;
import edu.nefu.webapp.common.utils.TokenUtil;
import edu.nefu.webapp.core.po.Student;
import edu.nefu.webapp.core.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 教务处信息模块
 */
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("api/student")
@RestController
public class StudentAPI {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);

    // service
    private final UserService userService;
    private final StudentService studentService;

    @Autowired
    public StudentAPI(UserService userService, StudentService studentService) {
        this.userService = userService;
        this.studentService = studentService;
    }

    /**
     * 学生绑定个人教务处信息到用户上
     * @param student
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "info")
    public RestData postInfo(@RequestBody Student student, HttpServletRequest request) {
        logger.info("POST studentInfo : " + JsonUtil.getJsonString(student));
        // 检查用户登陆状态并获取用户
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查当前用户登陆状态");
        // 进行绑定
        Map data = studentService.postInfo(student, user);
        // 返回
        return data == null ? new RestData(1, "请检查您所输入的教务处信息") :
                new RestData(data);
    }

    /**
     * 查询当前绑定的教务处信息
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "info")
    public RestData getInfo(HttpServletRequest request) {
        // 检查用户登陆状态并获取用户
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查当前用户登陆状态");
        // 尝试获取信息并返回
        Map data = studentService.getInfo(user);
        return data == null ? new RestData(1, "未能获取到" + user.getUsername() + "的相关教务处信息")
                : new RestData(data);
    }

}
