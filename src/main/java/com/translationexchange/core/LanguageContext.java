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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.translationexchange.core.rulesengine.Variable;

public class LanguageContext extends Base {

    /**
     * Reference back to the language it belongs to
     */
    private Language language;

    /**
     * Unique key identifying the context => num, gender, list, etc..
     */
    private String keyword;

    /**
     * Description of the context
     */
    private String description;

    /**
     * List of available rule keys. num => [one, few, many, other]
     */
    private List<String> keys;

    /**
     * Expression indicating which tokens belong to this context
     */
    private String tokenExpression;

    /**
     * List of variable names that an object must support for the context
     */
    private List<String> variableNames;

    /**
     * Mapping of parameters to rules
     */
    private Object tokenMapping;

    /**
     * Hash of all the rules for the context => {one: rule, few: rule, ...}
     */
    private Map<String, LanguageContextRule> rules;

    /**
     * Fallback rule for the context
     */
    private LanguageContextRule fallbackRule;


    /**
     * Default constructor
     */
    public LanguageContext() {
        super();
    }

    /**
     *
     * @param attributes
     */
    public LanguageContext(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("language") != null)
            setLanguage((Language) attributes.get("language"));

        setKeyword((String) attributes.get("keyword"));
        setDescription((String) attributes.get("description"));
        setTokenExpression((String) attributes.get("token_expression"));
        setTokenMapping(attributes.get("token_mapping"));

        if (attributes.get("keys") != null)
            setKeys(new ArrayList<String>((List) attributes.get("keys")));

        if (attributes.get("variables") != null)
            setVariableNames(new ArrayList<String> ((List) attributes.get("variables")));

        if (attributes.get("rules") != null) {
            Iterator entries = ((Map<String, Object>) attributes.get("rules")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                LanguageContextRule rule = new LanguageContextRule((Map) entry.getValue());
                rule.setLanguageContext(this);
                if (rule.isFallback()) setFallbackRule(rule);
                addRule(rule);
            }
        }
    }

    /**
     *
     * @param tokenName
     * @return
     */
    public boolean isApplicableToTokenName(String tokenName) {
        if (getTokenExpression() == null)
            return false;
        Pattern p = Utils.parsePattern(getTokenExpression());
        Matcher m = p.matcher(tokenName);
        return m.find();
    }

    /**
     *
     * @param object
     * @return
     */
    public Map<String, Object> getVariables(Object object) {
        Map<String, Object> vars = new HashMap<String, Object>();
        for (String varName : getVariableNames()) {
            Variable var = Tml.getConfig().getContextVariable(getKeyword(), varName);
            vars.put(varName, var.getValue(this, object));
        }
        return vars;
    }

    /**
     *
     * @param object
     * @return
     */
    public LanguageContextRule findMatchingRule(Object object) {
        Map<String, Object> tokenVars = getVariables(object);

        for (LanguageContextRule rule : getRules().values()) {
            if (rule.isFallback()) continue;
            if (rule.evaluate(tokenVars))
                return rule;
        }

        return this.fallbackRule;
    }

    /**
     *
     * @return
     */
    public Object getTokenMapping() {
        return this.tokenMapping;
    }


    /**
     *
     * @return
     */
    public String toString() {
        return  this.keyword + "(" + this.language.getLocale() + ")";
    }

    /**
     *
     * @return
     */
    public LanguageContextRule getFallbackRule() {
        return this.fallbackRule;
    }

    /**
     *
     * @return Language of the context
     */
    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void addRule(LanguageContextRule rule) {
        if (rules == null)
            rules = new HashMap<String, LanguageContextRule>();
        rules.put(rule.getKeyword(), rule);
    }

    public LanguageContextRule getRule(String key) {
        if (rules == null)
            return null;
        return rules.get(key);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public String getTokenExpression() {
        return tokenExpression;
    }

    public void setTokenExpression(String tokenExpression) {
        this.tokenExpression = tokenExpression;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }

    public void setVariableNames(List<String> variableNames) {
        this.variableNames = variableNames;
    }

    public void setTokenMapping(Object tokenMapping) {
        this.tokenMapping = tokenMapping;
    }

    public Map<String, LanguageContextRule> getRules() {
        return rules;
    }

    public void setRules(Map<String, LanguageContextRule> rules) {
        this.rules = rules;
    }

    public void setFallbackRule(LanguageContextRule fallbackRule) {
        this.fallbackRule = fallbackRule;
    }
}
