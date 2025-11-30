package com.editor.xml.parser;

import java.util.List;

public class XmlValidator {


    public ValidationResult validate(String xml) {
        ValidationResult result = new ValidationResult();
        // Dummy implementation for demonstration purposes
        if (xml == null || xml.isEmpty()) {
            result.isValid = false;
            result.errorCount = 1;
            result.errorLines = List.of(1);
        } else {
            result.isValid = true;
            result.errorCount = 0;
            result.errorLines = List.of();
        }
        return result;
    }

    public String fix(String xml) {
        // Dummy implementation for demonstration purposes
        if (xml == null || xml.isEmpty()) {
            return "<root></root>";
        }
        return xml;
    }
}

class ValidationResult {
    boolean isValid;
    int errorCount;
    List<Integer> errorLines;
}
