package edu.nefu.webapp.biz.spider.impl;

import edu.nefu.webapp.biz.spider.NefuSpider;
import edu.nefu.webapp.common.utils.DateUtil;
import edu.nefu.webapp.common.utils.MD5Util;
import edu.nefu.webapp.core.po.Student;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import sun.security.provider.MD5;

import javax.print.Doc;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class NefuSpiderImpl implements NefuSpider {

    /**
     * 该方法用于模拟登陆获取jessionid
     * @param student
     * @return
     */
    private String getJessionID(Student student) {
        Map<String, String> getCookies = null;
        try {
            getCookies = Jsoup.connect("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xk/LoginToXk")
            .data("USERNAME", student.getStudentid())
            .data("PASSWORD", student.getStudentpw())
            .method(Connection.Method.POST)
            .execute()
            .cookies();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getCookies.get("JSESSIONID");
    }

    @Override
    public Map checkStudentAcount(Student student) {
        // 根据模拟登陆之后返回的页面来判断是否登陆成功
        String body = "";
        try {
            body = Jsoup.connect("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xk/LoginToXk")
                    .data("USERNAME", student.getStudentid())
                    .data("PASSWORD", student.getStudentpw())
                    .post().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (body.indexOf("登录个人中心") != -1)
            return null; // 登陆失败的页面有这句话
        // 再次模拟登陆，获取JSESSIONID，此时用户名和密码肯定正确
        String jession = getJessionID(student);
        // 然后去请求页面抓取学生姓名
        try {
            Document doc = Jsoup.connect("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/framework/main.jsp")
                    .cookie("JSESSIONID", jession)
                    .get();
            String nameAndId = doc.body().getElementById("Top1_divLoginName").html();    // 王晓红(2015214111)
            if (nameAndId == null)
                return null;
            // 在抓取用户的当前的学期
            doc = Jsoup.connect("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/kbxx/jsjy_query")
                    .cookie("JSESSIONID", jession)
                    .get();
            String curTerm = doc.getElementById("xnxqh")
                    .select("option[selected]")
                    .val();
            Map<String, String> data = new HashMap();
            data.put("studentname", nameAndId.substring(0, nameAndId.indexOf("(")));
            data.put("curTerm", curTerm);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据格式
     * Map<教室名称，空闲列表>
     *     空闲列表：List[星期一0102， 星期一0304， ...，星期三0101， .... ，星期日0708， 星期日0910]
     *     被占用或有课用1表示，可用用0表示
     *     教室名称格式：城栋楼102
     * @param student
     * @return
     */
    @Override
    public Map getFreeClassRoom(Student student) {
        // 首先模拟登陆获取权限
        String jessionid = getJessionID(student);
        if (jessionid == null)
            return null;
        // 学期直接从学生信息中获取，直接去爬教务处的教室借用申请的内嵌页面
        try {
            Document doc = Jsoup.connect("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/kbxx/jsjy_query2")
                    .data("typewhere", "jszq")
                    .data("xnxqh", student.getCurTerm())
                    .data("bjfh", "=")
                    .cookie("JSESSIONID", jessionid)
                    .post();
            // 此时获取的是很多的tr标记，每一个tr里面包含某个教室里面本周所有的课程的td，这里使用1.8的流和lambda来处理，并行提高效率
            Elements trs = doc.select("tr[jsbh]");
            Map<String, Map> data = new HashMap<>();   // Map<教学楼和教室号, 按星期分类的Map>
            trs.stream()    // 这里将爬取到的教室的数据进行整理
                    .forEach(n -> { // n是tr
                        Map<Integer, List> weekMap = new HashMap<>(); // Map<星期几， 按课节排好的标志位>
                        // 首先得到教学楼和教室号
                        Element classnameTd = n.child(0);
                        String buildAndRoomName = classnameTd.toString()
                                .substring(classnameTd.toString().indexOf("id=\"jsids\">") + 11, classnameTd.toString().indexOf("(")).trim();   // 这里字符串类似“文法楼305-4”，“新体育馆1001”
                        // 教务处上的每个星期的教师占用情况是排好的，没被占用也会显示，所以这里利用数字规律来判断是星期几
                        // 每天6堂课，也就是每6个td是一天，那么设置计数器从1开始，(counter - 1) / 6 + 1 即可，星期一计数器是“1~6”，星期二是“7~12”
                        int counter = 1;
                        int week = 1;
                        List<Integer> usedRecord = new LinkedList<>();
                        if (n.children().size() == 43) {    // 证明是正常的需要记录的列, 0~42
                            for (int i = 1; i < 6 * 7 + 1; i++) {
                                // 这里空教室设为0，非空设为1
                                Element classRoom = n.child(i);
                                if (classRoom.html().trim().length() == 0)  // 证明是空教室
                                    usedRecord.add(0);
                                else
                                    usedRecord.add(1);
                                counter++;
                                if (counter != 1 && ((counter - 1) % 6 == 0)) { // 此时证明已经到了下一天，对应counter值为7，13 ....
                                    weekMap.put(week++, usedRecord);   // 放进去
                                    usedRecord = new LinkedList<>();
                                }
                            }
//                            测试代码
//                            weekMap.values().stream()
//                                    .forEach(m -> {
//                                        m.stream()
//                                                .forEach(System.out::print);
//                                        System.out.println();
//                                    });
                            data.put(buildAndRoomName, weekMap);    //循环结束，将当前教室的记录放到最终结果Map中
                        }
                    });
            return data;    // 返回
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据格式 Map<序号，Map<课程属性名，属性值>>
     *     属性：课程名、任课教师、周次、星期数、节次、教室
     *     对应英文：lessonName, teacher, week, dayInWeek, classInDay, classRoom
     *     对应值：“数据结构”，“李艳娟”，“1,2,3,4,5,6,7,8,9”，“2”，“2”，“丹青楼304”
     * @param student
     * @return
     */
    @Override
    public Map getClassTable(Student student) {
        // 首先模拟登陆获取JSESSIONID
        String jessionid = getJessionID(student);
        if (jessionid == null)
            return null;
        // 创建数据总映射
        Map<Integer, Map<String, Object> > returnData = new HashMap<>();
        int counter = 0;
        // 抓取页面
        try {
            Document doc = Jsoup.connect("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xskb/xskb_list.do")
                    .cookie("JSESSIONID", jessionid)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36")
                    .get();
            // 获取课程表表体
            Element classTable = doc.getElementById("kbtable").child(0);
            // 第一个tr是表头，是星期几，不算第一个tr共有6行tr，分别是child(1) ~ child(6)
            for (int classInDay = 1; classInDay < 7; classInDay++)  {   // 这个迭代器是tr的迭代器，迭代的是纵坐标，也就是课节，如“第一二节”、“第三四节”
                Element tr = classTable.child(classInDay);
                // 这里遍历tr的每个行，也就是星期几，同样要跳过第一个td，因为第一个td就是课节名，如“第一二节”、“第三四节”
                for (int dayInWeek = 1; dayInWeek < 8; dayInWeek++) {
                    Element td = tr.child(dayInWeek);   // 这里就是我们要解析的每一个课程的信息
                    // td中class="kbcontent"的<div>中就是我们需要的数据，可以利用br将各个属性分割开
                    Element div = td.getElementsByClass("kbcontent").first();
                    String [] attributes = div.html().toString().split("<br>"); // 获得了每个属性
                    if (attributes.length <= 1) // 这里跳过空的课程
                        continue;
                    List properties = new ArrayList();
                    Arrays.stream(attributes).forEach(n -> {    // 遍历每个属性并规整化，导出成列表
                        n = n.replace("\n", "").replace("</font>", "")
                                .replaceAll("<font.*\">", "").replace("(周)", "");    // 简单的进行处理
                        if (n.contains("周次(节次)"))
                            n = n.replace("<font title=\"周次(节次)\">", "");
//                        System.out.println(n);
                        properties.add(n);
                    });
                    // 遍历整理好的列表按规则加入属性，这里写的冗余一些以便处理
                    Map<String, Object> classData = new HashMap<>();    // 这里是每一节课程的属性映射，可能为4或9个属性
                    // 这里可以正常流程走，会在接下来处理一个格子两节课的情况
                    classData.put("lessonName", properties.get(0));
                    classData.put("teacher", properties.get(1));
                    classData.put("week", DateUtil.formatWeeks((String)properties.get(2)));
                    classData.put("classRoom", properties.get(3));
                    classData.put("dayInWeek", dayInWeek);
                    classData.put("classInDay", classInDay);
                    if (properties.size() == 9) {   // 这里证明还有一节课，第四个数据是一行“---------------------”，所以直接从3跳到5
                        // 提交数据
                        returnData.put(counter++, classData);
                        classData = new HashMap<>();
                        // 继续处理第二节课
                        classData.put("lessonName", properties.get(5));
                        classData.put("teacher", properties.get(6));
                        classData.put("week", DateUtil.formatWeeks((String)properties.get(7)));
                        classData.put("classRoom", properties.get(8));
                        classData.put("dayInWeek", dayInWeek);
                        classData.put("classInDay", classInDay);
                    }
                    // 提交数据
                    returnData.put(counter++, classData);
                }
            }
            // 现在所有课程都已经处理完了，返回数据
            return returnData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 爬的是教务处上的学习完成情况属性
     * 主要爬取的信息包括：
     *      学生的专业、加权平均分、专业排名、班级排名、平均学分绩点
     *      各类学分统计，包括要求学分，已修学分，还需学分，正修读学分，不及格学分，
     *      分别是：subject, avg, subjectRank, classRank, avgPoint, allScore
     *                      , doneScore, needScore, doingScore, badScore
     * @param student
     * @return
     */
    @Override
    public Map getStudentSchoolStatus(Student student) {
        // 首先模拟登陆获取JSESSIONID
        String jessionid = getJessionID(student);
        if (jessionid == null)
            return null;
        // 创建数据总映射
        Map<String, String> returnData = new HashMap<>();
        try {
            // 获取页面
            Document doc = Jsoup.connect("http://jwcnew.nefu.edu.cn/dblydx_jsxsd/xsxj/doQueryXxwcqkKcsx.do")
                    .cookie("JSESSIONID", jessionid)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36")
                    .post();
            // 获取学生的专业名称
            Element title = doc.getElementsByTag("table").get(1);
            returnData.put("subject", title.child(0).child(0).child(0).child(0).html().split("&")[0].trim());   // 放进去
            Element rank = doc.getElementsByTag("table").get(1).nextElementSibling();   // rank
            // avg, subjectRank, classRank, avgPoint, allScore
            String [] datas = rank.toString().substring(5, rank.toString().length() - 6).trim().split("，");
            returnData.put("avg", datas[0].substring(datas[0].indexOf("：") + 1, datas[0].length()).trim());
            returnData.put("subjectRank", datas[1].substring(datas[1].indexOf("：") + 1, datas[1].length()).trim());
            returnData.put("classRank", datas[2].substring(datas[2].indexOf("：") + 1, datas[2].length()).trim());
            returnData.put("avgPoint", datas[3].substring(datas[3].indexOf("：") + 1, datas[3].length()).trim());
            returnData.put("allScore", datas[4].substring(datas[4].indexOf("：") + 1, datas[4].length()).trim());
            // doneScore, needScore, doingScore, badScore
            Element score = doc.getElementsByTag("table").get(2).child(0).child(6);   // rank
            returnData.put("doneScore", score.child(2).child(0).html());
            returnData.put("needScore", score.child(3).child(0).html());
            returnData.put("doingScore", score.child(4).child(0).html());
            returnData.put("badScore", score.child(5).child(0).html());
            return returnData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Test
//    public void test() {
//        Student student = new Student();
//        student.setStudentid("2015214119");
//        student.setStudentpw("zjx199628");
//        student.setCurTerm("2017-2018-2");
//        Map map = getStudentSchoolStatus(student);
//        map.forEach((n ,m) -> {
//            System.out.println(n + " : " + m);
//        });
//    }
//
//    /**
//     * 测试教室占用信息爬取
//     */
//    @Test
//    public void testForClassRoom() {
//        Student student = new Student();
//        student.setStudentid("2015214119");
//        student.setStudentpw("zjx199628");
//        student.setCurTerm("2017-2018-2");
//        Map map = getFreeClassRoom(student);
//        Map data = (Map)map.get("成栋楼303");
//        for (Object week : data.keySet()) {
//            System.out.print(week + " : ");
//            List list = (List) data.get(((Integer)week));
//            list.stream().forEach(System.out::print);
//            System.out.println();
//        }
//    }
//
//    /**
//     * 测试课表爬取
//     */
//    @Test
//    public void testForClassTable() {
//        Student student = new Student();
//        student.setStudentid("2015214119");
//        student.setStudentpw("zjx199628");
//        student.setCurTerm("2017-2018-2");
//        Map map = getClassTable(student);
//        map.keySet().stream()
//                .forEach(n -> {
//                    System.out.print(n + " : ");
//                    Map<String, Object> data = (Map<String, Object>) map.get(n);
//                    data.keySet().stream()
//                            .forEach(m -> {
//                                System.out.print(m + "-");
//                                System.out.println(data.get(m));
//                            });
//                    System.out.println();
//                });
//    }
//



//    @Test
//    public void test() {
//        String testStr = "<input aaa='sss' >\"城栋楼201    \"";
//        System.out.println(testStr.replaceAll("<.*>", "").replace("\"", "").trim());
//    }

}
