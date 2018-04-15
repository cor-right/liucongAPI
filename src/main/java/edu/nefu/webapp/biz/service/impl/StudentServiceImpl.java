package edu.nefu.webapp.biz.service.impl;

import edu.nefu.webapp.biz.service.StudentService;
import edu.nefu.webapp.biz.spider.NefuSpider;
import edu.nefu.webapp.core.mapper.StudentMapper;
import edu.nefu.webapp.core.po.Student;
import edu.nefu.webapp.core.po.User;
import edu.nefu.webapp.core.vo.StudentUserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class StudentServiceImpl implements StudentService {

    // spider
    private final NefuSpider nefuSpider;

    // mapper
    private final StudentMapper studentMapper;

    public StudentServiceImpl(NefuSpider nefuSpider, StudentMapper studentMapper) {
        this.nefuSpider = nefuSpider;
        this.studentMapper = studentMapper;
    }

    @Override
    public Map postInfo(Student student, User user) {
        // 利用爬虫检查学生的教务处信息是否正确，同时抓取学生姓名
        Map studentdata = nefuSpider.checkStudentAcount(student);
        if (studentdata == null)
            return null;
        student.setStudentname(studentdata.get("studentname").toString());
        student.setCurTerm(studentdata.get("curTerm").toString());
        // 装载数据
        Map<String, Object> data = new HashMap<>();
        data.put("studentid", student.getStudentid());
        data.put("curTerm", 1);
        data.put("curTerm", student.getCurTerm());
        data.put("studentname", student.getStudentname());
        // 将教务处信息绑定到用户上，先尝试修改，修改失败证明数据库中没有当前用户的记录，则直接进行插入
        StudentUserVo vo = new StudentUserVo(student.getStudentid(), studentdata.get("studentname").toString(), student.getStudentpw(), studentdata.get("curTerm").toString(), user.getUid());
        return studentMapper.updateStudentAcount(vo) == 0 ?
                studentMapper.insertStudentAcount(vo) == 0 ? null : data
                : data;
    }

    @Override
    public Map getInfo(User user) {
        // 尝试查询
        Student student = studentMapper.selectStudentbyUID(user.getUid());
        if (student == null)
            return null;
        else {
            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("studentid", student.getStudentid());
            data.put("studentname", student.getStudentname());
            data.put("curTerm", student.getCurTerm());
            data.put("curWeek", student.getCurWeek());
            data.put("lastRefresh", student.getLastRefreshTime());
            return data;
        }
    }

}
