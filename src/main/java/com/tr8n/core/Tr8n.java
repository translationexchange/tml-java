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

import java.util.Map;

public class Tr8n {
    private static Tr8n instance = null;

    public static Tr8n getInstance() {
        if(instance == null) {
            instance = new Tr8n();
        }
        return instance;
    }

    /**
     * Public Static Interface
     */

    /**
     *
     * @param label
     * @return
     */
    public static String translate(String label) {
        return translate(label, "");
    }

    public static String translate(String label, String description) {
        return translate(label, description, null);
    }

    public static String translate(String label, String description, Map<String, Object> tokens) {
        return getInstance().translate(label, description, null, null);
    }

    public static String translate(String label, Map<String, Object> tokens) {
        return getInstance().translate(label, "", tokens, null);
    }

    public static String translate(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getInstance().translate(label, "", tokens, options);
    }




    Configuration config;
    Application application;
    Language currentLanguage;
    Cache cache;
    Logger logger;

    protected Tr8n() {
        this.config = new Configuration();
        this.cache = new Cache();
        this.logger = new Logger();
    }

    public static Configuration getConfig() {
        return getInstance().config;
    }

    public static Application getApplication() {
        return getInstance().application;
    }

    public static Language getCurrentLanguage() {
        return getInstance().currentLanguage;
    }

    public static Cache getCache() {
        return getInstance().cache;
    }

    public static Logger getLogger() {
        return getInstance().logger;
    }

    public String translate(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        return "";
    }


}
