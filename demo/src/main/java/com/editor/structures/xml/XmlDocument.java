package com.editor.structures.xml;

import com.editor.xml.formatter.XmlFormatter;
import com.editor.xml.formatter.XmlMinifier;

public class XmlDocument {
    private XmlNode root;
    public XmlDocument(XmlNode root)
    {
        this.root = root;
    }
    public XmlNode getRoot() {
        return root;
    }

    public String toStringFormatted(String format)
    {
        if(format == "format"){
            // call corresponding method to prettifiy.
        }

        if(format == "mini"){
            // Minifiy's the entire XML DOCUMENT
            return XmlMinifier.toMinifiedString(root);
        }
        return null;
    }
}
