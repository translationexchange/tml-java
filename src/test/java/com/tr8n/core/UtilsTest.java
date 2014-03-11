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
    }

}