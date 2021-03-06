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

package com.translationexchange.core.tokenizers.tokens;

import com.translationexchange.core.Utils;
import com.translationexchange.core.models.User;
import com.translationexchange.core.tokens.MethodToken;

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
                token.getObjectValue(Utils.map("name", "Michael"), "name")
        );

        Assert.assertEquals(
                "{user.name}",
                token.getObjectValue(Utils.map("name", "Michael"), "wrongname")
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
