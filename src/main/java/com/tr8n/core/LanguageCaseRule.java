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

import com.tr8n.core.rulesengine.Parser;

import java.util.List;
import java.util.Map;

public class LanguageCaseRule extends Base {

    /**
     * Reference back to the language case the rule belongs to
     */
    LanguageCase languageCase;

    /**
     * Rule description
     */
    String description;

    /**
     * Rule evaluation examples
     */
    String examples;

    /**
     * Conditions in symbolic notations form
     */
    String conditions;

    /**
     * Compiled conditions in the array form
     */
    List conditionsExpression;

    /**
     * Operations in the symbolic notations form
     */
    String operations;

    /**
     * Compiled operations in the array form
     */
    List operationsExpression;

    /**
     *
     * @param attributes
     */
    public LanguageCaseRule(Map attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    public void updateAttributes(Map attributes) {
        if (attributes.get("language_case") != null)
            this.languageCase = (LanguageCase) attributes.get("language_case");

        this.description = (String) attributes.get("description");
        this.examples = (String) attributes.get("examples");

        this.conditions = (String) attributes.get("conditions");
        if (attributes.get("conditions_expression") instanceof List) {
            this.conditionsExpression = (List) attributes.get("conditions_expression");
        }

        this.operations = (String) attributes.get("operations");
        if (attributes.get("operations_expression") instanceof List) {
            this.operationsExpression = (List) attributes.get("operations_expression");
        }
    }


    public List getOperationsExpression() {
        if (this.operationsExpression == null) {
            Parser p = new Parser(this.operations);
            this.operationsExpression = (List) p.parse();
        }

        return this.conditionsExpression;
    }

    public List getConditionsExpression() {
        if (this.conditionsExpression == null) {
            Parser p = new Parser(this.conditions);
            this.conditionsExpression = (List) p.parse();
        }

        return this.conditionsExpression;
    }
    /**
     * Extracts gender value from the object
     * @param object
     * @return
     */
    public Map genderVariables(Object object) {
        return null;
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
        return true;
    }

    /**
     * Applies operations and returns the modified value
     * @param value
     * @return
     */
    public String apply(String value) {
        return "";
    }

}
