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

import java.util.Observable;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;
import com.translationexchange.core.dummy.DummyApplication;

/**
 * Created by michael on 3/16/14.
 */
public class TmlTest extends BaseTest {

    @BeforeClass
    public static void configureTml() {
        Tml.getConfig().setApplicationClass("com.translationexchange.core.dummy.DummyApplication");
    }

    @Test
    public void testCreation() {
        Tml.init("application.json", "37f812fac93a71088");
        Assert.assertTrue(Tml.getSession() instanceof Session);
        Assert.assertTrue(Tml.getApplication() instanceof Application);
        Assert.assertTrue(Tml.getApplication() instanceof DummyApplication); // check
                                                                             // mock
                                                                             // properly
        Assert.assertEquals("37f812fac93a71088", Tml.getApplication().getAccessToken());
        Assert.assertTrue(Tml.isSchedulerRunning());
        Assert.assertEquals("en-US", Tml.getCurrentLanguage().getLocale());
        Assert.assertEquals(null, Tml.getCurrentTranslator());
    }

    @Test
    public void testSwitchLanguage() {
        Tml.init("application.json", "37f812fac93a71088");
        Assert.assertEquals("en-US", Tml.getCurrentLanguage().getLocale());

        Tml.switchLanguage(Tml.getApplication().getLanguage("da"));
        Assert.assertEquals("da", Tml.getCurrentLanguage().getLocale());

        Tml.switchLanguage(Tml.getApplication().getLanguage("en-US"), Utils.buildMap("foo", "bar"));
        Assert.assertEquals("en-US", Tml.getCurrentLanguage().getLocale());
    }

    @Test
    public void testSwitchSource() {
        Tml.init("application.json", "37f812fac93a71088");
        Assert.assertEquals(null, Tml.getCurrentSource());

        Tml.setCurrentSource("index");
        Assert.assertEquals("index", Tml.getCurrentSource());
    }

    @Test
    public void testBeginBlockWithOptions() {
        Tml.init("application.json", "37f812fac93a71088");
        Tml.setCurrentSource("index");
        Tml.beginBlockWithOptions(Utils.buildMap("source", "navigation"));
        Assert.assertEquals(Utils.buildList("index", "navigation"), Tml.getSession().getSourcePath());
        Assert.assertEquals(Utils.buildMap("source", "navigation"), Tml.getBlockOptions());
        Tml.endBlock();
        Assert.assertEquals(Utils.buildMap(), Tml.getBlockOptions());
    }

    @Test
    public void testTranslator() {
        Tml.init("application.json", "37f812fac93a71088");
        Tml.setCurrentTranslator(new Translator(Utils.buildMap("name", "xepa4ep", "email", "r.kamun@gmail.com",
                "inline", true)));
        Assert.assertEquals("xepa4ep", Tml.getCurrentTranslator().getName());
    }

    @Test
    public void testInitSource() {
        Tml.init("application.json", "37f812fac93a71088");
        Tml.initSource("index");
        Assert.assertEquals("index",
                Tml.getApplication().getSource("index", Tml.getCurrentLanguage().getLocale(), Utils.buildMap())
                        .getKey());
    }

    @Test
    public void testInitLanguage() {
        Tml.init("application.json", "37f812fac93a71088");
        Tml.initLanguage("unknown");
        Assert.assertEquals("unknown", Tml.getApplication().getLanguage("unknown").getLocale());

    }

    @Test
    public void testIntegrationWithSampleApp() {
        Tml.init("application.json", "37f812fac93a71088");
//        Assert.assertEquals("Hello World", Tml.translate("Hello World"));
//
//        Tml.beginBlockWithOptions(Utils.buildMap("source", "sample"));
//
//        Assert.assertEquals("Hello World", Tml.translate("Hello World"));
//
//        Tml.endBlock();
//
//        Assert.assertEquals("You have 5 messages",
//                Tml.translate("You have {count||message}", Utils.buildMap("count", 5)));
//
//        Assert.assertEquals("You have 1 message",
//                Tml.translate("You have {count|| one: message, other: messages}", Utils.buildMap("count", 1)));
//
//        Assert.assertEquals("You have 5 messages",
//                Tml.translate("You have {count|| one: message, other: messages}", Utils.buildMap("count", 5)));
//
//        Assert.assertEquals(
//                "You have <strong>5 messages</strong>",
//                Tml.translate("You have [indent: {count||message}]",
//                        Utils.buildMap("count", 5, "indent", "<strong>{$0}</strong>")));
//
//        Assert.assertEquals(
//                "<strong>You</strong> have <strong>5 messages</strong>",
//                Tml.translate("[indent: You] have [indent: {count||message}]",
//                        Utils.buildMap("count", 5, "indent", "<strong>{$0}</strong>")));
//
//        Assert.assertEquals(
//                "<strong>You have <i>5 messages</i></strong> in your mailbox",
//                Tml.translate("[bold: You have [italic: {count||message}]] in your mailbox",
//                        Utils.buildMap("count", 5, "bold", "<strong>{$0}</strong>", "italic", "<i>{$0}</i>")));
    }

    // }

    @AfterClass
    public static void deconfigureTml() {
        Tml.getConfig().setApplicationClass("com.translationexchange.core.Application");
    }

}
