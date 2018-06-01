/*
 * Copyright (c) 2018 Translation Exchange, Inc. All rights reserved.
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
 *
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core;

import com.translationexchange.core.languages.Language;
import com.translationexchange.core.rulesengine.Variable;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class ConfigurationTest {

  @Test
  public void testRulesEngineVariables() {
    Configuration config = new Configuration();

    Variable var = config.getContextVariable("number", "@n");
    Assert.assertEquals(
        1,
        var.getValue(null, 1)
    );

    var = config.getContextVariable("gender", "@gender");
    Assert.assertEquals(
        "male",
        var.getValue(null, Utils.map("gender", "male"))
    );

    var = config.getContextVariable("gender", "@gender");
    Assert.assertEquals(
        "female",
        var.getValue(null, Utils.map("object", Utils.map("gender", "female", "name", "Michael"), "attribute", "name"))
    );

    var = config.getContextVariable("genders", "@genders");
    Assert.assertEquals(
        Utils.buildList("male"),
        var.getValue(null, Utils.map("gender", "male"))
    );

    Date today = new Date();

    var = config.getContextVariable("date", "@date");
    Assert.assertEquals(
        today,
        var.getValue(null, today)
    );

    var = config.getContextVariable("list", "@count");
    Assert.assertEquals(
        1,
        var.getValue(null, Utils.buildList("one"))
    );

    Assert.assertEquals(
        2,
        var.getValue(null, Utils.buildList("one", "two"))
    );

    Assert.assertEquals(
        1,
        var.getValue(null, "one")
    );
  }

  @Test
  public void testDefaultTokenValues() {
    Configuration config = new Configuration();

    Assert.assertEquals(
        "&nbsp;",
        config.getDefaultTokenValue("nbsp")
    );

    Assert.assertEquals(
        "&nbsp;",
        config.getDefaultTokenValue("nbsp", "data")
    );

    Assert.assertEquals(
        "&nbsp;",
        config.getDefaultTokenValue("nbsp", "data", "html")
    );

    Assert.assertEquals(
        " ",
        config.getDefaultTokenValue("nbsp", "data", "text")
    );

    Assert.assertEquals(
        "<strong>{$0}</strong>",
        config.getDefaultTokenValue("strong", "decoration", "html")
    );

    config.addDefaultTokenValue("test", "data", "html", "hello world");

    Assert.assertEquals(
        "hello world",
        config.getDefaultTokenValue("test", "data", "html")
    );

  }

  @Test
  public void testGetDefaultLanguage() {
    Configuration config = new Configuration();
    Language defaultLanguage = config.getDefaultLanguage();
    Assert.assertEquals("en", defaultLanguage.getLocale());
    Assert.assertEquals("English", defaultLanguage.getEnglishName());
    Assert.assertNotNull(defaultLanguage.getLanguageCaseByKeyword("ord"));
  }

}
