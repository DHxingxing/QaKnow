package com.haisen.qaknow.parser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParsedDocument {
    private String content;
    private Integer page;
}
