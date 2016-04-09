package com.translationexchange.core;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import com.translationexchange.core.dummy.DummyApplication;
import com.translationexchange.core.models.User;


public class SessionTest extends BaseTest {
    
    @BeforeClass
    public static void configureTml() {
        Tml.getConfig().setApplicationClass("com.translationexchange.core.dummy.DummyApplication");
    }
    
    @Test
    public void testCreation() {
        Session s = new Session();
        Assert.assertTrue(s instanceof Session);
        Assert.assertTrue(Observable.class.isAssignableFrom(s.getClass()));
        
        Session session = new Session(Utils.buildMap(
                "key", "application.json",
                "token", "ae2827377bc4295db76667469db67bd8ed",
                "host", "http://localhost:8081",
                "cdn_host", "http://cdn.localhost:8081",
                "source", "index",
                "locale", "en",
                "applicationClass", "com.translationexchange.core.dummy.DummyApplication"));
        
        Assert.assertTrue(session.getApplication() instanceof Application);
        
        Assert.assertEquals("en", session.getCurrentLanguage().getLocale());
        
        session.switchLanguage(session.getApplication().getLanguage("da"));
        Assert.assertEquals("da", session.getCurrentLanguage().getLocale());
        
        session.setCurrentLocale("en");
        Assert.assertEquals("en", session.getCurrentLanguage().getLocale());
        
        Assert.assertEquals("index", session.getCurrentSource());
        
        session.setCurrentSource("navigation");
        Assert.assertEquals("navigation", session.getCurrentSource());
        
        session.beginBlockWithOptions(Utils.buildMap("option1", "value1"));
        session.getBlockOption("option1");
    }
    
    @Test
    public void testBlockOptions() {
        Session session = new Session(Utils.buildMap(
                "key", "application.json",
                "source", "index",
                "locale", "en",
                "applicationClass", "com.translationexchange.core.dummy.DummyApplication"));
        
        Assert.assertEquals(Utils.buildMap(), session.getBlockOptions());
        
        session.beginBlockWithOptions(Utils.buildMap("option1", "value1"));
        Assert.assertEquals("value1", (String) session.getBlockOption("option1"));
        
        session.beginBlockWithOptions(Utils.buildMap("option2", "value2"));   // nested
        Assert.assertEquals(null, session.getBlockOption("option1"));
        
        session.endBlock();
        Assert.assertEquals("value1", (String) session.getBlockOption("option1"));
        
        Assert.assertEquals(Utils.buildMap("option1", "value1"), session.getBlockOptions());
    }
    
    @Test
    public void testGetSourcePath() {
        Session session = new Session(Utils.buildMap(
                "key", "application.json",
                "source", "index",
                "locale", "en",
                "applicationClass", "com.translationexchange.core.dummy.DummyApplication"));
        Assert.assertEquals(Utils.buildList("index"), session.getSourcePath());
        
        session.beginBlockWithOptions(Utils.buildMap("source", "navigation"));
        Assert.assertEquals(Utils.buildList("index", "navigation"), session.getSourcePath());
        
        session.beginBlockWithOptions(Utils.buildMap("source", "left_panel"));
        Assert.assertEquals(Utils.buildList("index", "navigation", "left_panel"), session.getSourcePath());
        
        session.endBlock();
        Assert.assertEquals(Utils.buildList("index", "navigation"), session.getSourcePath());
    }
    
    @Test
    public void testIsInlineModeEnabled() {
        Session session = new Session(Utils.buildMap(
                "key", "application.json",
                "source", "index",
                "locale", "en",
                "applicationClass", "com.translationexchange.core.dummy.DummyApplication"));
        Assert.assertFalse(session.isInlineModeEnabled());
        
        session.setCurrentTranslator(new Translator(Utils.buildMap(
                "name", "rustem",
                "email", "r.kamun@gmail.com",
                "manager", null,
                "inline", true,
                "features", null)));
        Assert.assertTrue(session.isInlineModeEnabled());
    }
    
    @Test
    public void testTranslate() {
        Session session = new Session(Utils.buildMap(
                "key", "application.json",
                "source", "index",
                "locale", "en",
                "applicationClass", "com.translationexchange.core.dummy.DummyApplication"));
        Assert.assertEquals("Hello world", session.translate("Hello world"));
        
        Assert.assertEquals("Hello!", session.translate("Hello!", "Greeting"));
        
        Assert.assertEquals(
                "Hello Anna!",
                session.translate("Hello {user}!", Utils.buildMap("user", new User("Anna", "female"))));
        
        Assert.assertEquals(
                "Anna phone is dialing.",
                session.translate("{user} phone is dialing.", Utils.buildMap("user", new User("Anna", "female")), Utils.buildMap()));
    }
    
    @Test
    public void testTranslateStyledString() {
        Session session = new Session(Utils.buildMap(
                "key", "application.json",
                "source", "index",
                "locale", "en",
                "applicationClass", "com.translationexchange.core.dummy.DummyApplication"));
        Assert.assertEquals("Hello world", session.translateStyledString("Hello world"));
        
        Assert.assertEquals("Hello!", session.translateStyledString("Hello!", "Greeting"));
        
        Assert.assertEquals(
                "Hello Anna!",
                session.translateStyledString("Hello {user}!", Utils.buildMap("user", new User("Anna", "female"))));
        
        Assert.assertEquals(
                "Anna phone is dialing.",
                session.translateStyledString("{user} phone is dialing.", Utils.buildMap("user", new User("Anna", "female")), Utils.buildMap()));
    }
    
    @AfterClass
    public static void deconfigureTml() {
        Tml.getConfig().setApplicationClass("com.translationexchange.core.Application");
    }
}
