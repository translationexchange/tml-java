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
import com.translationexchange.core.tokenizers.DataTokenValue;
import com.translationexchange.core.tokens.DataToken;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DataTokenTest {

    @Test
    public void testTokenNames() {
        DataToken token = new DataToken("{user}");

        Assert.assertEquals(
                "{user}",
                token.getFullName()
        );

        Assert.assertEquals(
                "user",
                token.getName()
        );

        Assert.assertEquals(
                "user",
                token.getObjectName()
        );

        token = new DataToken("{ user }");

        Assert.assertEquals(
                "{ user }",
                token.getFullName()
        );

        Assert.assertEquals(
                "user",
                token.getName()
        );

        Assert.assertEquals(
                "user",
                token.getName(Utils.map())
        );

        Assert.assertEquals(
                "{user}",
                token.getName(Utils.map("parens", true))
        );

    }

    @Test
    public void testTokenContexts() {
        DataToken token = new DataToken("{user}");

        token = new DataToken("{user:gender}");
        Assert.assertEquals(
                Arrays.asList("gender"),
                token.getLanguageContextKeys()
        );

        token = new DataToken("{user : gender}");
        Assert.assertEquals(
                Arrays.asList("gender"),
                token.getLanguageContextKeys()
        );

        token = new DataToken("{ user : gender }");
        Assert.assertEquals(
                Arrays.asList("gender"),
                token.getLanguageContextKeys()
        );

        token = new DataToken("{user:gender:value}");
        Assert.assertEquals(
                Arrays.asList("gender", "value"),
                token.getLanguageContextKeys()
        );

        token = new DataToken("{user : gender : value}");
        Assert.assertEquals(
                Arrays.asList("gender", "value"),
                token.getLanguageContextKeys()
        );

        Assert.assertEquals(
                Arrays.asList("gender", "value"),
                token.getLanguageContextKeys()
        );
    }

    @Test
    public void testTokenCases() {
        DataToken token = new DataToken("{count::ord}");

        Assert.assertEquals(
                Arrays.asList("ord"),
                token.getLanguageCaseKeys()
        );

        token = new DataToken("{count::ordinal::ord}");
        Assert.assertEquals(
                Arrays.asList("ordinal", "ord"),
                token.getLanguageCaseKeys()
        );

        token = new DataToken("{count :: ordinal :: ord}");
        Assert.assertEquals(
                Arrays.asList("ordinal", "ord"),
                token.getLanguageCaseKeys()
        );

        token = new DataToken("{count :number ::ordinal ::ord}");
        Assert.assertEquals(
                Arrays.asList("number"),
                token.getLanguageContextKeys()
        );

        Assert.assertEquals(
                Arrays.asList("ordinal", "ord"),
                token.getLanguageCaseKeys()
        );

        Assert.assertEquals(
                Arrays.asList("ordinal", "ord"),
                token.getLanguageCaseKeys()
        );

        Assert.assertEquals(
                "{count}",
                token.getName(Utils.map("parens", true))
        );

        Assert.assertEquals(
                "{count:number}",
                token.getName(Utils.map("parens", true, "context_keys", true))
        );

        Assert.assertEquals(
                "{count::ordinal::ord}",
                token.getName(Utils.map("parens", true, "case_keys", true))
        );

        Assert.assertEquals(
                "{count:number::ordinal::ord}",
                token.getName(Utils.map("parens", true, "context_keys", true, "case_keys", true))
        );

        Assert.assertEquals(
                "count:number::ordinal::ord",
                token.getName(Utils.map("parens", false, "context_keys", true, "case_keys", true))
        );
    }

    @Test
    public void testTokenValue() {
        DataToken token = new DataToken("{count}");
        Assert.assertEquals(
                "{count}",
                token.getValue(Utils.map("user", "Michael"))
        );

        Assert.assertEquals(
                "1",
                token.getValue(Utils.map("count", "1"))
        );

        Assert.assertEquals(
                "1",
                token.getValue(Utils.map("count", 1))
        );

        Assert.assertEquals(
                "01",
                token.getValue(Utils.map("count", Utils.buildList(1, "01")))
        );

        Assert.assertEquals(
                "Hello",
                token.getValue(Utils.map("count", new DataTokenValue() {
                    public Object getContextObject() {
                        return 1;
                    }

                    public String getSubstitutionValue() {
                        return "Hello";
                    }
                }))
        );

        final User user = new User("Michael", "male");
        token = new DataToken("{user}");
        Assert.assertEquals(
                "Michael",
                token.getValue(Utils.map("user", user))
        );

        token = new DataToken("{user}");
        Assert.assertEquals(
                "Michael",
                token.getValue(Utils.map("user", new DataTokenValue() {
                    public Object getContextObject() {
                        return user;
                    }

                    public String getSubstitutionValue() {
                        return user.getFirstName();
                    }
                }))
        );

        Assert.assertEquals(
                "Berk",
                token.getValue(Utils.map("user",  Utils.buildList(user, "Berk")))
        );

        Assert.assertEquals(
                "{user}",
                token.getValue(Utils.map("user",  Utils.buildList(user)))
        );

        Assert.assertEquals(
                "Michael",
                token.getValue(Utils.map("user",  Utils.map(
                        "object", Utils.map(
                            "name", "Michael",
                            "gender", "male"
                        ),
                        "attribute", "name"
                       )
                    )
                )
        );

        Assert.assertEquals(
                "Michael",
                token.getValue(Utils.map("user",  Utils.map(
                        "object", Utils.map(
                            "name", "Michael",
                            "gender", "male"
                        ),
                        "property", "name"
                    )
                )
            )
        );

        Assert.assertEquals(
                "Mike",
                token.getValue(Utils.map("user",  Utils.map(
                        "object", Utils.map(
                            "name", "Michael",
                            "gender", "male"
                        ),
                        "value", "Mike"
                    )
                )
            )
        );

        Assert.assertEquals(
                "{user}",
                token.getValue(Utils.map("user",  Utils.map(
                        "object", Utils.map(
                           "name", "Michael",
                            "gender", "male"
                        )
                )
             )
           )
        );


        Assert.assertEquals(
                "{user}",
                token.getValue(Utils.map("user",  Utils.map(
                        "object", user,
                        "property", "name"
                )
             )
          )
        );

        Assert.assertEquals(
                "{user}",
                token.getValue(Utils.map("user",  Utils.map()))
        );

        token = new DataToken("{nbsp}");
        Assert.assertEquals(
                "nbsp",
                token.getValue(Utils.map("nbsp", "nbsp"))
        );
        Assert.assertEquals(
                "&nbsp;",
                token.getValue(null)
        );
        Assert.assertEquals(
                "&nbsp;",
                token.getValue(Utils.map("abc", "abc"))
        );
    }

    @Test
    public void testContextObject() {
        DataToken token = new DataToken("{count::ord}");

        Assert.assertEquals(
                null,
                token.getContextObject(Utils.map())
        );

        Assert.assertEquals(
                null,
                token.getContextObject(null)
        );

        Assert.assertEquals(
                1,
                token.getContextObject(Utils.map("count", 1))
        );

        Assert.assertEquals(
                10,
                token.getContextObject(Utils.map("count", new DataTokenValue() {
                    public Object getContextObject() {
                        return 10;
                    }

                    public String getSubstitutionValue() {
                        return "10";
                    }
                }))
        );

        Assert.assertEquals(
                1,
                token.getContextObject(Utils.map("count", Utils.buildList(1, 1)))
        );


        Assert.assertEquals(
                1,
                token.getContextObject(Utils.map("count", Utils.map("object", 1)))
        );

        Assert.assertEquals(
                Utils.map("a","b"),
                token.getContextObject(Utils.map("count", Utils.map("a","b")))
        );

    }

}
