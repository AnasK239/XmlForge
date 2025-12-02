package com.editor.util;

public class JsonBuilder {
    //Small helper to build JSON text manually (since we may avoid external libs).

    public JsonBuilder beginObject(){
        return this;
    }

    public JsonBuilder endObject()
    {
        return this;
    }

    public JsonBuilder beginArray()
    {
        return this;
    }

    public JsonBuilder endArray()
    {
        return this;
    }

    public JsonBuilder field(String name, String value)
    {
        return this;
    }

    public JsonBuilder field(String name, Number value)
    {
        return this;
    }

    public JsonBuilder field(String name, boolean value)
    {
        return this;
    }

    public String build()
    {
        return "";
    }

}
