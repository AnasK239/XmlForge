package com.editor.xml.formatter;
import com.editor.structures.xml.XmlDocument;
public class XmlFormatter {
    //Prettify / indent the XML.

    public String format(XmlDocument doc){
        // Dummy implementation for demonstration purposes
        // In a real implementation, you would parse the XML and reformat it with proper indentation.
        return doc.toStringFormatted(); // Returning as-is for this dummy implementation.
    }

    public String format(String xml)
    {
        //Could call XmlParser.parse(xml) then pretty-print tree.
    }
}
