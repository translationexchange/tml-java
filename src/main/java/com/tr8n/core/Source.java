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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Source extends Base {

    /**
     * Reference back to the application it belongs to
     */
    Application application;

    /**
     * Locale of the language it currently holds
     */
    String locale;

    /**
     * Source key
     */
    String key;

    /**
     * Source name given by the admin or developer
     */
    String name;

    /**
     * Source description
     */
    String description;

    /**
     * Translation keys registered with the source
     */
    Map<String, TranslationKey> translationKeys;

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
            this.application = (Application) attributes.get("application");

        this.key = (String) attributes.get("key");
        this.locale = (String) attributes.get("locale");
        this.name = (String) attributes.get("name");
        this.description = (String) attributes.get("description");


        this.translationKeys = new HashMap<String, TranslationKey>();
        if (attributes.get("translation_keys") != null) {
            for (Object data : ((List) attributes.get("translation_keys"))) {
                Map attrs = new HashMap((Map) data);
                attrs.put("application", this.application);

                TranslationKey translationKey = new TranslationKey(attrs);
                translationKey = this.application.cacheTranslationKey(translationKey);
                this.translationKeys.put(translationKey.getKey(), translationKey);
            }
        }
    }

    public void load() {
        try {
            Map data = (Map) this.application.get("source", Utils.buildMap("source", this.key, "locale", this.locale, "translations", "true"));
            this.updateAttributes(data);
        } catch (Exception ex) {
            Tr8n.getLogger().error("Failed to load source");
            Tr8n.getLogger().error(ex);
            Tr8n.getLogger().error(StringUtils.join(ex.getStackTrace(), "\n"));
        }
    }

    public TranslationKey getTranslationKey(String hashKey) {
        if (this.translationKeys == null)
            return null;
        return this.translationKeys.get(hashKey);
    }

    public String getKey() {
        return key;
    }
}
