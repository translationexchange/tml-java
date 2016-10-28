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

import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michael on 3/10/14.
 */
public class UtilsTest extends BaseTest {

    @Test
    public void testBuildingMap() {
        Map<String, Object> expectation = new HashMap<String, Object>();
        expectation.put("name", "Michael");
        expectation.put("gender", "male");

        Map<String, Object> result = Utils.map(
                "name", "Michael",
                "gender", "male"
        );

        Assert.assertEquals(
                expectation,
                result
        );
    }

    @Test
    public void testExtendMap() {
        Map<String, Object> expectation = new HashMap<String, Object>();
        expectation.put("first_name", "Michael");
        expectation.put("last_name", "Berk");
        
        Assert.assertEquals(
	        expectation,
	        Utils.map(
	            "first_name", "Michael",
	            "last_name", "Berk"
	        )
        );
        
        expectation.put("gender", "male");

        Assert.assertEquals(
    	        expectation,
    	        Utils.extendMap(Utils.map(
    	            "first_name", "Michael",
    	            "last_name", "Berk"
    	        ), "gender", "male")
            );
    }    
    
    @Test
    public void testBuildingStringMap() {
        Map<String, Object> expectation = new HashMap<String, Object>();
        expectation.put("name", "Michael");
        expectation.put("gender", "male");

        Map<String, String> result = Utils.buildStringMap(
                "name", "Michael",
                "gender", "male"
        );

        Assert.assertEquals(
                expectation,
                result
        );
    }
    
    @Test
    public void testBuildingList() {
        List<Object> expectation = new ArrayList<Object>();
        expectation.add("Michael");
        expectation.add("male");

        List<Object> result = Utils.buildList(
                "Michael", "male"
        );

        Assert.assertEquals(
                expectation,
                result
        );
    }


    @Test
    public void testBuildingQueryString() throws Exception {
        String query = Utils.buildQueryString(Utils.map("name", "Michael", "gender", "male"));

        Assert.assertEquals(
                "gender=male&name=Michael",
                query
        );

        query = Utils.buildQueryString(Utils.map("name", "John Peterson", "gender", "male"));

        Assert.assertEquals(
                "gender=male&name=John+Peterson",
                query
        );

    }

    @Test
    public void testBuildingUrl() throws Exception {
        URL url = Utils.buildURL("http://google.com", "/search", Utils.map("q", "test"));

        Assert.assertEquals(
                "http://google.com/search?q=test",
                url.toString()
        );

        url = Utils.buildURL("http://google.com", "/search");

        Assert.assertEquals(
                "http://google.com/search",
                url.toString()
        );

        url = Utils.buildURL("http://google.com", "search");

        Assert.assertEquals(
                "http://google.com/search",
                url.toString()
        );

    }

    @Test
    public void testSetNestedMapValues() {
        Map<String, Object> root = new HashMap<String, Object>();

        Utils.setNestedMapValue(root, "a.b.c", "hello");

        Assert.assertEquals(
                "hello",
                Utils.getNestedMapValue(root, "a.b.c")
        );
    }

    @Test
    public void testParseJson() {
        Assert.assertEquals(
                null,
                Utils.parseJSON(null)
        );

        Assert.assertEquals(
                null,
                Utils.parseJSON("{'d'}")
        );

        Assert.assertEquals(
                Utils.map("name", "Michael"),
                Utils.parseJSON("{\"name\": \"Michael\"}")
        );
    }

    @Test
    public void testBuildJson() {
        Assert.assertEquals(
                null,
                Utils.buildJSON(null)
        );

        Assert.assertEquals(
                "{\"name\":\"Michael\"}",
                Utils.buildJSON(Utils.map("name", "Michael"))
        );
    }


    @Test(expected=IllegalArgumentException.class)
    public void testBuildMapWithOneArgumentFails() {
        Assert.assertEquals(
                null,
                Utils.map("a")
        );
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBuildStringMapWithOneArgumentFails() {
        Assert.assertEquals(
                null,
                Utils.buildStringMap("a")
        );
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBuildMapWithNullValueFails() {
        Assert.assertEquals(
                null,
                Utils.map("a", "b", null, "c")
        );
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBuildStringMapWithNullValueFails() {
        Assert.assertEquals(
                null,
                Utils.buildStringMap("a", "b", null, "c")
        );
    }
    
    @Test
    public void testBuildMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "b");

        Assert.assertEquals(
                map,
                Utils.map("a", "b")
        );
    }

    @Test
    public void testJoin() {
        Assert.assertEquals(
        		"1, 2, 3",
        		Utils.join(Utils.buildList("1", "2", "3"), ", ")
        );
    }
    
    
//    @Test
//    public void testEncodeDecodeParams() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("key", "value");
//
//        String signedData = Utils.signAndEncode(map, "secret");
//
//        Assert.assertEquals(
//                map,
//                Utils.decodeAndVerify(signedData, "secret")
//        );
//    }

}