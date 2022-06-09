/*
Navicat MySQL Data Transfer

Source Server         : data
Source Server Version : 50611
Source Host           : localhost:3306
Source Database       : stu_learning_system

Target Server Type    : MYSQL
Target Server Version : 50611
File Encoding         : 65001

Date: 2019-02-14 23:26:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `a_id` int(10) NOT NULL AUTO_INCREMENT,
  `a_acc` char(16) DEFAULT NULL,
  `a_pwd` char(16) DEFAULT NULL,
  `a_type` enum('0','1') DEFAULT '0',
  `d_code` char(4) DEFAULT NULL,
  PRIMARY KEY (`a_id`),
  KEY `d_code` (`d_code`),
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`d_code`) REFERENCES `departments` (`d_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin', 'admin', '1', '3101');
INSERT INTO `admin` VALUES ('2', 'shidi', 'shidi', '0', '3102');
INSERT INTO `admin` VALUES ('3', 'super', '111', '1', '3101');

-- ----------------------------
-- Table structure for chapter
-- ----------------------------
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
  `ch_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `ch_num` int(4) DEFAULT NULL COMMENT '章节数(第几章节)',
  `ch_name` varchar(30) DEFAULT NULL COMMENT '章节名',
  `c_id` int(10) DEFAULT NULL COMMENT '课程id(外键)',
  PRIMARY KEY (`ch_id`),
  KEY `chapter_ibfk_1` (`c_id`),
  CONSTRAINT `chapter_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chapter
-- ----------------------------
INSERT INTO `chapter` VALUES ('1', '1', '第一章', '2');
INSERT INTO `chapter` VALUES ('2', '2', '第二章', '2');
INSERT INTO `chapter` VALUES ('3', '3', '第三章', '2');
INSERT INTO `chapter` VALUES ('4', '4', '第四章', '2');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `c_id` int(10) NOT NULL AUTO_INCREMENT,
  `c_name` varchar(255) DEFAULT NULL COMMENT '课程名',
  `c_credits` float(2,1) DEFAULT NULL COMMENT '学分',
  `c_introduce` varchar(255) DEFAULT NULL COMMENT '简介',
  `c_start_study_time` date DEFAULT NULL COMMENT '学习结束时间',
  `c_stop_study_time` date DEFAULT NULL,
  `c_sort_class` enum('选修','必修') DEFAULT NULL COMMENT '课程类型',
  `c_cover_path` varchar(765) DEFAULT NULL COMMENT '封面路径',
  `c_target` varchar(765) DEFAULT NULL,
  PRIMARY KEY (`c_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('2', '心理学', '3.0', '红红火火恍恍惚惚哈哈哈哈', '2016-10-29', '2016-12-01', '选修', 'class_cover_img\\1.jpg', '我是课程目标');
INSERT INTO `course` VALUES ('3', '管理学', '2.5', 'SDKSDKSDKSDK', '2016-01-28', '2016-10-28', '必修', 'class_cover_img\\2.jpg', 'SDK');
INSERT INTO `course` VALUES ('4', '操作系统', '2.0', '操作系统你好', '2016-09-26', '2016-09-28', '必修', 'D:\\MyEclipse\\apache-tomcat-7.0.72\\webapps\\StudentSystem\\class_cover_img\\$K~K2GA2E_2S5SIOR])C7)K.jpg', '全都挂科');
INSERT INTO `course` VALUES ('10', '编译原理', '1.0', '很重要', '2016-09-28', '2016-10-29', '必修', 'class_cover_img/6b6e567cjw1dnfbcrb7zkj.jpg', '理解程序运行过程');
INSERT INTO `course` VALUES ('11', '国际贸易规则', '1.0', '这是简介', '2016-11-01', '2016-11-22', '必修', 'class_cover_img\\$K~K2GA2E_2S5SIOR])C7)K.jpg', '这是目标');
INSERT INTO `course` VALUES ('12', '会计基础', '1.0', '这是简介', '2016-11-16', '2016-11-30', '必修', 'class_cover_img\\~HC%[~93DABD84`W8]90T85.jpg', '这是目标');
INSERT INTO `course` VALUES ('13', '高级会计', '2.0', '这是简介', '2016-11-01', '2016-11-30', '必修', 'class_cover_img\\$K~K2GA2E_2S5SIOR])C7)K.jpg', '这是目标');
INSERT INTO `course` VALUES ('14', '初级会计学', '1.0', '这是简介', '2016-11-02', '2016-11-24', '必修', 'class_cover_img\\$K~K2GA2E_2S5SIOR])C7)K.jpg', '这是目标');
INSERT INTO `course` VALUES ('15', '宇宙学', '9.0', '我们的征途是星辰大海', '2016-11-06', '2016-11-14', '必修', 'class_cover_img\\$K~K2GA2E_2S5SIOR])C7)K.jpg', '征服宇宙');
INSERT INTO `course` VALUES ('16', '宇宙学', '9.0', '我们的征途是星辰大海', '2016-11-01', '2016-11-10', '必修', 'class_cover_img\\$K~K2GA2E_2S5SIOR])C7)K.jpg', '征服宇宙');
INSERT INTO `course` VALUES ('17', '国际金融', '1.0', '这是简介', '2016-10-31', '2016-11-16', '必修', 'class_cover_img\\$K~K2GA2E_2S5SIOR])C7)K.jpg', '这是目标');
INSERT INTO `course` VALUES ('18', '国际贸易', '2.0', '简介', '2016-11-15', '2016-11-29', '必修', 'class_cover_img\\ic_student.png', '目标');
INSERT INTO `course` VALUES ('19', '软件工程', '2.0', '这是目标', '2016-11-02', '2016-11-30', '必修', 'class_cover_img\\ic_student.jpg', '这是简介');

-- ----------------------------
-- Table structure for course_class
-- ----------------------------
DROP TABLE IF EXISTS `course_class`;
CREATE TABLE `course_class` (
  `pcc_id` int(10) NOT NULL AUTO_INCREMENT,
  `c_id` int(10) DEFAULT NULL,
  `pc_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`pcc_id`),
  KEY `c_id` (`c_id`),
  KEY `pc_id` (`pc_id`),
  CONSTRAINT `course_class_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `course_class_ibfk_2` FOREIGN KEY (`pc_id`) REFERENCES `professional` (`pc_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_class
-- ----------------------------
INSERT INTO `course_class` VALUES ('1', '2', '1');
INSERT INTO `course_class` VALUES ('2', '2', '2');
INSERT INTO `course_class` VALUES ('3', '2', '3');
INSERT INTO `course_class` VALUES ('4', '13', '88');
INSERT INTO `course_class` VALUES ('5', '13', '89');
INSERT INTO `course_class` VALUES ('6', '13', '95');
INSERT INTO `course_class` VALUES ('7', '13', '96');
INSERT INTO `course_class` VALUES ('8', '13', '101');
INSERT INTO `course_class` VALUES ('9', '13', '106');
INSERT INTO `course_class` VALUES ('10', '13', '111');
INSERT INTO `course_class` VALUES ('11', '15', '94');
INSERT INTO `course_class` VALUES ('12', '15', '100');
INSERT INTO `course_class` VALUES ('13', '15', '109');
INSERT INTO `course_class` VALUES ('14', '15', '110');
INSERT INTO `course_class` VALUES ('15', '17', '90');
INSERT INTO `course_class` VALUES ('16', '17', '91');
INSERT INTO `course_class` VALUES ('17', '3', '1');
INSERT INTO `course_class` VALUES ('18', '4', '2');
INSERT INTO `course_class` VALUES ('19', null, null);
INSERT INTO `course_class` VALUES ('20', '18', '88');
INSERT INTO `course_class` VALUES ('21', '18', '89');
INSERT INTO `course_class` VALUES ('22', '19', '29');
INSERT INTO `course_class` VALUES ('23', '19', '32');
INSERT INTO `course_class` VALUES ('24', '19', '33');
INSERT INTO `course_class` VALUES ('25', '19', '34');

-- ----------------------------
-- Table structure for course_exam
-- ----------------------------
DROP TABLE IF EXISTS `course_exam`;
CREATE TABLE `course_exam` (
  `e_id` int(11) NOT NULL AUTO_INCREMENT,
  `e_question` varchar(255) DEFAULT NULL COMMENT '问题',
  `e_options` varchar(255) DEFAULT NULL COMMENT '选项',
  `e_answer` varchar(255) DEFAULT NULL COMMENT '答案',
  `e_score` float DEFAULT NULL COMMENT '得分',
  `c_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`e_id`),
  KEY `c_id` (`c_id`),
  CONSTRAINT `course_exam_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_exam
-- ----------------------------
INSERT INTO `course_exam` VALUES ('17', '题目1', '{\"optionA\":\"选项A\",\"optionB\":\"选项b\",\"optionC\":\"选项c\",\"optionD\":\"选项d\"}', 'C', null, '2');
INSERT INTO `course_exam` VALUES ('18', 'sdfsa', '{\"optionA\":\"sadf\",\"optionB\":\"sadf\",\"optionC\":\"asdfas\",\"optionD\":\"fasdf\"}', 'A', null, '2');
INSERT INTO `course_exam` VALUES ('19', 'sdfsad', '{\"optionA\":\"asdfsad\",\"optionB\":\"fasdf\",\"optionC\":\"sadfas\",\"optionD\":\"dfasdf\"}', 'C', null, '2');
INSERT INTO `course_exam` VALUES ('20', 'sadfas', '{\"optionA\":\"fasdfas\",\"optionB\":\"dfasd\",\"optionC\":\"fasdfsa\",\"optionD\":\"dfasdf\"}', 'D', null, '2');
INSERT INTO `course_exam` VALUES ('21', '萨芬的', '{\"optionA\":\"撒旦法\",\"optionB\":\"撒旦法\",\"optionC\":\"是\",\"optionD\":\"按时\"}', 'A', null, '2');
INSERT INTO `course_exam` VALUES ('22', 'vfrhgrbfdbfdbfd', '{\"optionA\":\"bbd\",\"optionB\":\"bddt\",\"optionC\":\"bdtt\",\"optionD\":\"bbtfntfgnftgnb\"}', 'B', null, '2');

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments` (
  `d_code` char(4) NOT NULL,
  `d_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`d_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES ('3101', '土木工程学院');
INSERT INTO `departments` VALUES ('3102', '文学院');
INSERT INTO `departments` VALUES ('3103', '外语学院');
INSERT INTO `departments` VALUES ('3104', '信息科学与工程学院');
INSERT INTO `departments` VALUES ('3105', '物理与机电工程学院');
INSERT INTO `departments` VALUES ('3106', '经济管理学院');
INSERT INTO `departments` VALUES ('3107', '法学院');
INSERT INTO `departments` VALUES ('3108', '政治与公共事务管理学院');
INSERT INTO `departments` VALUES ('3109', '化学与环境工程学院');
INSERT INTO `departments` VALUES ('3110', '数学与统计学院');
INSERT INTO `departments` VALUES ('3111', '旅游与地理学院');
INSERT INTO `departments` VALUES ('3112', '教育学院');
INSERT INTO `departments` VALUES ('3113', '音乐学院');
INSERT INTO `departments` VALUES ('3114', '美术与设计学院');
INSERT INTO `departments` VALUES ('3115', '英东农业科学与工程学院');
INSERT INTO `departments` VALUES ('3116', '英东生命科学学院');
INSERT INTO `departments` VALUES ('3117', '英东食品科学与工程学院');
INSERT INTO `departments` VALUES ('3118', '体育学院');

-- ----------------------------
-- Table structure for depart_profess
-- ----------------------------
DROP TABLE IF EXISTS `depart_profess`;
CREATE TABLE `depart_profess` (
  `dp_id` int(10) NOT NULL AUTO_INCREMENT,
  `d_code` char(4) DEFAULT NULL,
  `pc_code` char(8) DEFAULT NULL,
  PRIMARY KEY (`dp_id`),
  KEY `d_code` (`d_code`),
  CONSTRAINT `depart_profess_ibfk_1` FOREIGN KEY (`d_code`) REFERENCES `departments` (`d_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of depart_profess
-- ----------------------------
INSERT INTO `depart_profess` VALUES ('3', '3101', '80101');
INSERT INTO `depart_profess` VALUES ('4', '3101', '80102');
INSERT INTO `depart_profess` VALUES ('5', '3102', '80201');
INSERT INTO `depart_profess` VALUES ('6', '3102', '80202');
INSERT INTO `depart_profess` VALUES ('7', '3103', '80301');
INSERT INTO `depart_profess` VALUES ('8', '3103', '80302');
INSERT INTO `depart_profess` VALUES ('9', '3104', '80401');
INSERT INTO `depart_profess` VALUES ('10', '3104', '80402');
INSERT INTO `depart_profess` VALUES ('11', '3104', '80403');
INSERT INTO `depart_profess` VALUES ('12', '3104', '80404');
INSERT INTO `depart_profess` VALUES ('13', '3104', '80405');
INSERT INTO `depart_profess` VALUES ('14', '3104', '80406');
INSERT INTO `depart_profess` VALUES ('15', '3105', '80501');
INSERT INTO `depart_profess` VALUES ('16', '3105', '80502');
INSERT INTO `depart_profess` VALUES ('17', '3105', '80503');
INSERT INTO `depart_profess` VALUES ('18', '3105', '80504');
INSERT INTO `depart_profess` VALUES ('19', '3105', '80505');
INSERT INTO `depart_profess` VALUES ('20', '3105', '80506');
INSERT INTO `depart_profess` VALUES ('21', '3106', '80601');
INSERT INTO `depart_profess` VALUES ('22', '3106', '80602');
INSERT INTO `depart_profess` VALUES ('23', '3106', '80603');
INSERT INTO `depart_profess` VALUES ('24', '3106', '80604');
INSERT INTO `depart_profess` VALUES ('25', '3106', '80605');
INSERT INTO `depart_profess` VALUES ('26', '3107', '80701');
INSERT INTO `depart_profess` VALUES ('27', '3108', '80801');
INSERT INTO `depart_profess` VALUES ('28', '3108', '80802');
INSERT INTO `depart_profess` VALUES ('29', '3108', '80803');
INSERT INTO `depart_profess` VALUES ('30', '3108', '80804');
INSERT INTO `depart_profess` VALUES ('31', '3109', '80901');
INSERT INTO `depart_profess` VALUES ('32', '3109', '80902');
INSERT INTO `depart_profess` VALUES ('33', '3109', '80903');
INSERT INTO `depart_profess` VALUES ('34', '3110', '81001');
INSERT INTO `depart_profess` VALUES ('35', '3110', '81002');
INSERT INTO `depart_profess` VALUES ('36', '3110', '81003');
INSERT INTO `depart_profess` VALUES ('37', '3110', '81004');
INSERT INTO `depart_profess` VALUES ('38', '3111', '81101');
INSERT INTO `depart_profess` VALUES ('39', '3111', '81102');
INSERT INTO `depart_profess` VALUES ('40', '3111', '81103');
INSERT INTO `depart_profess` VALUES ('41', '3112', '81201');
INSERT INTO `depart_profess` VALUES ('42', '3112', '81202');
INSERT INTO `depart_profess` VALUES ('43', '3112', '81203');
INSERT INTO `depart_profess` VALUES ('44', '3113', '81301');
INSERT INTO `depart_profess` VALUES ('45', '3113', '81302');
INSERT INTO `depart_profess` VALUES ('46', '3114', '81401');
INSERT INTO `depart_profess` VALUES ('47', '3114', '81402');
INSERT INTO `depart_profess` VALUES ('48', '3115', '81501');
INSERT INTO `depart_profess` VALUES ('49', '3115', '81502');
INSERT INTO `depart_profess` VALUES ('50', '3115', '81503');
INSERT INTO `depart_profess` VALUES ('51', '3115', '81504');
INSERT INTO `depart_profess` VALUES ('52', '3116', '81601');
INSERT INTO `depart_profess` VALUES ('53', '3116', '81602');
INSERT INTO `depart_profess` VALUES ('54', '3117', '81701');
INSERT INTO `depart_profess` VALUES ('55', '3117', '81702');
INSERT INTO `depart_profess` VALUES ('56', '3118', '81801');
INSERT INTO `depart_profess` VALUES ('57', '3118', '81802');

-- ----------------------------
-- Table structure for difficult_point
-- ----------------------------
DROP TABLE IF EXISTS `difficult_point`;
CREATE TABLE `difficult_point` (
  `dp_id` int(10) NOT NULL AUTO_INCREMENT,
  `dp_name` varchar(255) DEFAULT NULL COMMENT '重难点名',
  `c_id` int(10) DEFAULT NULL COMMENT '课程id',
  `dp_note` text,
  PRIMARY KEY (`dp_id`),
  KEY `c_id` (`c_id`),
  CONSTRAINT `difficult_point_ibfk_2` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of difficult_point
-- ----------------------------
INSERT INTO `difficult_point` VALUES ('1', 'javascript的dom操作', '2', 'dom1級操作，2即操作');
INSERT INTO `difficult_point` VALUES ('3', '通过 HTTP 请求加载远程数据。', '2', 'jQuery 底层 AJAX 实现。简单易用的高层实现见 $.get, $.post 等。$.ajax() 返回其创建的 XMLHttpRequest 对象。大多数情况下你无需直接操作该函数，除非你需要操作不常用的选项，以获得更多的灵活性。\r\n\r\n最简单的情况下，$.ajax()可以不带任何参数直接使用。\r\n\r\n注意，所有的选项都可以通过$.ajaxSetup()函数来全局设置。\r\n\r\n回调函数\r\n\r\n如果要处理$.ajax()得到的数据，则需要使用回调函数。beforeSend、error、dataFilter、success、complete。\r\n\r\nbeforeSend 在发送请求之前调用，并且传入一个XMLHttpRequest作为参数。 \r\nerror 在请求出错时调用。传入XMLHttpRequest对象，描述错误类型的字符串以及一个异常对象（如果有的话） \r\ndataFilter 在请求成功之后调用。传入返回的数据以及\"dataType\"参数的值。并且必须返回新的数据（可能是处理过的）传递给success回调函数。 \r\nsuccess 当请求之后调用。传入返回后的数据，以及包含成功代码的字符串。 \r\ncomplete 当请求完成之后调用这个函数，无论成功或失败。传入XMLHttpRequest对象，以及一个包含成功或错误代码的字符串。 \r\n数据类型\r\n\r\n$.ajax()函数依赖服务器提供的信息来处理返回的数据。如果服务器报告说返回的数据是XML，那么返回的结果就可以用普通的XML方法或者jQuery的选择器来遍历。如果见得到其他类型，比如HTML，则数据就以文本形式来对待。\r\n\r\n通过dataType选项还可以指定其他不同数据处理方式。除了单纯的XML，还可以指定 html、json、jsonp、script或者text。\r\n\r\n其中，text和xml类型返回的数据不会经过处理。数据仅仅简单的将XMLHttpRequest的responseText或responseHTML属性传递给success回调函数，\r\n\r\n\'\'\'注意\'\'\'，我们必须确保网页服务器报告的MIME类型与我们选择的dataType所匹配。比如说，XML的话，服务器端就必须声明 text/xml 或者 application/xml 来获得一致的结果。\r\n\r\n如果指定为html类型，任何内嵌的JavaScript都会在HTML作为一个字符串返回之前执行。类似的，指定script类型的话，也会先执行服务器端生成JavaScript，然后再把脚本作为一个文本数据返回。\r\n\r\n如果指定为json类型，则会把获取到的数据作为一个JavaScript对象来解析，并且把构建好的对象作为结果返回。为了实现这个目的，他首先尝试使用JSON.parse()。如果浏览器不支持，则使用一个函数来构建。JSON数据是一种能很方便通过JavaScript解析的结构化数据。如果获取的数据文件存放在远程服务器上（域名不同，也就是跨域获取数据），则需要使用jsonp类型。使用这种类型的话，会创建一个查询字符串参数 callback=? ，这个参数会加在请求的URL后面。服务器端应当在JSON数据前加上回调函数名，以便完成一个有效的JSONP请求。如果要指定回调函数的参数名来取代默认的callback，可以通过设置$.ajax()的jsonp参数。\r\n\r\n');
INSERT INTO `difficult_point` VALUES ('7', '返回值:XMLHttpRequestjQuery.getJSON(url, [data], [callback])', '2', '通过 HTTP GET 请求载入 JSON 数据。\r\n\r\n在 jQuery 1.2 中，您可以通过使用JSONP形式的回调函数来加载其他网域的JSON数据，如 \"myurl?callback=?\"。jQuery 将自动替换 ? 为正确的函数名，以执行回调函数。 注意：此行以后的代码将在这个回调函数执行前执行。\r\n\r\n参数\r\nurl,[data],[callback]String,Map,FunctionV1.0url:发送请求地址。\r\n\r\ndata:待发送 Key/value 参数。\r\n\r\ncallback:载入成功时回调函数。\r\n\r\n示例\r\n描述:\r\n从 Flickr JSONP API 载入 4 张最新的关于猫的图片。\r\n\r\nHTML 代码:\r\n<div id=\"images\"></div>jQuery 代码:\r\n$.getJSON(\"http://api.flickr.com/services/feeds/photos_public.gne?tags=cat&tagmode=any&format=json&jsoncallback=?\", function(data){\r\n  $.each(data.items, function(i,item){\r\n    $(\"<img/>\").attr(\"src\", item.media.m).appendTo(\"#images\");\r\n    if ( i == 3 ) return false;\r\n  });\r\n});描述:\r\n从 test.js 载入 JSON 数据并显示 JSON 数据中一个 name 字段数据。\r\n\r\njQuery 代码:\r\n$.getJSON(\"test.js\", function(json){\r\n  alert(\"JSON Data: \" + json.users[3].name);\r\n});描述:\r\n从 test.js 载入 JSON 数据，附加参数，显示 JSON 数据中一个 name 字段数据。\r\n\r\njQuery 代码:\r\n$.getJSON(\"test.js\", { name: \"John\", time: \"2pm\" }, function(json){\r\n  alert(\"JSON Data: \" + json.users[3].name);\r\n});');
INSERT INTO `difficult_point` VALUES ('13', 'toggleClass(class|fn[,sw])', '2', '概述\r\n如果存在（不存在）就删除（添加）一个类。\r\n\r\n\r\n参数\r\nclassStringV1.0CSS类名\r\n\r\nclass,switchString,BooleanV1.31:要切换的CSS类名. \r\n\r\n2:用于决定元素是否包含class的布尔值。\r\n\r\nswitchBooleanV1.4用于决定元素是否包含class的布尔值。\r\n\r\nfunction(index, class,switch)[, switch] Function,BooleanV1.41:用来返回在匹配的元素集合中的每个元素上用来切换的样式类名的一个函数。接收元素的索引位置和元素旧的样式类作为参数。\r\n\r\n2: 一个用来判断样式类添加还是移除的 boolean 值。 \r\n\r\n示例\r\n参数class 描述:\r\n为匹配的元素切换 \'selected\' 类\r\n\r\njQuery 代码:\r\n$(\"p\").toggleClass(\"selected\");参数class,switch 描述:\r\n每点击三下加上一次 \'highlight\' 类\r\n\r\nHTML 代码:\r\n<strong>jQuery 代码:</strong>jQuery 代码:\r\n  var count = 0;\r\n  $(\"p\").click(function(){\r\n      $(this).toggleClass(\"highlight\", count++ % 3 == 0);\r\n  });回调函数 描述:\r\n根据父元素来设置class属性\r\n\r\njQuery 代码:\r\n$(\'div.foo\').toggleClass(function() {\r\n  if ($(this).parent().is(\'.bar\') {\r\n    return \'happy\';\r\n  } else {\r\n    return \'sad\';\r\n  }\r\n});');
INSERT INTO `difficult_point` VALUES ('14', 'toggleClass(class|fn[,sw])', '2', '概述\r\n如果存在（不存在）就删除（添加）一个类。\r\n\r\n\r\n参数\r\nclassStringV1.0CSS类名\r\n\r\nclass,switchString,BooleanV1.31:要切换的CSS类名. \r\n\r\n2:用于决定元素是否包含class的布尔值。\r\n\r\nswitchBooleanV1.4用于决定元素是否包含class的布尔值。\r\n\r\nfunction(index, class,switch)[, switch] Function,BooleanV1.41:用来返回在匹配的元素集合中的每个元素上用来切换的样式类名的一个函数。接收元素的索引位置和元素旧的样式类作为参数。\r\n\r\n2: 一个用来判断样式类添加还是移除的 boolean 值。 \r\n\r\n示例\r\n参数class 描述:\r\n为匹配的元素切换 \'selected\' 类\r\n\r\njQuery 代码:\r\n$(\"p\").toggleClass(\"selected\");参数class,switch 描述:\r\n每点击三下加上一次 \'highlight\' 类\r\n\r\nHTML 代码:\r\n<strong>jQuery 代码:</strong>jQuery 代码:\r\n  var count = 0;\r\n  $(\"p\").click(function(){\r\n      $(this).toggleClass(\"highlight\", count++ % 3 == 0);\r\n  });回调函数 描述:\r\n根据父元素来设置class属性\r\n\r\njQuery 代码:\r\n$(\'div.foo\').toggleClass(function() {\r\n  if ($(this).parent().is(\'.bar\') {\r\n    return \'happy\';\r\n  } else {\r\n    return \'sad\';\r\n  }\r\n});');
INSERT INTO `difficult_point` VALUES ('15', 'val([val|fn|arr])\r\n', '2', '概述\r\n获得匹配元素的当前值。\r\n\r\n在 jQuery 1.2 中,可以返回任意元素的值了。包括select。如果多选，将返回一个数组，其包含所选的值。\r\n\r\n参数\r\nvalStringV1.0要设置的值。\r\n\r\nfunction(index, value)FunctionV1.4此函数返回一个要设置的值。接受两个参数，index为元素在集合中的索引位置，text为原先的text值。\r\n\r\narrayArray<String>V1.0用于 check/select 的值\r\n\r\n示例\r\n无参数 描述:\r\n获取文本框中的值\r\n\r\njQuery 代码:\r\n$(\"input\").val();参数val 描述:\r\n设定文本框的值\r\n\r\njQuery 代码:\r\n$(\"input\").val(\"hello world!\");回调函数 描述:\r\n设定文本框的值\r\n\r\njQuery 代码:\r\n$(\'input:text.items\').val(function() {\r\n  return this.value + \' \' + this.className;\r\n});参数array 描述:\r\n设定一个select和一个多选的select的值\r\n\r\nHTML 代码:\r\n<select id=\"single\">\r\n  <option>Single</option>\r\n  <option>Single2</option>\r\n</select>\r\n<select id=\"multiple\" multiple=\"multiple\">\r\n  <option selected=\"selected\">Multiple</option>\r\n  <option>Multiple2</option>\r\n  <option selected=\"selected\">Multiple3</option>\r\n</select><br/>\r\n<input type=\"checkbox\" value=\"check1\"/> check1\r\n<input type=\"checkbox\" value=\"check2\"/> check2\r\n<input type=\"radio\" value=\"radio1\"/> radio1\r\n<input type=\"radio\" value=\"radio2\"/> radio2jQuery 代码:\r\n$(\"#single\").val(\"Single2\");\r\n$(\"#multiple\").val([\"Multiple2\", \"Multiple3\"]);\r\n$(\"input\").val([\"check2\", \"radio1\"]);');
INSERT INTO `difficult_point` VALUES ('16', 'http://mvnrepository.com/artifact/org.mybatis/mybatis', '2', '配置文件：\r\n<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD config 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\">\r\n\r\n\r\n映射文件：\r\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n\r\nLog4J配置\r\nlog4j.rootLogger=debug, stdout \r\nlog4j.logger.com.etc.test.dao =TRACE\r\nlog4j.appender.stdout=org.apache.log4j.ConsoleAppender\r\nlog4j.appender.stdout.layout=org.apache.log4j.PatternLayout\r\nlog4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n\r\n\r\n');
INSERT INTO `difficult_point` VALUES ('17', 'public static void main(String[] args) throws IOException { ', '2', 'public static void main(String[] args) throws IOException {\r\n		String resourse = \"mybatis-config.xml\";\r\n		SqlSession session = null;\r\n		Reader reader  = null;\r\n		//加载配置文件，创建会话工厂\r\n		reader = Resources.getResourceAsReader(resourse);\r\n		SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(reader);\r\n		//创建会话对象，相当于JDBC获取连接对象\r\n		session = ssf.openSession();\r\n		//执行SQL语句，获得结果\r\n	\r\n		\r\n		\r\n		\r\n		//关闭资源，勿忘！\r\n		session.close();\r\n		reader.close();\r\n		}');
INSERT INTO `difficult_point` VALUES ('18', '<script src=\"bower_components/bootstrap/dist/js/bootstrap.min.js\"></script>\r\n', '2', '<script type=\"text/javascript\">\r\n			function addEvt() {\r\n				var iframe = document.getElementById(\'content\');\r\n				iframe.style.height = content.document.documentElement.scrollHeight + 100 + \"px\";\r\n			}\r\n		</script>\r\n\r\n		<script src=\"bower_components/bootstrap/dist/js/bootstrap.min.js\"></script>\r\n\r\n		<!-- library for cookie management -->\r\n		<script src=\"js/jquery.cookie.js\"></script>\r\n		<!-- calender plugin -->\r\n		<script src=\'bower_components/moment/min/moment.min.js\'></script>\r\n		<script src=\'bower_components/fullcalendar/dist/fullcalendar.min.js\'></script>\r\n		<!-- data table plugin -->\r\n		<script src=\'js/jquery.dataTables.min.js\'></script>\r\n\r\n		<!-- select or dropdown enhancer -->\r\n		<script src=\"bower_components/chosen/chosen.jquery.min.js\"></script>\r\n		<!-- plugin for gallery image view -->\r\n		<script src=\"bower_components/colorbox/jquery.colorbox-min.js\"></script>\r\n		<!-- notification plugin -->\r\n		<script src=\"js/jquery.noty.js\"></script>\r\n		<!-- library for making tables responsive -->');
INSERT INTO `difficult_point` VALUES ('19', '随着知识经济的到来，学习不再完全由教学机构来预设，', '2', '学习者有了更多的参与权与决策权，\r\n同时他们也更期待用按需学习来替代现在的课程模型，\r\n因此，自主学习成为了未来学校教育中越来越不可或缺的重要组成部分。 \r\n本选题正是基于该背景而提出的，它将传统的课程分割成相对独立的知识模块，\r\n每个模块包括详细的学习目标、计划，以及对应的学分，\r\n学习者可以脱离以往的课程，\r\n而根据自己的需要来选择模块化的学习，然后达到培养计划中所规定的学分。');
INSERT INTO `difficult_point` VALUES ('20', '通过远程 HTTP GET 请求载入信息', '2', 'url,[data],[callback],[type]String,Map,Function,StringV1.0url:待载入页面的URL地址\r\n\r\ndata:待发送 Key/value 参数。\r\n\r\ncallback:载入成功时回调函数。\r\n\r\ntype:返回内容格式，xml, html, script, json, text, _default。\r\n\r\n示例\r\n');

-- ----------------------------
-- Table structure for exam_score
-- ----------------------------
DROP TABLE IF EXISTS `exam_score`;
CREATE TABLE `exam_score` (
  `es_id` int(11) NOT NULL AUTO_INCREMENT,
  `s_id` int(10) DEFAULT NULL COMMENT '学生id',
  `es_score` float(50,2) DEFAULT NULL COMMENT '成绩',
  `c_id` int(10) DEFAULT NULL COMMENT '课程',
  PRIMARY KEY (`es_id`),
  KEY `c_id` (`c_id`),
  KEY `s_id` (`s_id`),
  CONSTRAINT `exam_score_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `exam_score_ibfk_2` FOREIGN KEY (`s_id`) REFERENCES `student` (`s_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_score
-- ----------------------------
INSERT INTO `exam_score` VALUES ('1', '3', '90.00', '2');
INSERT INTO `exam_score` VALUES ('2', '4', '95.00', '2');
INSERT INTO `exam_score` VALUES ('3', '5', '100.00', '2');

-- ----------------------------
-- Table structure for homework
-- ----------------------------
DROP TABLE IF EXISTS `homework`;
CREATE TABLE `homework` (
  `hw_id` int(10) NOT NULL AUTO_INCREMENT,
  `hw_question` varchar(255) DEFAULT NULL COMMENT '问题',
  `hw_options` varchar(255) DEFAULT NULL COMMENT '选项',
  `hw_answer` varchar(255) DEFAULT NULL COMMENT '答案',
  `hw_score` float(3,1) DEFAULT NULL COMMENT '得分',
  `ch_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`hw_id`),
  KEY `ch_id` (`ch_id`),
  CONSTRAINT `homework_ibfk_1` FOREIGN KEY (`ch_id`) REFERENCES `chapter` (`ch_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of homework
-- ----------------------------

-- ----------------------------
-- Table structure for homework_score
-- ----------------------------
DROP TABLE IF EXISTS `homework_score`;
CREATE TABLE `homework_score` (
  `hs_id` int(10) NOT NULL AUTO_INCREMENT,
  `s_id` int(10) DEFAULT NULL COMMENT '学生id',
  `ch_id` int(10) DEFAULT NULL COMMENT '章节id',
  `ch_score` float(10,2) DEFAULT NULL COMMENT '章节成绩',
  PRIMARY KEY (`hs_id`),
  KEY `s_id` (`s_id`),
  KEY `ch_id` (`ch_id`),
  CONSTRAINT `homework_score_ibfk_1` FOREIGN KEY (`s_id`) REFERENCES `student` (`s_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `homework_score_ibfk_2` FOREIGN KEY (`ch_id`) REFERENCES `chapter` (`ch_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of homework_score
-- ----------------------------
INSERT INTO `homework_score` VALUES ('3', '3', '2', '95.00');
INSERT INTO `homework_score` VALUES ('4', '3', '1', '96.00');
INSERT INTO `homework_score` VALUES ('5', '3', '3', '97.00');
INSERT INTO `homework_score` VALUES ('6', '3', '4', '98.00');
INSERT INTO `homework_score` VALUES ('8', '4', '1', '93.00');
INSERT INTO `homework_score` VALUES ('9', '4', '2', '98.00');
INSERT INTO `homework_score` VALUES ('10', '4', '3', '97.00');
INSERT INTO `homework_score` VALUES ('11', '4', '4', '100.00');
INSERT INTO `homework_score` VALUES ('12', '5', '1', '50.00');
INSERT INTO `homework_score` VALUES ('13', '5', '2', '60.00');
INSERT INTO `homework_score` VALUES ('14', '5', '3', '70.00');
INSERT INTO `homework_score` VALUES ('15', '5', '4', '80.00');

-- ----------------------------
-- Table structure for professional
-- ----------------------------
DROP TABLE IF EXISTS `professional`;
CREATE TABLE `professional` (
  `pc_id` int(10) NOT NULL AUTO_INCREMENT,
  `pc_grade` char(10) DEFAULT NULL,
  `pc_name` varchar(255) DEFAULT NULL,
  `pc_class` int(2) DEFAULT NULL,
  `pc_code` char(10) DEFAULT NULL,
  PRIMARY KEY (`pc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of professional
-- ----------------------------
INSERT INTO `professional` VALUES ('1', '2013', '无机非金属材料工程', '1', '80101');
INSERT INTO `professional` VALUES ('2', '2013', '土木工程', '1', '80102');
INSERT INTO `professional` VALUES ('3', '2014', '无机非金属材料工程', '1', '80101');
INSERT INTO `professional` VALUES ('4', '2014', '无机非金属材料工程', '2', '80101');
INSERT INTO `professional` VALUES ('5', '2014', '土木工程', '1', '80102');
INSERT INTO `professional` VALUES ('6', '2015', '无机非金属材料工程', '1', '80101');
INSERT INTO `professional` VALUES ('7', '2015', '土木工程', '1', '80102');
INSERT INTO `professional` VALUES ('8', '2015', '土木工程', '2', '80102');
INSERT INTO `professional` VALUES ('9', '2016', '无机非金属材料工程', '1', '80101');
INSERT INTO `professional` VALUES ('10', '2016', '土木工程', '1', '80102');
INSERT INTO `professional` VALUES ('11', '2013', '汉语言文学（师范）', '1', '80201');
INSERT INTO `professional` VALUES ('12', '2013', '新闻学', '1', '80202');
INSERT INTO `professional` VALUES ('13', '2014', '汉语言文学（师范）', '1', '80201');
INSERT INTO `professional` VALUES ('14', '2014', '汉语言文学（师范）', '2', '80201');
INSERT INTO `professional` VALUES ('15', '2014', '新闻学', '1', '80202');
INSERT INTO `professional` VALUES ('16', '2015', '汉语言文学（师范）', '1', '80201');
INSERT INTO `professional` VALUES ('17', '2015', '新闻学', '1', '80202');
INSERT INTO `professional` VALUES ('18', '2015', '新闻学', '2', '80202');
INSERT INTO `professional` VALUES ('19', '2016', '汉语言文学（师范）', '1', '80201');
INSERT INTO `professional` VALUES ('20', '2016', '新闻学', '1', '80202');
INSERT INTO `professional` VALUES ('21', '2013', '英语（师范）', '1', '80301');
INSERT INTO `professional` VALUES ('22', '2013', '日语（师范）', '1', '80302');
INSERT INTO `professional` VALUES ('23', '2014', '英语（师范）', '1', '80301');
INSERT INTO `professional` VALUES ('24', '2014', '日语（师范）', '1', '80302');
INSERT INTO `professional` VALUES ('25', '2015', '英语（师范）', '1', '80301');
INSERT INTO `professional` VALUES ('26', '2015', '日语（师范）', '1', '80302');
INSERT INTO `professional` VALUES ('27', '2016', '英语（师范）', '1', '80301');
INSERT INTO `professional` VALUES ('28', '2016', '日语（师范）', '1', '80302');
INSERT INTO `professional` VALUES ('29', '2013', '计算机科学与技术', '1', '80401');
INSERT INTO `professional` VALUES ('30', '2015', '计算机科学与技术', '1', '80401');
INSERT INTO `professional` VALUES ('31', '2014', '计算机科学与技术', '1', '80401');
INSERT INTO `professional` VALUES ('32', '2013', '计算机科学与技术', '4', '80401');
INSERT INTO `professional` VALUES ('33', '2013', '计算机科学与技术', '3', '80401');
INSERT INTO `professional` VALUES ('34', '2013', '计算机科学与技术', '2', '80401');
INSERT INTO `professional` VALUES ('35', '2016', '计算机科学与技术', '1', '80401');
INSERT INTO `professional` VALUES ('36', '2013', '通信工程', '1', '80402');
INSERT INTO `professional` VALUES ('37', '2013', '通信工程', '2', '80402');
INSERT INTO `professional` VALUES ('38', '2014', '通信工程', '1', '80402');
INSERT INTO `professional` VALUES ('39', '2015', '通信工程', '1', '80402');
INSERT INTO `professional` VALUES ('40', '2016', '通信工程', '1', '80402');
INSERT INTO `professional` VALUES ('41', '2013', '信息管理与信息系统', '1', '80403');
INSERT INTO `professional` VALUES ('42', '2013', '信息管理与信息系统', '2', '80403');
INSERT INTO `professional` VALUES ('43', '2014', '信息管理与信息系统', '1', '80403');
INSERT INTO `professional` VALUES ('44', '2015', '信息管理与信息系统', '1', '80403');
INSERT INTO `professional` VALUES ('45', '2016', '信息管理与信息系统', '1', '80403');
INSERT INTO `professional` VALUES ('46', '2013', '物联网工程', '1', '80404');
INSERT INTO `professional` VALUES ('47', '2013', '物联网工程', '2', '80404');
INSERT INTO `professional` VALUES ('48', '2014', '物联网工程', '1', '80404');
INSERT INTO `professional` VALUES ('49', '2014', '物联网工程', '2', '80404');
INSERT INTO `professional` VALUES ('50', '2015', '物联网工程', '1', '80404');
INSERT INTO `professional` VALUES ('51', '2016', '物联网工程', '1', '80404');
INSERT INTO `professional` VALUES ('52', '2013', '软件工程', '1', '80405');
INSERT INTO `professional` VALUES ('53', '2013', '软件工程', '2', '80405');
INSERT INTO `professional` VALUES ('54', '2014', '软件工程', '1', '80405');
INSERT INTO `professional` VALUES ('55', '2015', '软件工程', '1', '80405');
INSERT INTO `professional` VALUES ('56', '2016', '软件工程', '1', '80405');
INSERT INTO `professional` VALUES ('57', '2013', '软件外包', '1', '80406');
INSERT INTO `professional` VALUES ('58', '2014', '软件外包', '1', '80406');
INSERT INTO `professional` VALUES ('59', '2015', '软件外包', '1', '80406');
INSERT INTO `professional` VALUES ('60', '2016', '软件外包', '1', '80406');
INSERT INTO `professional` VALUES ('61', '2013', '物理学（师范）', '1', '80501');
INSERT INTO `professional` VALUES ('62', '2013', '物理学（师范）', '2', '80501');
INSERT INTO `professional` VALUES ('63', '2013', '物理学（师范）', '3', '80501');
INSERT INTO `professional` VALUES ('64', '2014', '物理学（师范）', '1', '80501');
INSERT INTO `professional` VALUES ('65', '2014', '物理学（师范）', '2', '80501');
INSERT INTO `professional` VALUES ('66', '2015', '物理学（师范）', '1', '80501');
INSERT INTO `professional` VALUES ('67', '2016', '物理学（师范）', '1', '80501');
INSERT INTO `professional` VALUES ('68', '2013', '电子信息科学与技术', '1', '80502');
INSERT INTO `professional` VALUES ('69', '2014', '电子信息科学与技术', '1', '80502');
INSERT INTO `professional` VALUES ('70', '2015', '电子信息科学与技术', '1', '80502');
INSERT INTO `professional` VALUES ('71', '2016', '电子信息科学与技术', '1', '80502');
INSERT INTO `professional` VALUES ('72', '2013', '自动化', '1', '80503');
INSERT INTO `professional` VALUES ('73', '2014', '自动化', '1', '80503');
INSERT INTO `professional` VALUES ('74', '2015', '自动化', '1', '80503');
INSERT INTO `professional` VALUES ('75', '2016', '自动化', '1', '80503');
INSERT INTO `professional` VALUES ('76', '2013', '机械设计制造及其自动化', '1', '80504');
INSERT INTO `professional` VALUES ('77', '2014', '机械设计制造及其自动化', '1', '80504');
INSERT INTO `professional` VALUES ('78', '2014', '机械设计制造及其自动化', '2', '80504');
INSERT INTO `professional` VALUES ('79', '2015', '机械设计制造及其自动化', '1', '80504');
INSERT INTO `professional` VALUES ('80', '2016', '机械设计制造及其自动化', '1', '80504');
INSERT INTO `professional` VALUES ('81', '2016', '机械设计制造及其自动化', '2', '80504');
INSERT INTO `professional` VALUES ('82', '2013', '交通运输', '1', '80505');
INSERT INTO `professional` VALUES ('83', '2013', '交通运输', '2', '80505');
INSERT INTO `professional` VALUES ('84', '2014', '交通运输', '1', '80505');
INSERT INTO `professional` VALUES ('85', '2014', '交通运输', '2', '80505');
INSERT INTO `professional` VALUES ('86', '2016', '交通运输', '1', '80505');
INSERT INTO `professional` VALUES ('87', '2016', '交通运输', '1', '80505');
INSERT INTO `professional` VALUES ('88', '2013', '国际经济与贸易', '1', '80601');
INSERT INTO `professional` VALUES ('89', '2013', '国际经济与贸易', '2', '80601');
INSERT INTO `professional` VALUES ('90', '2014', '国际经济与贸易', '1', '80601');
INSERT INTO `professional` VALUES ('91', '2014', '国际经济与贸易', '2', '80601');
INSERT INTO `professional` VALUES ('92', '2015', '国际经济与贸易', '1', '80601');
INSERT INTO `professional` VALUES ('93', '2015', '国际经济与贸易', '2', '80601');
INSERT INTO `professional` VALUES ('94', '2016', '国际经济与贸易', '1', '80601');
INSERT INTO `professional` VALUES ('95', '2013', '会计学', '1', '80602');
INSERT INTO `professional` VALUES ('96', '2013', '会计学', '2', '80602');
INSERT INTO `professional` VALUES ('97', '2014', '会计学', '1', '80602');
INSERT INTO `professional` VALUES ('98', '2014', '会计学', '2', '80602');
INSERT INTO `professional` VALUES ('99', '2015', '会计学', '1', '80602');
INSERT INTO `professional` VALUES ('100', '2016', '会计学', '1', '80602');
INSERT INTO `professional` VALUES ('101', '2013', '工商管理', '1', '80603');
INSERT INTO `professional` VALUES ('102', '2014', '工商管理', '1', '80603');
INSERT INTO `professional` VALUES ('103', '2014', '工商管理', '2', '80603');
INSERT INTO `professional` VALUES ('104', '2015', '工商管理', '1', '80603');
INSERT INTO `professional` VALUES ('105', '2016', '工商管理', '1', '80603');
INSERT INTO `professional` VALUES ('106', '2013', '人力资源管理', '1', '80604');
INSERT INTO `professional` VALUES ('107', '2014', '人力资源管理', '1', '80604');
INSERT INTO `professional` VALUES ('108', '2015', '人力资源管理', '1', '80604');
INSERT INTO `professional` VALUES ('109', '2016', '人力资源管理', '1', '80604');
INSERT INTO `professional` VALUES ('110', '2016', '人力资源管理', '2', '80604');
INSERT INTO `professional` VALUES ('111', '2013', '国际贸易类(国际商务（创业管理）)', '1', '80605');
INSERT INTO `professional` VALUES ('112', '2014', '国际贸易类(国际商务（创业管理）)	', '1', '80605');
INSERT INTO `professional` VALUES ('113', '2015', '国际贸易类(国际商务（创业管理）)	', '1', '80605');
INSERT INTO `professional` VALUES ('114', '2016', '国际贸易类(国际商务（创业管理）)	', '1', '80605');
INSERT INTO `professional` VALUES ('115', '2013', '法学', '1', '80701');
INSERT INTO `professional` VALUES ('116', '2014', '法学', '1', '80701');
INSERT INTO `professional` VALUES ('117', '2015', '法学', '1', '80701');
INSERT INTO `professional` VALUES ('118', '2016', '法学', '1', '80701');
INSERT INTO `professional` VALUES ('119', '2013', '思想政治教育（师范）', '1', '80801');
INSERT INTO `professional` VALUES ('120', '2014', '思想政治教育（师范）', '1', '80801');
INSERT INTO `professional` VALUES ('121', '2015', '思想政治教育（师范）', '1', '80801');
INSERT INTO `professional` VALUES ('122', '2016', '思想政治教育（师范）', '1', '80801');
INSERT INTO `professional` VALUES ('123', '2013', '行政管理', '1', '80802');
INSERT INTO `professional` VALUES ('124', '2014', '行政管理', '1', '80802');
INSERT INTO `professional` VALUES ('125', '2015', '行政管理', '1', '80802');
INSERT INTO `professional` VALUES ('126', '2016', '行政管理', '1', '80802');
INSERT INTO `professional` VALUES ('127', '2013', '公共事业管理', '1', '80803');
INSERT INTO `professional` VALUES ('128', '2013', '公共事业管理', '2', '80803');
INSERT INTO `professional` VALUES ('129', '2014', '公共事业管理', '1', '80803');
INSERT INTO `professional` VALUES ('130', '2015', '公共事业管理', '1', '80803');
INSERT INTO `professional` VALUES ('131', '2016', '公共事业管理', '1', '80803');
INSERT INTO `professional` VALUES ('132', '2013', '历史学（师范）', '1', '80804');
INSERT INTO `professional` VALUES ('133', '2014', '历史学（师范）', '1', '80804');
INSERT INTO `professional` VALUES ('134', '2015', '历史学（师范）', '1', '80804');
INSERT INTO `professional` VALUES ('135', '2016', '历史学（师范）', '1', '80804');
INSERT INTO `professional` VALUES ('136', '2013', '化学', '1', '80901');
INSERT INTO `professional` VALUES ('137', '2014', '化学', '1', '80901');
INSERT INTO `professional` VALUES ('138', '2015', '化学', '1', '80901');
INSERT INTO `professional` VALUES ('139', '2016', '化学', '1', '80901');
INSERT INTO `professional` VALUES ('140', '2013', '环境工程', '1', '80902');
INSERT INTO `professional` VALUES ('141', '2014', '环境工程', '1', '80902');
INSERT INTO `professional` VALUES ('142', '2015', '环境工程', '1', '80902');
INSERT INTO `professional` VALUES ('143', '2016', '环境工程', '1', '80902');
INSERT INTO `professional` VALUES ('144', '2013', '应用化学', '1', '80903');
INSERT INTO `professional` VALUES ('145', '2014', '应用化学', '1', '80903');
INSERT INTO `professional` VALUES ('146', '2015', '应用化学', '1', '80903');
INSERT INTO `professional` VALUES ('147', '2016', '应用化学', '1', '80903');
INSERT INTO `professional` VALUES ('148', '2013', '数学与应用数学（师范）', '1', '81001');
INSERT INTO `professional` VALUES ('149', '2014', '数学与应用数学（师范）', '1', '81001');
INSERT INTO `professional` VALUES ('150', '2015', '数学与应用数学（师范）', '1', '81001');
INSERT INTO `professional` VALUES ('151', '2016', '数学与应用数学（师范）', '1', '81001');
INSERT INTO `professional` VALUES ('152', '2013', '信息与计算科学', '1', '81002');
INSERT INTO `professional` VALUES ('153', '2014', '信息与计算科学', '1', '81002');
INSERT INTO `professional` VALUES ('154', '2015', '信息与计算科学', '1', '81002');
INSERT INTO `professional` VALUES ('155', '2016', '信息与计算科学', '1', '81002');
INSERT INTO `professional` VALUES ('156', '2013', '统计学', '1', '81003');
INSERT INTO `professional` VALUES ('157', '2014', '统计学', '1', '81003');
INSERT INTO `professional` VALUES ('158', '2015', '统计学', '1', '81003');
INSERT INTO `professional` VALUES ('159', '2016', '统计学', '1', '81003');
INSERT INTO `professional` VALUES ('160', '2013', '应用统计学', '1', '81004');
INSERT INTO `professional` VALUES ('161', '2014', '应用统计学', '1', '81004');
INSERT INTO `professional` VALUES ('162', '2015', '应用统计学', '1', '81004');
INSERT INTO `professional` VALUES ('163', '2016', '应用统计学', '1', '81004');
INSERT INTO `professional` VALUES ('164', '2013', '地理科学（师范）', '1', '81101');
INSERT INTO `professional` VALUES ('165', '2014', '地理科学（师范）', '1', '81101');
INSERT INTO `professional` VALUES ('166', '2015', '地理科学（师范）', '1', '81101');
INSERT INTO `professional` VALUES ('167', '2016', '地理科学（师范）', '1', '81101');
INSERT INTO `professional` VALUES ('168', '2013', '人文地理与城乡规划', '1', '81102');
INSERT INTO `professional` VALUES ('169', '2014', '人文地理与城乡规划', '1', '81102');
INSERT INTO `professional` VALUES ('170', '2015', '人文地理与城乡规划', '1', '81102');
INSERT INTO `professional` VALUES ('171', '2016', '人文地理与城乡规划', '1', '81102');
INSERT INTO `professional` VALUES ('172', '2013', '旅游管理', '1', '81103');
INSERT INTO `professional` VALUES ('173', '2014', '旅游管理', '1', '81103');
INSERT INTO `professional` VALUES ('174', '2015', '旅游管理', '1', '81103');
INSERT INTO `professional` VALUES ('175', '2016', '旅游管理', '1', '81103');
INSERT INTO `professional` VALUES ('176', '2013', '心理学（师范）', '1', '81201');
INSERT INTO `professional` VALUES ('177', '2014', '心理学（师范）', '1', '81201');
INSERT INTO `professional` VALUES ('178', '2015', '心理学（师范）', '1', '81201');
INSERT INTO `professional` VALUES ('179', '2016', '心理学（师范）', '1', '81201');
INSERT INTO `professional` VALUES ('180', '2013', '小学教育（师范）', '1', '81202');
INSERT INTO `professional` VALUES ('181', '2014', '小学教育（师范）', '1', '81202');
INSERT INTO `professional` VALUES ('182', '2015', '小学教育（师范）', '1', '81202');
INSERT INTO `professional` VALUES ('183', '2016', '小学教育（师范）', '1', '81202');
INSERT INTO `professional` VALUES ('184', '2013', '数字媒体技术', '1', '81203');
INSERT INTO `professional` VALUES ('185', '2014', '数字媒体技术', '1', '81203');
INSERT INTO `professional` VALUES ('186', '2015', '数字媒体技术', '1', '81203');
INSERT INTO `professional` VALUES ('187', '2016', '数字媒体技术', '1', '81203');
INSERT INTO `professional` VALUES ('188', '2013', '音乐与舞蹈学类(音乐学（师范）、音乐表演）', '1', '81301');
INSERT INTO `professional` VALUES ('189', '2014', '音乐与舞蹈学类(音乐学（师范）、音乐表演）', '1', '81301');
INSERT INTO `professional` VALUES ('190', '2015', '音乐与舞蹈学类(音乐学（师范）、音乐表演）', '1', '81301');
INSERT INTO `professional` VALUES ('191', '2016', '音乐与舞蹈学类(音乐学（师范）、音乐表演）', '1', '81301');
INSERT INTO `professional` VALUES ('192', '2013', '舞蹈学（师范）', '1', '81302');
INSERT INTO `professional` VALUES ('193', '2013', '舞蹈学（师范）', '2', '81302');
INSERT INTO `professional` VALUES ('194', '2014', '舞蹈学（师范）', '1', '81302');
INSERT INTO `professional` VALUES ('195', '2015', '舞蹈学（师范）', '1', '81302');
INSERT INTO `professional` VALUES ('196', '2016', '舞蹈学（师范）', '1', '81302');
INSERT INTO `professional` VALUES ('197', '2013', '设计学类（环境设计、服装与服饰设计、产品设计、视觉传达设计）', '1', '81401');
INSERT INTO `professional` VALUES ('198', '2014', '设计学类（环境设计、服装与服饰设计、产品设计、视觉传达设计）', '1', '81401');
INSERT INTO `professional` VALUES ('199', '2015', '设计学类（环境设计、服装与服饰设计、产品设计、视觉传达设计）', '1', '81401');
INSERT INTO `professional` VALUES ('200', '2016', '设计学类（环境设计、服装与服饰设计、产品设计、视觉传达设计）', '1', '81401');
INSERT INTO `professional` VALUES ('201', '2013', '绘画（师范）', '1', '81402');
INSERT INTO `professional` VALUES ('202', '2014', '绘画（师范）', '1', '81402');
INSERT INTO `professional` VALUES ('203', '2015', '绘画（师范）', '1', '81402');
INSERT INTO `professional` VALUES ('204', '2016', '绘画（师范）', '1', '81402');
INSERT INTO `professional` VALUES ('205', '2013', '园林', '1', '81501');
INSERT INTO `professional` VALUES ('206', '2014', '园林', '1', '81501');
INSERT INTO `professional` VALUES ('207', '2015', '园林', '1', '81501');
INSERT INTO `professional` VALUES ('208', '2016', '园林', '1', '81501');
INSERT INTO `professional` VALUES ('209', '2013', '园艺', '1', '81502');
INSERT INTO `professional` VALUES ('210', '2014', '园艺', '1', '81502');
INSERT INTO `professional` VALUES ('211', '2015', '园艺', '1', '81502');
INSERT INTO `professional` VALUES ('212', '2016', '园艺', '1', '81502');
INSERT INTO `professional` VALUES ('213', '2013', '动物科学', '1', '81503');
INSERT INTO `professional` VALUES ('214', '2014', '动物科学', '1', '81503');
INSERT INTO `professional` VALUES ('215', '2015', '动物科学', '1', '81503');
INSERT INTO `professional` VALUES ('216', '2016', '动物科学', '1', '81503');
INSERT INTO `professional` VALUES ('217', '2013', '土地资源管理', '1', '81504');
INSERT INTO `professional` VALUES ('218', '2014', '土地资源管理', '1', '81504');
INSERT INTO `professional` VALUES ('219', '2015', '土地资源管理', '1', '81504');
INSERT INTO `professional` VALUES ('220', '2016', '土地资源管理', '1', '81504');
INSERT INTO `professional` VALUES ('221', '2013', '生物科学类(生物科学(师范)、生物技术)', '1', '81601');
INSERT INTO `professional` VALUES ('222', '2014', '生物科学类(生物科学(师范)、生物技术)', '1', '81601');
INSERT INTO `professional` VALUES ('223', '2015', '生物科学类(生物科学(师范)、生物技术)', '1', '81601');
INSERT INTO `professional` VALUES ('224', '2016', '生物科学类(生物科学(师范)、生物技术)', '1', '81601');
INSERT INTO `professional` VALUES ('225', '2013', '生物工程', '1', '81602');
INSERT INTO `professional` VALUES ('226', '2014', '生物工程', '1', '81602');
INSERT INTO `professional` VALUES ('227', '2015', '生物工程', '1', '81602');
INSERT INTO `professional` VALUES ('228', '2016', '生物工程', '1', '81602');
INSERT INTO `professional` VALUES ('229', '2013', '食品科学与工程', '1', '81701');
INSERT INTO `professional` VALUES ('230', '2014', '食品科学与工程', '1', '81701');
INSERT INTO `professional` VALUES ('231', '2015', '食品科学与工程', '1', '81701');
INSERT INTO `professional` VALUES ('232', '2016', '食品科学与工程', '1', '81701');
INSERT INTO `professional` VALUES ('233', '2013', '食品质量与安全', '1', '81702');
INSERT INTO `professional` VALUES ('234', '2014', '食品质量与安全', '1', '81702');
INSERT INTO `professional` VALUES ('235', '2015', '食品质量与安全', '1', '81702');
INSERT INTO `professional` VALUES ('236', '2016', '食品质量与安全', '1', '81702');
INSERT INTO `professional` VALUES ('237', '2013', '体育教育（师范）', '1', '81801');
INSERT INTO `professional` VALUES ('238', '2013', '体育教育（师范）', '2', '81801');
INSERT INTO `professional` VALUES ('239', '2014', '体育教育（师范）', '1', '81801');
INSERT INTO `professional` VALUES ('240', '2015', '体育教育（师范）', '1', '81801');
INSERT INTO `professional` VALUES ('241', '2016', '体育教育（师范）', '1', '81801');
INSERT INTO `professional` VALUES ('242', '2013', '社会体育指导与管理', '1', '81802');
INSERT INTO `professional` VALUES ('243', '2014', '社会体育指导与管理', '1', '81802');
INSERT INTO `professional` VALUES ('244', '2015', '社会体育指导与管理', '1', '81802');
INSERT INTO `professional` VALUES ('245', '2016', '社会体育指导与管理', '1', '81802');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `s_id` int(10) NOT NULL AUTO_INCREMENT,
  `s_name` char(4) DEFAULT NULL COMMENT '姓名',
  `s_sex` enum('男','女') DEFAULT NULL COMMENT '性别',
  `s_phone` char(11) DEFAULT NULL COMMENT '手机',
  `s_email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `pc_id` int(10) DEFAULT NULL COMMENT '专业id',
  `s_acc` char(16) DEFAULT NULL COMMENT '学号',
  `s_pwd` char(16) DEFAULT NULL COMMENT '密码',
  `s_qq` char(15) DEFAULT NULL COMMENT 'qq',
  PRIMARY KEY (`s_id`),
  KEY `pc_id` (`pc_id`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`pc_id`) REFERENCES `professional` (`pc_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('3', '施迪健', '男', '13272439071', '603367493@qq.com', '1', '13115021031', '123456', '603367493');
INSERT INTO `student` VALUES ('4', '徐绍殷', '男', '15913999836', '15913999836@163.com', '2', '13115021011', '123456', '159139983');
INSERT INTO `student` VALUES ('5', '潘红茂', '男', '12456781234', '12345678152@qq.com', '3', '13115212346', '123456', '123458148');

-- ----------------------------
-- Table structure for study_resource
-- ----------------------------
DROP TABLE IF EXISTS `study_resource`;
CREATE TABLE `study_resource` (
  `sr_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `sr_path` varchar(50) DEFAULT NULL COMMENT '资源路径',
  `c_id` int(10) DEFAULT NULL COMMENT '课程id',
  PRIMARY KEY (`sr_id`),
  KEY `c_id` (`c_id`),
  CONSTRAINT `study_resource_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of study_resource
-- ----------------------------

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `t_id` int(10) NOT NULL AUTO_INCREMENT,
  `t_name` char(4) DEFAULT NULL COMMENT '名字',
  `t_sex` enum('男','女') DEFAULT NULL COMMENT '性别',
  `t_phone` char(11) DEFAULT NULL COMMENT '手机',
  `t_email` varchar(30) DEFAULT NULL COMMENT '邮箱',
  `t_acc` char(16) DEFAULT NULL COMMENT '账号',
  `t_pwd` char(16) DEFAULT NULL,
  `t_state` enum('0','1') DEFAULT NULL COMMENT '状态',
  `d_code` char(4) DEFAULT NULL,
  PRIMARY KEY (`t_id`),
  KEY `d_code` (`d_code`),
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`d_code`) REFERENCES `departments` (`d_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1', 'SDK', '女', '110', '110@sdk.com', '0001', '123456', '1', '3101');
INSERT INTO `teacher` VALUES ('6', '刘宇恒', '男', '13428346646', '983174852@qq.com', '13115031069', '123456789', '0', '3106');
INSERT INTO `teacher` VALUES ('7', '刘宇恒', '男', '18219222904', null, '13115031055', '123456789', '0', '3109');
INSERT INTO `teacher` VALUES ('8', '师弟', '男', '18219222904', '983174852@qq.com', '13115021031', '', '0', '3104');
INSERT INTO `teacher` VALUES ('22', '张三', '男', '13428346666', '983178523@qq.com', '13115031056', '123456', '0', '3113');
INSERT INTO `teacher` VALUES ('23', '小黑', null, null, null, '13115031057', '123456', '1', '3114');
INSERT INTO `teacher` VALUES ('24', '小黑', null, null, null, '13115031096', '123456', '1', '3114');
INSERT INTO `teacher` VALUES ('26', '小兰', null, null, null, '13115031051', '123456', '1', '3113');
INSERT INTO `teacher` VALUES ('27', '刘德华', null, null, null, '13115031085', '123456', '1', '3116');
INSERT INTO `teacher` VALUES ('28', '小刚', null, null, null, '13115031065', '123456', '1', '3113');
INSERT INTO `teacher` VALUES ('29', '小红', null, null, null, '13115031052', '123456', '1', '3114');
INSERT INTO `teacher` VALUES ('30', '林峰', null, null, null, '13115031075', '123456', '1', '3114');
INSERT INTO `teacher` VALUES ('31', '小刚', null, null, null, '131153155', '123456', '1', '3114');
INSERT INTO `teacher` VALUES ('32', '刘晓亮', '男', '18564878970', '9864812@qq.com', '13115031050', '123456', '0', '3114');
INSERT INTO `teacher` VALUES ('33', '潘洪茂', null, null, null, '13115011017', 'phm13579', '1', '3111');

-- ----------------------------
-- Table structure for teacher_course
-- ----------------------------
DROP TABLE IF EXISTS `teacher_course`;
CREATE TABLE `teacher_course` (
  `tc_id` int(10) NOT NULL AUTO_INCREMENT,
  `t_id` int(10) DEFAULT NULL COMMENT '教师id',
  `c_id` int(10) DEFAULT NULL COMMENT '课程id',
  PRIMARY KEY (`tc_id`),
  KEY `t_id` (`t_id`),
  KEY `c_id` (`c_id`),
  CONSTRAINT `teacher_course_ibfk_1` FOREIGN KEY (`t_id`) REFERENCES `teacher` (`t_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `teacher_course_ibfk_2` FOREIGN KEY (`c_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher_course
-- ----------------------------
INSERT INTO `teacher_course` VALUES ('8', '1', '2');
INSERT INTO `teacher_course` VALUES ('10', '8', '2');
INSERT INTO `teacher_course` VALUES ('11', '8', '4');
INSERT INTO `teacher_course` VALUES ('12', '6', '15');
INSERT INTO `teacher_course` VALUES ('13', '6', '17');
INSERT INTO `teacher_course` VALUES ('14', '6', '18');
