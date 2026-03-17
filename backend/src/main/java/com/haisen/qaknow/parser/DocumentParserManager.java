package com.haisen.qaknow.parser;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class DocumentParserManager {
    private final List<DocumentParser> parsers;

    public DocumentParserManager(List<DocumentParser> parsers) {
        this.parsers = parsers;
    }

    public ParsedDocument parse(String type, File file) throws IOException {
        return parsers.stream().filter(p -> p.supports(type)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的文件类型: " + type))
                .parse(file);
    }
}
