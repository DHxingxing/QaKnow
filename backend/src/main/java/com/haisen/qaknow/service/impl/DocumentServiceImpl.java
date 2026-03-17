package com.haisen.qaknow.service.impl;

import com.haisen.qaknow.domain.KbChunk;
import com.haisen.qaknow.domain.KbDocument;
import com.haisen.qaknow.embedding.EmbeddingService;
import com.haisen.qaknow.parser.*;
import com.haisen.qaknow.rag.VectorStoreService;
import com.haisen.qaknow.repository.KbChunkRepository;
import com.haisen.qaknow.repository.KbChunkVectorMappingRepository;
import com.haisen.qaknow.repository.KbDocumentRepository;
import com.haisen.qaknow.service.DocumentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final KbDocumentRepository documentRepository;
    private final KbChunkRepository chunkRepository;
    private final KbChunkVectorMappingRepository mappingRepository;
    private final DocumentParserManager parserManager;
    private final ChunkingStrategy chunkingStrategy;
    private final EmbeddingService embeddingService;
    private final VectorStoreService vectorStoreService;

    @Value("${app.storage.path:./data/uploads}")
    private String storagePath;

    public DocumentServiceImpl(KbDocumentRepository documentRepository, KbChunkRepository chunkRepository,
                               KbChunkVectorMappingRepository mappingRepository, DocumentParserManager parserManager,
                               ChunkingStrategy chunkingStrategy, EmbeddingService embeddingService,
                               VectorStoreService vectorStoreService) {
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.mappingRepository = mappingRepository;
        this.parserManager = parserManager;
        this.chunkingStrategy = chunkingStrategy;
        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
    }

    @Override
    public KbDocument upload(MultipartFile file) throws IOException {
        String original = file.getOriginalFilename();
        String ext = original == null || !original.contains(".") ? "txt" : original.substring(original.lastIndexOf('.') + 1);
        if (!List.of("txt", "md", "pdf").contains(ext.toLowerCase())) {
            throw new IllegalArgumentException("仅支持 txt/md/pdf");
        }
        Files.createDirectories(Path.of(storagePath));
        Path target = Path.of(storagePath, System.currentTimeMillis() + "-" + original);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        KbDocument doc = new KbDocument();
        doc.setName(original);
        doc.setType(ext.toLowerCase());
        doc.setSize(file.getSize());
        doc.setStoragePath(target.toString());
        doc.setParseStatus("UPLOADED");
        doc.setCreatedAt(LocalDateTime.now());
        return documentRepository.save(doc);
    }

    @Override
    public List<KbDocument> list() {
        return documentRepository.findAll();
    }

    @Override
    public KbDocument detail(Long id) {
        return documentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("文档不存在"));
    }

    @Override
    @Transactional
    public int parse(Long id) throws IOException {
        KbDocument doc = detail(id);
        ParsedDocument parsed = parserManager.parse(doc.getType(), Path.of(doc.getStoragePath()).toFile());
        List<ChunkResult> chunks = chunkingStrategy.split(parsed.getContent());
        chunkRepository.deleteAll(chunkRepository.findByDocumentIdOrderByChunkOrderAsc(id));
        for (ChunkResult item : chunks) {
            KbChunk chunk = new KbChunk();
            chunk.setDocumentId(id);
            chunk.setChapterTitle(item.getChapterTitle());
            chunk.setChunkText(item.getText());
            chunk.setChunkOrder(item.getOrder());
            chunk.setStartPos(item.getStart());
            chunk.setEndPos(item.getEnd());
            chunk.setPage(parsed.getPage());
            chunkRepository.save(chunk);
        }
        doc.setParseStatus("PARSED");
        documentRepository.save(doc);
        return chunks.size();
    }

    @Override
    @Transactional
    public int index(Long id) {
        List<KbChunk> chunks = chunkRepository.findByDocumentIdOrderByChunkOrderAsc(id);
        mappingRepository.deleteByChunkIdIn(chunks.stream().map(KbChunk::getId).toList());
        for (KbChunk chunk : chunks) {
            vectorStoreService.saveChunkVector(chunk.getId(), embeddingService.embed(chunk.getChunkText()));
        }
        KbDocument doc = detail(id);
        doc.setParseStatus("INDEXED");
        documentRepository.save(doc);
        return chunks.size();
    }
}
