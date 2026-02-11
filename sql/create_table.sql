-- 需要使用 MariaDB 数据库，否则不支持 VECTOR 类型
CREATE DATABASE IF NOT EXISTS `ai_guide_db`;

USE `ai_guide_db`;

CREATE TABLE if not exists `user` (
    `id`            bigint          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_name`     varchar(256)    NOT NULL UNIQUE COMMENT '用户名',
    `phone`         char(11)        DEFAULT NULL UNIQUE COMMENT '手机号',
    `user_password` varchar(256)    DEFAULT NULL COMMENT '用户密码',
    `avatar_url`    varchar(256)    DEFAULT NULL COMMENT '头像地址',
    `role`          tinyint(1)      DEFAULT NULL COMMENT '角色',
    `status`        tinyint(1)      DEFAULT '0' COMMENT '状态(0-正常,1-禁用)',
    `create_time`   datetime        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     tinyint(1)      DEFAULT '0' COMMENT '是否删除？',
    PRIMARY KEY (`id`)
) COMMENT='用户表' collate=utf8mb4_unicode_ci;

CREATE TABLE if not exists `chat_room` (
    `id`            bigint          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title`         varchar(256)    NOT NULL UNIQUE COMMENT '聊天室标题',
    `app_code`      tinyint         NOT NULL COMMENT '应用编码',
    `user_id`       bigint          NOT NULL COMMENT '用户ID',
    `create_time`   datetime        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     tinyint(1)      DEFAULT '0' COMMENT '是否删除？',
    PRIMARY KEY (`id`),
    INDEX `idx_app_code` (`app_code`),
    INDEX `idx_user_id` (`user_id`)
) COMMENT='聊天室表' collate=utf8mb4_unicode_ci;

CREATE TABLE if not exists `chat` (
    `id`            bigint          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `room_id`       bigint          NOT NULL COMMENT '房间ID',
    `user_id`       bigint          NOT NULL COMMENT '用户ID',
    `question`      text            NOT NULL COMMENT '问题',
    `answer`        text            NOT NULL COMMENT '答案',
    `create_time`   datetime        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     tinyint(1)      DEFAULT '0' COMMENT '是否删除？',
    PRIMARY KEY (`id`),
    INDEX `idx_room_id` (`room_id`),
    INDEX `idx_user_id` (`user_id`)
) COMMENT='聊天记录表' collate=utf8mb4_unicode_ci;

CREATE TABLE if not exists `app_vector` (
    `id`            BIGINT                              NOT NULL COMMENT 'id',
    `name`          VARCHAR(256)                        NOT NULL UNIQUE COMMENT '向量名称',
    `embedding`     VECTOR(1536)                        NOT NULL UNIQUE COMMENT '向量数据',
    `app_code`      tinyint                             NOT NULL COMMENT '归属应用',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL COMMENT '创建时间',
    `update_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     tinyint  DEFAULT '0'                NOT NULL COMMENT '是否删除？',
    PRIMARY KEY (`id`),
    INDEX `idx_app_code`(`app_code`)
) COMMENT='向量数据表' collate=utf8mb4_unicode_ci;