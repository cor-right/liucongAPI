
# UserInfo
# t_user

CREATE TABLE `t_user`(
	`uid` CHAR(32) PRIMARY KEY,
	`username` VARCHAR(16) UNIQUE,
	`password` VARCHAR(32) NOT NULL,
	`headUrl` VARCHAR(128),
	`token` CHAR(32)
)ENGINE=INNODB DEFAULT CHARSET='utf8';


# StudentInfo
# t_student

CREATE  TABLE  `t_student` (
  `uid` CHAR(32) PRIMARY KEY ,
  `studentid` CHAR(16) ,
  `studentname` CHAR(16),
  `studentpw` CHAR(32) NOT  NULL ,
  `curTerm` char(16) NOT NULL DEFAULT 1,
  `curWeek` INT(8) NOT NULL DEFAULT 1,
  `lastRefreshTime` CHAR(32)
)ENGINE=INNODB DEFAULT CHARSET='utf8';

# ClassRoom
# t_classroot

CREATE TABLE `t_classroot` (
  `classroomid` ,
  `building` CHAR(16) , # 丹青楼城栋楼锦秀楼
  `num` CHAR(8),  # 门牌号
  `ama` int(1),
  `amb` int(1),
  `pma` int(1),
  `pmb` int(1),
  `nighta` int(1),
  `nightb` int(1)

)ENGINE=INNODB DEFAULT CHARSET='utf8';


# Table
# t_classtable

CREATE  TABLE  `t_classtable` (
  `classid` CHAR(16) PRIMARY KEY,
  `studentid` CHAR(16) ,
  `className` VARCHAR(64),
  `teacher` VARCHAR(32),
  `week` VARCHAR(64),
  `dayInWeek` INT(8),
  `classInDay` INT(8),
  `classRoom` VARCHAR(32)
)ENGINE=INNODB DEFAULT CHARSET='utf8';







