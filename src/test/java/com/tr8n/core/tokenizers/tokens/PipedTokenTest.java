package com.tr8n.core.tokenizers.tokens;

import com.tr8n.core.tokenizers.tokens.DataToken;
import com.tr8n.core.tokenizers.tokens.PipedToken;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/12/14.
 */
public class PipedTokenTest {

    @Test
    public void testTokenNames() {
        PipedToken token = new PipedToken("{count|| item, items}");

        Assert.assertEquals(
                "{count|| item, items}",
                token.getFullName()
        );

    }

}
