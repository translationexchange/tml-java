package com.translationexchange.core.dummy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Map;

import com.translationexchange.core.Application;
import com.translationexchange.core.HttpClient;

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
            this.httpClient = mock(HttpClient.class);
        return this.httpClient;
    }
}
