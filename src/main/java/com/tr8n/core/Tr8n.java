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

import java.text.AttributedString;
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

    /**
     *
     * @param label
     * @return
     */
    public static AttributedString translateAttributedString(String label) {
        return translateAttributedString(label, "");
    }

    public static AttributedString translateAttributedString(String label, String description) {
        return translateAttributedString(label, description, null);
    }

    public static AttributedString translateAttributedString(String label, String description, Map<String, Object> tokens) {
        return getInstance().translateAttributedString(label, description, null, null);
    }

    public static AttributedString translateAttributedString(String label, Map<String, Object> tokens) {
        return getInstance().translateAttributedString(label, "", tokens, null);
    }

    public static AttributedString translateAttributedString(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getInstance().translateAttributedString(label, "", tokens, options);
    }

    Configuration config;
    Application application;
    Cache cache;
    Logger logger;
    Request request;

    protected Tr8n() {
    }

    public static Application getApplication() {
        return getInstance().application;
    }

    public static Configuration getConfig() {
        if (getInstance().config == null)
            getInstance().config = new Configuration();

        return getInstance().config;
    }

    public static Cache getCache() {
        if (getInstance().cache  == null)
            getInstance().cache = new Cache();
        return getInstance().cache;
    }

    public static Request getRequest() {
        if (getInstance().request == null)
            getInstance().request = new Request();
        return getInstance().request;
    }

    public static Language getCurrentLanguage() {
        return getRequest().getCurrentLanguage();
    }

    public static Logger getLogger() {
        if (getInstance().logger == null)
            getInstance().logger = new Logger();

        return getInstance().logger;
    }

    public String translate(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        return (String) this.getCurrentLanguage().translate(label, description, tokens, options);
    }

    public AttributedString translateAttributedString(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
//        return this.currentLanguage.translate(label, description, tokens, options);
        return null;
    }

}
