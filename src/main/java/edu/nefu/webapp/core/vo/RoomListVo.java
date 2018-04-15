package edu.nefu.webapp.core.vo;

/**
 * 用于进行自习室查询时的VO
 */
public class RoomListVo {

    private String build;   // 教学楼，三种取值：城栋楼，丹青楼，锦绣楼
    private int level;  // 楼层
    private int classNum;  // 课节，12节算1，23节算2，以此类推，一天6节课

    private String classRoomName;   // 查看某个教室具体信息的时候用

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }
}
