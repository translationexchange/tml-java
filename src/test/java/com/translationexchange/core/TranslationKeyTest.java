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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.reflect.Whitebox.invokeMethod;

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
    
    private Language ru, en, unk;
    
    private List<TranslationKey> loadKeys(String locale, String resourceName) {
        String resource = String.join("/", "/translations", locale, resourceName);
        List<Map<String, Object>> data = (List<Map<String, Object>>)loadJSON(resource);
        List<TranslationKey> transKeys = new ArrayList<TranslationKey>();
        for(Map<String, Object> transItem : data) {
            TranslationKey tKey = new TranslationKey(Utils.buildMap("label", transItem.get("label"), "locale", "en-US"));
            List<Translation> trans = new ArrayList<Translation>();
            
            List<Map<String, Object>> keyTransData = ((Map<String, List<Map<String, Object>>>) transItem.get("translations")).get("ru");
            for(Map<String, Object> tranItem : keyTransData) {
                tranItem.put("locale", "ru");
                tranItem.put("language", ru);
                tranItem.put("translation_key", tKey);
                trans.add(new Translation(tranItem));
            }
            tKey.setTranslations("ru", trans);
            transKeys.add(tKey);
        }
        return transKeys;
    }
    
    @Before
    public void initObjects() {
        ru = new Language(loadJSONMap("/languages/ru.json"));
        en = new Language(loadJSONMap("/languages/en-US.json"));
        unk = new Language(Utils.buildMap("locale", "unknown"));
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
        
        TranslationKey emptyKey = new TranslationKey();
        Assert.assertTrue(emptyKey instanceof TranslationKey);
        
        Map<String, Object> translationData = loadJSONMap("/translations/ru/gender.json");
        TranslationKey fullKey = new TranslationKey(loadJSONMap("/translations/ru/gender.json"));
        Assert.assertTrue(fullKey.getTranslations("ru").size() == 0);
        
        Application app = new Application(loadJSONMap("/application.json"));
        translationData.put("application", app);
        TranslationKey fullKeyWithApp = new TranslationKey(translationData);
        Assert.assertTrue(fullKeyWithApp.getTranslations("ru").size() > 0);
    }
    
    @Test
    public void testMisc() {
        TranslationKey tkey = new TranslationKey(Utils.buildMap(
                "label", "Hello World",
                "description", "Greeting"
        ));
        Assert.assertEquals("Hello World" + " (" + "en-US" + ")", tkey.toString());
        
        Assert.assertFalse(tkey.isLocked());
        
        tkey.setLocked(true);
        Assert.assertTrue(tkey.isLocked());
        
        Assert.assertFalse(tkey.hasTranslations());
        
        Assert.assertEquals(
                Utils.buildMap("label", "Hello World", "description", "Greeting", "locale", "en-US"),
                tkey.toMap());
        
        tkey.setAllowedDataTokenNames(Arrays.asList(new String[]{"target", "user"}));
        Assert.assertEquals(
                Arrays.asList(new String[]{"target", "user"}),
                tkey.getAllowedDataTokenNames());
        
        Assert.assertEquals(null, tkey.getLevel());
        
        Assert.assertEquals(null, tkey.getApplication());
    }
        
    @Test
    public void testTranslate() {
        final User user = new User("Анна", "female");
        final User target = new User("Майкл", "male");
        List<TranslationKey> keys = loadKeys("ru", "genders.json");
        TranslationKey keyWithCtx = PowerMockito.spy(keys.get(0));
        Assert.assertEquals("Анна любезно дала тебе 2 яблока",
                             keyWithCtx.translate(ru, Utils.buildMap("actor", user, "count", "2"), Utils.buildMap()));
        
        TranslationKey simpleKey = keys.get(1);
        Assert.assertEquals("{actor} любит {target::gen}.",
                            simpleKey.translate(ru, Utils.buildMap("actor", user, "target", target)));
        
        TranslationKey noTokensKey = keys.get(3);
        Assert.assertEquals("Привет мир.", noTokensKey.translate(ru));
    }
    
    @Test
    public void testFindFirstAcceptableTrans() throws Exception{
        List<TranslationKey> keys = loadKeys("ru", "genders.json");
        TranslationKey simpleKey = PowerMockito.spy(keys.get(1));
        String actualLabel = ((Translation) invokeMethod(simpleKey, "findFirstAcceptableTranslation", ru, Utils.buildMap())).getLabel();
        Assert.assertEquals("{actor} любит {target::gen}.", actualLabel);
        
        Assert.assertNull(invokeMethod(simpleKey, "findFirstAcceptableTranslation", unk, Utils.buildMap()));
        
        final User user = new User("Анна", "female");
        TranslationKey keyWithCtx = PowerMockito.spy(keys.get(0));
        String transLabel = ((Translation) invokeMethod(
                keyWithCtx, "findFirstAcceptableTranslation", ru, Utils.buildMap("actor", user))
        ).getLabel();
        Assert.assertEquals("{actor} любезно дала тебе {count||яблоко, яблока, яблок}", transLabel);
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
