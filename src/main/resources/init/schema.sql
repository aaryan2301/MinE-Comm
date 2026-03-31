CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;

CREATE TABLE IF NOT EXISTS vector_store (
    id uuid PRIMARY KEY,
    content text,
    metadata json,
    embedding halfvec(3072) -- Use halfvec here
);

-- Create HNSW index for fast search
CREATE INDEX IF NOT EXISTS vector_store_embedding_idx 
ON vector_store 
USING HNSW (embedding halfvec_cosine_ops);
