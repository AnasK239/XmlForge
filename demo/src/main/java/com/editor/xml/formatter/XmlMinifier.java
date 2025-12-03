package com.editor.xml.formatter;

import com.editor.structures.xml.XmlNode;

public class XmlMinifier {
    //Remove unnecessary whitespace.

    public String minify(String xml)
    {
        //Remove line breaks, extra spaces between tags, etc.
        return xml.replaceAll(">\\s+<", "><").trim(); // IDK if this right
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
}
