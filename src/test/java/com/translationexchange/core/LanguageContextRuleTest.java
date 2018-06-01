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

import com.translationexchange.core.languages.LanguageContextRule;

/**
 * Created by michael on 3/10/14.
 */
public class LanguageContextRuleTest extends BaseTest {

    @Test
    public void testEvaluatingRules() {
        LanguageContextRule rule = new LanguageContextRule(Utils.map(
                "keyword", "many",
                "description", "{token} mod 10 is 0 or {token} mod 10 in 5..9 or {token} mod 100 in 11..14",
                "examples", "0, 5-20, 25-30, 35-40...",
                "conditions", "(|| (= 0 (mod @n 10)) (in '5..9' (mod @n 10)) (in '11..14' (mod @n 100)))"
        ));

        Assert.assertEquals(
                "many",
                rule.getKeyword()
        );

        Assert.assertEquals(
                "{token} mod 10 is 0 or {token} mod 10 in 5..9 or {token} mod 100 in 11..14",
                rule.getDescription()
        );

        Assert.assertEquals(
                "0, 5-20, 25-30, 35-40...",
                rule.getExamples()
        );

        Assert.assertEquals(
                "(|| (= 0 (mod @n 10)) (in '5..9' (mod @n 10)) (in '11..14' (mod @n 100)))",
                rule.getConditions()
        );

        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@n", 5))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@n", 9))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@n", 11))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@n", 12))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@n", 14))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@n", 50))
        );

        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.map("@n", 1))
        );
        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.map("@n", 2))
        );
        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.map("@n", 4))
        );
        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.map("@n", 51))
        );

        rule = new LanguageContextRule(Utils.map("keyword", "female", "conditions", "(= 'female' @gender)"));

        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.map("@gender", "male"))
        );

        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@gender", "female"))
        );

        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.map("@gender", "unknown"))
        );


        rule = new LanguageContextRule(Utils.map("keyword", "female", "conditions", "(&& (= 1 (count @genders)) (all @genders 'female'))"));

        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@genders", Utils.buildList("female")))
        );
        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.map("@genders", Utils.buildList("female", "female")))
        );

        rule = new LanguageContextRule(Utils.map("keyword", "female", "conditions", "(&& (> (count @genders) 1) (all @genders 'female'))"));

        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.map("@genders", Utils.buildList("female")))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.map("@genders", Utils.buildList("female", "female")))
        );


    }

}
