package com.haisen.qaknow.parser;

import java.io.File;
import java.io.IOException;

public interface DocumentParser {
    boolean supports(String type);
    ParsedDocument parse(File file) throws IOException;
}
