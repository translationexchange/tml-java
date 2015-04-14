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

import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;

/**
 * Created by michael on 3/16/14.
 */
public class TmlTest extends BaseTest {

//    @Test
    public void testIntegrationWithSandbox() {
        Tml.init("37f812fac93a71088", "a9dc95ff798e6e1d1", "https://sandbox.tr8nhub.com");
        Assert.assertEquals(
            "Hello World",
            Tml.translate("Hello World")
        );

        Tml.beginBlockWithOptions(Utils.buildMap("source", "sample"));
        
        Assert.assertEquals(
            "Hello World",
            Tml.translate("Hello World")
        );
        
        Tml.endBlock();

        Assert.assertEquals(
                "You have 5 messages",
                Tml.translate("You have {count||message}", Utils.buildMap("count", 5))
            );

        Assert.assertEquals(
                "You have 1 message",
                Tml.translate("You have {count|| one: message, other: messages}", Utils.buildMap("count", 1))
            );

        Assert.assertEquals(
                "You have 5 messages",
                Tml.translate("You have {count|| one: message, other: messages}", Utils.buildMap("count", 5))
            );
        
        Assert.assertEquals(
                "You have <strong>5 messages</strong>",
                Tml.translate("You have [indent: {count||message}]", Utils.buildMap(
                		"count", 5,
                		"indent", "<strong>{$0}</strong>"
                ))
            );

        Assert.assertEquals(
                "<strong>You</strong> have <strong>5 messages</strong>",
                Tml.translate("[indent: You] have [indent: {count||message}]", Utils.buildMap(
                		"count", 5,
                		"indent", "<strong>{$0}</strong>"
                ))
            );

        Assert.assertEquals(
                "<strong>You have <i>5 messages</i></strong> in your mailbox",
                Tml.translate("[bold: You have [italic: {count||message}]] in your mailbox", Utils.buildMap(
                		"count", 5,
                		"bold", "<strong>{$0}</strong>",
                		"italic", "<i>{$0}</i>"
                ))
            );
        
    }

}
