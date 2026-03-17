CREATE DATABASE IF NOT EXISTS qaknow DEFAULT CHARACTER SET utf8mb4;
USE qaknow;

CREATE TABLE IF NOT EXISTS kb_document (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(20) NOT NULL,
  size BIGINT,
  storage_path VARCHAR(500),
  parse_status VARCHAR(30),
  created_at DATETIME
);

CREATE TABLE IF NOT EXISTS kb_chunk (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  document_id BIGINT NOT NULL,
  chapter_title VARCHAR(255),
  chunk_text TEXT,
  chunk_order INT,
  start_pos INT,
  end_pos INT,
  page INT
);

CREATE TABLE IF NOT EXISTS kb_chunk_vector_mapping (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  chunk_id BIGINT NOT NULL,
  vector_data TEXT,
  vector_dim INT
);

CREATE TABLE IF NOT EXISTS kb_qa_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  question VARCHAR(500),
  answer TEXT,
  citations_json TEXT,
  created_at DATETIME
);

CREATE TABLE IF NOT EXISTS kb_summary (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  document_id BIGINT NOT NULL,
  summary_text TEXT,
  created_at DATETIME
);

CREATE TABLE IF NOT EXISTS kb_interview_question (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  document_id BIGINT NOT NULL,
  question_text TEXT,
  answer_text TEXT,
  created_at DATETIME
);
