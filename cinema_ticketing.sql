CREATE DATABASE IF NOT EXISTS cinema_ticketing DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE cinema_ticketing;

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `phone` varchar(11) NOT NULL COMMENT '手机号',
  `sex` varchar(2) NOT NULL COMMENT '性别',
  `id_number` varchar(18) NOT NULL COMMENT '身份证号',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:禁用，1:启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人',
  `update_user` bigint NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='员工表';

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int NOT NULL COMMENT '类型 1:电影类型 2:影院类型',
  `name` varchar(32) NOT NULL COMMENT '分类名称',
  `sort` int NOT NULL DEFAULT '0' COMMENT '顺序',
  `status` int DEFAULT '1' COMMENT '分类状态 0:禁用，1:启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人',
  `update_user` bigint NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='分类表';

DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) NOT NULL COMMENT '电影名称',
  `category_id` bigint NOT NULL COMMENT '电影分类id',
  `price` decimal(10,2) NOT NULL COMMENT '票价',
  `image` varchar(255) NOT NULL COMMENT '电影海报',
  `description` varchar(255) NOT NULL COMMENT '电影描述',
  `duration` int NOT NULL COMMENT '电影时长(分钟)',
  `director` varchar(50) COMMENT '导演',
  `actors` varchar(255) COMMENT '主演',
  `release_date` date COMMENT '上映日期',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:下架 1:上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人',
  `update_user` bigint NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='电影表';

DROP TABLE IF EXISTS `cinema`;
CREATE TABLE `cinema` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '影院名称',
  `address` varchar(255) NOT NULL COMMENT '影院地址',
  `phone` varchar(20) COMMENT '联系电话',
  `image` varchar(255) COMMENT '影院图片',
  `description` varchar(255) COMMENT '影院描述',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:关闭 1:营业',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人',
  `update_user` bigint NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='影院表';

DROP TABLE IF EXISTS `hall`;
CREATE TABLE `hall` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cinema_id` bigint NOT NULL COMMENT '影院id',
  `name` varchar(50) NOT NULL COMMENT '影厅名称',
  `type` varchar(20) NOT NULL COMMENT '影厅类型(IMAX/3D/2D/4DX等)',
  `row_count` int NOT NULL COMMENT '行数',
  `col_count` int NOT NULL COMMENT '列数',
  `total_seats` int NOT NULL COMMENT '总座位数',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:维护中 1:正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人',
  `update_user` bigint NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_cinema_id` (`cinema_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='影厅表';

DROP TABLE IF EXISTS `seat`;
CREATE TABLE `seat` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `hall_id` bigint NOT NULL COMMENT '影厅id',
  `row_num` int NOT NULL COMMENT '行号',
  `col_num` int NOT NULL COMMENT '列号',
  `seat_type` varchar(20) NOT NULL COMMENT '座位类型(普通/VIP/情侣座等)',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:损坏 1:正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hall_row_col` (`hall_id`,`row_num`,`col_num`),
  KEY `idx_hall_id` (`hall_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='座位表';

DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `movie_id` bigint NOT NULL COMMENT '电影id',
  `cinema_id` bigint NOT NULL COMMENT '影院id',
  `hall_id` bigint NOT NULL COMMENT '影厅id',
  `show_date` date NOT NULL COMMENT '放映日期',
  `show_time` time NOT NULL COMMENT '放映时间',
  `end_time` time NOT NULL COMMENT '结束时间',
  `price` decimal(10,2) NOT NULL COMMENT '票价',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:已取消 1:正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人',
  `update_user` bigint NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_movie_id` (`movie_id`),
  KEY `idx_cinema_id` (`cinema_id`),
  KEY `idx_hall_id` (`hall_id`),
  KEY `idx_show_date` (`show_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='排片表';

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(50) NOT NULL COMMENT '订单号',
  `status` int NOT NULL COMMENT '订单状态 1:待支付 2:已支付 3:已取消 4:已退款',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `schedule_id` bigint NOT NULL COMMENT '排片id',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `pay_time` datetime COMMENT '支付时间',
  `pay_method` int COMMENT '支付方式 1:微信 2:支付宝',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0:未支付 1:已支付 2:退款',
  `amount` decimal(10,2) NOT NULL COMMENT '实付金额',
  `remark` varchar(255) COMMENT '备注',
  `user_name` varchar(32) COMMENT '用户名',
  `phone` varchar(11) COMMENT '手机号',
  `cancel_reason` varchar(255) COMMENT '取消原因',
  `cancel_time` datetime COMMENT '取消时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number` (`number`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_schedule_id` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='票务表';

DROP TABLE IF EXISTS `ticket_detail`;
CREATE TABLE `ticket_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ticket_id` bigint NOT NULL COMMENT '票务id',
  `seat_id` bigint NOT NULL COMMENT '座位id',
  `price` decimal(10,2) NOT NULL COMMENT '票价',
  `row_num` int NOT NULL COMMENT '行号',
  `col_num` int NOT NULL COMMENT '列号',
  `seat_type` varchar(20) NOT NULL COMMENT '座位类型',
  PRIMARY KEY (`id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_seat_id` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='票务详情表';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` varchar(45) NOT NULL COMMENT '微信openid',
  `name` varchar(32) COMMENT '姓名',
  `phone` varchar(11) COMMENT '手机号',
  `sex` varchar(2) COMMENT '性别',
  `id_number` varchar(18) COMMENT '身份证号',
  `avatar` varchar(255) COMMENT '头像',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

DROP TABLE IF EXISTS `seat_selection`;
CREATE TABLE `seat_selection` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `schedule_id` bigint NOT NULL COMMENT '排片id',
  `seat_id` bigint NOT NULL COMMENT '座位id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_schedule_id` (`schedule_id`),
  KEY `idx_seat_id` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='选座表';

INSERT INTO `employee` (`username`, `name`, `password`, `phone`, `sex`, `id_number`, `status`, `create_time`, `update_time`, `create_user`, `update_user`) 
VALUES ('admin', '管理员', 'e10adc3949ba59abbe56e057f20f883e', '13800138000', '1', '110101199001011234', 1, NOW(), NOW(), 1, 1);

INSERT INTO `category` (`type`, `name`, `sort`, `status`, `create_time`, `update_time`, `create_user`, `update_user`) VALUES
(1, '动作片', 1, 1, NOW(), NOW(), 1, 1),
(1, '喜剧片', 2, 1, NOW(), NOW(), 1, 1),
(1, '爱情片', 3, 1, NOW(), NOW(), 1, 1),
(1, '科幻片', 4, 1, NOW(), NOW(), 1, 1),
(1, '恐怖片', 5, 1, NOW(), NOW(), 1, 1),
(1, '动画片', 6, 1, NOW(), NOW(), 1, 1),
(2, 'IMAX影院', 1, 1, NOW(), NOW(), 1, 1),
(2, '3D影院', 2, 1, NOW(), NOW(), 1, 1),
(2, '4DX影院', 3, 1, NOW(), NOW(), 1, 1);