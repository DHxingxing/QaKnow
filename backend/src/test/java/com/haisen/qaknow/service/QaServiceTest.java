package com.haisen.qaknow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haisen.qaknow.client.LlmClient;
import com.haisen.qaknow.domain.KbChunk;
import com.haisen.qaknow.domain.KbDocument;
import com.haisen.qaknow.dto.AskResponse;
import com.haisen.qaknow.rag.VectorStoreService;
import com.haisen.qaknow.repository.KbDocumentRepository;
import com.haisen.qaknow.repository.KbQaRecordRepository;
import com.haisen.qaknow.service.impl.QaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class QaServiceTest {
    @Test
    void shouldReturnAnswerAndCitations() {
        VectorStoreService vectorStoreService = Mockito.mock(VectorStoreService.class);
        LlmClient llmClient = prompt -> "这是测试回答";
        KbDocumentRepository documentRepository = Mockito.mock(KbDocumentRepository.class);
        KbQaRecordRepository qaRecordRepository = Mockito.mock(KbQaRecordRepository.class);
        QaServiceImpl service = new QaServiceImpl(vectorStoreService, llmClient, documentRepository, qaRecordRepository, new ObjectMapper());

        KbChunk chunk = new KbChunk();
        chunk.setId(1L);chunk.setDocumentId(10L);chunk.setChapterTitle("Java");chunk.setChunkOrder(1);chunk.setChunkText("Java 是一门面向对象语言");
        KbDocument doc = new KbDocument(); doc.setId(10L); doc.setName("java.md");
        Mockito.when(vectorStoreService.search("什么是Java", 4)).thenReturn(List.of(chunk));
        Mockito.when(documentRepository.findAllById(Mockito.anyCollection())).thenReturn(List.of(doc));

        AskResponse res = service.ask("什么是Java");
        Assertions.assertTrue(res.getAnswer().contains("测试"));
        Assertions.assertEquals(1, res.getCitations().size());
    }
}
