package com.editor.xml.parser;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;
import com.editor.xml.parser.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class XmlValidator {

    public ValidationResult validate(String xml) {

        ValidationResult result = new ValidationResult();

        XmlParser parser = new XmlParser();
        parser.parse(xml);

        List<String> errors = parser.getErrors();
        List<Integer> errorLines = parser.getErrorLineNumbers();

        if (errors.isEmpty()) {
            result.setValid(true);
            result.setErrorCount(0);
            result.setErrorLines(new ArrayList<>());
            result.errorMessages = new ArrayList<>();
            System.out.println("XML is valid.");
        } else {
            result.setValid(false);
            result.setErrorCount(errors.size());
            result.setErrorLines(new ArrayList<>(errorLines));
            result.errorMessages = new ArrayList<>(errors);

            // >>>>>>>>>> CHANGED: Better error display
            System.out.println("XML is NOT valid. Found " + result.getErrorCount() + " error(s):");

            for (int i = 0; i < errors.size(); i++) {
                System.out.println("  [Line " + result.getErrorLines().get(i) + "] " + result.errorMessages.get(i));
            }
        }

        return result;
    }

    public String fix(String xml) {

            if (xml == null || xml.trim().isEmpty()) {
                return "<root></root>";
            }

            XmlParser parser = new XmlParser();
            XmlDocument doc = parser.parse(xml);

            // New: Build XML from the repaired tree
            String fixed = serialize(doc.getRoot());
            return fixed;
            }

    // >>>>>>>>>> ADDED: New method to fix missing < or >
    private String fixMissingBrackets(String s) {
        StringBuilder fixed = new StringBuilder();
        int i = 0;

        while (i < s.length()) {
            // 1) HANDLE BROKEN CLOSING TAGS
            if (s.startsWith("/" , i)) {
                // Broken closing tag without '<'
                int j = i + 1;

                // read name
                while (j < s.length() && Character.isLetterOrDigit(s.charAt(j))) {
                    j++;
                }

                String name = s.substring(i+1, j);

                // if followed by '>' → fix: add '<'
                if (j < s.length() && s.charAt(j) == '>') {
                    fixed.append("</").append(name).append(">");
                    i = j + 1;
                    continue;
                }
            }


            // 2) HANDLE MISSING > INSIDE A TAG <...
            if (s.charAt(i) == '<') {

                int start = i;
                int j = i + 1;

                // read until space, '/', or end
                while (j < s.length() && s.charAt(j) != '>' && s.charAt(j) != '<') {
                    j++;
                }

                // CASE A: Found unexpected '<' before '>'  => missing '>'
                if (j < s.length() && s.charAt(j) == '<') {

                    String content = s.substring(start + 1, j).trim();

                    // self closing
                    if (content.endsWith("/")) {
                        fixed.append("<").append(content).append(">");
                    }
                    // closing tag
                    else if (content.startsWith("/")) {
                        fixed.append("<").append(content).append(">");
                    }
                    // opening tag
                    else {
                        fixed.append("<").append(content).append(">");
                    }

                    i = j;
                    continue;
                }

                // CASE B: reached end without finding '>'
                if (j >= s.length()) {

                    String content = s.substring(start + 1).trim();

                    // same handling: add missing >
                    fixed.append("<").append(content).append(">");

                    return fixed.toString(); // end
                }
            }
            // FALLBACK — original logic: missing <
            char c = s.charAt(i);

            if (c == '>') {
                String wordBefore = getLastWord(fixed.toString());

                if (isValidTagName(wordBefore) && tagExistsInDocument(wordBefore, s)) {
                    int wordStart = fixed.length() - wordBefore.length();
                    fixed.insert(wordStart, '<');
                }

                fixed.append('>');
            } else {
                fixed.append(c);
            }

            i++;
        }

        return fixed.toString();
    }


    // Get last word from string
    private String getLastWord(String s) {
        int i = s.length() - 1;
        
        // Skip whitespace
        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        
        StringBuilder word = new StringBuilder();
        
        // Collect word
        while (i >= 0 && !Character.isWhitespace(s.charAt(i)) 
               && s.charAt(i) != '<' && s.charAt(i) != '>') {
            word.insert(0, s.charAt(i));
            i--;
        }
        
        return word.toString();
    }

    //  Check if tag exists in document
    private boolean tagExistsInDocument(String name, String xml) {
        if (name == null || name.isEmpty()) return false;

        String n = name.trim();

        return xml.contains("<" + n + ">") ||
                xml.contains("<" + n + " ") ||
                xml.contains("<" + n + "/") ||
                xml.contains("</" + n + ">");
    }

    private String fixLeadingTextProblem(String s) {

        int i = 0;

        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }

        if (i < s.length() && s.charAt(i) != '<') {
            return "<root>" + s + "</root>";
        }

        return s;
    }

    private String fixMultiRootProblem(String s) {

        int roots = 0;
        int i = 0;

        while (i < s.length()) {

            int p = s.indexOf("<", i);
            if (p == -1) break;

            if (p + 1 < s.length() && Character.isLetter(s.charAt(p + 1))) {
                roots++;
                if (roots > 1) {
                    return "<root>" + s + "</root>";
                }
            }

            int close = s.indexOf(">", p);
            if (close == -1) break;

            i = close + 1;
        }

        return s;
    }

    private String fixMissingClosingTags(String s) {

        String out = "";
        ArrayList<String> stack = new ArrayList<>();

        int i = 0;

        while (i < s.length()) {

            if (s.charAt(i) == '<') {

                int end = s.indexOf(">", i);
                if (end == -1) return s;

                String tag = s.substring(i, end + 1);

                // Opening tag
                if (tag.length() > 2 && tag.charAt(1) != '/' && !tag.endsWith("/>")) {
                    String name = extractName(tag);
                    if (name.length() > 0) {
                        stack.add(name);
                    }
                }

                // Closing tag
                if (tag.startsWith("</")) {
                    if (!stack.isEmpty()) {
                        stack.remove(stack.size() - 1);
                    }
                }

                out += tag;
                i = end + 1;
            }
            else {
                out += s.charAt(i);
                i++;
            }
        }

        // Close remaining tags
        for (int j = stack.size() - 1; j >= 0; j--) {
            out += "</" + stack.get(j) + ">";
        }

        return out;
    }

    private String extractName(String tag) {

        int i = 1;
        while (i < tag.length() &&
                tag.charAt(i) != '>' &&
                tag.charAt(i) != ' ' &&
                tag.charAt(i) != '/') {
            i++;
        }

        return tag.substring(1, i);
    }

    // >>>>>>>>>> ADDED: Validate tag name
    private boolean isValidTagName(String name) {
        if (name == null || name.isEmpty()) return false;

        name = name.trim();
        if (name.contains(" ")) return false;

        char first = name.charAt(0);
        if (!(Character.isLetter(first) || first == '_')) return false;

        for (int j = 0; j < name.length(); j++) {
            char c = name.charAt(j);
            if (!(Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == ':')) {
                return false;
            }
        }

        return true;
    }

    public static String fixTagName(String oldName) {

        String name = oldName.trim();

        // Remove spaces
        String noSpaces = "";
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isWhitespace(c)) {
                noSpaces += c;
            }
        }

        // Keep allowed characters only
        String cleaned = "";
        for (int i = 0; i < noSpaces.length(); i++) {
            char c = noSpaces.charAt(i);

            if (Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == ':') {
                cleaned += c;
            }
        }

        // Remove leading digits
        while (cleaned.length() > 0 && Character.isDigit(cleaned.charAt(0))) {
            cleaned = cleaned.substring(1);
        }

        if (cleaned.length() == 0) {
            return "tag";
        }

        return cleaned;
    }
    // serialize repaired XML tree
    private String serialize(XmlNode node) {
        StringBuilder sb = new StringBuilder();
        serializeNode(node, sb, 0);
        return sb.toString();
    }

    private void serializeNode(XmlNode node, StringBuilder sb, int indent) {
        String pad = "  ".repeat(indent);

        // TEXT NODE
        if (node.getName() == null) {
            sb.append(pad).append(node.getTextContent()).append("\n");
            return;
        }

        // OPENING TAG
        sb.append(pad).append("<").append(node.getName()).append(">").append("\n");

        // CHILDREN
        for (XmlNode child : node.getChildren()) {
            serializeNode(child, sb, indent + 1);
        }

        // CLOSING TAG
        sb.append(pad).append("</").append(node.getName()).append(">").append("\n");
    }

}

