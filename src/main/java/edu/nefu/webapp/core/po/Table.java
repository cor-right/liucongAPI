package edu.nefu.webapp.core.po;

/**
 * Linked with Table `t_classtable`
 */
public class Table {

    private String classid;
    private String studentid;
    private String className;
    private String teacher;
    private String week;
    private int dayInWeek;
    private int classInDay;
    private String classRoom;

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(int dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    public int getClassInDay() {
        return classInDay;
    }

    public void setClassInDay(int classInDay) {
        this.classInDay = classInDay;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    @Override
    public String toString() {
        return "Table{" +
                "classid='" + classid + '\'' +
                ", studentid='" + studentid + '\'' +
                ", className='" + className + '\'' +
                ", teacher='" + teacher + '\'' +
                ", week='" + week + '\'' +
                ", dayInWeek=" + dayInWeek +
                ", classInDay=" + classInDay +
                ", classRoom='" + classRoom + '\'' +
                '}';
    }
}
