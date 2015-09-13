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

package com.translationexchange.core.tokenizers;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.translationexchange.core.Utils;
import com.translationexchange.core.tokenizers.DataTokenizer;
import com.translationexchange.core.tokens.Token;

/**
 * Created by michael on 3/11/14.
 */
public class DataTokenizerTest {

    @Test
    public void testTokenization() {
        DataTokenizer dt = new DataTokenizer("Hello {user}");

        Assert.assertEquals(
                1,
                dt.tokens.size()
        );

        Token token = (Token) dt.tokens.get(0);
        Assert.assertEquals(
                "{user}",
                token.getFullName()
        );

        dt = new DataTokenizer();

        dt.tokenize("{user} has {count} messages");
        Assert.assertEquals(
                Arrays.asList("user", "count"),
                dt.getTokenNames()
        );

        dt.tokenize("Hello {user}, how are you {user}");
        Assert.assertEquals(
                Arrays.asList("user"),
                dt.getTokenNames()
        );

        Assert.assertEquals(
                Arrays.asList("user"),
                dt.getTokenNames()
        );
        
        for(String label : Utils.buildStringList(
        		"Hello {user}",
        		"Hello {user:gender}",
        		"Hello {user : gender}",
        		"Hello {user : gender :: gen}",
        		"Hello {user.name::gen}",
        		"Hello { user }",
        		"Hello {{user}}",
        		"Hello {{ user }}",
        		"Hello %{user}",
        		"Hello %{ user }"
        )) {
            dt = new DataTokenizer(label, Utils.buildStringList("user"));
            Assert.assertEquals(
	            Arrays.asList("user"),
	            dt.getAllowedTokenNames()
            );
        }
        
    }

    @Test
    public void testSubstitution() {
        DataTokenizer dt = new DataTokenizer("Hello {user}");

        Assert.assertEquals(
                "Hello Michael",
                dt.substitute(Utils.buildMap("user", "Michael"))
        );

    }

}
