package com.haisen.qaknow.service.impl;

import com.haisen.qaknow.client.LlmClient;
import com.haisen.qaknow.domain.KbChunk;
import com.haisen.qaknow.domain.KbInterviewQuestion;
import com.haisen.qaknow.domain.KbSummary;
import com.haisen.qaknow.rag.PromptTemplates;
import com.haisen.qaknow.repository.KbChunkRepository;
import com.haisen.qaknow.repository.KbInterviewQuestionRepository;
import com.haisen.qaknow.repository.KbSummaryRepository;
import com.haisen.qaknow.service.KnowledgeExtraService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class KnowledgeExtraServiceImpl implements KnowledgeExtraService {
    private final KbChunkRepository chunkRepository;
    private final KbSummaryRepository summaryRepository;
    private final KbInterviewQuestionRepository interviewRepository;
    private final LlmClient llmClient;

    public KnowledgeExtraServiceImpl(KbChunkRepository chunkRepository, KbSummaryRepository summaryRepository,
                                     KbInterviewQuestionRepository interviewRepository, LlmClient llmClient) {
        this.chunkRepository = chunkRepository;
        this.summaryRepository = summaryRepository;
        this.interviewRepository = interviewRepository;
        this.llmClient = llmClient;
    }

    @Override
    public KbSummary generateSummary(Long documentId) {
        String content = buildDocContent(documentId);
        String result = llmClient.complete(PromptTemplates.summaryPrompt(content));
        KbSummary summary = new KbSummary();
        summary.setDocumentId(documentId);
        summary.setSummaryText(result);
        summary.setCreatedAt(LocalDateTime.now());
        return summaryRepository.save(summary);
    }

    @Override
    public List<KbInterviewQuestion> generateInterview(Long documentId) {
        String content = buildDocContent(documentId);
        String result = llmClient.complete(PromptTemplates.interviewPrompt(content));
        String[] lines = result.split("\\R");
        List<KbInterviewQuestion> saved = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            KbInterviewQuestion q = new KbInterviewQuestion();
            q.setDocumentId(documentId);
            q.setQuestionText(i < lines.length ? "面试题" + (i + 1) + ": " + lines[i] : "面试题" + (i + 1));
            q.setAnswerText("参考答案：请结合知识库上下文展开回答。");
            q.setCreatedAt(LocalDateTime.now());
            saved.add(interviewRepository.save(q));
        }
        return saved;
    }

    private String buildDocContent(Long documentId) {
        List<KbChunk> chunks = chunkRepository.findByDocumentIdOrderByChunkOrderAsc(documentId);
        return chunks.stream().limit(8).map(KbChunk::getChunkText).reduce("", (a, b) -> a + "\n" + b);
    }
}
