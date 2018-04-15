package edu.nefu.webapp.biz.service;

import edu.nefu.webapp.core.po.User;
import edu.nefu.webapp.core.vo.RoomListVo;

import java.util.Map;

/**
 * 自习室相关的业务逻辑
 */
public interface RoomService {

    /**
     * 获取空闲自习室的列表及其相关信息的业务逻辑
     * @param vo
     * @return
     */
    Map getFreeRoomList(RoomListVo vo, User user);

    /**
     * 获取某个教室当前所有课节的详细信息的业务逻辑
     * @param vo
     * @param user
     * @return
     */
    Map getRoomDetail(RoomListVo vo, User user);

}
