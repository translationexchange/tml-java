package com.translationexchange.core.dummy;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Map;

import com.translationexchange.core.Application;
import com.translationexchange.core.BaseTest;
import com.translationexchange.core.HttpClient;
import com.translationexchange.core.Tml;

public class DummyApplication extends Application {
    
    private HttpClient httpClient;
    
    /**
     * Default constructor
     */
    public DummyApplication() {
        super();
    }
    
    /**
     * <p>Constructor for Application.</p>
     *
     * @param attributes a {@link java.util.Map} object.
     */
    public DummyApplication(Map<String, Object> attributes) {
        super(attributes);
    }
    
    public HttpClient getHttpClient() {
        if (this.httpClient == null)
            this.httpClient = spy(new HttpClient(this));
        
        // Mocking
        try {
            when(this.httpClient.getJSONMap(eq("projects/" + getKey() + "/definition"), anyMap())).thenReturn(
                    BaseTest.loadJSONMap("/" + getKey()));
        } catch(Exception ex) {
            Tml.getLogger().logException(
                    String.format("Could not load application under this key: %s"), ex);
        }
        
        return this.httpClient;
    }
}
