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

        Tr8n.beginBlockWithOptions(Utils.buildMap("source", "sample"));
        
        Assert.assertEquals(
            "Hello World",
            Tr8n.translate("Hello World")
        );
        
        Tr8n.endBlock();

        Assert.assertEquals(
                "You have 5 messages",
                Tr8n.translate("You have {count||message}", Utils.buildMap("count", 5))
            );

        Assert.assertEquals(
                "You have 1 message",
                Tr8n.translate("You have {count|| one: message, other: messages}", Utils.buildMap("count", 1))
            );

        Assert.assertEquals(
                "You have 5 messages",
                Tr8n.translate("You have {count|| one: message, other: messages}", Utils.buildMap("count", 5))
            );
        
        Assert.assertEquals(
                "You have <strong>5 messages</strong>",
                Tr8n.translate("You have [indent: {count||message}]", Utils.buildMap(
                		"count", 5,
                		"indent", "<strong>{$0}</strong>"
                ))
            );

        Assert.assertEquals(
                "<strong>You</strong> have <strong>5 messages</strong>",
                Tr8n.translate("[indent: You] have [indent: {count||message}]", Utils.buildMap(
                		"count", 5,
                		"indent", "<strong>{$0}</strong>"
                ))
            );

        Assert.assertEquals(
                "<strong>You have <i>5 messages</i></strong> in your mailbox",
                Tr8n.translate("[bold: You have [italic: {count||message}]] in your mailbox", Utils.buildMap(
                		"count", 5,
                		"bold", "<strong>{$0}</strong>",
                		"italic", "<i>{$0}</i>"
                ))
            );
        
    }

}
