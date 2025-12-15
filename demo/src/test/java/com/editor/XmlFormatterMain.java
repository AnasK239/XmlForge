package com.editor;

import com.editor.io.FileManager;
import com.editor.structures.xml.*;
import com.editor.xml.formatter.XmlFormatter;
import com.editor.xml.formatter.XmlMinifier;
import com.editor.xml.parser.XmlParser;

public class XmlFormatterMain {


    public static void main(String[] args) {

        XmlFormatter formatter = new XmlFormatter();
        XmlMinifier minifier = new XmlMinifier();

        String xml = FileManager.readFile("demo/src/test/resources/sample.xml");

        XmlParser parser = new XmlParser();
        XmlDocument doc2 = parser.parse(xml);

        System.out.println(formatter.format(doc2));
        System.out.println(minifier.minify(doc2));
    }
}