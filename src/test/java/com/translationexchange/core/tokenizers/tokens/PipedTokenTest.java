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

package com.translationexchange.core.tokenizers.tokens;

import org.junit.Assert;
import org.junit.Test;

import com.translationexchange.core.Utils;
import com.translationexchange.core.tokens.PipedToken;

/**
 * Created by michael on 3/12/14.
 */
public class PipedTokenTest {

    @Test
    public void testTokenNames() {
        PipedToken token = new PipedToken("{count|| item, items}");

        Assert.assertEquals(
                "{count|| item, items}",
                token.getFullName()
        );
    }

    @Test
    public void testSeparator() {
        PipedToken token = new PipedToken("{count|| item, items}");

        Assert.assertEquals(
                "||",
                token.getSeparator()
        );

        token = new PipedToken("{count| item, items}");

        Assert.assertEquals(
                "|",
                token.getSeparator()
        );
    }


    /**
     * token:      {count|| one: message, many: messages}
     * results in: {"one": "message", "many": "messages"}
     *
     * token:      {count|| message}
     * transform:  [{"one": "{$0}", "other": "{$0::plural}"}, {"one": "{$0}", "other": "{$1}"}]
     * results in: {"one": "message", "other": "messages"}
     *
     * token:      {count|| message, messages}
     * transform:  [{"one": "{$0}", "other": "{$0::plural}"}, {"one": "{$0}", "other": "{$1}"}]
     * results in: {"one": "message", "other": "messages"}
     *
     * token:      {user| Dorogoi, Dorogaya}
     * transform:  ["unsupported", {"male": "{$0}", "female": "{$1}", "other": "{$0}/{$1}"}]
     * results in: {"male": "Dorogoi", "female": "Dorogaya", "other": "Dorogoi/Dorogaya"}
     *
     * token:      {actors:|| likes, like}
     * transform:  ["unsupported", {"one": "{$0}", "other": "{$1}"}]
     * results in: {"one": "likes", "other": "like"}
     *
     */
    @Test
    public void testParameters() {
        PipedToken token = new PipedToken("{count|| item, items}");

        Assert.assertEquals(
                Utils.buildList("item", "items"),
                token.getParameters()
        );

        token = new PipedToken("{count|| one: message, many: messages}");
        Assert.assertEquals(
                Utils.buildList("one: message", "many: messages"),
                token.getParameters()
        );

        token = new PipedToken("{count|| message}");
        Assert.assertEquals(
                Utils.buildList("message"),
                token.getParameters()
        );
    }


   @Test
    public void testParameterValueMap() {
       PipedToken token = new PipedToken("{count|| one: message, many: messages}");

       Assert.assertEquals(
               Utils.buildMap("one", "message", "many", "messages"),
               token.getParameterMap(null)
       );

       token = new PipedToken("{count|| item, items}");

       Assert.assertEquals(
               null,
               token.getParameterMap(null)
       );

       Assert.assertEquals(
               null,
               token.getParameterMap(null)
       );

       token = new PipedToken("{count|| message}");

       Assert.assertEquals(
               Utils.buildMap("one", "message", "other", "message"),
               token.getParameterMap(Utils.buildMap("one", "{$0}", "other", "{$0::plural}"))
       );

       Assert.assertEquals(
               Utils.buildMap("one", "message", "other", "message"),
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$0::plural}"), Utils.buildMap("one", "{$0}", "other", "{$1}")))
       );

       token = new PipedToken("{count|| message, messages}");

       Assert.assertEquals(
               Utils.buildMap("one", "message", "other", "messages"),
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$0::plural}"), Utils.buildMap("one", "{$0}", "other", "{$1}")))
       );

       Assert.assertEquals(
               null,
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$0::plural}"), "undefined"))
       );

       Assert.assertEquals(
               null,
               token.getParameterMap("undefined")
       );

       Assert.assertEquals(
               null,
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$0::plural}")))
       );

       Assert.assertEquals(
               null,
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$3::plural}")))
       );

    }
}
