package com.editor.xml.parser;

import java.util.List;

public class XmlValidator {


    public ValidationResult validate(String xml) {
        ValidationResult result = new ValidationResult();
        // Dummy implementation for demonstration purposes
        if (xml == null || xml.isEmpty()) {
            result.setValid(false);
            result.setErrorCount(1);
            result.setErrorLines(List.of(1));
        } else {
            result.setValid(true);
            result.setErrorCount(0);
            result.setErrorLines(List.of());
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