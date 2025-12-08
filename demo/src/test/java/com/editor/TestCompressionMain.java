package com.editor;

import com.editor.compression.Compressor;
import com.editor.io.FileManager;
import com.editor.structures.xml.XmlDocument;
import com.editor.xml.formatter.XmlMinifier;
import com.editor.xml.parser.XmlParser;

public class TestCompressionMain {
    public static void main(String[] args) {

        Compressor bpe = new Compressor();
        String original = FileManager.readFile("demo\\src\\main\\resources\\samples\\sample.xml");

        XmlParser parser = new XmlParser();
        XmlDocument document = parser.parse(original);
        XmlMinifier minifier = new XmlMinifier();
        String minified_in = minifier.minify(document);
        // System.err.println(minified);
        String compressed = bpe.compress(document);
        // System.out.println("Compressed:" + compressed);
        String decompressed = bpe.decompress(compressed);
        System.out.println("Decompressed:" + decompressed);
        String minified_out = minifier.minify(decompressed);
        System.out.println("Match_min: " + minified_in.equals(minified_out));
        System.out.println("Match: " + original.equals(decompressed));
        // output the decompressed XML to a file
        FileManager.writeFile("demo\\src\\main\\resources\\samples\\decompressed.xml", decompressed);
        // compare sizes
        System.out.println("Original size: " + original.length());
        System.out.println("Compressed size: " + compressed.length());

        System.out.println("Differences: \n" + getDifferences(original, decompressed));
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
