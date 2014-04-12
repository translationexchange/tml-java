package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/16/14.
 */
public class Tr8nTest extends BaseTest {

    @Test
    public void testIntegrationWithSandbox() {
        Tr8n.init("37f812fac93a71088", "a9dc95ff798e6e1d1", "https://sandbox.tr8nhub.com");
        Assert.assertEquals(
            "Hello World",
            Tr8n.translate("Hello World")
        );

//        Tr8n.setCurrentLocale("ru");
//        Assert.assertEquals(
//                "Привет Мир",
//                Tr8n.tr("Hello World")
//        );

    }

}
