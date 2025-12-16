package com.editor.xml.parser;

class XmlToken {

    public enum Type {
        OPENING_TAG,
        CLOSING_TAG,
        SELF_CLOSING_TAG,
        TEXT
    }

    private Type type;
    private String tagName;
    private String textContent;
    private int line;
    private int column;

    public XmlToken(Type type, String tagName, String textContent, int line, int column) {
        this.type = type;
        this.tagName = tagName;
        this.textContent = textContent;
        this.line = line;
        this.column = column;
    }

    public Type getType() { return type; }
    public String getTagName() { return tagName; }
    public String getTextContent() { return textContent; }
    public int getLine() { return line; }
    public int getColumn() { return column; }

    public boolean isText() { return type == Type.TEXT; }
    public boolean isOpeningTag() { return type == Type.OPENING_TAG; }
    public boolean isClosingTag() { return type == Type.CLOSING_TAG; }
    public boolean isSelfClosingTag() { return type == Type.SELF_CLOSING_TAG; }

    public String toString() {
        switch (type) {
            case TEXT:
                return "TEXT(\"" + textContent + "\") at line " + line;
            case OPENING_TAG:
                return "OPEN <" + tagName + " at line " + line;
            case CLOSING_TAG:
                return "CLOSE </" + tagName + "> at line " + line;
            case SELF_CLOSING_TAG:
                return "SELF <" + tagName;
            default:
                return "UNKNOWN TOKEN at line " + line;
        }
    }
}
