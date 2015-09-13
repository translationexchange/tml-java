/**
 * Copyright (c) 2015 Translation Exchange, Inc. All rights reserved.
 *
 *  _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 *    | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 *    | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 *    | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 *    |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 *                                                                                        __/ |
 *                                                                                       |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.translationexchange.core;

import java.util.Iterator;
import java.util.Map;

import com.translationexchange.core.tokens.DataToken;

public class Translation extends Base {

    /**
     * Reference to the translation key it belongs to
     */
    private TranslationKey translationKey;

    /**
     * Reference to the language it belongs to
     */
    private Language language;

    /**
     * Translation locale
     */
    private String locale;

	/**
     * Translation label
     */
    private String label;
    
    /**
     * Translation context hash:
     * {token1: {context1: rule1}, token2: {context2: rule2}}
     */
    private Map<String, Object> context;


    /**
     * Default constructor
     */
    public Translation() {
    }

    /**
     *
     * @param attributes
     */
    public Translation(Map<String, Object> attributes) {
        super(attributes);
    }

    protected String getLocale() {
		return locale;
	}

	protected void setLocale(String locale) {
		this.locale = locale;
	}
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public TranslationKey getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(TranslationKey translationKey) {
        this.translationKey = translationKey;
    }

    public Map<String,Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
    
    /**
     *
     * @param attributes
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("language") != null)
        	setLanguage((Language) attributes.get("language"));

        if (attributes.get("translation_key") != null)
        	setTranslationKey((TranslationKey) attributes.get("translation_key"));

        setLocale((String) attributes.get("locale"));
        
        if (getLanguage() == null && getLocale() != null && getTranslationKey() != null)
        	setLanguage(translationKey.getApplication().getLanguage((String) attributes.get("locale")));
        
        setLabel((String) attributes.get("label"));
        setContext((Map) attributes.get("context"));
    }

    /**
     * Check if the translation has context rules
     * @return
     */
    public boolean hasContext() {
        return getContext() != null && getContext().size() > 0;
    }

    /**
     * Checks if the translation is valid for the given tokens
     *
     *  {
     *    "count" => {"number":"one"},
     *    "user" => {"gender":"male"}
     *  }
     *
     * @param tokens
     * @return
     */
    @SuppressWarnings("unchecked")
	public boolean isValidTranslationForTokens(Map<String, Object> tokens) {
        if (!hasContext()) return true;

        Iterator<Map.Entry<String, Object>> entries = this.context.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            Map<String, String> rules = (Map<String, String>) entry.getValue();

            Object tokenObject = DataToken.getContextObject(tokens, entry.getKey());
            if (tokenObject == null) return false;

            Iterator<Map.Entry<String, String>> ruleEntries = rules.entrySet().iterator();
            while (ruleEntries.hasNext()) {
                Map.Entry<String, String> ruleEntry = ruleEntries.next();

                if (ruleEntry.getValue().equals(LanguageContextRule.TR8N_DEFAULT_RULE_KEYWORD))
                    continue;

                LanguageContext context = getLanguage().getContextByKeyword(ruleEntry.getKey());
                if (context == null)
                    return false;

                LanguageContextRule rule = context.findMatchingRule(tokenObject);
                if (rule == null || !rule.getKeyword().equals(ruleEntry.getValue()))
                    return false;
            }
        }

        return true;
    }

    public String toString() {
        return label + " (" + getLanguage().getLocale() + ")";
    }

}
