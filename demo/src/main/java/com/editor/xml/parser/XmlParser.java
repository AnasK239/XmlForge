package com.editor.xml.parser;
import java.util.Stack;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;

public class XmlParser {
    //Build XmlDocument from raw string.

    public XmlDocument parse(String xml)
    {
        Stack<XmlNode> stack = new Stack<>();
        XmlNode root=null;
        XmlTokenizer tokenizer = new XmlTokenizer(xml);
        while(tokenizer.hasNext())
        {
            XmlToken token = tokenizer.next();
            if(token.isOpeningTag()){
            XmlNode n =new XmlNode(token.getTagName() ,token.getAttributes());
            stack.push(n);
            }
            else if (token.isSelfClosingTag()){
                XmlNode n =new XmlNode(token.getTagName(), token.getAttributes());
                if(!stack.empty()){
                    stack.peek().addChild(n);
                }
                else {
                    // self closing root (rare but valid)
                if (root != null) {
                    // ERROR: multiple roots
                }
                root = n;
                }
            }
            else if(token.isText()){
                XmlNode n =new XmlNode(token.getTextContent());
                stack.peek().addChild(n);
            }
            else if (token.isClosingTag()){
               XmlNode popNode= stack.pop();
                if(!popNode.getName().equals(token.getTagName())){
                    //ERROR : Tags dont match 
                }
                if(!stack.empty()){    // If stack still has parent  attach to it
                    stack.peek().addChild(popNode);
                }
                else{                 //Stack is empty this is the root
                    if (root != null) {
                // ERROR: multiple roots
                }
                root = popNode;
                }
            }
        }
        // Parsing logic to build XmlDocument from tokens
        //Use a stack (allowed DS) to push open tags; when encountering closing tag, pop & attach as child.
        if (!stack.empty()) {
    // ERROR: missing closing tag(s)
        }

        return new XmlDocument(root);
    }
    private XmlNode parseNode()
    {
        // Helper method to parse individual XmlNode from tokens
        return null;
    }
    public static void main(String[] args) {

        // Simple test XML
        String xml =
                "<users>\n" +
                "    <user id=\"1\">\n" +
                "        <name>Ahmed</name>\n" +
                "        <age>20</age>\n" +
                "        <bio>\n" +
                "            Software engineer.\n" +
                "        </bio>\n" +
                "        <post title=\"Hello World\" likes=\"10\" />\n" +
                "    </user>\n" +
                "</users>";

        // Create parser instance
        XmlParser parser = new XmlParser();

        // Parse XML
        XmlDocument doc = parser.parse(xml);

        // Get root node
        XmlNode root = doc.getRoot();

        // Print structure
        printNode(root, 0);
    }

    // helper method to print tree
    private static void printNode(XmlNode node, int indent) {

        String spaces = " ".repeat(indent);

        if (node.isTextNode()) {
            System.out.println(spaces + "TEXT: \"" + node.getTextContent() + "\"");
            return;
        }

        System.out.println(spaces + "<" + node.getName() + ">");

        if (node.getChildren() != null) {
            for (XmlNode child : node.getChildren()) {
                printNode(child, indent + 4);
            }
        }

        System.out.println(spaces + "</" + node.getName() + ">");
    }
}


 

    
