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

import com.tr8n.core.rulesengine.Variable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("language") != null)
            this.language = (Language) attributes.get("language");

        this.keyword = (String) attributes.get("keyword");
        this.description = (String) attributes.get("description");
        this.tokenExpression = (String) attributes.get("token_expression");
        this.tokenMapping = attributes.get("token_mapping");

        if (attributes.get("keys") != null)
            this.keys = new ArrayList<String>((List) attributes.get("keys"));

        if (attributes.get("variables") != null)
            this.variableNames = new ArrayList<String> ((List) attributes.get("variables"));

        this.rules = new HashMap<String, LanguageContextRule>();
        if (attributes.get("rules") != null) {
            Iterator entries = ((Map<String, Object>) attributes.get("rules")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                LanguageContextRule rule = new LanguageContextRule((Map) entry.getValue());
                rule.setLanguageContext(this);
                if (rule.isFallback()) this.fallbackRule = rule;
                this.rules.put((String) entry.getKey(), rule);
            }
        }
    }

    /**
     *
     * @param tokenName
     * @return
     */
    public boolean isApplicableToTokenName(String tokenName) {
        Pattern p = Utils.parsePattern(this.tokenExpression);
        Matcher m = p.matcher(tokenName);
        return m.find();
    }

    /**
     *
     * @param object
     * @return
     */
    public Map vars(Object object) {
        Map vars = new HashMap();
        for (String varName : this.variableNames) {
            Variable var = Tr8n.getConfig().getContextVariable(this.keyword, varName);
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
        Map tokenVars = this.vars(object);

        Iterator entries = this.rules.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            LanguageContextRule rule = (LanguageContextRule) thisEntry.getValue();

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
}
