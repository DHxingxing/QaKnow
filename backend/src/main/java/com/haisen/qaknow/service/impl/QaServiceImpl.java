package com.haisen.qaknow.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haisen.qaknow.client.LlmClient;
import com.haisen.qaknow.domain.KbChunk;
import com.haisen.qaknow.domain.KbDocument;
import com.haisen.qaknow.domain.KbQaRecord;
import com.haisen.qaknow.dto.AskResponse;
import com.haisen.qaknow.dto.CitationDto;
import com.haisen.qaknow.rag.PromptTemplates;
import com.haisen.qaknow.rag.VectorStoreService;
import com.haisen.qaknow.repository.KbDocumentRepository;
import com.haisen.qaknow.repository.KbQaRecordRepository;
import com.haisen.qaknow.service.QaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QaServiceImpl implements QaService {
    private final VectorStoreService vectorStoreService;
    private final LlmClient llmClient;
    private final KbDocumentRepository documentRepository;
    private final KbQaRecordRepository qaRecordRepository;
    private final ObjectMapper objectMapper;

    public QaServiceImpl(VectorStoreService vectorStoreService, LlmClient llmClient,
                         KbDocumentRepository documentRepository, KbQaRecordRepository qaRecordRepository,
                         ObjectMapper objectMapper) {
        this.vectorStoreService = vectorStoreService;
        this.llmClient = llmClient;
        this.documentRepository = documentRepository;
        this.qaRecordRepository = qaRecordRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public AskResponse ask(String question) {
        List<KbChunk> chunks = vectorStoreService.search(question, 4);
        Map<Long, String> docNameMap = documentRepository.findAllById(chunks.stream().map(KbChunk::getDocumentId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(KbDocument::getId, KbDocument::getName));
        String context = chunks.isEmpty() ? "" : buildContext(chunks, docNameMap);
        String answer = chunks.isEmpty()
                ? "根据当前知识库内容无法确认"
                : llmClient.complete(PromptTemplates.qaPrompt(question, context));
        List<CitationDto> citations = chunks.stream().map(c -> CitationDto.builder()
                .documentName(docNameMap.getOrDefault(c.getDocumentId(), "未知文档"))
                .chapterTitle(c.getChapterTitle())
                .chunkOrder(c.getChunkOrder())
                .snippet(c.getChunkText().substring(0, Math.min(120, c.getChunkText().length())))
                .build()).toList();
        saveRecord(question, answer, citations);
        return AskResponse.builder().answer(answer).citations(citations).build();
    }

    private String buildContext(List<KbChunk> chunks, Map<Long, String> docNameMap) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (KbChunk c : chunks) {
            sb.append("[").append(i++).append("] ")
                    .append(docNameMap.getOrDefault(c.getDocumentId(), "未知文档"))
                    .append("-").append(c.getChapterTitle())
                    .append("-chunk").append(c.getChunkOrder()).append("\n")
                    .append(c.getChunkText()).append("\n\n");
        }
        return sb.toString();
    }

    private void saveRecord(String question, String answer, List<CitationDto> citations) {
        try {
            KbQaRecord record = new KbQaRecord();
            record.setQuestion(question);
            record.setAnswer(answer);
            record.setCitationsJson(objectMapper.writeValueAsString(citations));
            record.setCreatedAt(LocalDateTime.now());
            qaRecordRepository.save(record);
        } catch (Exception e) {
            throw new RuntimeException("保存问答历史失败", e);
        }
    }

    @Override
    public List<KbQaRecord> history() {
        return qaRecordRepository.findAllByOrderByCreatedAtDesc();
    }
}
