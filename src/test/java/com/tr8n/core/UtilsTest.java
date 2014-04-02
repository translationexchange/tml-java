package com.tr8n.core;

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

        Map<String, Object> result = Utils.buildMap(
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
        String query = Utils.buildQueryString(Utils.buildMap("name", "Michael", "gender", "male"));

        Assert.assertEquals(
                "name=Michael&gender=male",
                query
        );

        query = Utils.buildQueryString(Utils.buildMap("name", "John Peterson", "gender", "male"));

        Assert.assertEquals(
                "name=John+Peterson&gender=male",
                query
        );

    }

    @Test
    public void testBuildingUrl() throws Exception {
        URL url = Utils.buildURL("http://google.com", "/search", Utils.buildMap("q", "test"));

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
        Configuration config = new Configuration();

        Map root = new HashMap();

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
                Utils.buildMap("name", "Michael"),
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
                Utils.buildJSON(Utils.buildMap("name", "Michael"))
        );
    }


    @Test(expected=IllegalArgumentException.class)
    public void testBuildMapWithOneArgumentFails() {
        Assert.assertEquals(
                null,
                Utils.buildMap("a")
        );
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBuildMapWithNullValueFails() {
        Assert.assertEquals(
                null,
                Utils.buildMap("a", "b", null, "c")
        );
    }

    @Test
    public void testBuildMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "b");

        Assert.assertEquals(
                map,
                Utils.buildMap("a", "b")
        );
    }

    @Test
    public void testEncodeDecodeParams() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");

        String signedData = Utils.signAndEncode(map, "secret");

        Assert.assertEquals(
                map,
                Utils.decodeAndVerify(signedData, "secret")
        );
    }

}