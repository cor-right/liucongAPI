package edu.nefu.webapp.biz.service.impl;

import edu.nefu.webapp.biz.service.ScoreService;
import edu.nefu.webapp.biz.spider.NefuSpider;
import edu.nefu.webapp.core.mapper.StudentMapper;
import edu.nefu.webapp.core.po.Student;
import edu.nefu.webapp.core.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ScoreServiceImpl implements ScoreService{

    // mapper
    private final StudentMapper studentMapper;

    // spider
    private final NefuSpider nefuSpider;

    @Autowired
    public ScoreServiceImpl(StudentMapper studentMapper, NefuSpider nefuSpider) {
        this.studentMapper = studentMapper;
        this.nefuSpider = nefuSpider;
    }

    /**
     * 这里不使用数据库，直接爬取最新的
     * @param user
     * @return
     */
    @Override
    public Map getCurrentStudentScoreData(User user) {
        // 获得当前用户绑定的教务处信息对象
        Student student = studentMapper.selectStudentbyUID(user.getUid());
        // 利用爬虫获得我们想要的信息
        return nefuSpider.getStudentSchoolStatus(student);
    }
}
