CREATE DATABASE IF NOT EXISTS cinema_ticketing DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE cinema_ticketing;

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `username` varchar(32) NOT NULL COMMENT 'Username',
  `name` varchar(32) NOT NULL COMMENT 'Name',
  `password` varchar(64) NOT NULL COMMENT 'Password',
  `phone` varchar(11) NOT NULL COMMENT 'Phone',
  `sex` varchar(2) NOT NULL COMMENT 'Gender',
  `id_number` varchar(18) NOT NULL COMMENT 'ID Number',
  `status` int NOT NULL DEFAULT '1' COMMENT 'Status 0:Disabled, 1:Enabled',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  `create_user` bigint NOT NULL COMMENT 'Create User',
  `update_user` bigint NOT NULL COMMENT 'Update User',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Employee Table';

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `type` int NOT NULL COMMENT 'Type 1:Movie Type 2:Cinema Type',
  `name` varchar(32) NOT NULL COMMENT 'Category Name',
  `sort` int NOT NULL DEFAULT '0' COMMENT 'Sort Order',
  `status` int DEFAULT '1' COMMENT 'Status 0:Disabled, 1:Enabled',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  `create_user` bigint NOT NULL COMMENT 'Create User',
  `update_user` bigint NOT NULL COMMENT 'Update User',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Category Table';

DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `name` varchar(32) NOT NULL COMMENT 'Movie Name',
  `category_id` bigint NOT NULL COMMENT 'Movie Category ID',
  `price` decimal(10,2) NOT NULL COMMENT 'Ticket Price',
  `image` varchar(255) NOT NULL COMMENT 'Movie Poster',
  `description` varchar(255) NOT NULL COMMENT 'Movie Description',
  `duration` int NOT NULL COMMENT 'Duration (minutes)',
  `director` varchar(50) COMMENT 'Director',
  `actors` varchar(255) COMMENT 'Actors',
  `release_date` date COMMENT 'Release Date',
  `status` int NOT NULL DEFAULT '1' COMMENT 'Status 0:Off Shelf, 1:On Shelf',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  `create_user` bigint NOT NULL COMMENT 'Create User',
  `update_user` bigint NOT NULL COMMENT 'Update User',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Movie Table';

DROP TABLE IF EXISTS `cinema`;
CREATE TABLE `cinema` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `name` varchar(50) NOT NULL COMMENT 'Cinema Name',
  `address` varchar(255) NOT NULL COMMENT 'Cinema Address',
  `phone` varchar(20) COMMENT 'Contact Phone',
  `image` varchar(255) COMMENT 'Cinema Image',
  `description` varchar(255) COMMENT 'Cinema Description',
  `status` int NOT NULL DEFAULT '1' COMMENT 'Status 0:Closed, 1:Open',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  `create_user` bigint NOT NULL COMMENT 'Create User',
  `update_user` bigint NOT NULL COMMENT 'Update User',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Cinema Table';

DROP TABLE IF EXISTS `hall`;
CREATE TABLE `hall` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `cinema_id` bigint NOT NULL COMMENT 'Cinema ID',
  `name` varchar(50) NOT NULL COMMENT 'Hall Name',
  `type` varchar(20) NOT NULL COMMENT 'Hall Type (IMAX/3D/2D/4DX etc.)',
  `row_count` int NOT NULL COMMENT 'Row Count',
  `col_count` int NOT NULL COMMENT 'Column Count',
  `total_seats` int NOT NULL COMMENT 'Total Seats',
  `status` int NOT NULL DEFAULT '1' COMMENT 'Status 0:Maintenance, 1:Normal',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  `create_user` bigint NOT NULL COMMENT 'Create User',
  `update_user` bigint NOT NULL COMMENT 'Update User',
  PRIMARY KEY (`id`),
  KEY `idx_cinema_id` (`cinema_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Hall Table';

DROP TABLE IF EXISTS `seat`;
CREATE TABLE `seat` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `hall_id` bigint NOT NULL COMMENT 'Hall ID',
  `row_num` int NOT NULL COMMENT 'Row Number',
  `col_num` int NOT NULL COMMENT 'Column Number',
  `seat_type` varchar(20) NOT NULL COMMENT 'Seat Type (Normal/VIP/Couple etc.)',
  `status` int NOT NULL DEFAULT '1' COMMENT 'Status 0:Broken, 1:Normal',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_hall_row_col` (`hall_id`,`row_num`,`col_num`),
  KEY `idx_hall_id` (`hall_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Seat Table';

DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `movie_id` bigint NOT NULL COMMENT 'Movie ID',
  `cinema_id` bigint NOT NULL COMMENT 'Cinema ID',
  `hall_id` bigint NOT NULL COMMENT 'Hall ID',
  `show_date` date NOT NULL COMMENT 'Show Date',
  `show_time` time NOT NULL COMMENT 'Show Time',
  `end_time` time NOT NULL COMMENT 'End Time',
  `price` decimal(10,2) NOT NULL COMMENT 'Ticket Price',
  `status` int NOT NULL DEFAULT '1' COMMENT 'Status 0:Cancelled, 1:Normal',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  `create_user` bigint NOT NULL COMMENT 'Create User',
  `update_user` bigint NOT NULL COMMENT 'Update User',
  PRIMARY KEY (`id`),
  KEY `idx_movie_id` (`movie_id`),
  KEY `idx_cinema_id` (`cinema_id`),
  KEY `idx_hall_id` (`hall_id`),
  KEY `idx_show_date` (`show_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Schedule Table';

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `number` varchar(50) NOT NULL COMMENT 'Order Number',
  `status` int NOT NULL COMMENT 'Order Status 1:Pending Payment 2:Paid 3:Cancelled 4:Refunded',
  `user_id` bigint NOT NULL COMMENT 'User ID',
  `schedule_id` bigint NOT NULL COMMENT 'Schedule ID',
  `order_time` datetime NOT NULL COMMENT 'Order Time',
  `pay_time` datetime COMMENT 'Payment Time',
  `pay_method` int COMMENT 'Payment Method 1:WeChat 2:Alipay',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT 'Payment Status 0:Unpaid 1:Paid 2:Refunded',
  `amount` decimal(10,2) NOT NULL COMMENT 'Actual Amount',
  `remark` varchar(255) COMMENT 'Remark',
  `user_name` varchar(32) COMMENT 'User Name',
  `phone` varchar(11) COMMENT 'Phone',
  `cancel_reason` varchar(255) COMMENT 'Cancel Reason',
  `cancel_time` datetime COMMENT 'Cancel Time',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_number` (`number`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_schedule_id` (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Ticket Table';

DROP TABLE IF EXISTS `ticket_detail`;
CREATE TABLE `ticket_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `ticket_id` bigint NOT NULL COMMENT 'Ticket ID',
  `seat_id` bigint NOT NULL COMMENT 'Seat ID',
  `price` decimal(10,2) NOT NULL COMMENT 'Ticket Price',
  `row_num` int NOT NULL COMMENT 'Row Number',
  `col_num` int NOT NULL COMMENT 'Column Number',
  `seat_type` varchar(20) NOT NULL COMMENT 'Seat Type',
  PRIMARY KEY (`id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_seat_id` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Ticket Detail Table';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `openid` varchar(45) NOT NULL COMMENT 'WeChat OpenID',
  `name` varchar(32) COMMENT 'Name',
  `phone` varchar(11) COMMENT 'Phone',
  `sex` varchar(2) COMMENT 'Gender',
  `id_number` varchar(18) COMMENT 'ID Number',
  `avatar` varchar(255) COMMENT 'Avatar',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='User Table';

DROP TABLE IF EXISTS `seat_selection`;
CREATE TABLE `seat_selection` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
  `user_id` bigint NOT NULL COMMENT 'User ID',
  `schedule_id` bigint NOT NULL COMMENT 'Schedule ID',
  `seat_id` bigint NOT NULL COMMENT 'Seat ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create Time',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_schedule_id` (`schedule_id`),
  KEY `idx_seat_id` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Seat Selection Table';

INSERT INTO `employee` (`username`, `name`, `password`, `phone`, `sex`, `id_number`, `status`, `create_time`, `update_time`, `create_user`, `update_user`) 
VALUES ('admin', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13800138000', '1', '110101199001011234', 1, NOW(), NOW(), 1, 1);

INSERT INTO `category` (`type`, `name`, `sort`, `status`, `create_time`, `update_time`, `create_user`, `update_user`) VALUES
(1, 'Action', 1, 1, NOW(), NOW(), 1, 1),
(1, 'Comedy', 2, 1, NOW(), NOW(), 1, 1),
(1, 'Romance', 3, 1, NOW(), NOW(), 1, 1),
(1, 'Sci-Fi', 4, 1, NOW(), NOW(), 1, 1),
(1, 'Horror', 5, 1, NOW(), NOW(), 1, 1),
(1, 'Animation', 6, 1, NOW(), NOW(), 1, 1),
(2, 'IMAX Cinema', 1, 1, NOW(), NOW(), 1, 1),
(2, '3D Cinema', 2, 1, NOW(), NOW(), 1, 1),
(2, '4DX Cinema', 3, 1, NOW(), NOW(), 1, 1);