package com.haisen.qaknow.parser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChunkResult {
    private String chapterTitle;
    private String text;
    private Integer order;
    private Integer start;
    private Integer end;
}
