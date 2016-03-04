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

package com.translationexchange.core;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationTest extends BaseTest {

    @Test
    public void testCreatingApplication() {
    	Application app = new Application();
        Assert.assertNull(
                app.getName()
        );

        Assert.assertNull(
                app.getAccessToken()
        );
        
        app.setAccessToken("abc");

        Assert.assertEquals(
	    		"abc",
	            app.getAccessToken()
        );
        
        app = new Application(loadJSONMap("/application.json"));

        Assert.assertEquals(
                "Tr8n Translation Service",
                app.getName()
        );

        Assert.assertEquals(
                "https://api.translationexchange.com",
                app.getHost()
        );

        Assert.assertEquals(
                "default",
                app.getKey()
        );

        Assert.assertEquals(
                "",
                app.getDescription()
        );

        Assert.assertEquals(
                Utils.buildMap(
                        "data", Utils.buildMap(
                            "nbsp", "&nbsp;"
                        ),
                        "decoration", Utils.buildMap(
                            "link", "<a href=\"{$href}\">{$0}</a>",
                            "strong", "<strong>{$0}</strong>"
                        )
                ),
                app.getTokens()
        );

        Assert.assertEquals(
                new Long(1),
                app.getThreshold()
        );

        Assert.assertEquals(
                "en-US",
                app.getDefaultLocale()
        );

        Assert.assertEquals(
                null,
                app.getCss()
        );

        Assert.assertEquals(
                13,
                app.getFeatures().size()
        );

        Assert.assertEquals(
                null,
                app.getFeaturedLanguages()
        );

        Assert.assertEquals(
                11,
                app.getLanguages().size()
        );
    }
    
    @Test
    public void testSubmittingMissingTranslationKeys() {
//    	Application app = new Application(loadJSONMap("/application.json"));
//
//    	app.submitMissingTranslationKeys();
//    	
//    	app.registerMissingTranslationKey(new TranslationKey(Utils.buildMap("label", "Hello World", "Description", "Greeting")));
//
//    	app.submitMissingTranslationKeys();
//    	
//    	app.registerMissingTranslationKey(new TranslationKey(Utils.buildMap("label", "Hello World", "Description", "Greeting")));
//    	
//    	Application appSpy = Mockito.spy(app);
//		Mockito.when(appSpy.registerKeys(Utils.buildMap())).thenReturn(true);
//    	
//		appSpy.submitMissingTranslationKeys();
    }
    
    
    @Test
    public void testMisc() {
    	Application app = new Application(loadJSONMap("/application.json"));
        
    	Assert.assertEquals(
        		"ru/translations",
        		app.getTranslationsCacheKey("ru")
        );
    	
    	Assert.assertEquals(
        		"Tr8n Translation Service (default)",
        		app.toString()
        );

    	Assert.assertTrue(
        		app.isFeatureEnabled("inline_translations")
        );

    	Assert.assertFalse(
        		app.isFeatureEnabled("custom")
        );
    	
    	Assert.assertTrue(
        		app.isSupportedLocale("ru")
        );
    	
    	Assert.assertFalse(
        		app.isSupportedLocale("kr")
        );

		app.addFeaturedLanguage("ru");
    	
    	Assert.assertEquals(
    			1,
        		app.getFeaturedLanguages().size()
        );
    	
    	TranslationKey tkey = new TranslationKey(Utils.buildMap("label", "Hello World", "Description", "Greeting"));
    	tkey.addTranslation(new Translation(Utils.buildMap("label", "Privet Mir", "locale", "ru")));
    	
    	app.cacheTranslationKey(tkey);

    	app.cacheTranslationKey(tkey);
    	
    	Assert.assertEquals(
    			Utils.buildMap(),
    			app.getSourcesByKeys()
    	);
    	
    }    
}
