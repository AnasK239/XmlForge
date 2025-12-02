package com.editor.util;

import java.util.Stack;

public class JsonBuilder {
    private final StringBuilder sb = new StringBuilder();
    private final Stack<Character> context = new Stack<>();
    private boolean needComma = false;
    private int indentLevel = 0;
    private final int indentStep = 4;

    private void appendIndent() {
        for (int i = 0; i < indentLevel * indentStep; i++) {
            sb.append(' ');
        }
    }

    private void ifNeedComma() {
        if (needComma) {
            sb.append(",");
        }
    }

    public JsonBuilder fieldName(String name) {
        ifNeedComma();
        sb.append('\n');
        appendIndent();
        sb.append('"').append(name).append('"').append(" : ");
        needComma = false;
        return this;
    }

    public JsonBuilder beginObject() {
        ifNeedComma();
        sb.append('\n');
        appendIndent();
        sb.append("{");
        context.push('}');
        indentLevel++;
        needComma = false;
        return this;
    }

    public JsonBuilder endObject() {
        if (!context.isEmpty() && context.peek() == '}') {
            context.pop();
            indentLevel--;
            sb.append('\n');
            appendIndent();
            sb.append('}');
            needComma = true;
        } else {
            System.out.println("Error in end object");
        }
        return this;
    }

    public JsonBuilder beginArray() {
        ifNeedComma();
        sb.append('\n');
        appendIndent();
        sb.append("[");
        context.push(']');
        indentLevel++;
        needComma = false;
        return this;
    }

    public JsonBuilder endArray() {
        if (!context.isEmpty() && context.peek() == ']') {
            context.pop();
            indentLevel--;
            sb.append('\n');
            appendIndent();
            sb.append(']');
            needComma = true;
        } else {
            System.out.println("Error array isn't closed");
        }
        return this;
    }

    public JsonBuilder field(String name, String value) {
        ifNeedComma();
        sb.append('\n');
        appendIndent();
        sb.append('"').append(name).append('"').append(" : ").append('"').append(value).append('"');
        needComma = true;
        return this;
    }

    public JsonBuilder field(String name, int value) {
        ifNeedComma();
        sb.append('\n');
        appendIndent();
        sb.append('"').append(name).append('"').append(" : ").append(value);
        needComma = true;
        return this;
    }

    public String build() {
        if (!context.isEmpty()) {
            System.out.println("Error in build -> stack isn't empty");
            return null;
        }
        return sb.toString();
    }
}
