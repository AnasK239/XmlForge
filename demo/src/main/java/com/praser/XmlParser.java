package com.praser;
import com.model.xml.XmlDoc;
import com.model.xml.XmlNode;
public class XmlParser {
    //Build XmlDocument from raw string.

    public XmlDoc parse(String xml)
    {
        XmlTokenizer tokenizer = new XmlTokenizer(xml);
        XmlDoc doc = new XmlDoc();
        // Parsing logic to build XmlDoc from tokens
        //Use a stack (allowed DS) to push open tags; when encountering closing tag, pop & attach as child.
        return doc;
    }
    private XmlNode parseNode()
    {
        // Helper method to parse individual XmlNode from tokens
        return null;
    }
}
