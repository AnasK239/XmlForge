package com.editor.xml.formatter;
import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;
import com.editor.xml.parser.XmlParser;

public class XmlFormatter {
    //Prettify / indent the XML.
    private static final int INDENT_SIZE = 4;

    public String format(XmlDocument doc) {
        StringBuilder sb = new StringBuilder();
        formatNode(doc.getRoot(), 0, sb);
        return sb.toString();
    }

    private void formatNode(XmlNode node, int depth, StringBuilder sb) {

        // TEXT NODE → just print text with indentation
        if (node.getName() == null) {
            String indent = " ".repeat(depth * INDENT_SIZE);
            sb.append(indent)
                    .append(node.getTextContent().trim())
                    .append("\n");
            return;
        }

        String tag = node.getName();
        String indent = " ".repeat(depth * INDENT_SIZE);

        boolean hasOnlyTextChild =
                node.getChildren().size() == 1 &&
                        node.getChildren().get(0).getName() == null;

        boolean isBodyOrTopic = tag.equals("body") || tag.equals("topic");

        // INLINE CASE: <id>1</id> , <name>Ahmed</name>
        if (hasOnlyTextChild && !isBodyOrTopic) {
            sb.append(indent)
                    .append("<").append(tag).append(">")

                    .append(node.getChildren().get(0).getTextContent().trim())

                    .append("</").append(tag).append(">\n");
            return;
        }

        // BLOCK CASE: <body> , <topic> , parent nodes
        sb.append(indent).append("<").append(tag).append(">\n");

        for (XmlNode child : node.getChildren()) {
            formatNode(child, depth + 1, sb);
        }

        sb.append(indent).append("</").append(tag).append(">\n");
    }


    public String format(String xml)
    {
        XmlParser parser = new XmlParser();
        XmlDocument doc2 = parser.parse(xml);
        return format(doc2);
    }





}
