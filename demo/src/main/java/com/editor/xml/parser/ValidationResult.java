package com.editor.xml.parser;

import java.util.List;

public class ValidationResult {

    private boolean isValid;
    private int errorCount;
    private List<Integer> errorLines;

    // ---- Getters ----
    public boolean isValid() {
        return isValid;
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
}
