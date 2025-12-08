package com.editor.xml.formatter;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;
import com.editor.xml.parser.XmlParser;

public class XmlMinifier {
    //Remove unnecessary whitespace.

    public String minify(XmlDocument doc) {
        StringBuilder sb = new StringBuilder();
        minifyNode(doc.getRoot(), sb);
        return sb.toString();
    }

    private void minifyNode(XmlNode node, StringBuilder sb) {

        // TEXT NODE → just append its text (NO TAG)
        if (node.getName() == null) {
            sb.append(node.getTextContent().trim());
            return;
        }

        String tag = node.getName();

        // OPEN TAG
        sb.append("<").append(tag).append(">");

        // CHILDREN
        for (XmlNode child : node.getChildren()) {
            minifyNode(child, sb);
        }

        // CLOSE TAG
        sb.append("</").append(tag).append(">");
    }

    // Collapse the entire text into one line.
    // <user>
    //    <name>Ahmed Ali</name>
    //</user>
    // Becomes : <user><name>Ahmed Ali</name></user>
    public static String toMinifiedString(XmlNode node){
        StringBuilder sb = new StringBuilder();

        if(node.isTextNode()) {
            sb.append(node.getTextContent().trim());
        }
        else{
            sb.append('<');
            sb.append(node.getName());
            sb.append('>');

            for(XmlNode child : node.getChildren()) {
                sb.append(toMinifiedString(child));
            }

            sb.append("</").append(node.getName()).append(">");
        }

        return sb.toString();
    }

    public String minify(String xml)
    {
        XmlParser parser = new XmlParser();
        XmlDocument doc = parser.parse(xml);
        return minify(doc);
    }
}
