/*
Navicat MySQL Data Transfer

Source Server         : data
Source Server Version : 50611
Source Host           : localhost:3306
Source Database       : onlinemusic

Target Server Type    : MYSQL
Target Server Version : 50611
File Encoding         : 65001

Date: 2019-02-14 23:27:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_album
-- ----------------------------
DROP TABLE IF EXISTS `t_album`;
CREATE TABLE `t_album` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '专辑名',
  `singer` varchar(255) NOT NULL COMMENT '歌手名',
  `views` int(11) NOT NULL COMMENT '访问量',
  `introduction` varchar(1000) NOT NULL COMMENT '介绍',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='专辑表';

-- ----------------------------
-- Records of t_album
-- ----------------------------
INSERT INTO `t_album` VALUES ('1', '十月飞雪', '汪峰', '10', '');

-- ----------------------------
-- Table structure for t_lyc
-- ----------------------------
DROP TABLE IF EXISTS `t_lyc`;
CREATE TABLE `t_lyc` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `lycName` varchar(50) NOT NULL COMMENT '歌词名',
  `songID` int(11) NOT NULL COMMENT '歌手id',
  `lycURL` varchar(255) NOT NULL COMMENT '歌词地址',
  `author` varchar(255) NOT NULL COMMENT '作者',
  `songName` varchar(255) DEFAULT NULL COMMENT '歌曲名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='歌词表';

-- ----------------------------
-- Records of t_lyc
-- ----------------------------

-- ----------------------------
-- Table structure for t_manager
-- ----------------------------
DROP TABLE IF EXISTS `t_manager`;
CREATE TABLE `t_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(30) NOT NULL COMMENT '管理员账号',
  `password` varchar(30) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='管理员表';

-- ----------------------------
-- Records of t_manager
-- ----------------------------
INSERT INTO `t_manager` VALUES ('1', 'root', '1234');

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `contant` longtext COMMENT '通知内容',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `status` varchar(255) DEFAULT NULL COMMENT '状态（启用：1。失效：2）',
  `updatedTime` datetime DEFAULT NULL COMMENT '更新时间',
  `songID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='通知表';

-- ----------------------------
-- Records of t_message
-- ----------------------------
INSERT INTO `t_message` VALUES ('1', '杨千嬅，原名杨泽嬅，1974年2月3日出生于中国香港，中国香港女歌手、演员。1995年参加第14届TVB8全球华人新秀歌唱大赛获得季军而入行。2000年以《少女的祈祷》一曲横扫香港乐坛四台颁奖典礼多个金曲奖项。2002年、2008年与2009年三度夺得十大劲歌金曲最受欢迎女歌星。2013年凭借《春娇与志明》夺得第32届香港电影金像奖最佳女主角，2014年主演电影《五个小孩的校长》，同年推出国语单曲《色惑》。2010年凭电影《抱抱俏佳人》获得香港电影评论学会大奖最佳女主角。 同年，也凭《志明与春娇》获香港电影金像奖提名最佳女主角，[3]由此成为继梅艳芳之后第二位获得香港乐坛与影坛标志性大奖的女艺人。参演电影《完美嫁衣》[4]，影片中杨千嬅扮演一个在婚礼上被未婚夫抛弃的准新娘，随后自己成为知名的婚礼策划师 。2013年7月，参演电影《单身男女2》 ，也是十年之后再次和古天乐合作。', '2017-09-10 12:07:50', '1', '2017-09-10 12:07:54', null);

-- ----------------------------
-- Table structure for t_singer
-- ----------------------------
DROP TABLE IF EXISTS `t_singer`;
CREATE TABLE `t_singer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) NOT NULL COMMENT '歌手名',
  `sex` varchar(5) NOT NULL COMMENT '性别',
  `views` int(11) NOT NULL COMMENT '访问量',
  `hobby` varchar(255) NOT NULL COMMENT '爱好',
  `type` varchar(20) DEFAULT NULL COMMENT '类型（欧美、日韩、港台、大陆）',
  `photoName` varchar(25) NOT NULL COMMENT '歌手头像名',
  `introduce` varchar(1000) DEFAULT NULL COMMENT '歌手介绍',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='歌手表';

-- ----------------------------
-- Records of t_singer
-- ----------------------------
INSERT INTO `t_singer` VALUES ('1', '汪峰', '男', '1', '开车', '港台', '../../img/汪峰.jpg', '汪峰很牛逼');
INSERT INTO `t_singer` VALUES ('2', '杨千嬅', '女', '2', '喝酒', '港台', '../../img/杨千嬅.jpg', '杨千嬅，原名杨泽嬅，1974年2月3日出生于中国香港，中国香港女歌手、演员。1995年参加第14届TVB8全球华人新秀歌唱大赛获得季军而入行。2000年以《少女的祈祷》一曲横扫香港乐坛四台颁奖典礼多个金曲奖项。2002年、2008年与2009年三度夺得十大劲歌金曲最受欢迎女歌星。2013年凭借《春娇与志明》夺得第32届香港电影金像奖最佳女主角，2014年主演电影《五个小孩的校长》，同年推出国语单曲《色惑》。2010年凭电影《抱抱俏佳人》获得香港电影评论学会大奖最佳女主角。 同年，也凭《志明与春娇》获香港电影金像奖提名最佳女主角，[3]由此成为继梅艳芳之后第二位获得香港乐坛与影坛标志性大奖的女艺人。参演电影《完美嫁衣》[4]，影片中杨千嬅扮演一个在婚礼上被未婚夫抛弃的准新娘，随后自己成为知名的婚礼策划师 。2013年7月，参演电影《单身男女2》 ，也是十年之后再次和古天乐合作。');
INSERT INTO `t_singer` VALUES ('3', '周杰伦', '男', '3', '小公举', '港台', '../../img/周杰伦.jpg', '摩羯座');

-- ----------------------------
-- Table structure for t_song
-- ----------------------------
DROP TABLE IF EXISTS `t_song`;
CREATE TABLE `t_song` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `songName` varchar(255) NOT NULL COMMENT '歌曲名',
  `singer` varchar(255) NOT NULL COMMENT '歌手名',
  `album` varchar(255) DEFAULT NULL COMMENT '专辑名',
  `typeName` varchar(50) NOT NULL COMMENT '类型（欧美、大陆、港台、日韩）',
  `fileSize` varchar(50) NOT NULL COMMENT '文件大小',
  `fileURL` varchar(255) NOT NULL COMMENT '文件地址',
  `format` varchar(10) NOT NULL COMMENT '格式',
  `hits` int(11) NOT NULL COMMENT '点击量',
  `download` int(11) NOT NULL COMMENT '下载量',
  `playTime` varchar(20) DEFAULT NULL COMMENT '播放时长',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='歌曲表';

-- ----------------------------
-- Records of t_song
-- ----------------------------
INSERT INTO `t_song` VALUES ('1', '无处安放', '汪峰', '', '港台', '10', '../../music/无处安放.mp3', 'mp3', '26', '9', '4:30');
INSERT INTO `t_song` VALUES ('2', '处处吻', '杨千嬅', '大城小事', '港台', '20', '../../music/处处吻.mp3', 'mp3', '13', '2', null);
INSERT INTO `t_song` VALUES ('3', '大城小事', '杨千嬅', '大城小事', '港台', '30', '../../music/大城小事.mp3', 'mp3', '1', '10', '2:20');
INSERT INTO `t_song` VALUES ('4', '回到过去', '周杰伦', null, '港台', '20', 'www', 'ogg', '11', '1', null);

-- ----------------------------
-- Table structure for t_songlist
-- ----------------------------
DROP TABLE IF EXISTS `t_songlist`;
CREATE TABLE `t_songlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `userID` int(11) NOT NULL COMMENT '用户名id',
  `songID` int(11) NOT NULL COMMENT '歌曲id',
  `name` varchar(50) NOT NULL COMMENT '歌单名称',
  PRIMARY KEY (`id`,`userID`,`songID`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='播放列表';

-- ----------------------------
-- Records of t_songlist
-- ----------------------------
INSERT INTO `t_songlist` VALUES ('2', '1', '2', '播放列表');
INSERT INTO `t_songlist` VALUES ('3', '1', '3', '播放列表');
INSERT INTO `t_songlist` VALUES ('18', '2', '1', '播放列表');
INSERT INTO `t_songlist` VALUES ('19', '2', '2', '播放列表');
INSERT INTO `t_songlist` VALUES ('20', '2', '4', '播放列表');
INSERT INTO `t_songlist` VALUES ('21', '2', '6', '播放列表');
INSERT INTO `t_songlist` VALUES ('22', '2', '1', '播放列表');
INSERT INTO `t_songlist` VALUES ('23', '2', '2', '播放列表');
INSERT INTO `t_songlist` VALUES ('24', '2', '4', '播放列表');
INSERT INTO `t_songlist` VALUES ('25', '2', '6', '播放列表');
INSERT INTO `t_songlist` VALUES ('26', '2', '1', '播放列表');
INSERT INTO `t_songlist` VALUES ('27', '2', '2', '播放列表');
INSERT INTO `t_songlist` VALUES ('28', '2', '4', '播放列表');
INSERT INTO `t_songlist` VALUES ('29', '2', '6', '播放列表');
INSERT INTO `t_songlist` VALUES ('30', '1', '10', '播放列表');
INSERT INTO `t_songlist` VALUES ('31', '1', '13', '播放列表');
INSERT INTO `t_songlist` VALUES ('32', '1', '14', '播放列表');
INSERT INTO `t_songlist` VALUES ('39', '1', '11', '播放列表');
INSERT INTO `t_songlist` VALUES ('40', '1', '15', '播放列表');
INSERT INTO `t_songlist` VALUES ('41', '1', '16', '播放列表');
INSERT INTO `t_songlist` VALUES ('42', '1', '20', '播放列表');
INSERT INTO `t_songlist` VALUES ('43', '1', '22', '播放列表');
INSERT INTO `t_songlist` VALUES ('44', '1', '33', '播放列表');

-- ----------------------------
-- Table structure for t_songtype
-- ----------------------------
DROP TABLE IF EXISTS `t_songtype`;
CREATE TABLE `t_songtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeName` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_songtype
-- ----------------------------
INSERT INTO `t_songtype` VALUES ('1', '儿歌');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(30) NOT NULL COMMENT '账号',
  `password` varchar(30) NOT NULL COMMENT '密码',
  `nickname` varchar(30) DEFAULT NULL COMMENT '昵称',
  `sex` varchar(5) DEFAULT NULL COMMENT '性别',
  `qq` varchar(15) DEFAULT NULL COMMENT 'QQ号码',
  `email` varchar(30) DEFAULT NULL COMMENT '邮件',
  `introduction` varchar(255) DEFAULT NULL COMMENT '介绍',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('2', '123456', '12345678', 'a', '男', '903936584', '903936584@qq.com', '你好');
INSERT INTO `t_user` VALUES ('3', '1234567', '12345678', '啊', '女', '131210989', '903936584@qq.com', '你好2');
INSERT INTO `t_user` VALUES ('5', '1234568', '12345678', 'QQ', '女', '', '', '');
INSERT INTO `t_user` VALUES ('6', '1234569', '12345678', '接', '女', '', '', '');
INSERT INTO `t_user` VALUES ('7', '1234560', '12345678', '是', '男', '', '', '');
INSERT INTO `t_user` VALUES ('13', '123456', '111111', null, null, null, null, null);
INSERT INTO `t_user` VALUES ('15', '1234', '12341', null, null, null, null, null);
INSERT INTO `t_user` VALUES ('16', '123456', '666666', null, null, null, null, null);
INSERT INTO `t_user` VALUES ('17', '12345679', '12345678', null, null, null, null, null);
