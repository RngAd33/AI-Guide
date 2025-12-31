-- 安装 pgvector 扩展
CREATE EXTENSION IF NOT EXISTS vector;

-- 创建向量存储表 1
CREATE TABLE IF NOT EXISTS love_pg_vector_store (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT NOT NULL,
    embedding vector(1536) NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
-- 为向量列创建 HNSW 索引（使用余弦距离）
CREATE INDEX IF NOT EXISTS idx_love_pg_vector_embedding_hnsw
    ON love_pg_vector_store
    USING hnsw (embedding vector_cosine_ops);
-- 为 metadata 创建 GIN 索引以便快速查询
CREATE INDEX IF NOT EXISTS idx_love_pg_vector_metadata
    ON love_pg_vector_store
    USING gin (metadata);
-- 为创建时间创建索引
CREATE INDEX IF NOT EXISTS idx_love_pg_vector_created_at
    ON love_pg_vector_store
    USING btree (created_at);

-- 创建向量存储表 2
CREATE TABLE IF NOT EXISTS psychology_pg_vector_store (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     content TEXT NOT NULL,
     embedding vector(1536) NOT NULL,
     metadata JSONB,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- 为向量列创建 HNSW 索引（使用余弦距离）
CREATE INDEX IF NOT EXISTS idx_love_pg_vector_embedding_hnsw
    ON psychology_pg_vector_store
        USING hnsw (embedding vector_cosine_ops);
-- 为 metadata 创建 GIN 索引以便快速查询
CREATE INDEX IF NOT EXISTS idx_love_pg_vector_metadata
    ON psychology_pg_vector_store
        USING gin (metadata);
-- 为创建时间创建索引
CREATE INDEX IF NOT EXISTS idx_love_pg_vector_created_at
    ON psychology_pg_vector_store
        USING btree (created_at);
