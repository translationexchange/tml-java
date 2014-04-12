package com.tr8n.core.tokenizers.tokens;

import org.junit.Assert;
import org.junit.Test;

import com.tr8n.core.Utils;

/**
 * Created by michael on 3/12/14.
 */
public class MustacheTokenTest {

    @Test
    public void testTokenNames() {
        MustacheToken token = new MustacheToken("{{user}}");

        Assert.assertEquals(
                "{{user}}",
                token.getFullName()
        );

        Assert.assertEquals(
                "user",
                token.getName()
        );

        Assert.assertEquals(
                "{{user}}",
                token.getName(Utils.buildMap("parens", true))
        );

    }

}
