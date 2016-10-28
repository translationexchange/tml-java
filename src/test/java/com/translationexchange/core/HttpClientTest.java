package com.translationexchange.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class HttpClientTest {
    private static MockWebServer server;
    
    @BeforeClass
    public static void initWebserver() throws IOException {
        server = new MockWebServer();
        server.start();
        final Dispatcher dispatcher = new Dispatcher() {
            
            private final Map<String, Object> MOCKING_SERVICES = Utils.map(
                    "projects\\/(\\w)+\\/definition", "project/definition.json"
                    );
            
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                String reqPath = request.getPath();
                Iterator<Entry<String, Object>> mockingServices = MOCKING_SERVICES.entrySet().iterator();
                while(mockingServices.hasNext()) {
                    Map.Entry<String, Object> cur = (Map.Entry<String, Object>) mockingServices.next();
                    if(isMatch(reqPath, cur.getKey())) {
                        return getMockResponse((String) cur.getValue());
                    }
                }
                return (new MockResponse()).setResponseCode(404);
            }
            
            private MockResponse getMockResponse(String resourceKey) {
                StringBuilder resourcePathBuilder = new StringBuilder();
                String resourcePath = resourcePathBuilder
                    .append("/mockwebserver")
                    .append("/")
                    .append(resourceKey).toString();
                return (new MockResponse()).setResponseCode(200)
                                           .addHeader("Content-Type", "application/json; charset=utf-8")
                                           .setBody(BaseTest.loadResource(resourcePath));
            }
            
            private boolean isMatch(String str, String regex) {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(str);
                return m.find();
            }
        };
        
        server.setDispatcher(dispatcher);
    }
    
    @Test
    public void testMockWebServer() throws IOException {
        HttpURLConnection response = makeRequest("projects/6c377447a542718bfd9fe0f5d8f11fae2827377bc4295db76667469db67bd8ed/definition");
        Assert.assertEquals(200, response.getResponseCode());
        
        HttpURLConnection responseUnknown = makeRequest("translationexchange.com");
        Assert.assertEquals(404, responseUnknown.getResponseCode());
    }
    
    private HttpURLConnection makeRequest(String requestString) throws IOException {
        final URL url = server.url(requestString).url();
        return (HttpURLConnection) url.openConnection();
    }
    
    
}
