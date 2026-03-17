package com.haisen.qaknow.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TitleAwareChunkingStrategyTest {
    @Test
    void shouldSplitByTitleAndLength() {
        TitleAwareChunkingStrategy strategy = new TitleAwareChunkingStrategy();
        String content = "# 第一章\nJava 基础内容\n# 第二章\nSpring Boot 开发";
        List<ChunkResult> chunks = strategy.split(content);
        Assertions.assertEquals(2, chunks.size());
        Assertions.assertEquals("第一章", chunks.get(0).getChapterTitle());
    }
}
