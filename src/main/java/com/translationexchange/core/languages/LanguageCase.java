
/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
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
 *
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core.languages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.translationexchange.core.Base;
import com.translationexchange.core.Tml;
import com.translationexchange.core.decorators.Decorator;
public class LanguageCase extends Base {

    /** Constant <code>TR8N_HTML_TAGS_REGEX</code> */
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
     *
     * @param attributes a {@link java.util.Map} object.
     */
    public LanguageCase(Map<String, Object> attributes) {
        super(attributes);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("language") != null)
            setLanguage((Language) attributes.get("language"));

        setKeyword((String) attributes.get("keyword"));
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
     *
     * @param value a {@link java.lang.String} object.
     * @return a {@link com.translationexchange.core.languages.LanguageCaseRule} object.
     */
    public LanguageCaseRule findMatchingRule(String value) {
        return findMatchingRule(value, null);
    }

    /**
     * Finds matching rule for value and object
     *
     * @param value		String value to be matched
     * @param object	Associated object (optional)
     * @return 			True/False if the rule was matched
     */
    public LanguageCaseRule findMatchingRule(String value, Object object) {
    	for (LanguageCaseRule rule : rules) {
    		if (rule.evaluate(value, object))
    			return rule;
    	}
        return null;
    }

    /**
     * Applies rule for value
     *
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String apply(String value) {
        return apply(value, null, null);
    }

    /**
     * Applies rule for value based on object properties
     *
     * @param value a {@link java.lang.String} object.
     * @param object a {@link java.lang.Object} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public String apply(String value, Object object, Map<String, Object> options) {
    	List<String> elements = new ArrayList<String>();
    	
    	if (type.equals("phrase")) {
    		elements.add(value);
    	} else {
    		elements.addAll(Arrays.asList(value.split("\\s\\/,;:"))); 
    	}
    	
        // TODO: use RegEx to split words and assemble them right back
    	Decorator decorator = Tml.getConfig().getDecorator();
    	
    	String transformedValue = value;
    	for (String element : elements) {
    		LanguageCaseRule rule = findMatchingRule(element, object);
    		
    		if (rule == null) continue;
    		
    		transformedValue = transformedValue.replaceAll(Pattern.quote(element), rule.apply(element));
        	transformedValue = decorator.decorateLanguageCase(this, rule, element, transformedValue, options);
    	}
    	
        return transformedValue;
    }

    /**
     * <p>Getter for the field <code>language</code>.</p>
     *
     * @return a {@link com.translationexchange.core.languages.Language} object.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * <p>Setter for the field <code>language</code>.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * <p>addRule.</p>
     *
     * @param rule a {@link com.translationexchange.core.languages.LanguageCaseRule} object.
     */
    public void addRule(LanguageCaseRule rule) {
        if (rules == null)
            rules = new ArrayList<LanguageCaseRule>();
        rules.add(rule);
    }

    /**
     * <p>getRule.</p>
     *
     * @param index a {@link java.lang.Integer} object.
     * @return a {@link com.translationexchange.core.languages.LanguageCaseRule} object.
     */
    public LanguageCaseRule getRule(Integer index) {
        if (rules == null)
            return null;
        return rules.get(index);
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getType() {
        return type;
    }

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link java.lang.String} object.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * <p>Getter for the field <code>keyword</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * <p>Setter for the field <code>keyword</code>.</p>
     *
     * @param keyword a {@link java.lang.String} object.
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * <p>Getter for the field <code>latinName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLatinName() {
        return latinName;
    }

    /**
     * <p>Setter for the field <code>latinName</code>.</p>
     *
     * @param latinName a {@link java.lang.String} object.
     */
    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    /**
     * <p>Getter for the field <code>nativeName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNativeName() {
        return nativeName;
    }

    /**
     * <p>Setter for the field <code>nativeName</code>.</p>
     *
     * @param nativeName a {@link java.lang.String} object.
     */
    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link java.lang.String} object.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>rules</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<LanguageCaseRule> getRules() {
        return rules;
    }

    /**
     * <p>Setter for the field <code>rules</code>.</p>
     *
     * @param rules a {@link java.util.List} object.
     */
    public void setRules(List<LanguageCaseRule> rules) {
        this.rules = rules;
    }
    
    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return getKeyword();
    }
}

