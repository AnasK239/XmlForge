package com.editor.structures.xml;
import java.util.ArrayList;
import java.util.List;

/*
This class represents the object type and corresponding content.
It can also contain nested nodes (children).
Everything is a NODE.

EX :
    <body>
    ...........Text
    </body>

    name : "body"
    textContent : "null"
    Attributes: EMPTY LIST
    children: A LIST WITH 1 CHILD THE TEXT NODE (...........Text)

Visual:

   [XmlNode Object 1: <body>]
   ├── name: "body"
   ├── textContent: null
   └── children:
          └── [XmlNode Object 2: Text]
                 ├── name: null
                 ├── textContent: "...........Text"
                 └── children: []
 */
public class XmlNode {

    private String name; // null for pure text nodes
    private String textContent; // null for non-pure text nodes

    private List<XmlAttribute> attributes; // UNUSED FOR NOW
    private List<XmlNode> children;

    public XmlNode(String name, String textContent) {
        this.name = name;
        this.textContent = textContent;
        this.attributes = new ArrayList<XmlAttribute>();
        this.children = new ArrayList<XmlNode>();
    }

    public void addChild(XmlNode child){
        this.children.add(child);
    }
    public void addAttribute(XmlAttribute attribute){
      this.attributes.add(attribute);
    }

    public boolean isTextNode(){ return (name == null); }

    public String getTextContent(){ return this.textContent; }
    public String getName(){ return this.name; }
    public List<XmlNode> getChildren(){ return this.children; }

}
