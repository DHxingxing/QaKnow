package com.haisen.qaknow.parser;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TitleAwareChunkingStrategy implements ChunkingStrategy {
    private static final int MAX_LEN = 500;

    @Override
    public List<ChunkResult> split(String content) {
        List<ChunkResult> results = new ArrayList<>();
        String[] lines = content.split("\\R");
        String currentTitle = "未命名章节";
        StringBuilder sectionText = new StringBuilder();
        int globalPos = 0;
        int chunkOrder = 1;

        for (String line : lines) {
            if (line.strip().startsWith("#")) {
                chunkOrder = flushSection(results, currentTitle, sectionText.toString(), globalPos - sectionText.length(), chunkOrder);
                sectionText.setLength(0);
                currentTitle = line.replaceFirst("^#+", "").trim();
            } else {
                sectionText.append(line).append("\n");
            }
            globalPos += line.length() + 1;
        }
        flushSection(results, currentTitle, sectionText.toString(), globalPos - sectionText.length(), chunkOrder);
        return results;
    }

    private int flushSection(List<ChunkResult> results, String title, String text, int startOffset, int orderStart) {
        if (text.isBlank()) {
            return orderStart;
        }
        int order = orderStart;
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + MAX_LEN, text.length());
            String chunk = text.substring(start, end).trim();
            if (!chunk.isEmpty()) {
                results.add(ChunkResult.builder()
                        .chapterTitle(title)
                        .text(chunk)
                        .order(order++)
                        .start(startOffset + start)
                        .end(startOffset + end)
                        .build());
            }
            start = end;
        }
        return order;
    }
}
