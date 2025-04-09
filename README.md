一、实验环境
Windows10
Java 17
Springboot 3.4.4
Maven 3.9.9
IDEA 2024.1.4
Postman v11.40.1

二、实验思路
主要采用springboot实现前端和后端。前端采用html，通过调用后端springboot控制器中的api来进行人员录入操作。另外编写独立程序，打包为jar文件，可独立运行，并实现增删改、排序等功能。
数据以Json形式存储，springboot数据存放于resource/static/data.json，独立APP数据存放于resource/static/appdata.json。

三、前端网页操作
1、网页概览
 
2、正常录入
 
点击提交后跳转到成功页面
 
返回首页后显示新录入的信息。
 
3、当信息有误时出现提示。
 
 

四、API设计
1. 添加人员
请求方式：POST
URL：http://localhost:8080/api/persons/add
功能：添加一条新的人员记录。	
请求头：
Content-Type: application/json
请求体：
{
  "template": {
    "data": [
      { "name": "name", "value": "张三" },
      { "name": "studentId", "value": "20230001" },
      { "name": "email", "value": "zhangsan@example.com" },
      { "name": "phoneNumber", "value": "13800138000" },
      { "name": "interest", "value": "编程" }
    ]
  }
}
响应格式：
{
  "success": true,
  "message": "添加成功",
  "person": {
    "name": "张三",
    "studentId": "20230001",
    "email": "zhangsan@example.com",
    "phoneNumber": "13800138000",
    "interest": "编程"
  }
}

2. 删除人员
请求方式：DELETE
URL：http://localhost:8080/api/persons/delete/{id}
功能：根据学号删除一条人员记录。
路径参数：
id：学号（如 20230001）
响应格式：
{
  "success": true,
  "message": "删除成功"
}

3. 更新人员
请求方式：PUT
URL：http://localhost:8080/api/persons/update/{id}
功能：根据学号更新一条人员记录。
路径参数：
id：学号（如 20230001）
请求头：
Content-Type: application/json
请求体：
{
  "template": {
    "data": [
      { "name": "name", "value": "张三" },
      { "name": "email", "value": "zhangsan_new@example.com" },
      { "name": "phoneNumber", "value": "13800138001" },
      { "name": "interest", "value": "编程和阅读" }
    ]
  }
}
响应格式：
{
  "success": true,
  "message": "更新成功",
  "person": {
    "name": "张三",
    "studentId": "20230001",
    "email": "zhangsan_new@example.com",
    "phoneNumber": "13800138001",
    "interest": "编程和阅读"
  }
}

4. 获取所有人员
请求方式：GET
URL：http://localhost:8080/api/persons/list
功能：获取所有人员记录。
响应格式：
[
  {
    "name": "张三",
    "studentId": "20230001",
    "email": "zhangsan@example.com",
    "phoneNumber": "13800138000",
    "interest": "编程"
  },
  {
    "name": "李四",
    "studentId": "20230002",
    "email": "lisi@example.com",
    "phoneNumber": "13900139000",
    "interest": "阅读"
  }
]

5. 获取单个人员
请求方式：GET
URL：http://localhost:8080/api/persons/{id}
功能：根据学号获取一条人员记录。
路径参数：
id：学号（如 20230001）
响应格式：
{
  "name": "张三",
  "studentId": "20230001",
  "email": "zhangsan@example.com",
  "phoneNumber": "13800138000",
  "interest": "编程"
}

五、API测试（使用Postman）
1.添加人员
测试http://localhost:8080/api/persons/add

{
  "template": {
    "data": [
      { "name": "name", "value": "张三" },
      { "name": "studentId", "value": "20230001" },
      { "name": "email", "value": "zhangsan@example.com" },
      { "name": "phoneNumber", "value": "13800138000" },
      { "name": "interest", "value": "编程" }
    ]
  }
}

 

2.删除人员
测试http://localhost:8080/api/persons/delete/20230001
Postman测试截图：
 

3.更新人员信息
测试http://localhost:8080/api/persons/update/20230001
 

4.获取所有人员信息
测试http://localhost:8080/api/persons/list
 

5.获取单个人员
测试http://localhost:8080/api/persons/20230001
 

六、自定义资源模板
[ {
  "name" : "李四",
  "studentId" : "20230002",
  "email" : "lisi@example.com",
  "phoneNumber" : "13900139000",
  "interest" : "运动"
}, {
  "name" : "王五",
  "studentId" : "20230003",
  "email" : "wangwu@example.com",
  "phoneNumber" : "13700137000",
  "interest" : "音乐"
}, {
  "name" : "赵六",
  "studentId" : "20230004",
  "email" : "zhaoliu@example.com",
  "phoneNumber" : "13600136000",
  "interest" : "旅行"
}, {
  "name" : "钱七",
  "studentId" : "20230005",
  "email" : "qianqi@example.com",
  "phoneNumber" : "13500135000",
  "interest" : "摄影"
}, {
  "name" : "张三",
  "studentId" : "20230001",
  "email" : "zhangsan_new@example.com",
  "phoneNumber" : "13800138001",
  "interest" : "编程和阅读"
} ]


七、独立运行app
使用maven将客户端App打包为app.jar，位于target目录下。
使用java –jar target/app.jar [options]运行。
为简化命令行代码，创建app.bat。
bat文件内容为
@echo off
java -jar target\app.jar %*
如此在命令行中即可使用该app。下面是功能演示。

1.-h
 
2.-a
 

3.-l 默认为倒序
 
-l ascend
 

4.-d
 
需要使用-i指定学号。

5.-u
 
可以单独更改一部分数据。也可全部更改。
 

6.增删改排序互斥
 

7.-u需要至少一个字段，-a需要全部字段
 

八、注意事项
在pom.xml中，将主文件设置为了PersonApp，因此打包主体为app。
如需打包DaceApplication，则需更改pom.xml中build部分的mainClass
