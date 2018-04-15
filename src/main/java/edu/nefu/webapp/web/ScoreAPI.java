package edu.nefu.webapp.web;

import edu.nefu.webapp.biz.service.ScoreService;
import edu.nefu.webapp.biz.service.StudentService;
import edu.nefu.webapp.biz.service.UserService;
import edu.nefu.webapp.common.RestData;
import edu.nefu.webapp.common.utils.JsonUtil;
import edu.nefu.webapp.common.utils.TokenUtil;
import edu.nefu.webapp.core.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 学分绩点模块
 */
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("api/score")
@RestController
public class ScoreAPI {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);

    // service
    private final UserService userService;
    private final StudentService studentService;
    private final ScoreService scoreService;

    @Autowired
    public ScoreAPI(UserService userService, StudentService studentService, ScoreService scoreService) {
        this.userService = userService;
        this.studentService = studentService;
        this.scoreService = scoreService;
    }


    /**
     * 查询个人学分绩点
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "all")
    public RestData getCurUserAllScroeData(HttpServletRequest request) {
        // 获取用户
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查当前用户登陆状态");
        logger.info("GET getSpecificWeekClassTable User : " + JsonUtil.getJsonString(user));
        // 查看用户是否绑定了教务处信息
        if(studentService.getInfo(user) == null)
            return new RestData(1, "请在操作前绑定您的教务处信息");
        // 执行业务逻辑并返回
        Map data = scoreService.getCurrentStudentScoreData(user);
        return data == null ? new RestData(1, "当前系统繁忙请稍后再试")
                : new RestData(data);
    }

}
