package com.tr8n.core.cache;

import java.util.Map;

public interface Cache {

    public Object fetch(String key, Map<String, Object> options);
    
    public void store(String key, Object data, Map<String, Object> options);

    public void delete(String key, Map<String, Object> options);

    public void reset();
    
}
