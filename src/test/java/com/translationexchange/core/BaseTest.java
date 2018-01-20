/*
 * Copyright (c) 2018 Translation Exchange, Inc. All rights reserved.
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
 *
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

import com.translationexchange.core.Utils;

public class BaseTest {

  public static Application mockedApplication(String resourceFile) {
    HttpClient mockedHttpClient = mock(HttpClient.class);
    Application app = spy(new Application(loadJSONMap("/" + resourceFile)));
    when(app.getHttpClient()).thenReturn(mockedHttpClient);
    return app;
  }

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
      System.out.println("Failed to load a resource: " + resourceName);
//        	e.printStackTrace();
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
    } catch (java.text.ParseException ex) {
      return null;
    }
  }

  public static Date parseTime(String value) {
    try {
      return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(value);
    } catch (java.text.ParseException ex) {
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
  public static Map<String, Object> loadJsonMapFromString(String jsonText) {
    return (Map<String, Object>) Utils.parseJSON(jsonText);
  }

  @SuppressWarnings("unchecked")
  public static List<Object> loadJsonListFromString(String jsonText) {
    return (List<Object>) Utils.parseJSON(jsonText);
  }
}



