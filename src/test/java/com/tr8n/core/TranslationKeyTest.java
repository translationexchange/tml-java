package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

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


    }

}
