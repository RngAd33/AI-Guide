-- 为每个AI应用创建向量表（需要 MySQL 9.x 版本）
USE `ai_guide_db`;

CREATE TABLE app_vectors (
    `id`            BIGINT                              NOT NULL COMMENT 'id',
    `name`          VARCHAR(256)                        NOT NULL UNIQUE COMMENT '向量名称',
    `embedding`     VECTOR(1536)                        NOT NULL UNIQUE COMMENT '向量数据',
    `app_code`      tinyint                             NOT NULL COMMENT '归属应用',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL COMMENT '创建时间',
    `update_time`   datetime DEFAULT CURRENT_TIMESTAMP  NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`     tinyint DEFAULT '0'                 NOT NULL COMMENT '是否删除？',
    PRIMARY KEY (`id`),
    index `idx_app_code` (`app_code`) USING BTREE
) COMMENT='向量数据表' collate=utf8mb4_unicode_ci;
