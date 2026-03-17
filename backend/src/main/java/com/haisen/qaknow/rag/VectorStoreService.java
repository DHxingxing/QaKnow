package com.haisen.qaknow.rag;

import com.haisen.qaknow.domain.KbChunk;

import java.util.List;

public interface VectorStoreService {
    void saveChunkVector(Long chunkId, List<Double> vector);
    List<KbChunk> search(String query, int topK);
}
