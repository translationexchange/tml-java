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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageContext extends Base {

    /**
     * Reference back to the language it belongs to
     */
    Language language;

    /**
     * Unique key identifying the context => num, gender, list, etc..
     */
    String keyword;

    /**
     * Description of the context
     */
    String description;

    /**
     * List of available rule keys. num => [one, few, many, other]
     */
    List<String> keys;

    /**
     * Expression indicating which tokens belong to this context
     */
    String tokenExpression;

    /**
     * List of variable names that an object must support for the context
     */
    List<String> variableNames;

    /**
     * Mapping of parameters to rules
     */
    Object tokenMapping;

    /**
     * Hash of all the rules for the context => {one: rule, few: rule, ...}
     */
    Map<String, LanguageContextRule> rules;

    /**
     * Fallback rule for the context
     */
    LanguageContextRule fallbackRule;

    /**
     * Configuration of the context - based on the global configuration in Tr8n
     */
    Map config;

    /**
     *
     * @param attributes
     */
    public LanguageContext(Map attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    public void updateAttributes(Map attributes) {
        if (attributes.get("language") != null)
            this.language = (Language) attributes.get("language");

        this.keyword = (String) attributes.get("keyword");
        this.description = (String) attributes.get("description");
        this.keys = (List<String>) attributes.get("keys");
        this.tokenExpression = (String) attributes.get("token_expression");
        this.variableNames = (List<String>) attributes.get("variables");
        this.tokenMapping = attributes.get("token_mapping");

        this.rules = new HashMap<String, LanguageContextRule>();

        if (attributes.get("rules") != null) {
            Map<String, Object> rulesHash = (Map<String, Object>) attributes.get("rules");
            Iterator entries = rulesHash.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry thisEntry = (Map.Entry) entries.next();
                String key = (String) thisEntry.getKey();
                Map ruleData = (Map) thisEntry.getValue();

                LanguageContextRule rule = new LanguageContextRule(ruleData);
                rule.languageContext = this;
                if (rule.isFallback()) this.fallbackRule = rule;
                rules.put(key, rule);
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

        // TODO: major research here



        return null;
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
    public String toString() {
//        return  this.keyword + "(" + this.language.locale + ")";
        return "";
    }
}
