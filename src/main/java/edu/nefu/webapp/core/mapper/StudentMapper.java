package edu.nefu.webapp.core.mapper;

import edu.nefu.webapp.core.po.Student;
import edu.nefu.webapp.core.vo.StudentUserVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StudentMapper {

    /**
     * 根据用户的uid修改学生的教务处信息
     */
    @Update("UPDATE `t_student` SET `studentid`=#{studentid}, `studentname`=#{studentname}, `studentpw`=#{studentpw}, `curTerm`=#{curTerm}, `lastRefreshTime`=#{lastRefreshTime} WHERE `uid`=#{uid}")
    int updateStudentAcount(StudentUserVo vo);

    /**
     * 添加学生的教务处信息并和用户绑定
     */
    @Insert("INSERT INTO `t_student` (`uid`, `studentid`, `studentname`, `studentpw`, `curTerm`, `lastRefreshTime`) VALUES (#{uid}, #{studentid}, #{studentname}, #{studentpw}, #{curTerm}, #{lastRefreshTime})")
    int insertStudentAcount(StudentUserVo vo);

    /**
     * 根据用户的UID查询绑定的教务处信息
     */
    @Select("SELECT * FROM `t_student` WHERE `uid`=#{uid}")
    Student selectStudentbyUID(String uid);

    /**
     * 根据studentid和uid设置currentweek
     */
    @Update("UPDATE `t_student` SET `curWeek`=#{curWeek} WHERE `studentid`=#{studentid} AND `uid`=#{uid}")
    int updateStudentCurWeekByStudentID(StudentUserVo vo);

}
