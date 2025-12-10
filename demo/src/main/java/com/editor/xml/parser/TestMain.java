package com.editor.xml.parser;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;

public class TestMain {

    public static void main(String[] args) {

        String xml = """

<users> 
        <user>
        <id>1</id>
        <name>Ahmed Ali</name>
        <posts>
            <post>
                <body>
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                </body>
                <topics>
                    <topic>
                        economy
                    </topic>
                    <topic>
                        finance
                    </topic>   
                </topics>
            </post>
            <post>
                <body>
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                </body>
                <topics>
                    <topic>
                        solar_energy
                    </topic>
                </topics>
            </post>
        </posts>
        </user>
</users>
""";

        // =============================
        // PARSE XML
        // =============================
        XmlParser parser = new XmlParser();
        XmlDocument document = parser.parse(xml);

        // =============================
        // PRINT PARSER ERRORS
        // =============================
        if (!parser.getErrors().isEmpty()) {
            System.out.println("==== ERRORS FOUND ====");
            for (int i = 0; i < parser.getErrors().size(); i++) {
                System.out.println("[Line " + parser.getErrorLineNumbers().get(i) + "] "
                        + parser.getErrors().get(i));
            }
        } else {
            System.out.println("No parser errors.");
        }

        // =============================
        // PRINT PARSED XML TREE
        // =============================
        System.out.println("\n==== PARSED XML TREE ====");
        printNode(document.getRoot(), 0);

        // =============================
        // BUILD FINAL XML STRING
        // =============================
//        String finalXml = nodeToString(document.getRoot(), 0);

        // =============================
        // VALIDATE FINAL XML
        // =============================
//        XmlValidator validator = new XmlValidator();
//        ValidationResult result = validator.validate(finalXml);
//
//        System.out.println("\n==== VALIDATION RESULT ====");
//        if (result.isValid) {
//            System.out.println("XML is valid!");
//        } else {
//            System.out.println("Validation Errors: " + result.errorCount);
//            for (int line : result.errorLines) {
//                System.out.println(" - Error at line: " + line);
//            }
//        }

        // =============================
        // PRINT FIXED XML
        // =============================
//        System.out.println("\n==== FINAL FIXED XML ====");
//        System.out.println(finalXml);
    }

    // =============================
    // PRINT TREE FOR DEBUG
    // =============================
    private static void printNode(XmlNode node, int indent) {
        if (node == null) return;

        String spaces = " ".repeat(indent);

        if (node.getName() != null) {
            System.out.println(spaces + "<" + node.getName() + ">");
        }

        if (node.getTextContent() != null && !node.getTextContent().trim().isEmpty()) {
            System.out.println(spaces + "  " + node.getTextContent().trim());
        }

        for (XmlNode child : node.getChildren()) {
            printNode(child, indent + 2);
        }

        if (node.getName() != null) {
            System.out.println(spaces + "</" + node.getName() + ">");
        }
    }

//    // =============================
//    // CONVERT TREE TO VALID XML STRING
//    // =============================
//    private static String nodeToString(XmlNode node, int indent) {
//        if (node == null || node.getName() == null) return "";
//
//        StringBuilder sb = new StringBuilder();
//        String spaces = " ".repeat(indent);
//
//        // Opening tag
//        sb.append(spaces).append("<").append(node.getName()).append(">\n");
//
//        // Text content
//        if (node.getTextContent() != null && !node.getTextContent().trim().isEmpty()) {
//            sb.append(spaces)
//                    .append("  ")
//                    .append(node.getTextContent().trim())
//                    .append("\n");
//        }
//
//        // Children
//        for (XmlNode child : node.getChildren()) {
//            sb.append(nodeToString(child, indent + 2));
//        }
//
//        // Closing tag
//        sb.append(spaces).append("</").append(node.getName()).append(">\n");
//
//        return sb.toString();
//    }
}
