package com.haisen.qaknow.parser;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PdfDocumentParser implements DocumentParser {
    @Override
    public boolean supports(String type) {
        return "pdf".equalsIgnoreCase(type);
    }

    @Override
    public ParsedDocument parse(File file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return new ParsedDocument(stripper.getText(document), document.getNumberOfPages());
        }
    }
}
