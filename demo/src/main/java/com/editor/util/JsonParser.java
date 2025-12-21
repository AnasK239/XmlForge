package com.editor.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    private String json;
    private int index;
    private int length;

    public Object parse(String jsonString) {
        if (jsonString == null)
            return null;
        this.json = jsonString.trim();
        this.index = 0;
        this.length = json.length();
        return parseValue();
    }

    private Object parseValue() {
        skipWhiteSpace();
        if (index >= length)
            return null;

        char c = json.charAt(index);

        if (c == '{')
            return parseObject();
        if (c == '[')
            return parseArray();
        if (c == '"')
            return parseString();
        if (c == 't') {
            consume("true");
            return true;
        }
        if (c == 'f') {
            consume("false");
            return false;
        }
        if (c == 'n') {
            consume("null");
            return null;
        }
        if (Character.isDigit(c) || c == '-')
            return parseNumber();

        throw new RuntimeException("Invalid JSON at index " + index + ": " + c);
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        consumeChar('{');
        skipWhiteSpace();
        if (peek() == '}') {
            consumeChar('}');
            return map;
        }

        while (true) {
            skipWhiteSpace();
            String key = parseString();
            skipWhiteSpace();
            consumeChar(':');
            Object value = parseValue();
            map.put(key, value);

            skipWhiteSpace();
            if (peek() == '}') {
                consumeChar('}');
                break;
            }
            consumeChar(',');
        }
        return map;
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        consumeChar('[');
        skipWhiteSpace();
        if (peek() == ']') {
            consumeChar(']');
            return list;
        }

        while (true) {
            list.add(parseValue());
            skipWhiteSpace();
            if (peek() == ']') {
                consumeChar(']');
                break;
            }
            consumeChar(',');
        }
        return list;
    }

    private String parseString() {
        consumeChar('"');
        StringBuilder sb = new StringBuilder();
        while (index < length) {
            char c = json.charAt(index++);
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\') {
                char escaped = json.charAt(index++);
                switch (escaped) {
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '/':
                        sb.append('/');
                        break;
                    case 'b':
                        sb.append('\b');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    default:
                        sb.append(escaped);
                        break;
                }
            } else {
                sb.append(c);
            }
        }
        throw new RuntimeException("Unterminated string");
    }

    private Number parseNumber() {
        int start = index;
        if (peek() == '-')
            index++;
        while (index < length && Character.isDigit(json.charAt(index)))
            index++;
        if (index < length && json.charAt(index) == '.') {
            index++;
            while (index < length && Character.isDigit(json.charAt(index)))
                index++;
            return Double.parseDouble(json.substring(start, index));
        }
        return Integer.parseInt(json.substring(start, index));
    }

    private void skipWhiteSpace() {
        while (index < length && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
    }

    private void consumeChar(char expected) {
        skipWhiteSpace();
        if (index >= length || json.charAt(index) != expected) {
            throw new RuntimeException("Expected '" + expected + "' at " + index);
        }
        index++;
    }

    private void consume(String expected) {
        if (json.startsWith(expected, index)) {
            index += expected.length();
        } else {
            throw new RuntimeException("Expected " + expected + " at " + index);
        }
    }

    private char peek() {
        if (index < length)
            return json.charAt(index);
        return 0;
    }
}