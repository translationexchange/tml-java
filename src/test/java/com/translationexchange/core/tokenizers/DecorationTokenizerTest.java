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

import com.translationexchange.core.Utils;
import com.translationexchange.core.tokenizers.DecorationTokenizer;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by michael on 3/14/14.
 */
public class DecorationTokenizerTest {

    @Test
    public void testTokenization() {
        DecorationTokenizer dt = new DecorationTokenizer("Hello [bold: World]");

        Assert.assertEquals(
                Arrays.asList("bold"),
                dt.getTokenNames()
        );

        dt = new DecorationTokenizer("Hello [bold: How are [italic: you?]]");

        Assert.assertEquals(
                Arrays.asList("bold", "italic"),
                dt.getTokenNames()
        );


        // broken
        dt.tokenize("[bold: Hello World");
        Assert.assertEquals(
                Arrays.asList("bold"),
                dt.getTokenNames()
        );

        dt.tokenize("[bold: Hello [strong: World]]");
        Assert.assertEquals(
                Arrays.asList("bold", "strong"),
                dt.getTokenNames()
        );

        // broken
        dt.tokenize("[bold: Hello [strong: World]");
        Assert.assertEquals(
                Arrays.asList("bold", "strong"),
                dt.getTokenNames()
        );

        dt.tokenize("[bold1: Hello [strong22: World]]");
        Assert.assertEquals(
                Arrays.asList("bold1", "strong22"),
                dt.getTokenNames()
        );

        dt.tokenize("[bold: Hello, [strong: how] [weak: are] you?]");
        Assert.assertEquals(
                Arrays.asList("bold", "strong", "weak"),
                dt.getTokenNames()
        );

        dt.tokenize("[bold: Hello, [strong: how [weak: are] you?]");
        Assert.assertEquals(
                Arrays.asList("bold", "strong", "weak"),
                dt.getTokenNames()
        );

        dt.tokenize("[link: you have [italic: [bold: {count}] messages] [light: in your mailbox]]");
        Assert.assertEquals(
                Arrays.asList("link", "italic", "bold", "light"),
                dt.getTokenNames()
        );

        dt.tokenize("[link] you have [italic: [bold: {count}] messages] [light: in your mailbox] [/link]");
        Assert.assertEquals(
                Arrays.asList("link", "italic", "bold", "light"),
                dt.getTokenNames()
        );
    }

    @Test
    public void testSubstitution() {
        DecorationTokenizer dt = new DecorationTokenizer("Hello [bold: World]");
        Assert.assertEquals(
                "Hello World",
                dt.substitute(Utils.buildMap())
        );

        dt.tokenize("[link] you have [italic: [bold: {count}] messages] [light: in your mailbox] [/link]");
        Assert.assertEquals(
                " you have {count} messages in your mailbox ",
                dt.substitute(Utils.buildMap())
        );

        dt.tokenize("[link]you have [italic: [bold: {count}] messages] [light: in your mailbox][/link]");
        Assert.assertEquals(
                "you have {count} messages in your mailbox",
                dt.substitute(Utils.buildMap())
        );
    }

}
