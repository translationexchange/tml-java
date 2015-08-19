/**
 * Copyright (c) 2015 Translation Exchange, Inc. All rights reserved.
 *
 *  _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 *    | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 *    | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 *    | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 *    |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 *                                                                                        __/ |
 *                                                                                       |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.translationexchange.core;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utils {

    /**
     * Checks if the object is an integer or a real number
     * @param obj
     * @return
     */
    public static boolean isNumeric(Object obj) {
        return obj.toString().matches("[+-]?\\d*(\\.\\d+)?");
    }

    /**
     * Checks if the object is an integer
     * @param obj
     * @return
     */
    public static boolean isInteger(Object obj) {
        return obj.toString().matches("[+-]?\\d*?");
    }

    /**
     * Parses integer
     * @param obj
     * @return
     */
    public static Integer parseInt(Object obj) {
        return Integer.parseInt("" + obj);
    }

    /**
     * Parses a double
     * @param obj
     * @return
     */
    public static Double parseDouble(Object obj) {
        return Double.parseDouble("" + obj);
    }

    /**
     * Creates a regular expression from a string pattern
     * @param str
     * @return
     */
    public static Pattern parsePattern(String str) {
        String regex = str;
        if (regex.startsWith("/"))
            regex = regex.substring(1, regex.length());
        if (regex.endsWith("/i"))
            regex = regex.substring(0, regex.length() - 2);
        else if (regex.endsWith("/"))
            regex = regex.substring(0, regex.length() - 1);

        return Pattern.compile(regex);
    }

    /**
     * Creates a query string out of parameters map
     * @param params
     * @return
     */
    public static String buildQueryString(Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> e = entries.next();
            if(sb.length() > 0) {
                sb.append('&');
            }
            sb.append(URLEncoder.encode((String) e.getKey(), "UTF-8")).append('=').append(URLEncoder.encode((String) e.getValue(), "UTF-8"));
        }
        return sb.toString();
    }

    /**
     *
     * @param path
     * @return
     * @throws MalformedURLException
     */
    public static URL buildURL(String host, String path, Map<String, Object> params) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(host);
        if (!host.endsWith("/") && !path.startsWith("/"))
            url.append("/");
        url.append(path);

        if (params != null && params.keySet().size() > 0) {
            url.append("?");
            url.append(buildQueryString(params));
        }
        return new URL(url.toString());
    }
   
    /**
     * Builds a URL
     * @param host
     * @param path
     * @return
     * @throws MalformedURLException
     */
    public static URL buildURL(String host, String path) throws Exception {
        return buildURL(host, path, null);
    }

    /**
     * Builds a map out of parameters
     * @param data
     * @return
     */
    public static Map<String, Object> buildMap(Object... data) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        if(data.length % 2 != 0)
            throw new IllegalArgumentException("Odd number of arguments");

        String key = null;
        Integer step = -1;

        for(Object value : data){
            step++;
            switch(step % 2){
                case 0:
                    if(value == null)
                        throw new IllegalArgumentException("Null key value");
                    key = (String) value;
                    continue;
                case 1:
                    result.put(key, value);
                    break;
            }
        }

        return result;
    }

    /**
     * Extends a map
     * 
     * @param original
     * @param data
     * @return
     */
    public static Map<String, Object> extendMap(Map<String, Object> original, Object... data) {
    	Map<String, Object> map = new HashMap<String, Object>(original);
    	map.putAll(buildMap(data));
    	return map;
    }
    
    /**
     * Builds a map out of parameters
     * 
     * For instance:
     * 
     * buildStringMap(
     *     "name", "Peter",
     *     "age", "35"
     * )
     * 
     * @param data
     * @return
     */
    public static Map<String, String> buildStringMap(String... data) {
        HashMap<String, String> result = new HashMap<String, String>();

        if(data.length % 2 != 0)
            throw new IllegalArgumentException("Odd number of arguments");

        String key = null;
        Integer step = -1;

        for(String value : data){
            step++;
            switch(step % 2){
                case 0:
                    if(value == null)
                        throw new IllegalArgumentException("Null key value");
                    key = (String) value;
                    continue;
                case 1:
                    result.put(key, value);
                    break;
            }
        }

        return result;
    }
    

    /**
     * Builds a list from parameters
     * @param data
     * @return
     */
    public static List<Object> buildList(Object... data) {
        List<Object> result = new ArrayList<Object>();

        for(Object value : data) {
            result.add(value);
        }

        return result;
    }

    /**
     * Builds a list of Strings
     * @param data
     * @return
     */
    public static List<String> buildStringList(String... data) {
        List<String> result = new ArrayList<String>();

        for(String value : data) {
            result.add(value);
        }

        return result;
    }

    /**
     * Parsing JSON from string
     * @param jsonText
     * @return
     */
    public static Object parseJSON(String jsonText) {
        if (jsonText == null) return null;

        JSONParser p = new JSONParser();
        Object obj = null;
        try {
            obj = p.parse(jsonText);
        } catch(ParseException pe){
            Tml.getLogger().logException(pe);
            return null;
        }

        return obj;
    }

    /**
     * Builds json from an object
     *
     * @param object
     * @return
     */
    public static String buildJSON(Object object) {
        if (object == null) return null;

        StringWriter out = new StringWriter();
        try {
            JSONValue.writeJSONString(object, out);
        } catch(IOException ex){
            Tml.getLogger().logException(ex);
            return null;
        }
        return out.toString();
    }

    /**
     * Trims all values in a list of strings
     * @param original
     * @return
     */
    public static List<String> trimListValues(List<String> original) {
        List<String> trimmedList = new ArrayList<String>();
        for (int i = 0; i < original.size(); i++)
            trimmedList.add((original.get(i).toString()).trim());
        return trimmedList;
    }


    /**
     * Returns a value from a nested map using a specific separator
     * @param map
     * @param key
     * @param separator
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Object getNestedMapValue(Map<String, Object> map, String key, String separator) {
        String[] parts = key.split(separator);

        for (int i=0; i<parts.length-1; i++) {
            String part = parts[i];
            Object obj = map.get(part);
            if (!(obj instanceof Map)) return null;
            map = (Map<String, Object>) obj;
        }

        return map.get(parts[parts.length-1]);
    }


    /**
     * Returns a value from a nested map using a dot separator
     * @param map
     * @param key
     * @return
     */
    public static Object getNestedMapValue(Map<String, Object> map, String key) {
        return getNestedMapValue(map, key, "\\.");
    }

    /**
     *
     * @param map
     * @param key
     * @param value
     * @param separator
     */
    @SuppressWarnings("unchecked")
	public static void setNestedMapValue(Map<String, Object> map, String key, Object value, String separator) {
        String[] parts = key.split(separator);

        for (int i=0; i<parts.length-1; i++) {
            String part = parts[i];
            Map<String, Object> child = (Map<String, Object>) map.get(part);
            if (child == null) {
                map.put(part, new HashMap<String, Object>());
                child = (Map<String, Object>) map.get(part);
            }
            map = child;
        }

        map.put(parts[parts.length-1], value);
    }

    /**
     *
     * @param map
     * @param key
     * @param value
     */
    public static void setNestedMapValue(Map<String, Object> map, String key, Object value) {
        setNestedMapValue(map, key, value, "\\.");
    }

    /**
     * 
     * @param objects
     * @param joiner
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static String join(List objects, String joiner) {
    	return join(objects.toArray(), joiner);
    }
    
    /**
     * 
     * @param objects
     * @param joiner
     * @return
     */
    public static String join(Object[] objects, String joiner) {
    	if (objects.length == 1)
    		return objects[0].toString();
    	
    	StringBuilder sb = new StringBuilder();
    	for (int i=0; i<objects.length; i++) {
    		sb.append(objects[i].toString());
    		if (i<objects.length-1) sb.append(joiner);
    	}
    	return sb.toString();
    }
}