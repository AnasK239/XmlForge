package com.editor.structures.xml;

/*
IMPORTANT: THIS WILL PROBABLY NOT BE USED

This class represents a key-value pair for some attribute in the XML Tree.
Example:
    "id" : "1"
    "role" : "admin"

This is used to give useful information about the object in question.
Each object type will typically contain a list of attributes defining it.

Example
 <user  id="1" role="admin" >

The sample given to us in project description doesn't even use this notation
just simple nested children.
*/

public class XmlAttribute {
    private String name;
    private String value;

    public XmlAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return name + "=" + value;
    }
}
