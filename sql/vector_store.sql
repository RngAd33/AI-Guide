-- 向量表创建交给框架处理
USE `ai_guide_db`;

CREATE TABLE if not exists `love_app_vector_store` (
    `id`            CHAR(36)                            NOT NULL COMMENT 'id',
    `content`       TEXT                                NOT NULL COMMENT '内容',
    `metadata`      JSON                                DEFAULT NULL COMMENT '元数据',
    `embedding`     BLOB                                 NOT NULL COMMENT '向量数据',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL COMMENT '创建时间',
    `update_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     tinyint  DEFAULT '0'                NOT NULL COMMENT '是否删除？',
    PRIMARY KEY (`id`)
) COMMENT='恋爱大师向量数据表' collate=utf8mb4_unicode_ci;

CREATE TABLE if not exists `psychology_app_vector_store` (
    `id`            CHAR(36)                            NOT NULL COMMENT 'id',
    `content`       TEXT                                NOT NULL COMMENT '内容',
    `metadata`      JSON                                DEFAULT NULL COMMENT '元数据',
    `embedding`     BLOB                                 NOT NULL COMMENT '向量数据',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL COMMENT '创建时间',
    `update_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     tinyint  DEFAULT '0'                NOT NULL COMMENT '是否删除？',
    PRIMARY KEY (`id`)
) COMMENT='小姐姐心理疏导向量数据表' collate=utf8mb4_unicode_ci;

CREATE TABLE if not exists `game_app_vector_store` (
    `id`            CHAR(36)                            NOT NULL COMMENT 'id',
    `content`       TEXT                                NOT NULL COMMENT '内容',
    `metadata`      JSON                                DEFAULT NULL COMMENT '元数据',
    `embedding`     BLOB                                NOT NULL COMMENT '向量数据',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL COMMENT '创建时间',
    `update_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     tinyint  DEFAULT '0'                NOT NULL COMMENT '是否删除？',
    PRIMARY KEY (`id`)
) COMMENT='游戏王向量数据表' collate=utf8mb4_unicode_ci;
