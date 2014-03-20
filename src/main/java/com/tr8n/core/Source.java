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

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Source extends Base {

    /**
     * Reference back to the application it belongs to
     */
    private Application application;

    /**
     * Locale of the language it currently holds
     */
    private String locale;

    /**
     * Source key
     */
    private String key;

    /**
     * Source name given by the admin or developer
     */
    private String name;

    /**
     * Source description
     */
    private String description;

    /**
     * Translation keys registered with the source
     */
    private Map<String, TranslationKey> translationKeys;

    /**
     * Default constructor
     */
    public Source() {
        super();
    }

    /**
     *
     * @param attributes
     */
    public Source(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("application") != null)
            setApplication((Application) attributes.get("application"));

        setKey((String) attributes.get("key"));
        setLocale((String) attributes.get("locale"));
        setName((String) attributes.get("name"));
        setDescription((String) attributes.get("description"));

        if (attributes.get("translation_keys") != null && getApplication() != null) {
            for (Object data : ((List) attributes.get("translation_keys"))) {
                Map attrs = new HashMap((Map) data);
                attrs.put("application", getApplication());

                TranslationKey translationKey = new TranslationKey(attrs);
                translationKey = getApplication().cacheTranslationKey(translationKey);
                addTranslationKey(translationKey);
            }
        }
    }

    /**
     * Loading source from service
     */
    public void load() {
        try {
            Map data = getApplication().getHttpClient().getJSONMap("source", Utils.buildMap(
                    "source", getKey(),
                    "locale", getLocale(),
                    "translations", "true")
            );
            this.updateAttributes(data);
        } catch (Exception ex) {
            Tr8n.getLogger().error("Failed to load source");
            Tr8n.getLogger().error(ex);
            Tr8n.getLogger().error(StringUtils.join(ex.getStackTrace(), "\n"));
        }
    }

    public void addTranslationKey(TranslationKey translationKey) {
        if (translationKeys == null) {
            translationKeys = new HashMap<String, TranslationKey>();
        }
        translationKeys.put(translationKey.getKey(), translationKey);
    }

    public TranslationKey getTranslationKey(String key) {
        if (translationKeys == null)
            return null;

        return translationKeys.get(key);
    }

    public String getKey() {
        return key;
    }

    public Application getApplication() {
        return application;
    }

    public String getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
