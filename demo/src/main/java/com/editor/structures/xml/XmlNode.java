package com.editor.structures.xml;

import java.util.List;

public class XmlNode {

    private String name; // null for pure text nodes
    private String textContent;
    private List<XmlAttribute> attributes;
    private List<XmlNode> children;

    public void addChild(XmlNode child){}
    public void addAttribute(XmlAttribute attribute){}
    public List<XmlNode> getChildren(){}
    public boolean isTextNode(){}
    public String toPrettyString(int indentLevel){}
    public String toMinifiedString(){}

    // We should use ArrayList here (allowed dynamic array).(msh fahem awy :))
}
