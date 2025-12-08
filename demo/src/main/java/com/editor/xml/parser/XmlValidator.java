package com.editor.xml.parser;

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
            result.isValid = true;
            result.errorCount = 0;
            result.errorLines = new ArrayList<>();
            result.errorMessages = new ArrayList<>();
            System.out.println("XML is valid.");
        } else {
            result.isValid = false;
            result.errorCount = errors.size();
            result.errorLines = new ArrayList<>(errorLines);
            result.errorMessages = new ArrayList<>(errors);

            System.out.println("XML is NOT valid. Found " + result.errorCount + " error(s):");

            for (int i = 0; i < errors.size(); i++) {
                System.out.println("Line " + result.errorLines.get(i) + ": " + result.errorMessages.get(i));
            }
        }

        return result;
    }

    public String fix(String xml) {

        // first check if the xml even exist lol
        if (xml == null || xml.trim().isEmpty()) {
            return "<root></root>";  // return basic xml
        }

        String out = xml;
        //out = trimOuterSpaces(out);
        out = fixLeadingTextProblem(out);
        out = fixMultiRootProblem(out);
        //out = fixSelfClosingSpacing(out);
        out = fixMissingClosingTags(out);

        return out;
    }

    //helper functions
    private String fixLeadingTextProblem(String s) {

        int i = 0;

        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {    // skip blank spaces 
            i++;
        }

        if (i < s.length() && s.charAt(i) != '<') {                        // if first nonspace char isn't '<' then its wrong xml
            return "<root>" + s + "</root>";
        }

        return s;
    }

    private String fixMultiRootProblem(String s) {

        int roots = 0;
        int i = 0;

        // loop all chars to see how many real tags are there
        while (i < s.length()) {

            int p = s.indexOf("<", i);
            if (p == -1) break; // no more tags

            // check if it's opening tag not closing
            if (p + 1 < s.length() && Character.isLetter(s.charAt(p + 1))) {
                roots++;
                if (roots > 1) {
                    // if more than one root just wrap everything
                    return "<root>" + s + "</root>";
                }
            }

            int close = s.indexOf(">", p);
            if (close == -1) break; // tag not even closed 

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
                if (end == -1) return s; // can't do much here

                String tag = s.substring(i, end + 1);

                // opening tag
                if (tag.length() > 2 && tag.charAt(1) != '/' && !tag.endsWith("/>")) {
                    // extract name manually
                    String name = extractName(tag);
                    if (name.length() > 0) {
                        stack.add(name);
                    }
                }

                // closing tag
                if (tag.startsWith("</")) {
                    if (!stack.isEmpty()) {
                        stack.remove(stack.size() - 1); // pop
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

        // now close whatever tags left in stack
        for (int j = stack.size() - 1; j >= 0; j--) {
            out += "</" + stack.get(j) + ">";
        }

        return out;
    }

    private String extractName(String tag) {

        int i = 1; // skip '<'
        while (i < tag.length() &&
                tag.charAt(i) != '>' &&
                tag.charAt(i) != ' ' &&
                tag.charAt(i) != '/') {
            i++;
        }

        return tag.substring(1, i);  // simple cut
    }

    public static String fixTagName(String oldName) {

        // remove spaces (begin/end)
        String name = oldName.trim();

        // remove spaces inside
        String noSpaces = "";
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isWhitespace(c)) {
                noSpaces += c;
            }
        }

        // keep allowed characters only
        String cleaned = "";
        for (int i = 0; i < noSpaces.length(); i++) {
            char c = noSpaces.charAt(i);

            // allowed = letters, numbers, _, -, :
            if (Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == ':') {
                cleaned += c;
            }
        }

        // tag can't start with a number
        while (cleaned.length() > 0 && Character.isDigit(cleaned.charAt(0))) {
            cleaned = cleaned.substring(1);
        }

        // if everything got removed -> fallback name
        if (cleaned.length() == 0) {
            return "tag";
        }

        return cleaned;
    }
}

class ValidationResult {
    boolean isValid;
    int errorCount;
    List<Integer> errorLines;
    List<String> errorMessages;
}
