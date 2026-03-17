package com.haisen.qaknow.parser;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class TextDocumentParser implements DocumentParser {
    @Override
    public boolean supports(String type) {
        return "txt".equalsIgnoreCase(type) || "md".equalsIgnoreCase(type);
    }

    @Override
    public ParsedDocument parse(File file) throws IOException {
        return new ParsedDocument(Files.readString(file.toPath()), 1);
    }
}
