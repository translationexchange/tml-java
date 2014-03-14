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
    Language language;

    /**
     * How to apply the case: "phrase" or "words"
     */
    String type;

    /**
     * Unique key identifying the language case
     */
    String keyword;

    /**
     * General name in Latin
     */
    String latinName;

    /**
     * Name in native language
     */
    String nativeName;

    /**
     * Description of the language case
     */
    String description;

    /**
     * List of all rules for the language case
     */
    List<LanguageCaseRule> rules;


    /**
     * Constructor with attributes
     * @param attributes
     */
    public LanguageCase(Map attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    public void updateAttributes(Map attributes) {
        if (attributes.get("language") != null) {
            this.language = (Language) attributes.get("language");
        }

        this.keyword = (String) attributes.get("language");
        this.latinName = (String) attributes.get("latin_name");
        this.nativeName = (String) attributes.get("native_name");
        this.description = (String) attributes.get("description");
        this.type = (String) attributes.get("application");

        this.rules = new ArrayList<LanguageCaseRule>();
        if (attributes.get("rules") != null) {
            for (Object data : ((List) attributes.get("rules"))) {
                this.rules.add(new LanguageCaseRule((Map) data));
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
        return  this.keyword + "(" + this.language.locale + ")";
    }

}
