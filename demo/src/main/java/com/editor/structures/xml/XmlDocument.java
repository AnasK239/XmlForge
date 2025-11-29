package com.editor.structures.xml;

public class XmlDocument {
    private XmlNode root;
    public XmlDocument(XmlNode root)
    {
        this.root = root;
    }
    public XmlNode getRoot() {
        return root;
    }
    public String toStringFormatted()
    {
        // uses XmlFormatter or node’s toPrettyString.
        return "";
    }
}
