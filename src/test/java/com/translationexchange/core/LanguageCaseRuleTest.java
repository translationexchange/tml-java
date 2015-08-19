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

import org.junit.Assert;
import org.junit.Test;

import com.translationexchange.core.LanguageCaseRule;
import com.translationexchange.core.Utils;

/**
 * Created by michael on 3/10/14.
 */
public class LanguageCaseRuleTest extends BaseTest {

    @Test
    public void testEvaluatingRules() {
       LanguageCaseRule rule = new LanguageCaseRule();
       Assert.assertNull(
               rule.getDescription()
       );

    		   
       rule = new LanguageCaseRule(Utils.buildMap(
    		   "conditions", "(= 1 1)",
    		   "operations", "(+ 1 1)"
       ));

       Assert.assertEquals(
    		   Utils.buildList("=", 1l, 1l),
               rule.getConditionsExpression()
       );
       
       Assert.assertEquals(
    		   Utils.buildList("+", 1l, 1l),
               rule.getOperationsExpression()
       );
       
       
       rule = new LanguageCaseRule(Utils.buildMap(
    		   "conditions", "(= @value 'Michael')",
    		   "operations", "(prepend 'Hello ' @value)"
       ));

       Assert.assertTrue(
    		   rule.evaluate("Michael")
       );
       
       Assert.assertEquals(
    		   "Hello Michael",
    		   rule.apply("Michael")
       );
       
    }

}