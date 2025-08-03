create table love_pg_vector_store
(
	id	uuid default uuid_generate_v4() not null
		primary key,
	content text,
	metadata json,
	embedding vector(1536)
);