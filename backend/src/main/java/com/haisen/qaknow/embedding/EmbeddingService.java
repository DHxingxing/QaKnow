package com.haisen.qaknow.embedding;

import java.util.List;

public interface EmbeddingService {
    List<Double> embed(String text);
}
