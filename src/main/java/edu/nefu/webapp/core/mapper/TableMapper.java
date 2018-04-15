package edu.nefu.webapp.core.mapper;

import edu.nefu.webapp.core.po.Table;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TableMapper {

    /**
     * 插入一条课程信息
     */
    @Insert("INSERT INTO `t_classtable` VALUES (#{classid}, #{studentid}, #{className}, #{teacher}, #{week}, #{dayInWeek}, #{classInDay}, #{classRoom})")
    int insertNewRecord(Table table);

    /**
     * 删除用户的所有相关课程信息
     */
    @Delete("DELETE FROM `t_classtable` WHERE `studentid`=#{studentid}")
    int deleteStudentClassData(String studentid);

    /**
     * 查询某个用户绑定的学生所有的课程信息
     */
    @Select("SELECT * FROM `t_classtable` WHERE  `studentid`=#{studentid}")
    List<Table> selectClassRecordsByStudentID(String studentid);

    /**
     * 根据classid查询某个课程的详细信息
     */
    @Select("SELECT * FROM `t_classtable` WHERE `classid`=#{classid}")
    Table selectClassRecordByClassID(String classid);



}
