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

import com.translationexchange.core.Language;
import com.translationexchange.core.Translation;
import com.translationexchange.core.TranslationKey;
import com.translationexchange.core.Utils;

/**
 * Created by michael on 3/15/14.
 */
public class TranslationTest extends BaseTest {

    @Test
    public void testCreation() {
        Language russian = new Language(loadJSONMap("/languages/ru.json"));

        TranslationKey tkey = new TranslationKey(Utils.buildMap(
                "key", "d541c79af1be6a05b1f16fca8b5730de",
                "label", "Hello World"
        ));

        Translation t = new Translation(Utils.buildMap(
                "translation_key", tkey,
                "language", russian,
                "label", "Привет Мир",
                "description", ""
        ));

        Assert.assertEquals(
                "Привет Мир",
                t.getLabel()
        );

        Assert.assertEquals(
                russian,
                t.getLanguage()
        );

        Assert.assertEquals(
                false,
                t.hasContext()
        );

        Assert.assertEquals(
                tkey,
                t.getTranslationKey()
        );

    }

    @Test
    public void testManualCreation() {
        Language russian = new Language(loadJSONMap("/languages/ru.json"));

        TranslationKey tkey = new TranslationKey(Utils.buildMap(
                "label", "You have {count|| message}."
        ));

        Translation t = new Translation();
        t.setLanguage(russian);
        t.setTranslationKey(tkey);
        t.setLabel("У вас есть {count} сообщение.");
        t.setContext(Utils.buildMap());

        Assert.assertEquals(
                false,
                t.hasContext()
        );

        Assert.assertEquals(
                true,
                t.isValidTranslationForTokens(Utils.buildMap("count", "1"))
        );

        t.setContext(Utils.buildMap("count", Utils.buildStringMap("number", "one")));

        Assert.assertEquals(
                true,
                t.hasContext()
        );

        Assert.assertEquals(
                true,
                t.isValidTranslationForTokens(Utils.buildMap("count", "1"))
        );

        t.setContext(Utils.buildMap("count", Utils.buildMap("number", "other")));

        Assert.assertEquals(
                true,
                t.isValidTranslationForTokens(Utils.buildMap("count", "1"))
        );

        t.setContext(Utils.buildMap("count", Utils.buildMap("abc", "few")));

        Assert.assertEquals(
                false,
                t.isValidTranslationForTokens(Utils.buildMap("count", "1"))
        );

        t.setContext(Utils.buildMap("count", Utils.buildMap("number", "few")));

        Assert.assertEquals(
                false,
                t.isValidTranslationForTokens(Utils.buildMap("count", "1"))
        );

        t.setContext(Utils.buildMap("count", Utils.buildMap("number", "one")));

        Assert.assertEquals(
                false,
                t.isValidTranslationForTokens(Utils.buildMap("counts", "1"))
        );

    }

}
