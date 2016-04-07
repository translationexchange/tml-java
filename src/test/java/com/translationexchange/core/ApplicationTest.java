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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.translationexchange.core.languages.Language;

import static org.mockito.Mockito.*;

public class ApplicationTest extends BaseTest {
	
	private HttpClient mockedHttpClient = mock(HttpClient.class);
	
	private Map<String, Object> buildTranslationsObject(String jsonFile) {
		List<Object> translationList = loadJSONList(jsonFile);
		Map<String, Object> translations = new HashMap<String, Object>();
		translations.put("results", translationList);
		return translations;
	}
	
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
    public void testCreatingCompleteApplication() {
    	Application app = new Application(loadJSONMap("/foody.json"));
    	String[] sourceKeys = new String[]{"foody.views.IndexView", "base", "header"};
    	HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("dry", true);
    	Assert.assertEquals(
        	new HashSet<String>(Arrays.asList(sourceKeys)),
        	Utils.getMapKeys(app.getSourcesByKeys()));
        Assert.assertEquals(
        	new HashSet<String>(Arrays.asList(new String[]{"zh-Hans-CN", "fb-LT", "ru", "ko", "pt-BR", "en", "ga", "kk-Cyrl-KZ", "ro", "zh-Hant-HK", "zh"})),
        	Utils.getMapKeys(app.getLanguagesByLocale()));
        
        Assert.assertEquals(
        	app.getSource("base", "en", options).getKey(),
        	"base");
        
        Assert.assertEquals(
        		"unknown",
            	app.getSource("unknown", "en", options).getKey());
    }
    
    @Test
    public void testLanguageGetters() throws Exception {
    	Application app = spy(new Application(loadJSONMap("/foody.json")));
    	Language language = spy(app.getLanguagesByLocale().get("fb-LT"));
        when(language.hasDefinition()).thenReturn(true);
        app.getLanguagesByLocale().put(language.getLocale(), language);
        Assert.assertEquals(
        		"en",
        		app.getLanguage().getLocale());
        Assert.assertEquals(
        		"fb-LT",
        		app.getLanguage("fb-LT").getLocale());
        
        when(app.getHttpClient()).thenReturn(mockedHttpClient);
        Assert.assertEquals(
        		"hmm-HM",
        		app.getLanguage("hmm-HM").getLocale());
    }
    
    @Test
    public void testTranslationKeysFunctionality() { 
    	Application app = spy(new Application(loadJSONMap("/application.json")));
    	Language ru = app.getLanguage("ru");
    	app.updateTranslationKeys(ru, loadJSONMap("/translations/ru/snippet.json"));
    	
    	Assert.assertEquals(
    			"c59e947093a020f150715057c38759fd",
    			app.getTranslationKey("c59e947093a020f150715057c38759fd").getKey());
    }
    
    @Test
    public void testLoadTranslations() throws Exception {
    	Application app = spy(new Application(loadJSONMap("/application.json")));
    	when(app.getHttpClient()).thenReturn(mockedHttpClient);
    	when(mockedHttpClient.getJSONMap(eq("projects/current/translations"), anyMap(), anyMap())).thenReturn(
    			loadJSONMap("/translations/ru/snippet.json"));
    	app.loadTranslations(app.getLanguage("ru"));
    	Assert.assertEquals(
    			"c59e947093a020f150715057c38759fd",
    			app.getTranslationKey("c59e947093a020f150715057c38759fd").getKey());
    }
    
    @Test
    public void testApplicationLoad() throws Exception {
    	Application app = spy(new Application());
    	app.setKey("6c377447a542718bfd9fe0f5d8f11fae2827377bc4295db76667469db67bd8ed");
    	when(app.getHttpClient()).thenReturn(mockedHttpClient);
    	Assert.assertNull(app.getName());
    	when(mockedHttpClient.getJSONMap(eq("projects/" + app.getKey() + "/definition"), anyMap(), anyMap())).thenReturn(loadJSONMap("/foody.json"));
    	app.load();
    	Assert.assertEquals("Foody", app.getName());
    }
    
    @Test
    public void testSubmittingMissingTranslationKeys() throws Exception {
    	Application app = spy(new Application(loadJSONMap("/application.json")));
    	when(app.isKeyRegistrationEnabled()).thenReturn(true);
    	TranslationKey dummyKey = new TranslationKey(Utils.buildMap("label", "Hello", "description", "Greeting"));
    	app.registerMissingTranslationKey(dummyKey);
    	Map<String, TranslationKey> registeredKeys = app.getMissingTranslationKeysBySources().get(Application.UNDEFINED_SOURCE);
    	Assert.assertTrue(registeredKeys.containsKey(dummyKey.getKey()));
    	
    	when(app.getHttpClient()).thenReturn(mockedHttpClient);
    	app.submitMissingTranslationKeys();
    	verify(app, times(1)).registerKeys(anyMap());
    	Assert.assertTrue(app.getMissingTranslationKeysBySources().isEmpty());
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
    	
    	Assert.assertEquals(
    			app.getDefaultLocale(),
    			app.getFirstAcceptedLocale("xyz,xyz2,xxx"));
    	Assert.assertEquals(
    			"no",
    			app.getFirstAcceptedLocale("xyz,xyz2,xxx,no"));
    	
    	Assert.assertEquals(
    			app.TREX_CDN_HOST,
    			app.getCdnHost());
    	app.setCdnHost("http://google.com");
    	Assert.assertEquals(
    			"http://google.com",
    			app.getCdnHost());
    	
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
