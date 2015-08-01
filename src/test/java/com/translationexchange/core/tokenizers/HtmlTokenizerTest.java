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

import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;
import com.translationexchange.core.tokenizers.DecorationTokenValue;
import com.translationexchange.core.tokenizers.HtmlTokenizer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/14/14.
 */
public class HtmlTokenizerTest {

    @Test
    public void testSubstitution() {
        HtmlTokenizer dt = new HtmlTokenizer("Hello [bold: World]");
        Assert.assertEquals(
                "Hello <strong>World</strong>",
                dt.substitute(Utils.buildMap("bold", "<strong>{$0}</strong>"))
        );

        Assert.assertEquals(
                "Hello <b>World</b>",
                dt.substitute(Utils.buildMap("bold", new DecorationTokenValue() {
                    public String getSubstitutionValue(String text) {
                        return "<b>" + text + "</b>";
                    }
                }))
        );

        // using default decorators
        Assert.assertEquals(
                "Hello <strong>World</strong>",
                dt.substitute(Utils.buildMap())
        );

        Assert.assertEquals(
                "Hello <strong>World</strong>",
                dt.substitute(null)
        );

        Tml.getConfig().addDefaultTokenValue("link", "decoration", "html", "<a href=\"{$href}\">{$0}</a>");

        dt.tokenize("[link]you have {count} messages[/link]");
        Assert.assertEquals(
            "<a href=\"www.google.com\">you have {count} messages</a>",
            dt.substitute(Utils.buildMap("link", Utils.buildMap("href", "www.google.com")))
        );

        dt.tokenize("<link>you have {count} messages</link>");
        Assert.assertEquals(
            "<a href=\"www.google.com\">you have {count} messages</a>",
            dt.substitute(Utils.buildMap("link", Utils.buildMap("href", "www.google.com")))
        );
        
        dt.tokenize("[link]you have [italic: [bold: {count}] messages] [light: in your mailbox][/link]");
        Assert.assertEquals(
                "<a href=\"www.google.com\">you have <i><b>{count}</b> messages</i> <l>in your mailbox</l></a>",
                dt.substitute(Utils.buildMap(
                        "link", Utils.buildMap("href", "www.google.com"),
                        "italic", "<i>{$0}</i>",
                        "bold", "<b>{$0}</b>",
                        "light", "<l>{$0}</l>"
                ))
        );

        dt.tokenize("<link>you have <italic><bold>{count}</bold> messages</italic> [light: in your mailbox]</link>");
        Assert.assertEquals(
                "<a href=\"www.google.com\">you have <i><b>{count}</b> messages</i> <l>in your mailbox</l></a>",
                dt.substitute(Utils.buildMap(
                        "link", Utils.buildMap("href", "www.google.com"),
                        "italic", "<i>{$0}</i>",
                        "bold", "<b>{$0}</b>",
                        "light", "<l>{$0}</l>"
                ))
        );        

        dt.tokenize("[custom: test]");
        Assert.assertEquals(
                "test",
                dt.substitute(null)
        );

    }
}
