package com.editor.compression;

import com.editor.structures.xml.XmlDocument;
import com.editor.xml.formatter.XmlFormatter;
import com.editor.xml.formatter.XmlMinifier;
import com.editor.xml.parser.XmlParser;

public class Compressor {
    //Role: API for compression.
    public String compress(XmlDocument input){
        XmlMinifier minifier = new XmlMinifier();
        String minifiedXml = minifier.minify(input);
        String bpeCompressed = new BpeCompressor().compress(minifiedXml);
        return bpeCompressed;
    }

    public String compress(String input){
        XmlMinifier minifier = new XmlMinifier();
        String minifiedXml = minifier.minify(input);
        String bpeCompressed = new BpeCompressor().compress(minifiedXml);
        return bpeCompressed;
    }

    public String decompress(String compressed){
        String bpeDecompressed = new BpeCompressor().decompress(compressed);
        XmlParser parser = new XmlParser();
        XmlDocument doc = parser.parse(bpeDecompressed);
        XmlFormatter formatter = new XmlFormatter();
        return formatter.format(doc);
    }
}
