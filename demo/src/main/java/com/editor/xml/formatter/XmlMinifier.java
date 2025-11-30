package com.editor.xml.formatter;

public class XmlMinifier {
    //Remove unnecessary whitespace.

    public String minify(String xml)
    {
        //Remove line breaks, extra spaces between tags, etc.
        return xml.replaceAll(">\\s+<", "><").trim(); // IDK if this right
    }
}
