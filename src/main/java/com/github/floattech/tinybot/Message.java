package com.github.floattech.tinybot;

import java.util.HashMap;

public class Message {

    public HashMap<String, Object> text(String text) {
        HashMap<String, Object> elem = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        data.put("text", text);
        elem.put("type", "text");
        elem.put("data", data);
        return elem;
    }

    public HashMap<String, Object> image(String file) {
        HashMap<String, Object> elem = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        data.put("file", file);
        elem.put("type", "image");
        elem.put("data", data);
        return elem;
    }

    public HashMap<String, Object> record(String file) {
        HashMap<String, Object> elem = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        data.put("file", file);
        elem.put("type", "record");
        elem.put("data", data);
        return elem;
    }

    public HashMap<String, Object> at(Integer qq) {
        HashMap<String, Object> elem = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        data.put("qq", qq);
        elem.put("type", "at");
        elem.put("data", data);
        return elem;
    }

    public HashMap<String, Object> xml(String str) {
        HashMap<String, Object> elem = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        data.put("data", str);
        elem.put("type", "xml");
        elem.put("data", data);
        return elem;
    }

    public HashMap<String, Object> json(String str) {
        HashMap<String, Object> elem = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        data.put("data", str);
        elem.put("type", "json");
        elem.put("data", data);
        return elem;
    }

    public HashMap<String, Object> music(String type, String id) {
        HashMap<String, Object> elem = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("id", id);
        elem.put("type", "music");
        elem.put("data", data);
        return elem;
    }
    
}
