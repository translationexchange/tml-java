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

package com.translationexchange.core.rulesengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.translationexchange.core.BaseTest;
import com.translationexchange.core.rulesengine.Parser;

public class ParserTest extends BaseTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSplittingTokens() {
        Parser parser = new Parser("(= 1 (mod n 10))");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "=", "1", "(", "mod", "n", "10", ")", ")"},
                parser.getTokens().toArray()
        );

        parser = new Parser("(&& (= 1 (mod @n 10)) (!= 11 (mod @n 100)))");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "&&", "(", "=", "1", "(", "mod", "@n", "10", ")", ")", "(", "!=", "11", "(", "mod", "@n", "100", ")", ")", ")"},
                parser.getTokens().toArray()
        );

        parser = new Parser("(= '1' @value)");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "=", "'1'", "@value", ")"},
                parser.getTokens().toArray()
        );

        parser = new Parser("(= 'abc' @value)");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "=", "'abc'", "@value", ")"},
                parser.getTokens().toArray()
        );

        parser = new Parser("(= 'hello world' @value)");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "=", "'hello world'", "@value", ")"},
                parser.getTokens().toArray()
        );
    }


    @SuppressWarnings("rawtypes")
    @Test
    public void testParsingExpressions() {
        Map data = loadJSONMap("/rules.json");
        Parser parser = new Parser();

        Iterator iter = data.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object expectation = (Object) data.get(key);

            Object result = parser.parse(key);

            if (expectation instanceof String) {
                org.junit.Assert.assertEquals(
                        result,
                        expectation
                );
            } else {
                List expectationList = (List) data.get(key);

                @SuppressWarnings("unchecked")
                List<String> results = (ArrayList<String>) result;

                org.junit.Assert.assertArrayEquals(
                        results.toArray(),
                        expectationList.toArray()
                );
            }
        }
    }

}
