package com.editor.xml.formatter;

import com.editor.structures.xml.XmlNode;


/*
Keep both functions:
--minify(String xml) — fast minifier for CLI + GUI
--toMinifiedString(XmlNode) — for internal processing after parsing


what does it do now:
Removes all whitespace
Preserves structure
Works regardless of input size
Fully functional in CLI & GUI

How it works:
Step 1 — Remove newlines and tabs
Step 2 — Remove whitespace between tags
Step 3 — Remove spaces before >
Step 4 — Fix self-closing tags
Step 5 — Final trim
 */
public class XmlMinifier {

    public String minify(String xml) {
        if (xml == null || xml.isEmpty()) return "";

        // Remove all newline & tab characters -> make it all in one line but still with indents
        String result = xml.replaceAll("[\\n\\t]", "");

        // Remove white spaces between tags: <tag>   <tag2> -> <tag><tag2>
        result = result.replaceAll(">\\s+<", "><");

        // Remove spaces before '>' -> <name     > -> <name>
        result = result.replaceAll("\\s+>", ">");

        // Remove spaces inside self-closing tags <tag   /> -> <tag/>
        result = result.replaceAll("/\\s+>", "/>");

        // Trim outer whitespace if exists
        return result.trim();
    }
    public static String toMinifiedString(XmlNode node) {
        StringBuilder sb = new StringBuilder();

        // Text node
        if (node.isTextNode()) {
            sb.append(node.getTextContent().trim());
            return sb.toString();
        }

        // Opening tag
        sb.append("<").append(node.getName());

        // Append attributes
        if (node.getAttributes() != null) {
            node.getAttributes().forEach(attr -> {
                sb.append(" ").append(attr.getName())
                        .append("=\"").append(attr.getValue()).append("\"");
            });
        }

        // Self-closing empty node
        if (node.getChildren().isEmpty()) {
            sb.append("/>");
            return sb.toString();
        }

        sb.append(">");

        // Children
        for (XmlNode child : node.getChildren()) {
            sb.append(toMinifiedString(child));
        }

        // Closing tag
        sb.append("</").append(node.getName()).append(">");

        return sb.toString();
    }
}

/*
public class XmlMinifier {
    //Remove unnecessary whitespace.



    public String minify(String xml)
    {
        //Remove line breaks, extra spaces between tags, etc.
        return xml.replaceAll(">\\s+<", "><").trim(); // IDK if this right
    }
    ------------ WHY THIS IS WRONG --------------
    This only removes whitespace between tags, but:
    It does NOT remove:
    Leading/trailing spaces inside text content
    Multiple consecutive spaces
    Newlines outside tags
    Tabs (\t)
    Spaces before closing brackets (<tag >)
    Spaces before self-closing (<tag />)
    Spaces inside attributes (attr = "value")


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
*/