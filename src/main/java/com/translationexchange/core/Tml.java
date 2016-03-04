/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.translationexchange.core.cache.Cache;
import com.translationexchange.core.cache.CacheAdapter;
import com.translationexchange.core.languages.Language;

/**
 * A static utility session wrapper class for using by Mobile and Desktop application.
 *
 * @author Berk
 * @version $Id: $Id
 */
public class Tml {
    /** Constant <code>VERSION="0.2.1"</code> */
    public static final String VERSION = "0.2.1";

    /**
     * Static instance of Tml session. For mobile and desktop applications, only a singleton session is needed. 
     * For web applications, the session must be created for every request. Session contains dynamic information per user/language.
     */
    private static Session session = null;

    /**
     * Tml configuration attribute
     */
    private static Configuration config;

    /**
     * Tml cache
     */
    private static Cache cache;

    /**
     * Tml logger
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
     * @return Tml instance
     */
    public static Session getSession() {
        return session;
    }

    
    /**
     * Set the current session
     *
     * @param newSession a {@link com.translationexchange.core.Session} object.
     */
    public static void setSession(Session newSession) {
    	session = newSession;
    }
    /**
     * Sets current language in the singleton instance
     *
     * @param locale a {@link java.lang.String} object.
     */
    public static void setCurrentLocale(String locale) {
        getSession().setCurrentLanguage(getSession().getApplication().getLanguage(locale));
    }

    /**
     * <p>init.</p>
     */
    public static void init() {
    	Map <String, Object> options = getConfig().getApplication();
        init((String) options.get("key"), (String) options.get("token"), options);
    }
    
    /**
     * <p>init.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param token a {@link java.lang.String} object.
     */
    public static void init(String key, String token) {
        init(key, token, null);
    }

    /**
     * <p>init.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param token a {@link java.lang.String} object.
     * @param options a {@link java.util.Map} object.
     */
    public static void init(String key, String token, Map<String, Object> options) {
    	if (options == null) {
    		options = new HashMap<String, Object>();
    	}
    	options.put("key", key);
    	options.put("token", token);
        setSession(new Session(options));
        if (!isSchedulerRunning()) startScheduledTasks();
    }
    
    /**
     * <p>isSchedulerRunning.</p>
     *
     * @return a boolean.
     */
    public static boolean isSchedulerRunning() {
        return applicationScheduleHandler != null;
    }
    
    /**
     * <p>startScheduledTasks.</p>
     */
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

    /**
     * <p>stopScheduledTasks.</p>
     */
    public static void stopScheduledTasks() {
        if (applicationScheduleHandler == null)
            return;

        applicationScheduleHandler.cancel(true);
        applicationScheduleHandler = null;
    }
    
    /**
     * Get an instance of the configuration object
     *
     * @return a {@link com.translationexchange.core.Configuration} object.
     */
    public static Configuration getConfig() {
        if (config == null)
            config = new Configuration();

        return config;
    }

    /**
     * Get an instance of the Cache object
     *
     * @return a {@link com.translationexchange.core.cache.Cache} object.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Cache getCache() {
    	if (!getConfig().isCacheEnabled())
    		return null;
    	
        if (cache  == null) {
        	try {        		
        		Map<String, Object> cacheData = getConfig().getCache();
        		String className = (String) cacheData.get("class");
				Class cacheClass = Class.forName(className);
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
     * @return a {@link com.translationexchange.core.Logger} object.
     */
    public static Logger getLogger() {
        if (logger == null)
            logger = new Logger();

        return logger;
    }


    /**
     **************************************************************************************************
     *
     * Instance Definition
     *
     * For web environment, where every request needs its own set of attributes, an instance of Tml must
     * be created and propagated with the request context. In those scenarios, a shared cache, like Memcached
     * or Redis should be used to ensure that all instances of application servers can reuse translations.
     *
     ***************************************************************************************************
     *
     * @return a {@link com.translationexchange.core.languages.Language} object.
     */
    public static Language getCurrentLanguage() {
        return getSession().getCurrentLanguage();
    }

    /**
     * <p>switchLanguage.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     */
    public static void switchLanguage(Language language) {
    	switchLanguage(language, null);
    }
    
    /**
     * <p>switchLanguage.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     * @param options a {@link java.util.Map} object.
     */
    public static void switchLanguage(Language language, Map<String, Object> options) {
    	// TODO: if the connection is not present, do it offline, if possible
    	// delete language cache
//    	if (options == null || options.get("offline") == null) {
//    		getCache().delete(language.getLocale(), Utils.buildMap("directory", true));
//    	}
    	getSession().switchLanguage(language);
    }

    /**
     * <p>setCurrentLanguage.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     */
    public void setCurrentLanguage(Language language) {
        getSession().setCurrentLanguage(language);
    }

    /**
     * <p>getCurrentSource.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCurrentSource() {
        return getSession().getCurrentSource();
    }

    /**
     * <p>setCurrentSource.</p>
     *
     * @param currentSource a {@link java.lang.String} object.
     */
    public void setCurrentSource(String currentSource) {
        getSession().setCurrentSource(currentSource);
    }

    /**
     * <p>beginBlockWithOptions.</p>
     *
     * @param options a {@link java.util.Map} object.
     */
    public static void beginBlockWithOptions(Map<String, Object> options) {
        getSession().beginBlockWithOptions(options);
    }

    /**
     * <p>getBlockOptions.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public static Map<String, Object> getBlockOptions() {
        return getSession().getBlockOptions();
    }

    /**
     * <p>endBlock.</p>
     */
    public static void endBlock() {
        getSession().endBlock();
    }

    /**
     * <p>getBlockOption.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object getBlockOption(String key) {
        return getSession().getBlockOptions().get(key);
    }

    /**
     * <p>getApplication.</p>
     *
     * @return a {@link com.translationexchange.core.Application} object.
     */
    public static Application getApplication() {
        return getSession().getApplication();
    }

    /**
     * <p>setApplication.</p>
     *
     * @param application a {@link com.translationexchange.core.Application} object.
     */
    public static void setApplication(Application application) {
        getSession().setApplication(application);
    }

    /**
     * <p>getCurrentTranslator.</p>
     *
     * @return a {@link com.translationexchange.core.Translator} object.
     */
    public static Translator getCurrentTranslator() {
        return getSession().getCurrentTranslator();
    }

    /**
     * <p>setCurrentTranslator.</p>
     *
     * @param currentTranslator a {@link com.translationexchange.core.Translator} object.
     */
    public static void setCurrentTranslator(Translator currentTranslator) {
        getSession().setCurrentTranslator(currentTranslator);
    }

    /**
     * <p>initSource.</p>
     *
     * @param key a {@link java.lang.String} object.
     */
    public static void initSource(String key) {
    	initSource(key, getCurrentLanguage().getLocale());
    }

    /**
     * <p>initSource.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param locale a {@link java.lang.String} object.
     */
    public static void initSource(String key, String locale) {
    	getApplication().getSource(key, locale, null);
    }
    
    /**
     * <p>initLanguage.</p>
     *
     * @param locale a {@link java.lang.String} object.
     */
    public static void initLanguage(String locale) {
    	getApplication().getLanguage(locale);
    }
    
    /**
     * <p>addObserver.</p>
     *
     * @param observer a {@link java.util.Observer} object.
     */
    public static void addObserver(Observer observer) {
    	getSession().addObserver(observer);
    }
    
    /******************************************************************************************************
     *
     * Helper Methods for translations
     *
     *******************************************************************************************************/

    /**
     * <p>translate.</p>
     *
     * @param label Label to be translated
     * @return translated label
     */
    public static String translate(String label) {
        return getSession().translate(label);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String translate(String label, String description) {
        return getSession().translate(label, description);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public static String translate(String label, String description, Map<String, Object> tokens) {
        return getSession().translate(label, description, tokens);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public static String translate(String label, Map<String, Object> tokens) {
        return getSession().translate(label, tokens);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public static String translate(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getSession().translate(label, tokens, options);
    }
}
