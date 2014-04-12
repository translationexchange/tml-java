package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ApplicationTest extends BaseTest {

    @Test
    public void testCreatingApplication() {
    	Application app = new Application();
        Assert.assertNull(
                app.getName()
        );

        Assert.assertNull(
                app.getSecret()
        );
        
        app.setAccessToken(Utils.buildMap("a", "b"));

        Assert.assertEquals(
        		Utils.buildMap("a", "b"),
                app.getAccessToken()
        );
        
        app = new Application(loadJSONMap("/application.json"));

        Assert.assertEquals(
                "Tr8n Translation Service",
                app.getName()
        );

        Assert.assertEquals(
                "https://tr8nhub.com",
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
                new Long(1),
                app.getTranslatorLevel()
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
                null,
                app.getShortcuts()
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
    	Application app = new Application(loadJSONMap("/application.json"));

    	app.setSecret(null);

    	app.submitMissingTranslationKeys();
    	
    	app.registerMissingTranslationKey(new TranslationKey(Utils.buildMap("label", "Hello World", "Description", "Greeting")));

    	app.submitMissingTranslationKeys();
    	
    	app.setSecret("abc");
    	
    	app.registerMissingTranslationKey(new TranslationKey(Utils.buildMap("label", "Hello World", "Description", "Greeting")));
    	
    	Application appSpy = Mockito.spy(app);
		Mockito.when(appSpy.registerKeys(Utils.buildMap())).thenReturn(true);
    	
		appSpy.submitMissingTranslationKeys();
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

    	Assert.assertEquals(
    			Utils.buildMap(),
    			app.getComponentsByKeys()
    	);
    	
    }    
}
