package com.editor;

import com.editor.compression.Compressor;
import com.editor.io.FileManager;
import com.editor.structures.xml.XmlDocument;
import com.editor.xml.formatter.XmlMinifier;
import com.editor.xml.parser.XmlParser;
import com.editor.xml.converter.XmlToJson; 

public class TestCompressionMain {
    public static void main(String[] args) {

        Compressor bpe = new Compressor();
        String original = FileManager.readFile("demo\\src\\main\\resources\\samples\\sample.xml");
        // compress and compare as XML
        String compressed = bpe.compress(original);
        String decompressed = bpe.decompress(compressed);
        System.out.println("Original vs Decompressed differences:\n" + getDifferences(original, decompressed));
        System.out.println("Original size: " + original.length());
        System.out.println("Compressed size: " + compressed.length());

        // convert to Json then compress and compare
        XmlToJson converter = new XmlToJson();
        XmlParser parser = new XmlParser();
        XmlDocument doc = parser.parse(original);
        String json = converter.toJson(doc);
        String compressedJson = bpe.compressJson(json);
        String decompressedJson = bpe.decompressToJson(compressedJson);
        System.out.println("Original JSON vs Decompressed JSON differences:\n" + getDifferences(json, decompressedJson));   
        System.out.println("Original JSON size: " + json.length());
        System.out.println("Compressed JSON size: " + compressedJson.length());
        
    }


    // method to get the diffrences between two strings and output the first 10 
    public static String getDifferences(String str1, String str2) {
        StringBuilder diffs = new StringBuilder();
        int len = Math.min(str1.length(), str2.length());
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diffs.append("Position ").append(i).append(": '")
                        .append(str1.charAt(i)).append("' vs '")
                        .append(str2.charAt(i)).append("'\n");
                count++;
                if (count >= 10) {
                    diffs.append("...and more differences.\n");
                    break;
                }
            }
        }
        if (str1.length() != str2.length()) {
            diffs.append("Strings have different lengths: ")
                    .append(str1.length()).append(" vs ")
                    .append(str2.length()).append("\n");
        }
        return diffs.toString();
    }

}
