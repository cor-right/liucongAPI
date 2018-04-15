# WEBAPP

----

<!-- TOC -->

- [1.1 Tips](#11-Tips)
- [2.1 用户模块](#21-用户模块) 
    - [2.1.1 用户登陆](#211-用户登陆)
    - [2.1.2 用户注册](#212-用户登陆)
    - [2.1.3 用户重置密码](#213-用户重置密码)
    - [2.1.4 用户注销](#214-用户注销)
- [2.2 教务处信息模块](#22-教务处信息模块) 
    - [2.2.1 绑定教务处信息](#221-绑定教务处信息)
    - [2.2.2 查询当前绑定的教务处信息](#222-查询当前绑定的教务处信息)
- [2.3 自习室模块](#23-自习室模块) 
    - [2.3.1 获取当天空闲自习室列表](#231-获取当天空闲自习室列表)
    - [2.3.2 获取某个教室当天所有课时的状态](#232-获取某个教室当天所有课时的状态)
- [2.4 课程表模块](#24-课程表模块) 
    - [2.4.1 更新课表](#241-更新课表)
    - [2.4.2 查询课表](#242-查询课表)
    - [2.4.3 查询课程详细信息](#243-查询课程详细信息)
    - [2.4.4 查询指定周的课程](#244-查询指定周的课程)
    - [2.4.5 修改当前周](#245-修改当前周)
- [2.5 学分绩点模块](#24-学分绩点模块) 
    - [2.5.1 查询当前学分绩点](#251-查询当前学分绩点)

    

<!-- /TOC-->

## 1.1 Tips

RestData : 
```json
{
    "code" : 0,
    "message" : "",
    "data" : {
        
    }
}
```

## 2.1 用户模块

### 2.1.1 用户登陆

- POST /api/user/login
- payload
```json
{
  "username" : "admin",
  "password" : "admin"
}
```
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {
    "username" : "admin",
    "head" : "xxx.jpg",
    "token" : "ABCD-EFGH-IJKL-MNOP"
  }
}
```

### 2.1.2 用户注册
- POST /api/user/register
- payload 
```json
{
  "username" : "admin",
  "password" : "admin"
}
```

- return 
```json
{
    "code" : 0,
    "message" : "",
    "data" : {}
}
```


### 2.1.3 用户注销

- POST /api/user/logout
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {}
}
```

### 2.1.4 用户重置密码

- PUT /api/user/info
- payload
```json
{
  "password" : "123456"
}
```
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {}
}
```

## 2.2 教务处信息模块

### 2.2.1 绑定教务处信息
- POST /api/student/info
- payload
```json
{
  "studentid" : "2015214111",
  "studentpw" : "123456789"
}
```
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {
    "studentid" : "",
     "curTerm" : 1,
     "curWeek" : 1,
     "studentname" : "王晓红"
  }
}
```

### 2.2.2 查询当前绑定的教务处信息
- GET /api/student/info
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {
     "username" : "admin",
     "studentname" : "李晓红",
     "studentid" : "2015214111",
     "curTerm" : 1,
     "curWeek" : 1,
     "lastRefresh" : "2011-01-01" 
  }  
}
```

### 2.2.3 修改当前学期
- PUT /api/student/term
- payload
```json
{
  "term" : 8
}
```
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {}
}
```

## 2.3 自习室模块

### 2.3.1 获取当天空闲自习室列表
- GET /api/room/list
- payload
```json
{
  "build" : "城栋楼",
  "level" : 10,
  "classNum" : 1
}
```

- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {
    "roomfree" : 3,
    "freerooms" : [
      "城栋楼1002",
      "城栋楼1003",
      "城栋楼1004"
    ]
  }
}
```

### 2.3.2 获取某个教室当天所有课时的状态
- GET /api/room/detail
- payload
```json
{
  "classRoomName" : "成栋楼430"
}
```
- return
```json
{
  "freeClassNum" : 3,
  "class1" : "占用",
  "class2" : "占用",
  "class3" : "占用",
  "class4" : "占用",
  "class5" : "占用",
  "class6" : "占用",
}
```

## 2.4 课程表模块

### 2.4.1 更新课表
- PUT /api/table/content
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {}
}
```

### 2.4.2 查询课表
- GET /api/table/content
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {
    "classes" : [ // x是星期，y是课节
      [{"name" : "数据结构",  "room" : "丹青422", "classid" : "asdasdasads"},  ...],  // 共七个元素，分别对应这星期n个某节课
      [...],
      [...],
      [...],
      [...],
      [...],
      "curWeek" : 1
    ]
  }
}
```

### 2.4.3 查询课程详细信息
- GET /api/table/detail
- payload 
```json
{
  "classid" : "ASDF465468ASD"
}
```
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {
    "className" : "走进故宫",
    "teacher" : "卓越网课,崔金刚副教授",
    "classRoom" : "丹青楼525",
    "week" : "9,11",
  }
}
```

### 2.4.4 查询指定周的课程
- GET /api/table/week
- payload 
```json
{
  "weeknum" : 5
}
```
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {
    "classes" : [ // x是星期，y是课节
      [{"name" : "数据结构",  "room" : "丹青422", "classid" : "asdasdasads"},  ...],  // 共七个元素，分别对应这星期n个某节课
      [...],
      [...],
      [...],
      [...],
      [...],
      "curWeek" : 1
    ]
  }
}
```

### 2.4.5 修改当前周
- PUT /api/table/week
- payload 
```json
{
  "weeknum" : 4
}
```
- return 
```json
{
  "code" : 0,
  "message" : "",
  "data" : {
  
  }
}
```


## 2.5 学分绩点模块

### 2.5.1 查询当前学分绩点
- GET /api/scord/all
- return 
```json
{
  "code " : 0,
  "message" : "",
  "data" : {
      "avgPoint" : "3.2291"
      "subjectRank" : "39"
      "avg" : "83.84"
      "doingScore" : "18.5"
      "needScore" : "37.0"
      "doneScore" : "121.0"
      "subject" : "[2015]计算机科学与技术"
      "badScore" : "0.0"
      "classRank" : "7"
      "allScore" : "153.5"
  }
}
```