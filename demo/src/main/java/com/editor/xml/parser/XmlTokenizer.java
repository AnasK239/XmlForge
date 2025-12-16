package com.editor.xml.parser;

import java.util.ArrayList;
import java.util.List;

public class XmlTokenizer {
    private List<String> syntaxErrors = new ArrayList<>();
    private List<Integer> syntaxErrorLines = new ArrayList<>();
    private String input;
    private int index;
    private int line;
    private int column;
    private int length;
    private XmlToken bufferedToken = null;


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
        if (bufferedToken != null) return true;
        skipWhitespaces();
        return index < length;
    }

    public XmlToken next() {
        if (bufferedToken != null) {
            XmlToken t = bufferedToken;
            bufferedToken = null;
            return t;
        }

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
        int startLine = line;
        int startColumn = column;

        index++;   // eat '<'
        column++;


        // FIX CASE: Missing '>' in tag before another '<' or EOF
        // This catches: <id , </id , <tag/ , <post
        int scan = index;
        while (scan < length && input.charAt(scan) != '>' && input.charAt(scan) != '<') {
            scan++;
        }


        // CASE A — Found '<' BEFORE we found '>'  → malformed tag
        if (scan < length && input.charAt(scan) == '<') {

            String content = input.substring(index, scan).trim();
            reportError("Missing '>' for tag <" + content + "> ", startLine);
            line++;
            // *** SKIP THE MALFORMED PART SO IT DOES NOT BECOME TEXT ***
            index = scan;
            column = startColumn + (scan - (startColumn - 1));

            // Fix closing tag: </id
            if (content.startsWith("/")) {
                String name = content.substring(1).trim();
                return new XmlToken(XmlToken.Type.CLOSING_TAG, name, null, startLine, startColumn);
            }

            // Fix self-closing: <tag/
            if (content.endsWith("/")) {
                String name = content.substring(0, content.length() - 1).trim();
                return new XmlToken(XmlToken.Type.SELF_CLOSING_TAG, name, null, startLine, startColumn);
            }

            // Fix opening tag: <id
            return new XmlToken(XmlToken.Type.OPENING_TAG, content.trim(), null, startLine, startColumn);
        }


        // CASE B — EOF reached without '>'
        if (scan >= length) {

            String content = input.substring(index).trim();
            reportError("Missing '>' before end of file for tag <" + content + "> ", startLine);

            // *** SKIP THE MALFORMED PART ***
            index = length;
            column = startColumn + (length - (startColumn - 1));

            if (content.startsWith("/")) {
                return new XmlToken(XmlToken.Type.CLOSING_TAG, content.substring(1).trim(), null, startLine, startColumn);
            }
            else if (content.endsWith("/")) {
                return new XmlToken(XmlToken.Type.SELF_CLOSING_TAG, content.substring(0, content.length() - 1).trim(), null, startLine, startColumn);
            }
            else {
                return new XmlToken(XmlToken.Type.OPENING_TAG, content.trim(), null, startLine, startColumn);
            }
        }





        while (index < length && input.charAt(index) != '>') {
            char c = input.charAt(index);

            if (c == '<') {
                // >>>>>>>>>> CHANGED: Store error and fix it
                reportError("Unexpected '<' inside tag — missing '>'", startLine);

                // Fix: pretend previous tag closed, start new tag
                index++; // consume the '<'
                column++;

                // Return the partial tag we collected so far as valid
                String partialContent = text.toString().trim();
                if (!partialContent.isEmpty()) {
                    if (!isValidTagName(partialContent)) {
                        String fixed = XmlValidator.fixTagName(partialContent);
                        reportError("Invalid tag name '" + partialContent + "' fixes to '" + fixed + "'", startLine);
                        partialContent = fixed;
                    }
                    return new XmlToken(XmlToken.Type.OPENING_TAG, partialContent, null, startLine, startColumn);
                }

                // Continue parsing the new tag
                return readTag();
            }

            text.append(c);
            if (c == '\n'&&bufferedToken == null) {
                line++;
                column = 1;
            } else {
                column++;
            }
            index++;
        }

        if (index >= length) {
            // >>>>>>>>>> CHANGED: Store error and auto-fix
            reportError("Missing '>' for tag starting at line " + startLine + ", column " + startColumn, startLine);

            // Auto-fix: pretend '>' exists
            String content = text.toString().trim();


            if (content.startsWith("/")) {
                String tagName = content.substring(1).trim();
                if (!isValidTagName(tagName)) {
                    String fixed = XmlValidator.fixTagName(tagName);
                    reportError("Invalid tag name '" + tagName + "' fixes to '" + fixed + "'", startLine);
                    tagName = fixed;
                }
                return new XmlToken(XmlToken.Type.CLOSING_TAG, tagName, null, startLine, startColumn);
            } else if (content.endsWith("/")) {
                String tagName = content.substring(0, content.length() - 1).trim();
                if (!isValidTagName(tagName)) {
                    String fixed = XmlValidator.fixTagName(tagName);
                    reportError("Invalid tag name '" + tagName + "' fixes to '" + fixed + "'", startLine);
                    tagName = fixed;
                }
                return new XmlToken(XmlToken.Type.SELF_CLOSING_TAG, tagName, null, startLine, startColumn);
            } else {
                String tagName = content.trim();
                if (!isValidTagName(tagName)) {
                    String fixed = XmlValidator.fixTagName(tagName);
                    reportError("Invalid tag name '" + tagName + "' fixes to '" + fixed + "'", startLine);
                    tagName = fixed;
                }
                return new XmlToken(XmlToken.Type.OPENING_TAG, tagName, null, startLine, startColumn);
            }
        }

        index++;   // eat '>'
        column++;

        String content = text.toString().trim();

        if (content.startsWith("/")) {

            String result = content.substring(1);
            String tagName = result.trim();

            if (result.trim().isEmpty()) {
                // >>>>>>>>>> CHANGED: Store error and auto-fix
                reportError("Empty tag name in closing tag", startLine);
                return new XmlToken(XmlToken.Type.CLOSING_TAG, "tag", null, startLine, startColumn);
            }

            if (!isValidTagName(tagName)) {
                String fixed = XmlValidator.fixTagName(tagName);
                reportError("Invalid tag name '" + tagName + "' fixes to '" + fixed + "'", startLine);
                tagName = fixed;
            }


            return new XmlToken(XmlToken.Type.CLOSING_TAG, tagName, null, startLine, startColumn);
        }

        else if (content.endsWith("/")) {
            String result = content.substring(0, content.length() - 1);
            String tagName = result.trim();

            if (result.trim().isEmpty()) {
                // >>>>>>>>>> CHANGED: Store error and auto-fix
                reportError("Empty tag name in self-closing tag", startLine);
                return new XmlToken(XmlToken.Type.SELF_CLOSING_TAG, "tag", null, startLine, startColumn);
            }

            if (!isValidTagName(tagName)) {
                String fixed = XmlValidator.fixTagName(tagName);
                reportError("Invalid tag name '" + tagName + "' fixes to '" + fixed + "'", startLine);
                tagName = fixed;

            }

            return new XmlToken(XmlToken.Type.SELF_CLOSING_TAG, tagName, null, startLine, startColumn);
        }

        else {
            String result = content;
            String tagName = result.trim();

            if (result.trim().isEmpty()) {
                // >>>>>>>>>> CHANGED: Store error and auto-fix
                reportError("Empty tag name in opening tag", startLine);
                return new XmlToken(XmlToken.Type.OPENING_TAG, "tag", null, startLine, startColumn);
            }

            if (!isValidTagName(tagName)) {
                String fixed = XmlValidator.fixTagName(tagName);
                reportError("Invalid tag name '" + tagName + "' fixes to '" + fixed + "'", startLine);
                tagName = fixed;
            }

            return new XmlToken(XmlToken.Type.OPENING_TAG, tagName, null, startLine, startColumn);
        }
    }

    private XmlToken readText() {  //return tag token case

        StringBuilder text = new StringBuilder();
        int startLine = line;
        int startColumn = column;

        while (index < length && input.charAt(index) != '<') {

            char c = input.charAt(index);


            // HANDLE: Missing '<' before closing/self tag inside text
            if (c == '>') {

                String word = getBackwardWord(index);

                // Move past '>'
                index++;
                column++;

                // ============================================================
                // NEW CASE — Closing tag missing '<' that may be embedded in text
                // Examples:
                //    "1/id>"  -> should become text "1" and closing tag </id>
                //    "/id>"   -> handled below (simple case)
                //    "some/text/id>" -> becomes "some/text" + closing tag </id>
                // ============================================================
                // Find last slash in the backward word (handles "1/id" case)
                int lastSlash = word.lastIndexOf('/');
                if (lastSlash != -1 && lastSlash < word.length() - 1) {
                    String suffix = word.substring(lastSlash + 1);
                    if (isValidTagName(suffix)) {
                        String tagName = suffix;
                        reportError("Missing '<' before closing tag </" + tagName + ">", startLine);

                        // Remove the '/tag' part from the accumulated text buffer, but keep any text before it
                        String fullText = text.toString();
                        int pos = fullText.lastIndexOf(word); // start index of the whole backward word in buffer

                        String keptBefore = "";
                        if (pos >= 0) {
                            // keep everything in the buffer before the backward word, then keep the part of the backward word before '/'
                            keptBefore = fullText.substring(0, pos) + word.substring(0, lastSlash);
                        } else {
                            // fallback: keep part before slash only
                            keptBefore = word.substring(0, lastSlash);
                        }

                        text = new StringBuilder(keptBefore);

// 1) Buffer the closing tag for next call
                        bufferedToken = new XmlToken(XmlToken.Type.CLOSING_TAG, tagName, null, startLine, startColumn);

// 2) Return the TEXT token now
                        String txt = text.toString().trim();
                        if (!txt.isEmpty()) {
                            return new XmlToken(XmlToken.Type.TEXT, null, txt, startLine, startColumn);
                        }

// 3) If no text exists, return buffered closing tag now
                        XmlToken t = bufferedToken;
                        bufferedToken = null;
                        return t;

                    }
                }


                // PREVIOUS SIMPLE CASE — word literally starts with '/' ("/id")
                if (word.startsWith("/") && isValidTagName(word.substring(1))) {
                    String tagName = word.substring(1).trim();
                    reportError("Missing '<' before closing tag </" + tagName + ">", startLine);

                    String fullText = text.toString();
                    int where = fullText.lastIndexOf(word);
                    String before = (where > 0) ? fullText.substring(0, where) : "";
                    text = new StringBuilder(before);

                    return new XmlToken(XmlToken.Type.CLOSING_TAG, tagName, null, startLine, startColumn);
                }


                //  Missing '<' before opening tag
                if (isValidTagName(word) && tagExistsInDocument(word)) {

                    reportError("Missing '<' before tag name: " + word, startLine);

                    String fullText = text.toString();
                    int where = fullText.lastIndexOf(word);

                    String beforeTag = (where > 0) ? fullText.substring(0, where) : "";

                    if (!beforeTag.trim().isEmpty()) {
                        return new XmlToken(XmlToken.Type.TEXT, null, beforeTag.trim(), startLine, startColumn);
                    }

                    input = input.substring(0, index - word.length() - 1)
                            + "<"
                            + input.substring(index - word.length() - 1);

                    index = index - word.length() - 1;
                    column--;

                    return readTag();
                }
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

        return next();
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

    public List<String> getSyntaxErrors() { return syntaxErrors; }
    public List<Integer> getSyntaxErrorLines() { return syntaxErrorLines; }

    private boolean tagExistsInDocument(String name) {

        if (name == null || name.isEmpty()) return false;

        String n = name.trim();

        return input.contains("<" + n + ">") ||
                input.contains("<" + n + " ") ||
                input.contains("<" + n + "/") ||
                input.contains("</" + n + ">");
    }

    private void reportError(String message, int line) {
        syntaxErrors.add(message);
        syntaxErrorLines.add(line);
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
    public void putBack(XmlToken t) {
        this.bufferedToken = t;
    }

}




