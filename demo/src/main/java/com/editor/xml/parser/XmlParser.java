package com.editor.xml.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;

public class XmlParser {

    // Build XmlDocument from raw string.
    private List<String> errors = new ArrayList<>();
    public List<String> getErrors() { return errors; }

    private List<Integer> errorLineNumbers = new ArrayList<>();
    public List<Integer> getErrorLineNumbers() { return errorLineNumbers; }

    public XmlDocument parse(String xml) {

        Stack<XmlNode> stack = new Stack<>();
        Stack<Integer> lineStack = new Stack<>();
        XmlNode root = null;

        XmlTokenizer tokenizer = new XmlTokenizer(xml);

        while (tokenizer.hasNext()) {

            XmlToken token = tokenizer.next();

            // ERROR token from tokenizer
            if (token.isError()) {
                errors.add("SYNTAX ERROR at line " + token.getLine()
                        + ", col " + token.getColumn() + ": "
                        + token.getTextContent());
                errorLineNumbers.add(token.getLine());
                continue;
            }

            // Opening tag
            if (token.isOpeningTag()) {
                XmlNode n = new XmlNode(token.getTagName(), null);
                stack.push(n);
                lineStack.push(token.getLine());
            }

            // Self-closing tag
            else if (token.isSelfClosingTag()) {

                XmlNode n = new XmlNode(token.getTagName(), null);

                if (!stack.empty()) {
                    stack.peek().addChild(n);
                } else {
                    // Self-closing root (allowed but rare)
                    if (root != null) {
                        errors.add("ERROR: Multiple root elements detected.");
                        errorLineNumbers.add(token.getLine());
                    }
                    root = n;
                }
            }

            // Text
            else if (token.isText()) {

                if (stack.empty() && root == null) {
                    errors.add("ERROR: Text found before the root element.");
                    errorLineNumbers.add(token.getLine());
                    continue;
                }
                if (stack.empty() && root != null) {
                    errors.add("ERROR: Text found outside the root element.");
                    errorLineNumbers.add(token.getLine());
                    continue;
                }

                XmlNode n = new XmlNode(null, token.getTextContent());
                stack.peek().addChild(n);
            }

            // Closing tag
            else if (token.isClosingTag()) {

                // Case 1 — No matching opening tag
                if (stack.empty()) {
                    errors.add("ERROR: Closing tag </" + token.getTagName()
                            + "> has no matching opening tag.");
                    errorLineNumbers.add(token.getLine());
                    continue;
                }

                XmlNode popNode = stack.pop();
                int openLine = lineStack.pop();

                // Case 2 — Mismatched tag
                boolean mismatched = false;

                if (!popNode.getName().equals(token.getTagName())) {
                    errors.add("ERROR: Mismatched tag. Expected </"
                            + popNode.getName() + "> but found </"
                            + token.getTagName() + ">.");
                    errorLineNumbers.add(token.getLine());
                    mismatched = true;
                }

                // Case 3 — Attach to parent OR root
                if (!stack.empty()) {
                    stack.peek().addChild(popNode);
                } else {

                    // mismatched root-level closing: ignore it
                    if (mismatched) {
                        if (root == null) {
                            root = popNode;
                        }
                        continue;
                    }

                    // proper root-closing
                    if (root != null) {
                        errors.add("ERROR: Multiple root elements detected.");
                        errorLineNumbers.add(token.getLine());
                    }
                    root = popNode;
                }
            }
        }

        // Missing closing tags at end of document
        while (!stack.empty()) {

            XmlNode n = stack.pop();
            int openLine = lineStack.pop();

            errors.add("ERROR: Missing closing tag for <"
                    + n.getName() + "> at line " + openLine + ".");
            errorLineNumbers.add(openLine);
        }

        return new XmlDocument(root);
    }

    private XmlNode parseNode() {
        // Helper method (unused)
        return null;
    }
}
