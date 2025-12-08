package com.editor.xml.parser;

import java.util.List;

public class ValidationResult {

    private boolean isValid;
    private int errorCount;
    private List<Integer> errorLines;
    private List<String> errorMessages;

    // ---- Getters ----
    public boolean isValid() {
        return isValid;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }   
    public int getErrorCount() {
        return errorCount;
    }

    public List<Integer> getErrorLines() {
        return errorLines;
    }

    // ---- Setters (used by XmlValidator) ----
    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public void setErrorCount(int count) {
        this.errorCount = count;
    }

    public void setErrorLines(List<Integer> lines) {
        this.errorLines = lines;
    }
    public void setErrorMessages(List<String> messages) {
        this.errorMessages = messages;
    }
}
