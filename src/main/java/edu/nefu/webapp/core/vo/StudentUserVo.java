package edu.nefu.webapp.core.vo;

import edu.nefu.webapp.common.utils.DateUtil;

/**
 *
 */
public class StudentUserVo {

    private String studentid;
    private String studentname;
    private String studentpw;
    private String curTerm;
    private int curWeek;

    private String uid;

    private String lastRefreshTime;

    public StudentUserVo () {

    }

    public StudentUserVo(String studentid, String studentname, String studentpw, String curTerm, String uid) {
        this.studentid = studentid;
        this.studentname = studentname;
        this.studentpw = studentpw;
        this.curTerm = curTerm;
        this.uid = uid;
        this.lastRefreshTime = DateUtil.getCurTimeStamp();
    }

    public int getCurWeek() {
        return curWeek;
    }

    public void setCurWeek(int curWeek) {
        this.curWeek = curWeek;
    }

    public String getCurTerm() {
        return curTerm;
    }

    public void setCurTerm(String curTerm) {
        this.curTerm = curTerm;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getStudentpw() {
        return studentpw;
    }

    public void setStudentpw(String studentpw) {
        this.studentpw = studentpw;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(String lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    @Override
    public String toString() {
        return "StudentUserVo{" +
                "studentid='" + studentid + '\'' +
                ", studentname='" + studentname + '\'' +
                ", studentpw='" + studentpw + '\'' +
                ", uid='" + uid + '\'' +
                ", lastRefreshTime='" + lastRefreshTime + '\'' +
                '}';
    }
}
