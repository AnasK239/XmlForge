package com.editor.xml.formatter;
/*
According to Level 1:
Formatting (Prettifying) the XML means:
-Add proper indentation for nested tags
-Ensure each tag starts on a new line
-Remove excessive whitespace
-Preserve content between tags
-Use indentation based on nesting level (commonly 2 or 4 spaces)
--- DISCLAIMER
The formatter does NOT validate XML — that’s the job of XmlValidator.
The formatter does NOT parse XML into XmlDocument — that’s XmlParser.
Formatter is purely string → formatted string.
 */
public class XmlFormatter {

    private static final String INDENT = "    "; // 4 spaces

    public String format(String xml) {
        if (xml == null || xml.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder(); //builds the final formatted XML
        StringBuilder token = new StringBuilder(); //buffer to collects characters until a tag is complete

        int level = 0; // indentation depth
        boolean insideTag = false;  // Flag

        for (int i = 0; i < xml.length(); i++) {
            char c = xml.charAt(i);

            // Start of a tag <...>
            if (c == '<') {
                // Flush previous text content if any
                String text = token.toString().trim();
                if (!text.isEmpty()) {
                    result.append(indent(level)).append(text).append("\n");
                }
                token.setLength(0);
                insideTag = true;
                token.append(c);
            }
            // End of tag >
            else if (c == '>') {
                token.append(c);
                insideTag = false;
                String tag = token.toString();
                // Closing tag </...>
                if (tag.startsWith("</")) {
                    level--;
                    result.append(indent(level)).append(tag).append("\n");
                }
                // Self-closing tag <.../>
                else if (tag.endsWith("/>")) {
                    result.append(indent(level)).append(tag).append("\n");
                }
                // Opening tag <...>
                else {
                    result.append(indent(level)).append(tag).append("\n");
                    level++;
                }
                token.setLength(0);
            }
            // Normal characters (text content)
            else {
                token.append(c);
            }
        }
        return result.toString().trim();
    }

    private String indent(int level) {
        return INDENT.repeat(Math.max(0, level));
    }
}
// ------------- How it works
/*
✔ Uses simple tokenization based on < and >

No need for full parser because formatting is purely structural.

-- Tracks indentation level
level++ after opening tag <tag>
level-- before printing closing tag </tag>
-- Handles:
<tag>
</tag>
<tag/>
Text nodes: Hello world

-- Uses a buffer token to collect text until we hit < or >.

 *Formatter is a pure utility.*
 The pipeline:
  FileManager → XmlTokenizer → XmlValidator → XmlParser →
  XmlDocument → XmlFormatter → GUI/CLI Output

 */