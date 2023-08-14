CREATE TABLE `t_user` (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                          `account` varchar(32) NOT NULL COMMENT '用户编码',
                          `user_name` varchar(32) DEFAULT NULL COMMENT '用户名称',
                          `phone_num` varchar(32) NOT NULL COMMENT '手机号',
                          `pic_url` varchar(128) NOT NULL COMMENT '用户头像链接',
                          `user_type` int DEFAULT NULL COMMENT '用户类型 0 机器人 1 真实用户',
                          `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          `updated_by` varchar(64) NOT NULL COMMENT '更新人',
                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `create_by` varchar(64) NOT NULL COMMENT '创建人',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息';


CREATE TABLE `t_news` (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                          `title` varchar(256) NOT NULL COMMENT '主题',
                          `short_context` varchar(128) DEFAULT NULL COMMENT '内容缩写',
                          `context` longtext DEFAULT NULL COMMENT '内容',
                          `pic_url` varchar(128) NOT NULL COMMENT '主题url',
                          `sort` int DEFAULT NULL COMMENT '排序置顶',
                          `sort_time` datetime DEFAULT NULL COMMENT '置顶过期时间',
                          `type` varchar(32) DEFAULT 'news' COMMENT '类型',
                          `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          `updated_by` varchar(64) NOT NULL COMMENT '更新人',
                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `create_by` varchar(64) NOT NULL COMMENT '创建人',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='动态';



CREATE TABLE `t_activity_header` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                     `title` varchar(256) NOT NULL COMMENT '主题',
                                     `pic_url` varchar(128) NOT NULL COMMENT '主题url',
                                     `sort` int DEFAULT NULL COMMENT '排序置顶',
                                     `sort_time` datetime DEFAULT NULL COMMENT '置顶过期时间',
                                     `participant_num` int NOT NULL DEFAULT 0 COMMENT '参与人数',
                                     `vote_num` int NOT NULL DEFAULT 0 COMMENT '投票数',
                                     `visit_num` int NOT NULL DEFAULT 0 COMMENT '访问数',
                                     `activity_type` varchar(64) NOT NULL COMMENT '活动类型',
                                     `vote_thyme` int NOT NULL DEFAULT 0 COMMENT '主题类型',
                                     `start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
                                     `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
                                     `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `updated_by` varchar(64) NOT NULL COMMENT '更新人',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `create_by` varchar(64) NOT NULL COMMENT '创建人',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动主表';



CREATE TABLE `t_activity_detail` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                     `activity_id` bigint NOT NULL COMMENT '活动主表id',
                                     `user_id` bigint NOT NULL COMMENT '参与人ID',
                                     `phone_num` varchar(128) NOT NULL COMMENT '参与人手机号',
                                     `location` varchar(128) NOT NULL COMMENT '参与人收货地址',
                                     `name` varchar(64) NOT NULL COMMENT '参与人收货人',
                                     `title` varchar(256) NOT NULL COMMENT '主题',
                                     `short_context` text DEFAULT NULL COMMENT '内容缩写',
                                     `pic_url` varchar(128) NOT NULL COMMENT '主题url',
                                     `order_num` int NOT NULL COMMENT '参赛编号',
                                     `vote_num` int NOT NULL DEFAULT 0 COMMENT '投票数',
                                     `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `updated_by` varchar(64) NOT NULL COMMENT '更新人',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `create_by` varchar(64) NOT NULL COMMENT '创建人',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动参与明细表';



CREATE TABLE `t_activity_vote_detail` (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                          `activity_detail_id` bigint NOT NULL COMMENT '活动明细表id',
                                          `user_id` bigint NOT NULL COMMENT '投票人ID',
                                          `vote_num` int NOT NULL DEFAULT 0 COMMENT '投票数',
                                          `gift_type` int NOT NULL DEFAULT 0 COMMENT '礼物类型',
                                          `vote_type` int NOT NULL DEFAULT 0 COMMENT '投票类型 0 为机器人 1为正常用户',
                                          `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                          `updated_by` varchar(64) NOT NULL COMMENT '更新人',
                                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `create_by` varchar(64) NOT NULL COMMENT '创建人',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动投票详情';

CREATE TABLE `t_news_detail` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                 `news_id` bigint NOT NULL COMMENT '动态ID',
                                 `context` longtext DEFAULT NULL COMMENT '内容',
                                 `type` varchar(32) DEFAULT 'news' COMMENT '类型',
                                 `orders` int NOT NULL DEFAULT 1 COMMENT '排序',
                                 `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `updated_by` varchar(64) NOT NULL COMMENT '更新人',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `create_by` varchar(64) NOT NULL COMMENT '创建人',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='动态详情';

CREATE TABLE `t_gift` (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                          `activity_type` varchar(64) NOT NULL COMMENT '活动类型',
                          `gift_name` varchar(64) NOT NULL COMMENT '礼物名称',
                          `vote_num` int NOT NULL DEFAULT 1 COMMENT '投票数',
                          `price` int NOT NULL DEFAULT 1 COMMENT '价值金额（分）',
                          `pic_url` varchar(128) NOT NULL COMMENT '图片地址',
                          `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          `updated_by` varchar(64) NOT NULL COMMENT '更新人',
                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `create_by` varchar(64) NOT NULL COMMENT '创建人',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='活动投票详情';




