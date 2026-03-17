# AI 知识库问答平台 MVP（QaKnow）

## 1. 项目简介
这是一个可本地运行的“AI 知识库问答平台 MVP”，覆盖主链路：
上传文档 -> 解析切片 -> 建立索引 -> 检索召回 -> 生成回答 -> 返回引用来源。

## 2. 项目结构
- `backend/`：Java 21 + Spring Boot 3 后端
- `frontend/`：Vue3 + Vite + Element Plus 前端
- `scripts/init.sql`：MySQL 初始化脚本
- `docs/`：补充文档目录
- `docker-compose.yml`：一键拉起 mysql/redis/backend/frontend

## 3. 功能清单（MVP）
- 文档上传：支持 `md/txt/pdf`
- 文档解析：PDFBox + 文本解析
- 切片策略：标题优先 + 长度阈值（500字符）
- 向量化：`EmbeddingService` 抽象 + 本地 hash 向量实现
- 检索：余弦相似度召回 + 简单重排
- RAG 问答：附带引用来源
- 会话历史：保存问答和引用
- 文档摘要：按文档生成摘要并保存
- 面试题生成：生成 5 条问答并保存

## 4. 快速启动
### 4.1 Docker 方式（推荐）
```bash
docker compose up --build
```
访问：
- 前端：http://localhost:5173
- 后端：http://localhost:8080

### 4.2 本地开发
#### 后端
```bash
cd backend
./mvnw spring-boot:run
```
#### 前端
```bash
cd frontend
npm install
npm run dev
```

## 5. 环境变量与配置
后端配置文件：`backend/src/main/resources/application.yml`
关键项：
- `spring.datasource.*`
- `spring.data.redis.*`
- `app.storage.path`
- `app.llm.mock`（默认 true，使用 mock LLM）
- `app.llm.base-url/api-key/model`（可切换 OpenAI 兼容接口）

## 6. 数据库表
- `kb_document`
- `kb_chunk`
- `kb_chunk_vector_mapping`
- `kb_qa_record`
- `kb_summary`
- `kb_interview_question`

初始化 SQL：`scripts/init.sql`

## 7. API 示例
统一返回结构：`{code,message,data}`

- `POST /api/documents/upload`（form-data: file）
- `GET /api/documents`
- `GET /api/documents/{id}`
- `POST /api/documents/{id}/parse`
- `POST /api/documents/{id}/index`
- `POST /api/qa/ask`
```json
{"question":"Java 的三大特性是什么？"}
```
- `GET /api/qa/history`
- `POST /api/documents/{id}/summary`
- `POST /api/documents/{id}/interview-questions`

## 8. 当前限制
- 当前向量库为本地关系型映射，已预留 `VectorStoreService`，可替换 pgvector/Milvus
- 默认使用 mock LLM，线上可配置 OpenAI 兼容接口
- 单用户模式，无 RBAC

## 9. 后续优化
- 接入 pgvector 或 Milvus
- 增加异步任务队列（解析/索引）
- 多用户鉴权与权限
- Prompt 调优与多模型路由
