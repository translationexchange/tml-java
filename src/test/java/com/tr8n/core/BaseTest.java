package com.tr8n.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Date;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BaseTest {

    public static String readFile(String filename) {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String loadResource(String resourceName) {
        String content = null;
        try {
            InputStream inputStream = BaseTest.class.getResourceAsStream(resourceName);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            content = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return content;
    }

    public static Object loadJSON(String resourceName) {
        String jsonText = loadResource(resourceName);
        if (jsonText == null) return null;
        return Utils.parseJSON(jsonText);
    }


    public static Date parseDate(String value) {
        try {
            return (new SimpleDateFormat("yyyy-MM-dd")).parse(value);
        } catch (java.text.ParseException ex){
            return null;
        }
    }

    public static Date parseTime(String value) {
        try {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(value);
        } catch (java.text.ParseException ex){
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadJSONMap(String resourceName) {
        return (Map<String, Object>) loadJSON(resourceName);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> loadJSONList(String resourceName) {
        return (List<Object>) loadJSON(resourceName);
    }

    @SuppressWarnings("unchecked")
    public static Map loadJsonMapFromString(String jsonText) {
        return (Map) Utils.parseJSON(jsonText);
    }

    @SuppressWarnings("unchecked")
    public static List loadJsonListFromString(String jsonText) {
        return (List) Utils.parseJSON(jsonText);
    }
}



