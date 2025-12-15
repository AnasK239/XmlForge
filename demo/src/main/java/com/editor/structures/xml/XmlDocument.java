package com.editor.structures.xml;

import com.editor.xml.formatter.XmlFormatter;
import com.editor.xml.formatter.XmlMinifier;

public class XmlDocument {
    private XmlNode root;

    public XmlDocument()
    {
        this.root = null;
    }
    public XmlDocument(XmlNode root)
    {
        this.root = root;
    }
    public XmlNode getRoot() {
        return root;
    }

    public void setRoot(XmlNode root) {
        this.root = root;
    }
}
