package com.editor.xml.parser;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;

import java.util.*;

public class XmlParser {

    private List<String> errors = new ArrayList<>();
    public List<String> getErrors() { return errors; }

    private List<Integer> errorLineNumbers = new ArrayList<>();
    public List<Integer> getErrorLineNumbers() { return errorLineNumbers; }
    private List<XmlNode> topLevelNodes = new ArrayList<>();
    private List<XmlToken> consumedTokens = new ArrayList<>();
    // Parent tags
    private static final String[] schemaParents = {
            "users", "user", "posts", "post", "topics", "topic", "body", "followers", "follower"
    };

    // Allowed children for each parent (matching index)
    private static final String[][] schemaChildren = {
            {"user"},                          // users
            {"id", "name", "posts", "followers"}, // user
            {"post"},                         // posts
            {"body", "topics"},               // post
            {"topic"},                        // topics
            {},                               // topic has no element children
            {},                               // body has no element children
            {"follower"},                     // followers
            {"id"}                            // follower
    };


    public XmlDocument parse(String xml) {

        Stack<XmlNode> stack = new Stack<>();
        Stack<Integer> lineStack = new Stack<>();
        XmlNode root = null;


        XmlTokenizer tokenizer = new XmlTokenizer(xml);

        while (tokenizer.hasNext()) {

            XmlToken token = tokenizer.next();


            if (token == null) break;
            consumedTokens.add(token);
            // OPENING TAG
            if (token.isOpeningTag()) {
                String open = token.getTagName();
                XmlNode n = new XmlNode(token.getTagName(), null,token.getLine());
                if (stack.empty()) {topLevelNodes.add(n);}

                //       If the new opening tag cannot be a child of the top,
                //      then the top tag should be auto-closed NOW.
                //      Example: <name>Ahmed Ali   then <posts>
                if (!stack.isEmpty()) {

                    XmlNode top = stack.peek();

                    // allowed children for the CURRENT top tag
                    String[] allowed = getAllowedChildren(top.getName());

                    boolean allowedChild = false;
                    for (String a : allowed) {
                        if (a.equals(open)) {
                            allowedChild = true;
                            break;
                        }
                    }

                    // If NOT allowed → close the top immediately
                    if (!allowedChild) {

                        // report missing closing tag
                        errors.add("Missing closing tag for <" + top.getName() + "> ");
                        errorLineNumbers.add(token.getLine());

                        // pop it
                        XmlNode closed = stack.pop();
                        lineStack.pop();

                        // attach it to parent
                        if (!stack.isEmpty()) {
                            stack.peek().addChild(closed);
                        } else {
                            topLevelNodes.add(closed);
                        }
                    }
                }


                stack.push(n);
                lineStack.push(token.getLine());
            }
            // SELF-CLOSING TAG
            else if (token.isSelfClosingTag()) {

                XmlNode n = new XmlNode(token.getTagName(), null, token.getLine());

                if (!stack.empty()) {
                    stack.peek().addChild(n);
                } else {
                    topLevelNodes.add(n);
                }
            }
            // TEXT
            else if (token.isText()) {

                String txt = token.getTextContent().trim();

                if (stack.empty()) {

                    // Error: text not inside any tag
                    if (!txt.isEmpty()) {
                        errors.add("ERROR: Text found outside any element.");
                        errorLineNumbers.add(token.getLine());
                    }

                    // Add text as root-level node
                    XmlNode textNode = new XmlNode(null, token.getTextContent() , token.getLine());
                    topLevelNodes.add(textNode);
                }
                else {
                    // Normal text inside an element
                    XmlNode n = new XmlNode(null, token.getTextContent(), token.getLine());
                    stack.peek().addChild(n);
                }
            }
            // CLOSING TAG  — ALL STRUCTURAL FIXES
            else if (token.isClosingTag()) {

                String closing = token.getTagName();
                // CASE 1 — No matching opening tag
                if (stack.empty()) {
                    errors.add("ERROR: Closing tag </" + closing + "> has no matching opening tag.");
                    errorLineNumbers.add(token.getLine());
                    continue;
                }

                XmlNode top = stack.peek();
                // 🔥 INSERTED: MISMATCH HANDLING BLOCK

                if (!top.getName().equals(closing)) {

                    int matchIndex = -1;
                    for (int i = stack.size() - 1; i >= 0; i--) {
                        XmlNode n = stack.get(i);
                        if (n.getName() != null && n.getName().equals(closing)) {
                            matchIndex = i;
                            break;
                        }
                    }

                    // CASE 2 — Missing closing tag (matching opener exists deeper)
                    if (matchIndex != -1) {

                        XmlNode missingClose = stack.pop();
                        int openLine = lineStack.pop();

                        int suggestedLine = openLine;
                        for (XmlNode c : missingClose.getChildren()) {
                            if (c.getLine() > openLine) {
                                suggestedLine = c.getLine() - 1;
                                break;
                            }
                        }

                        errors.add("Missing closing tag for <" + missingClose.getName() +
                                ">");
                        errorLineNumbers.add(token.getLine()-1);

                        if (!stack.isEmpty()) stack.peek().addChild(missingClose);
                        else topLevelNodes.add(missingClose);

                        tokenizer.putBack(token); // 🔥 Reprocess same closing tag
                        continue;
                    }

                    // CASE 3 — Pure mismatched closing: </closing> does not match anything
                    errors.add("Mismatched closing tag </" + closing +
                            "> — expected </" + top.getName() + ">.");
                    errorLineNumbers.add(token.getLine());

                    // Auto-close the top
                    stack.pop();
                    lineStack.pop();

                    if (!stack.isEmpty()) stack.peek().addChild(top);
                    else topLevelNodes.add(top);

                    continue; // 🔥 consume mismatched closing tag
                }



                // CASE 3 — Missing opening tag for <closing>
                if (top.getName() == null || !top.getName().equals(closing)) {

                    // Allowed children for this missing tag
                    String[] allowed = getAllowedChildren(closing);

                    List<XmlNode> children = top.getChildren();
                    List<XmlNode> toMove = new ArrayList<>();

                    int insertLine = token.getLine(); // fallback if no children

                    // Special case: <body> missing => move TEXT nodes too (body contains text)
                    boolean moveTextNodes = closing.equals("body");

                    // Identify children to move into the missing tag
                    for (XmlNode child : children) {

                        // CASE A — Normal element child (match allowed children)
                        if (child.getName() != null) {

                            boolean allowedChild = false;
                            for (String s : allowed) {
                                if (s.equals(child.getName())) {
                                    allowedChild = true;
                                    break;
                                }
                            }

                            if (allowedChild) {
                                toMove.add(child);
                                if (toMove.size() == 1) {
                                    insertLine = child.getLine(); // first moved child determines line
                                }
                            }
                        }

                        // CASE B — Missing <body>: move TEXT nodes inside body
                        else if (moveTextNodes &&
                                child.getTextContent() != null &&
                                !child.getTextContent().trim().isEmpty()) {

                            toMove.add(child);

                            if (toMove.size() == 1) {
                                insertLine = child.getLine();
                            }
                        }
                    }

                    // Report error with correct insert line
                    errors.add("Missing opening tag <" + closing + "> ");
                    errorLineNumbers.add(insertLine-1);

                    // Create missing element with correct line number
                    XmlNode missing = new XmlNode(closing, null, insertLine);

                    // Remove only moved children from parent
                    children.removeAll(toMove);

                    // Add moved children into the missing node
                    for (XmlNode c : toMove) {
                        missing.addChild(c);
                    }

                    // Attach missing tag inside parent
                    top.addChild(missing);

                    continue;
                }




                // CASE 1B — Correct matching closing tag
                stack.pop();
                lineStack.pop();

                if (!stack.empty()) {
                    stack.peek().addChild(top);
                }else {
                    // This node is a root-level element
                    if (!topLevelNodes.contains(top)) {
                    topLevelNodes.add(top);
                }
                }
            }
        }


        // EOF — Missing closing tags
        while (!stack.empty()) {

            XmlNode n = stack.pop();
            int openLine = lineStack.pop();

            // find first child line that indicates where tag should close
            int correctCloseLine = openLine; // fallback

            List<XmlNode> kids = n.getChildren();

            for (XmlNode child : kids) {

                if (child.getLine() > openLine) {

                    // found next content after opening tag
                    // this is where the closing tag should go
                    correctCloseLine = child.getLine() - 1;
                    break;
                }
            }

            errors.add("ERROR: Missing closing tag for <" + n.getName() +
                    "> ");

            errorLineNumbers.add(correctCloseLine);
        }
        // MERGE tokenizer errors
        errors.addAll(tokenizer.getSyntaxErrors());
        errorLineNumbers.addAll(tokenizer.getSyntaxErrorLines());
        //  wrap multiple top-level elements inside <root>
        if (topLevelNodes.size() > 1) {

            // Report error
            errors.add("ERROR: Multiple root elements detected. Wrapped inside <root>.");
            errorLineNumbers.add(1);

            XmlNode wrapper = new XmlNode("root", null);

            for (XmlNode n : topLevelNodes) {
                wrapper.addChild(n);
            }

            root = wrapper;

        } else if (topLevelNodes.size() == 1) {

            root = topLevelNodes.get(0);
        }
        // <<< ADDED


        return new XmlDocument(root);
    }
    //helper functions
    private String[] getAllowedChildren(String tagName) {
        for (int i = 0; i < schemaParents.length; i++) {
            if (schemaParents[i].equals(tagName)) {
                return schemaChildren[i];
            }
        }
        return new String[0]; // no children allowed
    }

}
