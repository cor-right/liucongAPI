package edu.nefu.webapp.biz.service.impl;

import edu.nefu.webapp.biz.service.TableService;
import edu.nefu.webapp.biz.spider.NefuSpider;
import edu.nefu.webapp.common.utils.DateUtil;
import edu.nefu.webapp.common.utils.MD5Util;
import edu.nefu.webapp.core.mapper.StudentMapper;
import edu.nefu.webapp.core.mapper.TableMapper;
import edu.nefu.webapp.core.po.Student;
import edu.nefu.webapp.core.po.Table;
import edu.nefu.webapp.core.po.User;
import edu.nefu.webapp.core.vo.StudentUserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 课表是需要用户手动发送指令去更新
 * 更新之后存到数据库里
 * 查询都是从数据库中查询
 */
@Transactional
@Service
public class TableServiceImpl implements TableService {

    // spider
    private final NefuSpider nefuSpider;

    // mapper
    private final TableMapper tableMapper;
    private final StudentMapper studentMapper;

    public TableServiceImpl(NefuSpider nefuSpider, TableMapper tableMapper, StudentMapper studentMapper) {
        this.nefuSpider = nefuSpider;
        this.tableMapper = tableMapper;
        this.studentMapper = studentMapper;
    }

    /**
     * 利用爬虫去教务处上爬取个人信息
     * 爬虫代码在spider中
     * 然后对返回的数据进行解析
     * @param user
     * @return
     *
     */
    @Override
    public boolean refreshClassTable(User user) {
        // 获得当前用户绑定的教务处信息对象
        Student student = studentMapper.selectStudentbyUID(user.getUid());
        // 进行爬取
        Map<String, Map<String, Object>> data = nefuSpider.getClassTable(student);
        // 先删除当前学生在数据库中的所有课程信息
        tableMapper.deleteStudentClassData(student.getStudentid());
        // 之后解析得到的数据并逐条存入数据库
        data.entrySet().stream().forEach(entry -> {    // 遍历得到的映射的每一条数据
            Map<String, Object> record = entry.getValue();
            Table table = new Table();
            table.setStudentid(student.getStudentid());
            table.setClassName((String)record.get("lessonName"));
            table.setTeacher((String)record.get("teacher"));
            table.setWeek((String)record.get("week"));
            table.setClassRoom((String)record.get("classRoom"));
            table.setDayInWeek((int)record.get("dayInWeek"));
            table.setClassInDay((int)record.get("classInDay"));
            // 构造classid并保存
            String idstr = student.getStudentid() + DateUtil.getCurTimeStamp() + String.valueOf(Math.random());
            String classid = MD5Util.MD5(idstr).substring(0, 16);
            table.setClassid(classid);
            tableMapper.insertNewRecord(table);
        });
        return true;
    }

    /**
     * 这里从数据库中查询学生的课表，如果没有的话就先更新
     * @param user
     * @return
     */
    @Override
    public Map getCurrentWeekClassTable(User user) {
        // 获得当前用户绑定的教务处信息对象
        Student student = studentMapper.selectStudentbyUID(user.getUid());
        // 获取课表
        List<Table> classes = tableMapper.selectClassRecordsByStudentID(student.getStudentid());
        // 如果没有获取成功就更新一次课表在获取
        if (classes.size() == 0) {
            if (refreshClassTable(user) == false)
                return null;
        }
        // 这里进行解析并构造返回的数据
        List<List<Map<String, String>>> returnData = new ArrayList<>(8);
        // 设定返回列表的初始值，为了保证没有课的时候也能占位
        for (int i = 0; i < 6; i++) {
            List<Map<String, String>> innerData = new ArrayList<>(8);
            for (int j = 0; j < 7; j++) {
                innerData.add(new HashMap<String, String>());
            }
            returnData.add(innerData);
        }
        classes.stream().forEach(lesson -> {
            List<String> weeks =  Arrays.stream(lesson.getWeek().split(",")).collect(Collectors.toList());
            if (weeks.contains(String.valueOf(student.getCurWeek()))) { // 判断当前周有没有这个课，当前周是从学生手动设置的信息中都出来的
                weeks.stream().forEach(n -> {
                    System.out.print(n);
                });
                List<Map<String, String>> innerData = returnData.get(lesson.getClassInDay() - 1);
                Map<String, String> data = new HashMap<>();
                data.put("name", lesson.getClassName());
                data.put("room", lesson.getClassRoom());
                data.put("classid", lesson.getClassid());
                innerData.set(lesson.getDayInWeek() - 1, data);
            }
        });
        // 设置返回值并返回
        Map<String, Object> data = new HashMap<>();
        data.put("curWeek", student.getCurWeek());
        data.put("classes", returnData);
        return data;
    }

    /**
     * 直接使用id查询
     * @param classid
     * @return
     */
    @Override
    public Map getClassDetailData(String classid) {
        Table record = tableMapper.selectClassRecordByClassID(classid);
        if (record == null)
            return null;
        Map<String, String> data = new HashMap<>();
        data.put("classRoom", record.getClassRoom());
        data.put("teacher", record.getTeacher());
        data.put("week", record.getWeek());
        data.put("className", record.getClassName());
        return data;
    }

    /**
     * 业务逻辑和上边的某个方法很相似
     * @param user
     * @param weeknum
     * @return
     */
    @Override
    public Map getSpecificWeekClassTable(User user, int weeknum) {
        // 获得当前用户绑定的教务处信息对象
        Student student = studentMapper.selectStudentbyUID(user.getUid());
        student.setCurWeek(weeknum);
        // 获取课表
        List<Table> classes = tableMapper.selectClassRecordsByStudentID(student.getStudentid());
        // 如果没有获取成功就更新一次课表在获取
        if (classes.size() == 0) {
            if (refreshClassTable(user) == false)
                return null;
        }
        // 这里进行解析并构造返回的数据
        List<List<Map<String, String>>> returnData = new ArrayList<>(8);
        // 设定返回列表的初始值，为了保证没有课的时候也能占位
        for (int i = 0; i < 6; i++) {
            List<Map<String, String>> innerData = new ArrayList<>(8);
            for (int j = 0; j < 7; j++) {
                innerData.add(new HashMap<String, String>());
            }
            returnData.add(innerData);
        }
        classes.stream().forEach(lesson -> {
            List<String> weeks =  Arrays.stream(lesson.getWeek().split(",")).collect(Collectors.toList());
            if (weeks.contains(String.valueOf(student.getCurWeek()))) { // 判断当前周有没有这个课，当前周是从学生手动设置的信息中都出来的
                weeks.stream().forEach(n -> {
                    System.out.print(n);
                });
                List<Map<String, String>> innerData = returnData.get(lesson.getClassInDay() - 1);
                Map<String, String> data = new HashMap<>();
                data.put("name", lesson.getClassName());
                data.put("room", lesson.getClassRoom());
                data.put("classid", lesson.getClassid());
                innerData.set(lesson.getDayInWeek() - 1, data);
            }
        });
        // 设置返回值并返回
        Map<String, Object> data = new HashMap<>();
        data.put("curWeek", student.getCurWeek());
        data.put("classes", returnData);
        return data;
    }

    /**
     * 修改当前周
     * @param user
     * @param weeknum
     * @return
     */
    @Override
    public boolean putCurrentWeek(User user, int weeknum) {
        // 获得当前用户绑定的教务处信息对象
        Student student = studentMapper.selectStudentbyUID(user.getUid());
        StudentUserVo vo = new StudentUserVo();
        // 修改信息
        vo.setUid(user.getUid());
        vo.setStudentid(student.getStudentid());
        vo.setCurWeek(weeknum);
        return studentMapper.updateStudentCurWeekByStudentID(vo) == 1 ? true  : false;
    }

}
