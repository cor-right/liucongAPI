package edu.nefu.webapp.core.po;

/**
 * Linked with Table `t_student`
 */
public class Student {

    private String uid;

    private String studentid;
    private String studentname;
    private String studentpw;

    private String curTerm;
    private int curWeek;

    private String lastRefreshTime;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentpw() {
        return studentpw;
    }

    public void setStudentpw(String studentpw) {
        this.studentpw = studentpw;
    }

    public String getCurTerm() {
        return curTerm;
    }

    public void setCurTerm(String curTerm) {
        this.curTerm = curTerm;
    }

    public int getCurWeek() {
        return curWeek;
    }

    public void setCurWeek(int curWeek) {
        this.curWeek = curWeek;
    }

    public String getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(String lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    @Override
    public String toString() {
        return "Student{" +
                "uid='" + uid + '\'' +
                ", studentid='" + studentid + '\'' +
                ", studentname='" + studentname + '\'' +
                ", studentpw='" + studentpw + '\'' +
                ", curTerm=" + curTerm +
                ", curWeek=" + curWeek +
                ", lastRefreshTime='" + lastRefreshTime + '\'' +
                '}';
    }
}
