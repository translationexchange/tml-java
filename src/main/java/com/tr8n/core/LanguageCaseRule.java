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

import java.util.List;
import java.util.Map;

public class LanguageCaseRule {

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
    List compiledConditions;

    /**
     * Operations in the symbolic notations form
     */
    String operations;

    /**
     * Compiled operations in the array form
     */
    List compiledOperations;

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
