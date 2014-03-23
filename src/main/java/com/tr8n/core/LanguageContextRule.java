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

import com.tr8n.core.rulesengine.Evaluator;
import com.tr8n.core.rulesengine.Parser;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;

public class LanguageContextRule extends Base {

    public static final String TR8N_DEFAULT_RULE_KEYWORD = "other";

    /**
     * Holds reference to the language context it belongs to
     */
    private LanguageContext languageContext;

    /**
     * Unique key of the context within the language
     */
    private String keyword;

    /**
     * Description of the rule
     */
    private String description;

    /**
     * Examples of the rule application
     */
    private String examples;

    /**
     * Conditions in symbolic notation
     */
    private String conditions;

    /**
     * Optimized conditions parsed into an array
     */
    private List<Object> conditionsExpression;

    /**
     * Default constructor
     */
    public LanguageContextRule() {
        super();
    }

    /**
     * Default constructor
     * @param attributes
     */
    public LanguageContextRule(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("language_context") != null)
            setLanguageContext((LanguageContext) attributes.get("language_context"));

        setKeyword((String) attributes.get("keyword"));
        setDescription((String) attributes.get("description"));
        setExamples((String) attributes.get("examples"));
        setConditions((String) attributes.get("conditions"));

        if (attributes.get("operations_expression") instanceof List) {
            this.conditionsExpression = (List) attributes.get("conditions_expression");
        }
    }

    /**
     *
     * @return boolean
     */
    public boolean isFallback() {
        return getKeyword().equals(TR8N_DEFAULT_RULE_KEYWORD);
    }

    /**
     *
     * @return List
     */
    @SuppressWarnings("unchecked")
	public List<Object> getConditionsExpression() {
        if (this.conditionsExpression == null) {
            Parser p = new Parser(this.conditions);
            this.conditionsExpression = (List<Object>) p.parse();
        }

        return this.conditionsExpression;
    }

    /**
     *
     * @param vars
     * @return boolean
     */
    public boolean evaluate(Map<String, Object> vars) {
        if (isFallback())
            return true;

        Evaluator e = new Evaluator();

        Iterator<Entry<String, Object>> entries = vars.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, Object> thisEntry = (Entry<String, Object>) entries.next();
            String key = (String) thisEntry.getKey();
            Object value = thisEntry.getValue();
            e.setVariable(key, value);
        }

        return (Boolean) e.evaluate(this.getConditionsExpression());
    }

    public String getKeyword() {
        return this.keyword;
    }

    public LanguageContext getLanguageContext() {
        return languageContext;
    }

    public void setLanguageContext(LanguageContext languageContext) {
        this.languageContext = languageContext;
    }

    public String getDescription() {
        return description;
    }

    public String getExamples() {
        return examples;
    }

    public String getConditions() {
        return conditions;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
}
