package com.editor;


import com.editor.xml.parser.XmlParser;
import com.editor.xml.parser.XmlToken;
import com.editor.xml.parser.XmlTokenizer;
import com.editor.xml.parser.XmlValidator;

public class TestMain {

    public static void main(String[] args) {

        XmlValidator validator = new XmlValidator();

        // ----------------------------
        // Helper for structured printing
        // ----------------------------
        Runnable separator = () -> System.out.println("\n----------------------------------------\n");

        // -------------------------------------------------
        // TOKENIZER TEST — see raw tokens
        // -------------------------------------------------
        System.out.println("===== TOKENIZER TEST =====");
        String tokTest = "<root><a>hello</a><b/></root>";
        XmlTokenizer t = new XmlTokenizer(tokTest);
        while (t.hasNext()) {
            XmlToken tok = t.next();
            System.out.println(tok);
        }
        separator.run();

        // -------------------------------------------------
        // PARSER TESTS
        // -------------------------------------------------
        System.out.println("===== PARSER TEST 1: Missing Closing Tag =====");
        parseOnly("<root><a></root>");
        separator.run();

        System.out.println("===== PARSER TEST 2: Nested Incorrect Closing =====");
        parseOnly("<a><b></c></b></a>");
        separator.run();

        System.out.println("===== PARSER TEST 3: Text Before Root =====");
        parseOnly("hello <a></a>");
        separator.run();

        System.out.println("===== PARSER TEST 4: Unexpected '<' =====");
        parseOnly("<root <name>></root>");
        separator.run();

        System.out.println("===== PARSER TEST 5: Self Closing =====");
        parseOnly("<root><a/><b/><c/></root>");
        separator.run();

        System.out.println("===== PARSER TEST 6: Multi-root =====");
        parseOnly("<a></a><b></b>");
        separator.run();


        // -------------------------------------------------
        // VALIDATOR TESTS (same ones you used before)
        // -------------------------------------------------
        System.out.println("===== VALIDATOR TESTS =====");

        testValidation("Missing Closing Tag", "<root><a></root>");

        testValidation("Mismatched Tag", "<name></nam>");

        testValidation("Closing Tag Without Opening", "</a>");

        testValidation("Text Before Root", "hello <root></root>");

        testValidation("Text After Root", "<root></root>hello");

        testValidation("Unexpected '<' inside tag", "<root <name>></root>");

        testValidation("Unexpected '>' in text", "<root>hello>world</root>");

        testValidation("VALID XML", "<root><a>hi</a></root>");

        testValidation("Very Deep Nesting", "<a><b><c><d></d></c></b></a>");

        testValidation("Missing Many Closing Tags", "<root><a><b><c><d>");

        testValidation("Random Bad Characters in Tag", "<a$#></a>");

        testValidation("Repeated Unexpected '<'", "<a<<<b></b></a>");

        testValidation("Tags With Large Whitespace", "<   a   ><   b   ></   b   ></   a   >");

        testValidation("Text With Many Special Characters", "<root>>>>><</root>");

        testValidation("Multi-root", "<a></a><b></b>");

        testValidation("Extremely Long Text Node", "<root>" + "x".repeat(2000) + "</root>");

        testValidation("Nested Incorrect Closing Tags", "<a><b><c></b></c></a>");

        testValidation("Lone Text Without Brackets", "hello");

        testValidation("Tag With Space Before Name", "<   tag></tag>");

        testValidation("Tag With Space After Name", "<tag   ></tag>");

        testValidation("Self-Closing With Spaces", "<tag   />");

        testValidation("Incorrect Self-Closing Patterns", "<tag/ / ></tag>");

        testValidation("Garbage Mixed XML", "<a><b></c>><<<</a>");

        separator.run();


        // -------------------------------------------------
        // FIXER TESTS
        // -------------------------------------------------
        System.out.println("===== FIXER TESTS =====");

        System.out.println("Fix Leading Text:");
        printFix("hello <a></a>");

        System.out.println("Fix Multi-root:");
        printFix("<a></a><b></b>");

        System.out.println("Fix Missing Closing Tags:");
        printFix("<root><a><b><c>");

        System.out.println("Fix Combined Broken XML:");
        printFix("xxx <a><b>hello</root>");

        System.out.println("\n===== ALL TESTS COMPLETE =====");
    }

    // --------------------------
    // Parser only test function
    // --------------------------
    private static void parseOnly(String xml) {
        XmlParser p = new XmlParser();
        p.parse(xml);

        if (p.getErrors().isEmpty()) {
            System.out.println("Parser: NO ERRORS");
            return;
        }

        for (int i = 0; i < p.getErrors().size(); i++) {
            System.out.println("Line " + p.getErrorLineNumbers().get(i) +
                               ": " + p.getErrors().get(i));
        }
    }

    // --------------------------
    // Validator test helper
    // --------------------------
    private static void testValidation(String title, String xml) {
        System.out.println("\n===== TEST: " + title + " =====");
        XmlValidator validator = new XmlValidator();
        validator.validate(xml);
    }

    // --------------------------
    // Fixer output helper
    // --------------------------
    private static void printFix(String xml) {
        XmlValidator v = new XmlValidator();
        System.out.println("Fixed XML:");
        System.out.println(v.fix(xml));
        System.out.println();
    }
}
