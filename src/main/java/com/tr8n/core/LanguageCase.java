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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LanguageCase extends Base {

    public static final String TR8N_HTML_TAGS_REGEX = "/<\\/?[^>]*>/";

    /**
     * Holds reference back to the language it belongs to
     */
    private Language language;

    /**
     * How to apply the case: "phrase" or "words"
     */
    private String type;

    /**
     * Unique key identifying the language case
     */
    private String keyword;

    /**
     * General name in Latin
     */
    private String latinName;

    /**
     * Name in native language
     */
    private String nativeName;

    /**
     * Description of the language case
     */
    private String description;

    /**
     * List of all rules for the language case
     */
    private List<LanguageCaseRule> rules;


    /**
     * Default constructor
     */
    public LanguageCase() {
        super();
    }

    /**
     * Constructor with attributes
     * @param attributes
     */
    public LanguageCase(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("language") != null)
            setLanguage((Language) attributes.get("language"));

        setKeyword((String) attributes.get("language"));
        setLatinName((String) attributes.get("latin_name"));
        setNativeName((String) attributes.get("native_name"));
        setDescription((String) attributes.get("description"));
        setType((String) attributes.get("application"));

        if (attributes.get("rules") != null) {
            for (Object data : ((List) attributes.get("rules"))) {
                LanguageCaseRule rule = new LanguageCaseRule((Map) data);
                rule.setLanguageCase(this);
                addRule(rule);
            }
        }
    }

    /**
     * Finds matching rule for value
     * @param value
     * @return
     */
    public LanguageCaseRule findMatchingRule(String value) {
        return findMatchingRule(value, null);
    }

    /**
     * Finds matching rule for value and object
     * @param value
     * @param object
     * @return
     */
    public LanguageCaseRule findMatchingRule(String value, Object object) {
        return null;
    }

    /**
     * Applies rule for value
     * @param value
     * @return
     */
    public String apply(String value) {
        return apply(value, null, null);
    }

    /**
     * Applies rule for value based on object properties
     * @param value
     * @param object
     * @return
     */
    public String apply(String value, Object object, Map options) {
        return value;
    }



    public String toString() {
        return  this.keyword + "(" + this.language.getLocale() + ")";
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void addRule(LanguageCaseRule rule) {
        if (rules == null)
            rules = new ArrayList<LanguageCaseRule>();
        rules.add(rule);
    }

    public LanguageCaseRule getRule(Integer index) {
        if (rules == null)
            return null;
        return rules.get(index);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LanguageCaseRule> getRules() {
        return rules;
    }

    public void setRules(List<LanguageCaseRule> rules) {
        this.rules = rules;
    }
}
