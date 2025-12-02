package com.editor.xml.parser;
import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;

public class XmlParser {
    //Build XmlDocument from raw string.

    public XmlDocument parse(String xml)
    {
        XmlTokenizer tokenizer = new XmlTokenizer(xml);
        XmlDocument doc = new XmlDocument();
        // Parsing logic to build XmlDocument from tokens
        //Use a stack (allowed DS) to push open tags; when encountering closing tag, pop & attach as child.
//        return doc;
        return null;
    }
    private XmlNode parseNode()
    {
        // Helper method to parse individual XmlNode from tokens
        return null;
    }
}
