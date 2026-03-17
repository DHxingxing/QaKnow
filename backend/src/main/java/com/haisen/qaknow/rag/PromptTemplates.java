package com.haisen.qaknow.rag;

public class PromptTemplates {
    private PromptTemplates() {
    }

    public static String qaPrompt(String question, String context) {
        return "你是知识库问答助手。必须严格基于上下文回答，禁止胡编。\n" +
                "如果上下文不足，请回答：根据当前知识库内容无法确认。\n" +
                "请在回答中保留引用标记如[1][2]。\n\n" +
                "问题：" + question + "\n\n上下文：\n" + context;
    }

    public static String summaryPrompt(String content) {
        return "请基于以下文档内容生成章节摘要，简洁且准确。若信息不足，明确指出。\n" + content;
    }

    public static String interviewPrompt(String content) {
        return "请根据以下内容生成5条面试题及参考答案。若内容不足请说明根据当前知识库内容无法确认。输出格式：Q:... A:...\n" + content;
    }
}
