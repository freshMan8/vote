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