package com.editor.xml.converter;

import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;
import com.editor.util.JsonParser;

import java.util.List;
import java.util.Map;

public class JsonToXml {

    public XmlDocument toXmlDocument(String jsonString) {
        JsonParser parser = new JsonParser();
        Object rootJson = parser.parse(jsonString);

        XmlDocument doc = new XmlDocument();

        if (rootJson instanceof Map) {
            Map<String, Object> rootMap = (Map<String, Object>) rootJson;

            if (rootMap.isEmpty()) {
                return doc;
            }

            String rootName = rootMap.keySet().iterator().next(); // users
            Object rootContent = rootMap.get(rootName);

            XmlNode rootNode = new XmlNode(rootName, null);

            constructXmlTree(rootNode, rootContent);

            doc.setRoot(rootNode);

        } else {
            throw new IllegalArgumentException("JSON root must be a Map Object to be converted to XML.");
        }

        return doc;
    }

    private void constructXmlTree(XmlNode parent, Object content) {
        if (content == null)
            return;

        if (content instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) content;

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String tagName = entry.getKey();
                Object tagValue = entry.getValue();

                if (tagValue instanceof List) {
                    XmlNode listContainer = new XmlNode(tagName, null);
                    parent.addChild(listContainer);

                    List<?> list = (List<?>) tagValue;
                    for (Object item : list) {
                        constructXmlTree(listContainer, item);
                    }
                } else {
                    XmlNode childNode = new XmlNode(tagName, null);
                    constructXmlTree(childNode, tagValue);
                    parent.addChild(childNode);
                }
            }

        } else if (content instanceof List) {
            for (Object item : (List<?>) content) {
                constructXmlTree(parent, item);
            }

        } else {
            String textValue = String.valueOf(content);
            XmlNode textNode = new XmlNode(null, textValue);
            parent.addChild(textNode);
        }
    }
}