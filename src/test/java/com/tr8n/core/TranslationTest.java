package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

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
