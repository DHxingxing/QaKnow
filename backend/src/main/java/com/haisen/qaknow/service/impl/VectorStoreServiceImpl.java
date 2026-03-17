package com.haisen.qaknow.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haisen.qaknow.domain.KbChunk;
import com.haisen.qaknow.domain.KbChunkVectorMapping;
import com.haisen.qaknow.embedding.EmbeddingService;
import com.haisen.qaknow.rag.VectorStoreService;
import com.haisen.qaknow.repository.KbChunkRepository;
import com.haisen.qaknow.repository.KbChunkVectorMappingRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VectorStoreServiceImpl implements VectorStoreService {
    private final ObjectMapper objectMapper;
    private final KbChunkVectorMappingRepository mappingRepository;
    private final KbChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;

    public VectorStoreServiceImpl(ObjectMapper objectMapper, KbChunkVectorMappingRepository mappingRepository,
                                  KbChunkRepository chunkRepository, EmbeddingService embeddingService) {
        this.objectMapper = objectMapper;
        this.mappingRepository = mappingRepository;
        this.chunkRepository = chunkRepository;
        this.embeddingService = embeddingService;
    }

    @Override
    public void saveChunkVector(Long chunkId, List<Double> vector) {
        try {
            KbChunkVectorMapping m = new KbChunkVectorMapping();
            m.setChunkId(chunkId);
            m.setVectorDim(vector.size());
            m.setVectorData(objectMapper.writeValueAsString(vector));
            mappingRepository.save(m);
        } catch (Exception e) {
            throw new RuntimeException("保存向量失败", e);
        }
    }

    @Override
    public List<KbChunk> search(String query, int topK) {
        List<Double> queryVector = embeddingService.embed(query);
        List<KbChunkVectorMapping> allMappings = mappingRepository.findAll();
        Map<Long, Double> scoreMap = new HashMap<>();
        for (KbChunkVectorMapping mapping : allMappings) {
            try {
                List<Double> chunkVector = objectMapper.readValue(mapping.getVectorData(), new TypeReference<>() {});
                scoreMap.put(mapping.getChunkId(), cosine(queryVector, chunkVector));
            } catch (Exception ignored) {
            }
        }
        List<Long> topIds = scoreMap.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .map(Map.Entry::getKey)
                .toList();
        Map<Long, KbChunk> chunkMap = chunkRepository.findAllById(topIds).stream().collect(Collectors.toMap(KbChunk::getId, c -> c));
        return topIds.stream().map(chunkMap::get).filter(Objects::nonNull).toList();
    }

    private double cosine(List<Double> a, List<Double> b) {
        double dot = 0, na = 0, nb = 0;
        int size = Math.min(a.size(), b.size());
        for (int i = 0; i < size; i++) {
            dot += a.get(i) * b.get(i);
            na += a.get(i) * a.get(i);
            nb += b.get(i) * b.get(i);
        }
        if (na == 0 || nb == 0) {
            return 0;
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }
}
