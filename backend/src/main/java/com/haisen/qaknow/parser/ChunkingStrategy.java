package com.haisen.qaknow.parser;

import java.util.List;

public interface ChunkingStrategy {
    List<ChunkResult> split(String content);
}
