package com.haisen.qaknow.embedding;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleHashEmbeddingService implements EmbeddingService {
    private static final int DIM = 64;

    @Override
    public List<Double> embed(String text) {
        List<Double> vector = new ArrayList<>();
        for (int i = 0; i < DIM; i++) {
            vector.add(0.0);
        }
        for (String token : text.toLowerCase().split("\\W+")) {
            if (!token.isBlank()) {
                int idx = Math.abs(token.hashCode()) % DIM;
                vector.set(idx, vector.get(idx) + 1.0);
            }
        }
        return vector;
    }
}
