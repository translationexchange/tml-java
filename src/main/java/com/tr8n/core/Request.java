package com.tr8n.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request is a Thread Safe mechanism for storing object structures for a specific request.
 *
 * In the case of multi-threaded applications, it is important that the requests are not shared across multiple threads
 *
 */
public class Request {

    /**
     * Stores the current language selected by the user
     */
    Language currentLanguage;

    /**
     * Allows to set a source for the entire request
     */
    String currentSource;

    /**
     * Allows developers to group translation keys for management only
     */
    List<Map> blockOptions;

    public Request() {
        this.currentSource = "undefined";
    }

    public Language getCurrentLanguage() {
        return this.currentLanguage;
    }

    public void setCurrentLanguage(Language language) {
        this.currentLanguage = language;
    }

    public String getCurrentSource() {
        return currentSource;
    }

    public void setCurrentSource(String currentSource) {
        this.currentSource = currentSource;
    }

    public void beginBlockWithOptions(Map options) {
        if (this.blockOptions == null) {
            this.blockOptions = new ArrayList<Map>();
        }

        this.blockOptions.add(0, options);
    }

    public Map getBlockOptions() {
        if (this.blockOptions == null)
            this.blockOptions = new ArrayList<Map>();

        if (this.blockOptions.size() == 0)
            return new HashMap();

        return this.blockOptions.get(0);
    }

    public void endBlock() {
        if (this.blockOptions == null || this.blockOptions.size() == 0)
            return;

        this.blockOptions.remove(0);
    }

    public Object getBlockOption(String key) {
        return this.getBlockOptions().get(key);
    }

}
