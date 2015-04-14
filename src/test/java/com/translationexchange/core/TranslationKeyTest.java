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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.translationexchange.core.Translation;
import com.translationexchange.core.TranslationKey;
import com.translationexchange.core.Utils;

/**
 * Created by michael on 3/11/14.
 */
public class TranslationKeyTest extends BaseTest {

    @Test
    public void testKeyGeneration() {
        Assert.assertEquals(
                "d541c79af1be6a05b1f16fca8b5730de",
                TranslationKey.generateKey("Hello World")
        );
    }

    @Test
    public void testCreation() {
        TranslationKey tkey = new TranslationKey(Utils.buildMap(
                "key", "d541c79af1be6a05b1f16fca8b5730de",
                "label", "Hello World",
                "description", ""
        ));

        Assert.assertEquals(
                "d541c79af1be6a05b1f16fca8b5730de",
                tkey.getKey()
        );

        Assert.assertEquals(
                "Hello World",
                tkey.getLabel()
        );

        Assert.assertEquals(
                "",
                tkey.getDescription()
        );

        List<Translation> translations = new ArrayList<Translation>();
        translations.add(new Translation(Utils.buildMap("locale", "ru", "label", "Privet Mir")));
        tkey.addTranslations(translations);
        
        Assert.assertEquals(
                1,
                tkey.getTranslations("ru").size()
        );
        
    }

}
