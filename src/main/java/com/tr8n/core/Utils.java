/*
 *  Copyright (c) 2014 Michael Berkovich, http://tr8nhub.com All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.tr8n.core;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

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
    public static String buildQueryString(Map params) throws Exception {
        StringBuilder sb = new StringBuilder();
        Iterator entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry e = (Map.Entry) entries.next();
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
    public static URL buildURL(String host, String path, Map params) throws Exception {
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
            Tr8n.getLogger().logException(pe);
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
            Tr8n.getLogger().logException(ex);
            return null;
        }
        return out.toString();
    }

    /**
     * Trims all values in a list of strings
     * @param original
     * @return
     */
    public static List<String> trimListValues(List original) {
        List trimmedList = new ArrayList<String>();
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
    public static Object getNestedMapValue(Map map, String key, String separator) {
        String[] parts = key.split(separator);

        for (int i=0; i<parts.length-1; i++) {
            String part = parts[i];
            Object obj = map.get(part);
            if (!(obj instanceof Map)) return null;
            map = (Map) obj;
        }

        return map.get(parts[parts.length-1]);
    }


    /**
     * Returns a value from a nested map using a dot separator
     * @param map
     * @param key
     * @return
     */
    public static Object getNestedMapValue(Map map, String key) {
        return getNestedMapValue(map, key, "\\.");
    }

    /**
     *
     * @param map
     * @param key
     * @param value
     * @param separator
     */
    public static void setNestedMapValue(Map map, String key, Object value, String separator) {
        String[] parts = key.split(separator);

        for (int i=0; i<parts.length-1; i++) {
            String part = parts[i];
            Map child = (Map) map.get(part);
            if (child == null) {
                map.put(part, new HashMap());
                child = (Map) map.get(part);
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
    public static void setNestedMapValue(Map map, String key, Object value) {
        setNestedMapValue(map, key, value, "\\.");
    }


    /**
     *
     * @param params
     * @param secret
     * @return
     */
    public static String signAndEncode(Map<String, Object> params, String secret) {
        try {
            Map<String, Object> encoded = new HashMap<String, Object>(params);
            encoded.put("algorithm", "HMAC-SHA256");
            encoded.put("ts", "" + (new Date()).getTime());
            String payload = buildJSON(encoded);
            payload = Base64.encodeBase64String(StringUtils.getBytesUtf8(payload));

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(StringUtils.getBytesUtf8(secret), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            String sig = Base64.encodeBase64String(sha256_HMAC.doFinal(StringUtils.getBytesUtf8(payload)));

            String singedPayload = Base64.encodeBase64String(StringUtils.getBytesUtf8(sig + "." + payload));
            return URLEncoder.encode(singedPayload, "UTF-8");
        } catch (Exception ex) {
            Tr8n.getLogger().logException(ex);
            return null;
        }
    }


    /**
     *
     * @param payload
     * @param secret
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Map<String, Object> decodeAndVerify(String payload, String secret) {
        try {
            String data = URLDecoder.decode(payload, "UTF-8");
            String signedRequest = StringUtils.newStringUtf8(Base64.decodeBase64(data));
            String parts[] = signedRequest.split("\\.");
            if (parts.length != 2) {
                Tr8n.getLogger().error("Invalid parameters");
                return null;
            }

            String jsonString = StringUtils.newStringUtf8(Base64.decodeBase64(parts[1]));
            Map<String, Object> params = (Map<String, Object>) parseJSON(jsonString);

            if (params.get("algorithm").equals("HMAC-SHA256")) {
                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                SecretKeySpec secretKey = new SecretKeySpec(StringUtils.getBytesUtf8(secret), "HmacSHA256");
                sha256_HMAC.init(secretKey);
                String expectedSig = Base64.encodeBase64String(sha256_HMAC.doFinal(StringUtils.getBytesUtf8(parts[1])));
                if (!expectedSig.trim().equals(parts[0].trim())) {
                    Tr8n.getLogger().error("Failed to verify signature");
                    return null;
                }
            } else {
                Tr8n.getLogger().error("Unsupported signature");
                return null;
            }

            params.remove("algorithm");
            params.remove("ts");
            return params;
        } catch (Exception ex) {
            Tr8n.getLogger().logException(ex);
            return null;
        }
    }

}
