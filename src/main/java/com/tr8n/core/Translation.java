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

import com.tr8n.core.tokenizers.tokens.DataToken;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
     * Translation label
     */
    private String label;

    /**
     * Translation context hash:
     * {token1: {context1: rule1}, token2: {context2: rule2}}
     */
    private Map context;


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

    /**
     *
     * @param attributes
     */
    @SuppressWarnings("rawtypes")
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("language") != null)
        	setLanguage((Language) attributes.get("language"));

        if (attributes.get("translation_key") != null)
        	setTranslationKey((TranslationKey) attributes.get("translation_key"));

        if (language == null && attributes.get("locale") != null && translationKey != null)
        	setLanguage(translationKey.getApplication().getLanguage((String) attributes.get("locale")));
        
        setLabel((String) attributes.get("label"));
        setContext((Map) attributes.get("context"));
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

    public Map getContext() {
        return context;
    }

    public void setContext(Map context) {
        this.context = context;
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
    public boolean isValidTranslationForTokens(Map tokens) {
        if (!hasContext()) return true;

        Iterator entries = this.context.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String tokenName = (String) entry.getKey();
            Map rules = (Map) entry.getValue();

            Object tokenObject = DataToken.getContextObject(tokens, tokenName);
            if (tokenObject == null) return false;

            Iterator ruleEntries = rules.entrySet().iterator();
            while (ruleEntries.hasNext()) {
                Map.Entry ruleEntry = (Map.Entry) ruleEntries.next();
                String contextKeyword = (String) ruleEntry.getKey();
                String ruleKeyword = (String) ruleEntry.getValue();

                if (ruleKeyword.equals(LanguageContextRule.TR8N_DEFAULT_RULE_KEYWORD))
                    continue;

                LanguageContext context = language.getContextByKeyword(contextKeyword);
                if (context == null)
                    return false;

                LanguageContextRule rule = context.findMatchingRule(tokenObject);
                if (rule == null || !rule.getKeyword().equals(ruleKeyword))
                    return false;
            }
        }

        return true;
    }


}
