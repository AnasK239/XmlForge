package com.editor.xml.converter;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;
import com.editor.util.JsonBuilder;
public class XmlToJson {

    public String toJson(XmlDocument doc) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginObject();
        nodeToJson(doc.getRoot(), jsonBuilder);
        jsonBuilder.endObject();
        return jsonBuilder.build().trim();
    }
    private void nodeToJson(XmlNode node, JsonBuilder jsonBuilder) {
        if (node.getName() != null
                && node.getChildren().size() == 1
                && node.getChildren().get(0).getTextContent() != null) {
            if (isInteger(node.getChildren().get(0).getTextContent())) {
                int value = Integer.parseInt(node.getChildren().get(0).getTextContent());
                jsonBuilder.field(node.getName(), value);
            } else {
                jsonBuilder.field(node.getName(), node.getChildren().get(0).getTextContent());
            }
        }

        else {
            if (node.getName() != null) {
                jsonBuilder.fieldName(node.getName());
            }

            boolean allChildrenHaveSameName = true;
            String firstChildName = null;
            if (!node.getChildren().isEmpty()) {
                firstChildName = node.getChildren().get(0).getName();
                for (XmlNode child : node.getChildren()) {
                    if (!firstChildName.equals(child.getName())) {
                        allChildrenHaveSameName = false;
                        break;
                    }
                }
            }

            if (allChildrenHaveSameName && node.getChildren().size() > 1) {
                jsonBuilder.beginArray();
                for (XmlNode child : node.getChildren()) {
                    jsonBuilder.beginObject();
                    nodeToJson(child, jsonBuilder);
                    jsonBuilder.endObject();
                }
                jsonBuilder.endArray();
            }
            else {
                jsonBuilder.beginObject();
                for (XmlNode child : node.getChildren()) {
                    nodeToJson(child, jsonBuilder);
                }
                jsonBuilder.endObject();
            }
        }
    }
    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
