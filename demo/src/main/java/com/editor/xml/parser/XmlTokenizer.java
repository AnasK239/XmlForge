package com.editor.xml.parser;


public class XmlTokenizer {

    private String input;  //// the entire XML input as a string
    private int index;     // index to navigate the input string
    private int line;      // used to indicate the current line number (starts at 1)
    private int column;    // used to indicate the current column number (starts at 1)
    private int length;    // total length of the input string

    public XmlTokenizer(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input XML cannot be null");
        }
        this.column = 1;
        this.index = 0;
        this.input = input;
        this.length = input.length();
        this.line = 1;
    }

    public boolean hasNext() {
        skipWhitespaces();
        return index < length;
    }

    public XmlToken next() {
        skipWhitespaces();
        if (index >= length) return null;

        if (input.charAt(index) == '<') {
            return readTag();
        } else {
            return readText();
        }
    }

    // Helper functions
    private void skipWhitespaces() {
        while (index < length && Character.isWhitespace(input.charAt(index))) {
            char c = input.charAt(index);
            if (c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
            index++;
        }
    }

    private XmlToken readTag() {

        StringBuilder text = new StringBuilder();
        int startLine = line;    // store the line to pass it to token class
        int startColumn = column; // store the column to pass it to token class

        index++;   // eat '<'
        column++;

        if (index >= length) { // prevent overflow like "<" or "<name"
            return new XmlToken(XmlToken.Type.ERROR, null,
                    "Tag starts but never closes", startLine, startColumn);
        }

        while (index < length && input.charAt(index) != '>') {
            char c = input.charAt(index);

            if (c == '<') {
                // Consume character to avoid infinite loop
                index++;
                column++;

                return new XmlToken(
                        XmlToken.Type.ERROR,
                        null,
                        "Unexpected '<' inside tag — missing '>'",
                        startLine,
                        startColumn
                );
            }

            text.append(c);
            if (c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
            index++;
        }

        if (index >= length) {
            return new XmlToken(XmlToken.Type.ERROR, null,
                    "Missing '>' for tag starting at line " + startLine + ", column " + startColumn,
                    startLine, startColumn);
        }

        index++;   // eat '>'
        column++;

        String content = text.toString().trim();

        if (content.startsWith("/")) {

            String result = content.substring(1);
            String tagName = result.trim();

            if (result.trim().isEmpty()) {
                return new XmlToken(XmlToken.Type.ERROR, null,
                        "Empty tag name in closing tag", startLine, startColumn);
            }

            if (!isValidTagName(tagName)) {
                String fixed = XmlValidator.fixTagName(tagName);
                System.out.println("WARNING at line " + startLine +
                        ": invalid tag name '" + tagName + "' corrected to '" + fixed + "'");
                tagName = fixed; // use fixed
            }

            return new XmlToken(XmlToken.Type.CLOSING_TAG, tagName, null, startLine, startColumn);
        }

        else if (content.endsWith("/")) {
            String result = content.substring(0, content.length() - 1);
            String tagName = result.trim();

            if (result.trim().isEmpty()) {
                return new XmlToken(XmlToken.Type.ERROR, null,
                        "Empty tag name in self-closing tag", startLine, startColumn);
            }

            if (!isValidTagName(tagName)) {
                String fixed = XmlValidator.fixTagName(tagName);
                System.out.println("WARNING at line " + startLine +
                        ": invalid tag name '" + tagName + "' corrected to '" + fixed + "'");
                tagName = fixed;
            }

            return new XmlToken(XmlToken.Type.SELF_CLOSING_TAG, tagName, null, startLine, startColumn);
        }

        else {
            String result = content;
            String tagName = result.trim();

            if (result.trim().isEmpty()) {
                return new XmlToken(XmlToken.Type.ERROR, null,
                        "Empty tag name in opening tag", startLine, startColumn);
            }

            if (!isValidTagName(tagName)) {
                String fixed = XmlValidator.fixTagName(tagName);
                System.out.println("WARNING at line " + startLine +
                        ": invalid tag name '" + tagName + "' corrected to '" + fixed + "'");
                tagName = fixed;
            }

            return new XmlToken(XmlToken.Type.OPENING_TAG, tagName, null, startLine, startColumn);
        }
    }

    private XmlToken readText() {

        StringBuilder text = new StringBuilder();
        int startLine = line;
        int startColumn = column;

        while (index < length && input.charAt(index) != '<') {

            char c = input.charAt(index);

            if (c == '>') {
                String word = getBackwardWord(index);

                index++;
                column++;

                if (isValidTagName(word) && tagExistsInDocument(word)) {
                    return new XmlToken(XmlToken.Type.ERROR, null,
                            "Missing '<' before tag name: " + word,
                            startLine, startColumn);
                }

                return new XmlToken(XmlToken.Type.ERROR, null,
                        "Unexpected '>' outside tag", line, column);
            }

            text.append(c);

            if (c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }

            index++;
        }

        String content = text.toString().trim();

        if (!content.isEmpty()) {
            return new XmlToken(XmlToken.Type.TEXT, null, content, startLine, startColumn);
        }

        return next(); // skip whitespace & get next token
    }

    private boolean isValidTagName(String name) {

        if (name == null) return false;

        name = name.trim();

        if (name.contains(" ")) return false;

        if (name.isEmpty()) return false;

        char first = name.charAt(0);
        if (!(Character.isLetter(first) || first == '_')) return false;

        for (char c : name.toCharArray()) {
            if (!(Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == ':')) {
                return false;
            }
        }

        return true;
    }

    private boolean tagExistsInDocument(String name) {

        if (name == null || name.isEmpty()) return false;

        String n = name.trim();

        return input.contains("<" + n + ">") ||
                input.contains("<" + n + " ") ||
                input.contains("<" + n + "/") ||
                input.contains("</" + n + ">");
    }

    private String getBackwardWord(int idx) {

        StringBuilder sb = new StringBuilder();
        int i = idx - 1;

        while (i >= 0 &&
                !Character.isWhitespace(input.charAt(i)) &&
                input.charAt(i) != '<' &&
                input.charAt(i) != '>') {

            sb.append(input.charAt(i));
            i--;
        }

        return sb.reverse().toString();
    }
}
