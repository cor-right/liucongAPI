package edu.nefu.webapp.web;

import edu.nefu.webapp.biz.service.RoomService;
import edu.nefu.webapp.biz.service.StudentService;
import edu.nefu.webapp.biz.service.UserService;
import edu.nefu.webapp.common.RestData;
import edu.nefu.webapp.common.utils.JsonUtil;
import edu.nefu.webapp.common.utils.TokenUtil;
import edu.nefu.webapp.core.po.User;
import edu.nefu.webapp.core.vo.RoomListVo;
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
 * 自习室模块
 */
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RequestMapping("api/room")
@RestController
public class RoomAPI {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);

    // service
    private final UserService userService;
    private final StudentService studentService;
    private final RoomService roomService;

    @Autowired
    public RoomAPI(UserService userService, StudentService studentService, RoomService roomService) {
        this.userService = userService;
        this.studentService = studentService;
        this.roomService = roomService;
    }

    /**
     * 获取符合条件的所有空闲的自习室
     *
     * @param vo
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "list")
    public RestData getList(RoomListVo vo, HttpServletRequest request) {
        // 用户状态检查
        logger.info("GET ROOM getList : " + JsonUtil.getJsonString(vo));
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查您的登陆状态");
        if(studentService.getInfo(user) == null)
            return new RestData(1, "请在操作前绑定您的教务处信息");
        // 数据输入范围检查
        String build = vo.getBuild();
        if (build == null || (!build.equals("丹青楼") && !build.equals("锦绣楼") && !build.equals("成栋楼")))
            return new RestData(1, "请检查您的教学楼输入是否符合规范");
        if (vo.getClassNum() > 6 || vo.getClassNum() < 1)
            return new RestData(1, "请检查您的课节数输入是否符合规范");
        Map data = roomService.getFreeRoomList(vo, user);
        return data == null ? new RestData(1, "未能成功查询到可用的自习室")
                : new RestData(data);
    }

    /**
     * 获取某个教室当天的详细占用情况
     * @param vo
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "detail")
    public RestData getDetail(RoomListVo vo, HttpServletRequest request) {
        // 用户状态检查
        logger.info("GET ROOM getList : " + JsonUtil.getJsonString(vo));
        User user = userService.getUserByToken(TokenUtil.getToken(request));
        if (user == null)
            return new RestData(1, "请检查您的登陆状态");
        if(studentService.getInfo(user) == null)
            return new RestData(1, "请在操作前绑定您的教务处信息");
        //
        Map data = roomService.getRoomDetail(vo, user);
        return data == null ? new RestData(1, "请检查您的教室名是否规范")
                : new RestData(data);
    }


}
