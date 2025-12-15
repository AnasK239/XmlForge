package com.editor;
import com.editor.io.FileManager;
import com.editor.structures.xml.XmlDocument;
import com.editor.xml.converter.JsonToXml;
import com.editor.xml.converter.XmlToJson;
import com.editor.xml.formatter.XmlFormatter;
import com.editor.xml.parser.XmlParser;

public class ConversionTest {
    
    public static void main(String[] args) {
        String original = FileManager.readFile("demo\\src\\main\\resources\\samples\\sample.xml");
        XmlParser parser = new XmlParser();
        XmlDocument document = parser.parse(original);

        XmlToJson converter = new XmlToJson();
        String json = converter.toJson(document);
        FileManager.writeFile("demo\\src\\main\\resources\\samples\\output.json", json);

        JsonToXml jsonToXml = new JsonToXml();
        XmlDocument convertedDoc = jsonToXml.toXmlDocument(json);
        XmlFormatter formatter = new XmlFormatter();
        String convertedXml = formatter.format(convertedDoc);
        FileManager.writeFile("demo\\src\\main\\resources\\samples\\converted.xml", convertedXml);

        // compare original and converted XML
        if (original.trim().equals(convertedXml.trim())) {
            System.out.println("Conversion successful, XML matches.");
        } else {
            System.out.println("Conversion failed, XML does not match.");
            System.out.println("Differences: \n" + getDifferences(original, convertedXml));
        }

    }

    
    private static String getDifferences(String str1, String str2) {
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
