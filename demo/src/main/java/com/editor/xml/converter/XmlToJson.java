package com.editor.xml.converter;
import com.editor.structures.graph.UserNode;
import com.editor.structures.xml.XmlDocument;
import com.editor.structures.xml.XmlNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlToJson {
    public String toJson(List users) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(users);
        }
        catch (Exception e) {e.printStackTrace();}
        return null;
    }
    private String nodeToJson(UserNode user) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(user);
        }
        catch (Exception e) {e.printStackTrace();}
        return null;}
}
