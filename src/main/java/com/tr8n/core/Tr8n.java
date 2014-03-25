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

import java.text.AttributedString;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * A utility session wrapper class for using by Mobile and Desktop application.
 */
public class Tr8n {

    /**
     * Static instance of Tr8n session
     */
    private static Session session = null;

    /**
     * Periodically send missing keys to the server, should only be used in a single user mode (desktop, mobile)
     */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
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

    public static void init(String key) {
        init(key, null);
    }

    public static void init(String key, String secret) {
        init(key, null, null);
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
                getLogger().debug("Running scheduled tasks...");
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

    public static Configuration getConfig() {
        return getSession().getConfig();
    }

    public static Cache getCache() {
        return getSession().getCache();
    }

    public static Logger getLogger() {
        return getSession().getLogger();
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

    /**
     * Current application
     */
    private Application application;

    /**
     * Stores the current language selected by the user
     */
    private Language currentLanguage;

    /**
     * Stores the current translator info
     */
    private Translator currentTranslator;

    /**
     * Allows to set a source for the entire request
     */
    private String currentSource;

    /**
     * Allows developers to group translation keys for management only
     */
    private List<Map> blockOptions;


    public static Language getCurrentLanguage() {
        return getSession().getCurrentLanguage();
    }

    public static void switchLanguage(Language language) {
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

    public void beginBlockWithOptions(Map options) {
        getSession().beginBlockWithOptions(options);
    }

    public Map getBlockOptions() {
        return getSession().getBlockOptions();
    }

    public void endBlock() {
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
    public static String tr(String label) {
        return getSession().translate(label);
    }

    public static String tr(String label, String description) {
        return getSession().translate(label, description);
    }

    public static String tr(String label, String description, Map<String, Object> tokens) {
        return getSession().translate(label, description, tokens);
    }

    public static String tr(String label, Map<String, Object> tokens) {
        return getSession().translate(label, tokens);
    }

    public static String tr(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getSession().translate(label, tokens, options);
    }

    /**
     *
     * @param label
     * @return
     */
    public static AttributedString tras(String label) {
        return getSession().translateAttributedString(label);
    }

    public static AttributedString tras(String label, String description) {
        return getSession().translateAttributedString(label, description);
    }

    public static AttributedString tras(String label, String description, Map<String, Object> tokens) {
        return getSession().translateAttributedString(label, description, tokens);
    }

    public static AttributedString tras(String label, Map<String, Object> tokens) {
        return getSession().translateAttributedString(label, tokens);
    }

    public static AttributedString tras(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getSession().translateAttributedString(label, tokens, options);
    }
}
