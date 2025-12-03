package com.editor.structures.xml;

import java.util.ArrayList;
import java.util.List;

public class XmlNode {

    private String name; // null for pure text nodes
    private String textContent;
    private List<XmlAttribute> attributes;
    private List<XmlNode> children;
    

   public XmlNode(String name, List<XmlAttribute> attrs) {    //constructor for tag Token
    this.name = name;
    this.attributes = (attrs != null ? attrs : new ArrayList<>());
    this.children = new ArrayList<>();
    this.textContent = null;
    }
    public XmlNode(String textContent) {                       //constructor for Text Token
    this.name = null;
    this.attributes = null;
    this.children = null;
    this.textContent = textContent;
    }


    
    public void addChild(XmlNode child){children.add(child);}
    public void addAttribute(XmlAttribute attribute){}
    public List<XmlNode> getChildren(){return children;}
     public boolean isTextNode(){return name == null;}
    // public String toPrettyString(int indentLevel){}
    // public String toMinifiedString(){}

    // We should use ArrayList here (allowed dynamic array).(msh fahem awy :))

    public String getName() {
        return name;
    }

    public String getTextContent() {
        return textContent;
    }

    public List<XmlAttribute> getAttributes() {
        return attributes;
    }
}
