package edu.nefu.webapp.biz.service.impl;

import edu.nefu.webapp.biz.service.RoomService;
import edu.nefu.webapp.biz.spider.NefuSpider;
import edu.nefu.webapp.common.utils.DateUtil;
import edu.nefu.webapp.core.mapper.StudentMapper;
import edu.nefu.webapp.core.po.Student;
import edu.nefu.webapp.core.po.User;
import edu.nefu.webapp.core.vo.RoomListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 自习室的业务并没有为他准备DAO
 * 自习室相关的数据源都来自爬虫爬取的教务处的信息
 * 数据库中不存在自习室的表
 */
@Transactional
@Service
public class RoomServiceImpl implements RoomService{

    // spider
    private final NefuSpider nefuSpider;

    // service
    private final StudentMapper studentMapper;

    @Autowired
    public RoomServiceImpl(NefuSpider nefuSpider, StudentMapper studentMapper) {
        this.nefuSpider = nefuSpider;
        this.studentMapper = studentMapper;
    }


    /**
     * 这里对数据解析的时候比较复杂，所以代码冗余一些以便分析逻辑
     * @param vo
     * @param user
     * @return
     */
    @Override
    public Map getFreeRoomList(RoomListVo vo, User user) {
        // 获得当前用户绑定的教务处信息对象
        Student student = studentMapper.selectStudentbyUID(user.getUid());
        // 分析用户的需求
        String build = vo.getBuild();
        int level = vo.getLevel();
        int classNum = vo.getClassNum();
        // 别的不说，先把数据拿下来
        Map<String, Map> data = nefuSpider.getFreeClassRoom(student);
        // 分析教室名称规律，最后两位为楼层内编号，前三字符为教学楼名称，则中间1~2位位楼层，这里依然使用1.8的流和lambda处理
        List<String> rooms = new LinkedList<>();
        data.keySet().stream()
                .forEach(roomName -> {
                    if (roomName.startsWith(build) &&  Integer.parseInt(roomName.substring(3, roomName.length() - 2)) == level) {  // 证明该教室的教学楼和楼层都符合
                        Map<Integer, List<Integer>> weekMap = data.get((Object)roomName);
                        weekMap.keySet().stream().forEach(System.out::println);
                        List classes = weekMap.get(DateUtil.getCurDayInWeek());// 获取今天对应的课程表
                        if ((Integer)classes.get(classNum - 1) == 0) {    // 判断选定的那节课是否是空的
                            rooms.add(roomName);    // 空的就加进去
                        }
                    }
                });
        // 返回
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("roomfree", rooms.size());
        returnData.put("freerooms", rooms);
        return returnData;
    }

    @Override
    public Map getRoomDetail(RoomListVo vo, User user) {
        System.out.println("into service");
        // 获得当前用户绑定的教务处信息对象
        Student student = studentMapper.selectStudentbyUID(user.getUid());
        // 分析用户的需求
        String build = vo.getBuild();
        int level = vo.getLevel();
        int classNum = vo.getClassNum();
        // 数据拿下来
        Map<String, Map> data = nefuSpider.getFreeClassRoom(student);
        // 遍历数据找我们要的教室
        Map<String, Object> returnData = new HashMap<>();
        data.keySet().stream()
                .forEach(roomName -> {
                    if (roomName.equals(vo.getClassRoomName())) {   // 找到了
                        Map<Integer, List<Integer>> weekMap = data.get((Object)roomName);
                        List classes = weekMap.get(DateUtil.getCurDayInWeek());// 获取今天对应的课程表
                        int counter = 0;
                        for (int i = 0; i < classes.size(); i++) {
                            String key = "class" + String.valueOf(i + 1) ;
                            String status = "占用";
                            if ((int)classes.get(i) == 0) {
                                status = "空闲";
                                counter++;
                            }
                            returnData.put(key, status);
                        }
                        returnData.put("freeClassNum", counter);
                    }

                });

        return returnData.get("freeClassNum") == null ? null : returnData;
    }
}
