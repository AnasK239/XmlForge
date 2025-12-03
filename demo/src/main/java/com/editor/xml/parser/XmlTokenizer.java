package com.editor.xml.parser;
import com.editor.structures.xml.XmlAttribute;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;





public class XmlTokenizer {
    private String input;  //// the entire XML input as a string
    private int index;     // index to navigate the input string
    private int line;      // used to indicate the current line number (starts at 1)
    private int column;    // used to indicate the current column number (starts at 1)
    private int length;    // total length of the input string


    public XmlTokenizer( String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input XML cannot be null");
        }
        this.column = 1;
        this.index = 0;
        this.input = input;
        this.length = input.length();
        this.line = 1;
    }
    public boolean hasNext()
    {
        skipWhitespaces();
        return index<length;
    }

    // (Where XmlToken is a small inner class or enum+data.)
    public XmlToken next()
    {
        skipWhitespaces();
        if (index >= length) return null;

        if (input.charAt(index) == '<') {
            return readTag();
        } else {
            return readText();
        }
            
    }
    //Helper functions 
    private void  skipWhitespaces(){
        while (index < length && Character.isWhitespace(input.charAt(index))) {
            char c = input.charAt(index);
            if (c=='\n'){
                line++;
                column=1;
            }else {
                column++;
            }
            index++;
}
}
    private XmlToken readTag(){
        StringBuilder text = new StringBuilder() ;
        int startLine=line;                       //store the line to pass it to token class                  
        int startColumn=column;                   //store the column to pass it to token class     
        while(index<length&&input.charAt(index)!='>')
        {
            char c =input.charAt(index);
            text.append(c);
            if (c == '\n') {        // update line/column
                line++;
                column = 1;
            } else {
                column++;
            }
            index++;                       
        }
        index++;            // eat the '>'
        column++;           //updates the column
        String content = text.toString();
        if (content.startsWith("</")){
            String result = content.substring(2);   // skip "</"
            return new XmlToken(XmlToken.Type.CLOSING_TAG, result, null, null, startLine, startColumn);    //creat a text token to return
        }else if (content.endsWith("/")){
            String result = content.substring(1, content.length() - 1);  // remove < and /
            int spaceIndex = result.indexOf(' ');
            String tagName;
            String attrString = "";

            if (spaceIndex == -1) {
                tagName = result;      // No attributes
                return new XmlToken(XmlToken.Type.SELF_CLOSING_TAG, tagName, null, null, startLine, startColumn);
            } else {
                tagName = result.substring(0, spaceIndex);
                attrString = result.substring(spaceIndex + 1);
                List<XmlAttribute> attrs = parseAttributes(attrString);
                return new XmlToken(XmlToken.Type.SELF_CLOSING_TAG, tagName, attrs, null, startLine, startColumn);
            }

            
            
           
        }else {
            String result = content.substring(1); // remove '<'
            int spaceIndex = result.indexOf(' ');  // Find first space: separates tagName from attributes
            String tagName;
            String attrString = "";
            if (spaceIndex == -1) {
                tagName = result;     // No attributes
                return new XmlToken(XmlToken.Type.OPENING_TAG, tagName, null, null, startLine, startColumn);
            } else {
                tagName = result.substring(0, spaceIndex);        // part before space
                attrString = result.substring(spaceIndex + 1);    // part after space
                List<XmlAttribute> attrs = parseAttributes(attrString);
                return new XmlToken(XmlToken.Type.OPENING_TAG, tagName, attrs, null, startLine, startColumn);

            }


              
            }

    }
    private XmlToken readText(){
        StringBuilder text = new StringBuilder() ;
        int startLine=line;                       //store the line to pass it to token class                  
        int startColumn=column;                   //store the column to pass it to token class 
        while(index<length&&input.charAt(index)!='<')
        {
            char c =input.charAt(index);
            text.append(c);
            if (c == '\n') {        // update line/column
                line++;
                column = 1;
            } else {
                column++;
            }
            index++;
        }
        String content = text.toString().trim();    
        if(!content.isEmpty())             
        {
        XmlToken t = new XmlToken(XmlToken.Type.TEXT, null, null, content, startLine, startColumn);    //creat a text token to return
        return t;
        }
        return next();     // Skip this useless whitespace and immediately get the next meaningful token.         

    }
    private List<XmlAttribute> parseAttributes(String s) {
    List<XmlAttribute> attrs = new ArrayList<>();      // list to store attributes

    int i = 0;                                         // current index in string
    int n = s.length();                                // total length of string

    while (i < n) {                                   

        while (i < n && s.charAt(i) == ' ') i++;       // skip leading spaces

        if (i >= n) break;                             // no more content → exit

        int keyStart = i;                               // start of attribute name
        while (i < n && s.charAt(i) != '=' && s.charAt(i) != ' ')
            i++;                                        // read key until '=' or space

        if (i >= n || s.charAt(i) != '=') break;        // if no '=', invalid → stop

        String key = s.substring(keyStart, i);          // extract the key (name)

        i++;                                           

        while (i < n && s.charAt(i) == ' ') i++;        

        if (i < n && s.charAt(i) == '"') i++;           
        int valueStart = i;                             // start of attribute value
        while (i < n && s.charAt(i) != '"')
            i++;                                        // read until closing quote "

        if (i >= n) break;                              // missing closing quote → stop

        String value = s.substring(valueStart, i);      // extract the value

        i++;                                            // skip closing quote "

        attrs.add(new XmlAttribute(key, value));        // store key/value as attribute
    }

    return attrs;                                       // return list of attributes
}


public static void main(String[] args) {              // main ftor testing 

    String xml =
            "<user id=\"10\" active=\"yes\">\n" +
            "    <name>Ahmed</name>\n" +
            "    <post title=\"Hello\" likes=\"20\" />\n" +
            "    <bio>\n" +
            "         Software engineer.\n" +
            "    </bio>\n" +
            "</user>";

    XmlTokenizer tokenizer = new XmlTokenizer(xml);

    while (tokenizer.hasNext()) {
        XmlToken token = tokenizer.next();
        System.out.println(token);
    }
}


}

class XmlToken {
    
    // The 4 types of XML tokens
    public enum Type {
        OPENING_TAG,    // <tag ...>
        CLOSING_TAG,    // </tag>
        SELF_CLOSING_TAG, // <tag ... />
        TEXT            // plain text between tags
    }

    private Type type;
    private String tagName;               // tag name OR null for TEXT
    private List<XmlAttribute> attributes; // attributes only for opening/self-closing tags
    private String textContent;           // only for TEXT
    private int line;                     // line number for error reporting
    private int column;                   // column number for error reporting

    public XmlToken(Type type, String tagName, List<XmlAttribute> attributes, 
                    String textContent, int line, int column) {
        this.type = type;
        this.tagName = tagName;
        this.attributes = attributes;
        this.textContent = textContent;
        this.line = line;
        this.column = column;
    }

    // Getters
    public Type getType() { return type; }
    public String getTagName() { return tagName; }
    public List<XmlAttribute> getAttributes() { return attributes; }
    public String getTextContent() { return textContent; }
    public int getLine() { return line; }
    public int getColumn() { return column; }

    // Helper Functions
    public boolean isText() { return type == Type.TEXT; }
    public boolean isOpeningTag() { return type == Type.OPENING_TAG; }
    public boolean isClosingTag() { return type == Type.CLOSING_TAG; }
    public boolean isSelfClosingTag() { return type == Type.SELF_CLOSING_TAG; }

    @Override
    public String toString() {
        switch (type) {
            case TEXT:
                return "TEXT(\"" + textContent + "\") at line " + line;
            case OPENING_TAG:
                return "OPEN <" + tagName + "> attrs=" + attributes + " at line " + line;

            case CLOSING_TAG:
                return "CLOSE </" + tagName + "> at line " + line;
            case SELF_CLOSING_TAG:
                return "SELF <" + tagName + " /> attrs=" + attributes;
            default:
                return "UNKNOWN TOKEN at line " + line;
        }
    }
    
}




 