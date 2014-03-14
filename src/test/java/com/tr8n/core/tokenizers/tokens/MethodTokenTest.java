package com.tr8n.core.tokenizers.tokens;

import com.tr8n.core.Utils;
import com.tr8n.core.models.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/13/14.
 */
public class MethodTokenTest {

    @Test
    public void testTokenNames() {
        MethodToken token = new MethodToken("{user.name}");

        Assert.assertEquals(
                "{user.name}",
                token.getFullName()
        );

        Assert.assertEquals(
                "user.name",
                token.getName()
        );

        Assert.assertEquals(
                "user",
                token.getObjectName()
        );

        Assert.assertEquals(
                "user",
                token.getObjectName()
        );

        Assert.assertEquals(
                "name",
                token.getMethodName()
        );

    }

    @Test
    public void testObjectValue() {
        MethodToken token = new MethodToken("{user.name}");

        Assert.assertEquals(
                "{user.name}",
                token.getObjectValue(null, "name")
        );

        Assert.assertEquals(
                "Michael",
                token.getObjectValue(Utils.buildMap("name", "Michael"), "name")
        );

        Assert.assertEquals(
                "{user.name}",
                token.getObjectValue(Utils.buildMap("name", "Michael"), "wrongname")
        );

        User user = new User("Michael", "male");

        Assert.assertEquals(
                "{user.name}",
                token.getObjectValue(user, "wrongname")
        );

        Assert.assertEquals(
                "Michael",
                token.getObjectValue(user, "getFirstName")
        );

    }

}
