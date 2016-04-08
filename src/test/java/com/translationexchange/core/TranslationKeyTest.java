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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.translationexchange.core.Translation;
import com.translationexchange.core.TranslationKey;
import com.translationexchange.core.Utils;
import com.translationexchange.core.languages.Language;
import com.translationexchange.core.models.User;
import com.translationexchange.core.tokenizers.Tokenizer;

/**
 * Created by michael on 3/11/14.
 */

final class DummyTokenizer extends Tokenizer {

    @Override
    protected void tokenize() {
        // TODO Auto-generated method stub
    }
    
    public static boolean isApplicable(String label) {
        return false;
    }

    @Override
    public Object substitute(Map<String, Object> tokensData, Language language, Map<String, Object> options) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

@RunWith(PowerMockRunner.class)
@PrepareForTest(TranslationKey.class)
public class TranslationKeyTest extends BaseTest {
    
    private Language ru, en;
    
    @Before
    public void initObjects() {
        ru = new Language(loadJSONMap("/languages/ru.json"));
        en = new Language(loadJSONMap("/languages/en-US.json"));
    }
    
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
    
    @Test
    public void testApplyTokenizer() throws Exception {
        final User user = new User("Michael", "male");
        TranslationKey tkey = new TranslationKey(Utils.buildMap(
                "key", "d541c79af1be6a05b1f16fca8b5730de",
                "label", "Hello {user}",
                "description", ""));
        Map<String, Object> tokens = Utils.buildMap("user", user);
        Assert.assertEquals(
                "Hello Michael",
                tkey.applyTokenizer(TranslationKey.DEFAULT_TOKENIZERS_DATA, tkey.getLabel(), ru, null, tokens, Utils.buildMap()));
        
        TranslationKey spiedKey = PowerMockito.spy(tkey);
        PowerMockito.doReturn(DummyTokenizer.class).when(spiedKey, "getTokenizerByKey", "dummy_tokenizer");
        Assert.assertEquals(
                "Hello {user}",
                spiedKey.applyTokenizer("dummy_tokenizer", tkey.getLabel(), ru, null, tokens, Utils.buildMap()));
        Assert.assertEquals(
                "Hello {user}",
                tkey.applyTokenizer("dummy_tokenizer", tkey.getLabel(), ru, null, tokens, Utils.buildMap()));
    }
    
    @Test
    public void testSubstitutions() throws Exception {
        final User user = new User("Michael", "male");
        TranslationKey tkey = new TranslationKey(Utils.buildMap(
                "key", "d541c79af1be6a05b1f16fca8b5730de",
                "label", "{user|He,She} has {count} items.",
                "description", ""));
        Map<String, Object> tokens = Utils.buildMap("user", user, "count", 2);
        Assert.assertEquals(
                "He has 2 items.",
                tkey.substitute(tkey.getLabel(), tokens, ru, en, Utils.buildMap())
        );
        
        TranslationKey tkey2 = new TranslationKey(Utils.buildMap(
                "label", "[link]you have {count} messages[/link]",
                "description", ""));
        Assert.assertEquals(
                "<a href='www.google.com'>you have {count} messages</a>",
                tkey2.substitute(tkey2.getLabel(),
                                Utils.buildMap("link", Utils.buildMap("href", "www.google.com")),
                                ru, en, Utils.buildMap("tokenizer", "html"))
        );
        
    }

}
