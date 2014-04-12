/*
 *  Copyright (c) 2014 Michael Berkovich, http://tr8nhub.com All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.tr8n.core;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.tr8n.core.cache.Cache;
import com.tr8n.core.cache.CacheAdapter;

/**
 * A static utility session wrapper class for using by Mobile and Desktop application.
 */
public class Tr8n {

    /**
     * Static instance of Tr8n session. For mobile and desktop applications, only a singleton session is needed. 
     * For web applications, the session must be created for every request. Session contains dynamic information per user/language.
     */
    private static Session session = null;

    /**
     * Tr8n configuration attribute
     */
    private static Configuration config;

    /**
     * Tr8n cache
     */
    private static Cache cache;

    /**
     * Tr8n logger
     */
    private static Logger logger;
    
    /**
     * Periodically send missing keys to the server, should only be used in a single user mode (desktop, mobile)
     */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    /**
     * Schedule handler
     */
    private static ScheduledFuture<?> applicationScheduleHandler;


    /**
     * Get the current session
     *
     * @return Tr8n instance
     */
    public static Session getSession() {
        return session;
    }

    
    /** 
     * Set the current session
     * 
     * @param newSession
     */
    public static void setSession(Session newSession) {
    	session = newSession;
    }
    /**
     * Sets current language in the singleton instance
     * @param locale
     */
    public static void setCurrentLocale(String locale) {
        getSession().setCurrentLanguage(getSession().getApplication().getLanguage(locale));
    }

    public static void init() {
        init(getConfig().getApplication());
    }
    
    public static void init(String key) {
        init(key, null);
    }

    public static void init(String key, String secret) {
        init(key, null, null);
    }

    public static void init(Map<String, String> params) {
    	init(params.get("key"), params.get("secret"), params.get("host"));
    }
    
    public static void init(String key, String secret, String host) {
        setSession(new Session(key, secret, host));
        if (!isSchedulerRunning()) startScheduledTasks();
    }
    
    public static boolean isSchedulerRunning() {
        return applicationScheduleHandler != null;
    }
    public static void startScheduledTasks() {
        if (applicationScheduleHandler != null)
            return;

        applicationScheduleHandler = scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
//                getLogger().debug("Running scheduled tasks...");
                getSession().getApplication().submitMissingTranslationKeys();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    public static void stopScheduledTasks() {
        if (applicationScheduleHandler == null)
            return;

        applicationScheduleHandler.cancel(true);
        applicationScheduleHandler = null;
    }

    
    /**
     * Get an instance of the configuration object
     * 
     * @return
     */
    public static Configuration getConfig() {
        if (config == null)
            config = new Configuration();

        return config;
    }

    /**
     * Get an instance of the Cache object
     * 
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Cache getCache() {
    	if (!getConfig().isCacheEnabled())
    		return null;
    	
        if (cache  == null) {
        	try {
        		Map<String, Object> cacheData = getConfig().getCache();
				Class cacheClass = Class.forName((String) cacheData.get("class"));
        		Constructor<CacheAdapter> constructor = cacheClass.getConstructor(Map.class);
        		cache = constructor.newInstance(cacheData);
        	} catch (Exception ex) {
        		getLogger().logException(ex);
        	}
        }
        return cache;
    }

    /**
     * Get an instance of the logger object
     * 
     * @return
     */
    public static Logger getLogger() {
        if (logger == null)
            logger = new Logger();

        return logger;
    }


    /****************************************************************************************************
     *
     * Instance Definition
     *
     * For web environment, where every request needs its own set of attributes, an instance of Tr8n must
     * be created and propagated with the request context. In those scenarios, a shared cache, like Memcached
     * or Redis should be used to ensure that all instances of application servers can reuse translations.
     *
     ****************************************************************************************************/


    public static Language getCurrentLanguage() {
        return getSession().getCurrentLanguage();
    }

    public static void switchLanguage(Language language) {
    	switchLanguage(language, null);
    }
    
    public static void switchLanguage(Language language, Map<String, Object> options) {
    	// TODO: if the connection is not present, do it offline, if possible
    	// delete language cache
    	if (options == null || options.get("offline") == null) {
    		getCache().delete(language.getLocale(), Utils.buildMap("directory", true));
    	}
    	getSession().switchLanguage(language);
    }

    public void setCurrentLanguage(Language language) {
        getSession().setCurrentLanguage(language);
    }

    public String getCurrentSource() {
        return getSession().getCurrentSource();
    }

    public void setCurrentSource(String currentSource) {
        getSession().setCurrentSource(currentSource);
    }

    public static void beginBlockWithOptions(Map<String, Object> options) {
        getSession().beginBlockWithOptions(options);
    }

    public static Map<String, Object> getBlockOptions() {
        return getSession().getBlockOptions();
    }

    public static void endBlock() {
        getSession().endBlock();
    }

    public static Object getBlockOption(String key) {
        return getSession().getBlockOptions().get(key);
    }

    public static Application getApplication() {
        return getSession().getApplication();
    }

    public static void setApplication(Application application) {
        getSession().setApplication(application);
    }

    public static Translator getCurrentTranslator() {
        return getSession().getCurrentTranslator();
    }

    public static void setCurrentTranslator(Translator currentTranslator) {
        getSession().setCurrentTranslator(currentTranslator);
    }

    public static void initSource(String key) {
    	initSource(key, getCurrentLanguage().getLocale());
    }

    public static void initSource(String key, String locale) {
    	getApplication().getSource(key, locale, null);
    }
    
    public static void initLanguage(String locale) {
    	getApplication().getLanguage(locale);
    }
    
    public static void addObserver(Observer observer) {
    	getSession().addObserver(observer);
    }
    
    /******************************************************************************************************
     *
     * Helper Methods for translations
     *
     *******************************************************************************************************/

    /**
     *
     * @param label Label to be translated
     * @return translated label
     */
    public static String translate(String label) {
        return getSession().translate(label);
    }

    public static String translate(String label, String description) {
        return getSession().translate(label, description);
    }

    public static String translate(String label, String description, Map<String, Object> tokens) {
        return getSession().translate(label, description, tokens);
    }

    public static String translate(String label, Map<String, Object> tokens) {
        return getSession().translate(label, tokens);
    }

    public static String translate(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getSession().translate(label, tokens, options);
    }

    /**
     *
     * @param label
     * @return
     */
    public static Object translateStyledString(String label) {
        return getSession().translateStyledString(label);
    }

    public static Object translateStyledString(String label, String description) {
        return getSession().translateStyledString(label, description);
    }

    public static Object translateStyledString(String label, String description, Map<String, Object> tokens) {
        return getSession().translateStyledString(label, description, tokens);
    }

    public static Object translateStyledString(String label, Map<String, Object> tokens) {
        return getSession().translateStyledString(label, tokens);
    }

    public static Object translateStyledString(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getSession().translateStyledString(label, tokens, options);
    }
}
