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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.translationexchange.core.rulesengine.Evaluator;
import com.translationexchange.core.rulesengine.Parser;

public class LanguageCaseRule extends Base {

	public static final String VARIABLE_NAME_VALUE = "@value";
	public static final String VARIABLE_NAME_GENDER = "@gender";
	
    /**
     * Reference back to the language case the rule belongs to
     */
    private LanguageCase languageCase;

    /**
     * Rule description
     */
    private String description;

    /**
     * Rule evaluation examples
     */
    private String examples;

    /**
     * Conditions in symbolic notations form
     */
    private String conditions;

    /**
     * Compiled conditions in the array form
     */
	private List<Object> conditionsExpression;

    /**
     * Operations in the symbolic notations form
     */
    private String operations;

    /**
     * Compiled operations in the array form
     */
    private List<Object> operationsExpression;

    /**
     * Default constructor
     */
    public LanguageCaseRule() {
        super();
    }

    /**
     * Creates rule with attributes
     * @param attributes
     */
    public LanguageCaseRule(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     * Updates rule attributes
     * @param attributes
     */
	@SuppressWarnings("unchecked")
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("language_case") != null)
            setLanguageCase((LanguageCase) attributes.get("language_case"));

        setDescription((String) attributes.get("description"));
        setExamples((String) attributes.get("examples"));
        setConditions((String) attributes.get("conditions"));

        if (attributes.get("conditions_expression") instanceof List) {
            this.conditionsExpression = (List<Object>) attributes.get("conditions_expression");
        }

        setOperations((String) attributes.get("operations"));
        if (attributes.get("operations_expression") instanceof List) {
            this.operationsExpression = (List<Object>) attributes.get("operations_expression");
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getOperationsExpression() {
        if (this.operationsExpression == null && this.getOperations() != null) {
            Parser p = new Parser(this.operations);
            this.operationsExpression = (List<Object>) p.parse();
        }

        return this.operationsExpression;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getConditionsExpression() {
        if (this.conditionsExpression == null && this.getConditions() != null) {
            Parser p = new Parser(this.getConditions());
            this.conditionsExpression = (List<Object>) p.parse();
        }

        return this.conditionsExpression;
    }

    /**
     * Always returns true or false for the result of the rule evaluation
     * @param value
     * @return
     */
    public boolean evaluate(String value) {
        return evaluate(value, null);
    }

    /**
     * Always returns true or false for the result of the rule evaluation
     * @param value
     * @param object
     * @return
     */
    public boolean evaluate(String value, Object object) {
    	if (getConditionsExpression() == null)
    		return false;
    	
    	Evaluator e = new Evaluator();
    	e.setVariable(VARIABLE_NAME_VALUE, value);
    	
    	if (object != null) {
    		Map<String, Object> variables = getGenderVariables(object);
    		if (variables != null) {
                Iterator<Entry<String, Object>> entries = variables.entrySet().iterator();
                while (entries.hasNext()) {
					Map.Entry<String, Object> entry = entries.next();
                    e.setVariable((String) entry.getKey(), entry.getValue());
                }
    		}
    	}
    	
        return (Boolean) e.evaluate(getConditionsExpression());
    }

    
    /**
     * Variables apply to gender based expressions
     * @param object
     * @return
     */
    private Map<String, Object> getGenderVariables(Object object) {
    	if (!conditions.contains(VARIABLE_NAME_GENDER))
    		return null;
    	
    	if (object == null)
    		return Utils.buildMap(VARIABLE_NAME_GENDER, "unknown");
    	
    	LanguageContext context = languageCase.getLanguage().getContextByKeyword("gender");
    	if (context == null)
    		return Utils.buildMap(VARIABLE_NAME_GENDER, "unknown");
    	
    	return context.getVariables(object);
    }
    
    /**
     * Applies operations and returns the modified value
     * @param value
     * @return
     */
    public String apply(String value) {
    	if (getOperationsExpression() == null)
    		return value;

    	Evaluator e = new Evaluator();
    	e.setVariable(VARIABLE_NAME_VALUE, value);
    	return (String) e.evaluate(getOperationsExpression());
    }


    public LanguageCase getLanguageCase() {
        return languageCase;
    }

    public void setLanguageCase(LanguageCase languageCase) {
        this.languageCase = languageCase;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getOperations() {
        return operations;
    }

    public void setOperations(String operations) {
        this.operations = operations;
    }
    
    public String toString() {
    	return getConditions();
    }
}
