
/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
 * <p/>
 * _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 * | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 * | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 * | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 * |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 * __/ |
 * |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Michael Berkovich
 * @version $Id: $Id
 */

package com.translationexchange.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.translationexchange.core.cache.Cache;
import com.translationexchange.core.cache.FileCache;
import com.translationexchange.core.decorators.Decorator;
import com.translationexchange.core.decorators.HtmlDecorator;
import com.translationexchange.core.decorators.PlainDecorator;
import com.translationexchange.core.languages.Language;
import com.translationexchange.core.languages.LanguageContext;
import com.translationexchange.core.rulesengine.Variable;

public class Configuration {

    /**
     * Joins source fragments together
     */
    public final static String SOURCE_SEPARATOR = "@:@";

    /**
     * Enables/Disables Tml
     */
    private Boolean enabled = true;

    /**
     * Application default locale fallback, if disabled
     */
    private String defaultLocale = "en-US";

    /**
     * Default translation key level
     */
    private Integer defaultLevel = 0;

    /**
     * Default decoration format
     */
    private String format = "html";

    /**
     * If using in CLI, enable to submit realtime
     */
    private Boolean submitMissingKeysRealTime = false;

    /**
     * Supported token classes
     */
    private List<String> tokenClasses;

    /**
     * Application class
     */
    private String applicationClass;

    /**
     * Application configuration
     */
    private Map<String, Object> application;

    /**
     * Context rules configuration and variable mapping
     */
    private Map<String, Object> contextRules;

    /**
     * Translator options configuration
     */
    private Map<String, Object> translatorOptions;

    /**
     * Cache configuration
     */
    private Map<String, Object> cache;

    /**
     * Logger configuration
     */
    private Map<String, Object> logger;

    /**
     * Default tokens - overloads application tokens
     */
    private Map<String, Object> defaultTokens;

    /**
     * Localization configuration
     */
    private Map<String, Object> localization;

    /**
     * Decorator class
     */
    private Decorator decorator;

    /**
     * Decorator class
     */
    private Map<String, String> tokenizerClasses;

    /**
     * Stores agent related configuration
     */
    private Map<String, Object> agent;

    /**
     * Default system language
     */
    private Language defaultLanguage;

    /**
     * Allows to force inline mode for key submission/verification
     * This is only used for applications with a User Interface
     * This mode must never be used in production
     */
    private boolean keyRegistrationMode;

    private boolean androidApp;

    private TmlMode tmlMode = TmlMode.CDN;

    /**
     * <p>Constructor for Configuration.</p>
     */
    public Configuration() {
        this.decorator = new PlainDecorator();

        this.tokenizerClasses = Utils.buildStringMap(
                "data", "com.translationexchange.core.tokenizers.DataTokenizer",
                "html", "com.translationexchange.core.tokenizers.HtmlTokenizer"
        );

        this.tokenClasses = Utils.buildStringList(
                "com.translationexchange.core.tokens.DataToken",
                "com.translationexchange.core.tokens.MethodToken",
                "com.translationexchange.core.tokens.PipedToken"
        );

        this.applicationClass = "com.translationexchange.core.Application";

        this.cache = Utils.buildMap(
                "enabled", true,
                "class", "com.translationexchange.core.cache.FileCache",
                "host", "localhost:11211",
                "adapter", "memcache"
        );

        this.agent = Utils.buildMap(
                "host", "https://tools.translationexchange.com/agent/stable/agent.min.js",
                "cache", 3600
        );

        buildDefaultContextRulesConfiguration();
        buildDefaultTranslatorOptionsConfiguration();
        buildDefaultLocalizationConfiguration();
        buildDefaultTokensConfiguration();
    }

    private void buildDefaultContextRulesConfiguration() {
        this.contextRules = Utils.buildMap(
                "number", Utils.buildMap(
                        "variables", Utils.buildMap(
                                "@n", new Variable() {
                                    public Object getValue(LanguageContext context, Object object) {
                                        return object;
                                    }
                                }
                        )
                ),
                "gender", Utils.buildMap(
                        "variables", Utils.buildMap(
                                "@gender", new Variable() {
                                    @SuppressWarnings("unchecked")
                                    public Object getValue(LanguageContext context, Object object) {
                                        if (object instanceof Map) {
                                            Map<String, Object> map = (Map<String, Object>) object;
                                            if (map.get("object") != null) {
                                                map = (Map<String, Object>) map.get("object");
                                            }
                                            return map.get("gender");
                                        }
                                        try {
                                            Method method = object.getClass().getMethod("getGender");
                                            if (method != null)
                                                return method.invoke(object);
                                        } catch (Exception ex) {
                                            Tml.getLogger().error("Object " + object.getClass().getName() + " does not support gender method");
                                        }

                                        return object;
                                    }
                                }
                        )
                ),
                "genders", Utils.buildMap(
                        "variables", Utils.buildMap(
                                "@genders", new Variable() {
                                    @SuppressWarnings("unchecked")
                                    public Object getValue(LanguageContext context, Object object) {
                                        List<String> genders = new ArrayList<String>();

                                        if (!(object instanceof List)) {
                                            genders.add(getContextVariable("gender", "@gender").getValue(context, object).toString());
                                            return genders;
                                        }

                                        for (Object obj : ((List<Object>) object)) {
                                            if (obj instanceof Map) {
                                                genders.add(((Map<String, Object>) object).get("gender").toString());
                                            } else {
                                                try {
                                                    Method method = obj.getClass().getMethod("getGender");
                                                    if (method != null)
                                                        genders.add((String) method.invoke(obj));
                                                } catch (Exception ex) {
                                                    Tml.getLogger().error("Object " + obj.getClass().getName() + " does not support gender method");
                                                }
                                            }
                                        }
                                        return genders;
                                    }
                                },
                                "@count", new Variable() {
                                    @SuppressWarnings("unchecked")
                                    public Object getValue(LanguageContext context, Object object) {
                                        if (!(object instanceof List)) return 1;
                                        return ((List<Object>) object).size();
                                    }
                                }
                        )
                ),
                "date", Utils.buildMap(
                        "variables", Utils.buildMap(
                                "@date", new Variable() {
                                    public Object getValue(LanguageContext context, Object object) {
                                        return object;
                                    }
                                }
                        )
                ),
                "list", Utils.buildMap(
                        "variables", Utils.buildMap(
                                "@count", new Variable() {
                                    @SuppressWarnings("unchecked")
                                    public Object getValue(LanguageContext context, Object object) {
                                        if (!(object instanceof List)) return 1;
                                        return ((List<Object>) object).size();
                                    }
                                }
                        )
                )
        );
    }

    private void buildDefaultTranslatorOptionsConfiguration() {
        this.translatorOptions = Utils.buildMap(
                "debug", false,
                "debug_format_html", "<span style='font-size:20px;color:red;'>{</span> {$0} <span style='font-size:20px;color:red;'>}</span>",
                "debug_format", "{{{{$0}}}}",
                "split_sentences", false,
                "nodes", Utils.buildMap(
                        "ignored", Utils.buildList(),
                        "scripts", Utils.buildList("style", "script", "code", "pre"),
                        "inline", Utils.buildList("a", "span", "i", "b", "img", "strong", "s", "em", "u", "sub", "sup"),
                        "short", Utils.buildList("i", "b"),
                        "splitters", Utils.buildList("br", "hr")),
                "attributes", Utils.buildMap(
                        "labels", Utils.buildList("title", "alt")),
                "name_mapping", Utils.buildMap(
                        "b", "bold",
                        "i", "italic",
                        "a", "link",
                        "img", "picture"),
                "data_tokens", Utils.buildMap(
                        "special", Utils.buildMap(
                                "enabled", true,
                                "regex", "(&[^;]*;)"),
                        "date", Utils.buildMap(
                                "enabled", true,
                                "formats", Utils.buildList(
                                        Utils.buildList("((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+\\d+,\\s+\\d+)", "{month} {day}, {year}"),
                                        Utils.buildList("((January|February|March|April|May|June|July|August|September|October|November|December)\\s+\\d+,\\s+\\d+)", "{month} {day}, {year}"),
                                        Utils.buildList("(\\d+\\s+(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec),\\s+\\d+)", "{day} {month}, {year}"),
                                        Utils.buildList("(\\d+\\s+(January|February|March|April|May|June|July|August|September|October|November|December),\\s+\\d+)", "{day} {month}, {year}")),
                                "name", "date"),
                        "rules", Utils.buildList(
                                Utils.buildMap("enabled", true, "name", "time", "regex", "(\\d{1,2}:\\d{1,2}\\s+([A-Z]{2,3}|am|pm|AM|PM)?)"),
                                Utils.buildMap("enabled", true, "name", "phone", "regex", "((\\d{1}-)?\\d{3}-\\d{3}-\\d{4}|\\d?\\(\\d{3}\\)\\s*\\d{3}-\\d{4}|(\\d.)?\\d{3}.\\d{3}.\\d{4})"),
                                Utils.buildMap("enabled", true, "name", "email", "regex", "([-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|io|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?)"),
                                Utils.buildMap("enabled", true, "name", "price", "regex", "(\\$\\d*(,\\d*)*(\\.\\d*)?)"),
                                Utils.buildMap("enabled", true, "name", "fraction", "regex", "(\\d+\\/\\d+)"),
                                Utils.buildMap("enabled", true, "name", "num", "regex", "(\\b\\d*(,\\d*)*(\\.\\d*)?%?\\b)")
                        ))
        );
    }

    private void buildDefaultLocalizationConfiguration() {
        this.localization = Utils.buildMap(
                "default_day_names", Utils.buildList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
                "default_abbr_day_names", Utils.buildList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
                "default_month_names", Utils.buildList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"),
                "default_abbr_month_names", Utils.buildList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"),
                "custom_date_formats", Utils.buildMap(
                        "default", "%m/%d/%Y",            // 07/4/2008
                        "short_numeric", "%m/%d",               // 07/4
                        "short_numeric_year", "%m/%d/%y",            // 07/4/08
                        "long_numeric", "%m/%d/%Y",            // 07/4/2008
                        "verbose", "%A, %B %d, %Y",       // Friday, July  4, 2008
                        "monthname", "%B %d",               // July 4
                        "monthname_year", "%B %d, %Y",           // July 4, 2008
                        "monthname_abbr", "%b %d",               // Jul 4
                        "monthname_abbr_year", "%b %d, %Y",           // Jul 4, 2008
                        "date_time", "%m/%d/%Y at %H:%M"   // 01/03/1010 at 5:30
                ),
                "token_mapping", Utils.buildMap(
                        "%a", "{short_week_day_name}",
                        "%A", "{week_day_name}",
                        "%b", "{short_month_name}",
                        "%B", "{month_name}",
                        "%p", "{am_pm}",
                        "%d", "{days}",
                        "%e", "{day_of_month}",
                        "%j", "{year_days}",
                        "%m", "{months}",
                        "%W", "{week_num}",
                        "%w", "{week_days}",
                        "%y", "{short_years}",
                        "%Y", "{years}",
                        "%l", "{trimed_hour}",
                        "%H", "{full_hours}",
                        "%I", "{short_hours}",
                        "%M", "{minutes}",
                        "%S", "{seconds}",
                        "%s", "{since_epoch}"
                )
        );
    }

    private void buildDefaultTokensConfiguration() {
        this.defaultTokens = Utils.buildMap(
                "html", Utils.buildMap(
                        "data", Utils.buildMap(
                                "ndash", "&ndash;",       // ?
                                "mdash", "&mdash;",       // ?
                                "iexcl", "&iexcl;",       // ?
                                "iquest", "&iquest;",      // ?
                                "quot", "&quot;",        // "
                                "ldquo", "&ldquo;",       // ?
                                "rdquo", "&rdquo;",       // ?
                                "lsquo", "&lsquo;",       // ?
                                "rsquo", "&rsquo;",       // ?
                                "laquo", "&laquo;",       // ?
                                "raquo", "&raquo;",       // ?
                                "nbsp", "&nbsp;",        // space
                                "lsaquo", "&lsaquo;",      // ?
                                "rsaquo", "&rsaquo;",      // ?
                                "br", "<br/>",         // line break
                                "lbrace", "{",
                                "rbrace", "}",
                                "trade", "&trade;"        // TM
                        ),
                        "decoration", Utils.buildMap(
                                "strong", "<strong>{$0}</strong>",
                                "bold", "<strong>{$0}</strong>",
                                "b", "<strong>{$0}</strong>",
                                "em", "<em>{$0}</em>",
                                "italic", "<i>{$0}</i>",
                                "i", "<i>{$0}</i>",
                                "link", "<a href='{$href}'>{$0}</a>",
                                "br", "<br>{$0}",
                                "strike", "<strike>{$0}</strike>",
                                "div", "<div id='{$id}' class='{$class}' style='{$style}'>{$0}</div>",
                                "span", "<span id='{$id}' class='{$class}' style='{$style}'>{$0}</span>",
                                "h1", "<h1>{$0}</h1>",
                                "h2", "<h2>{$0}</h2>",
                                "h3", "<h3>{$0}</h3>"
                        )
                ),

                "text", Utils.buildMap(
                        "data", Utils.buildMap(
                                "ndash", "?",        // ?
                                "mdash", "?",        // ?
                                "iexcl", "?",        // ?
                                "iquest", "?",        // ?
                                "quot", "\"",       // "
                                "ldquo", "?",        // ?
                                "rdquo", "?",        // ?
                                "lsquo", "?",        // ?
                                "rsquo", "?",        // ?
                                "laquo", "?",        // ?
                                "raquo", "?",        // ?
                                "nbsp", " ",        // space
                                "lsaquo", "?",        // ?
                                "rsaquo", "?",        // ?
                                "br", "\n",       // line break
                                "lbrace", "{",
                                "rbrace", "}",
                                "trade", "?"         // TM
                        ),
                        "decoration", Utils.buildMap(
                                "strong", "{$0}",
                                "bold", "{$0}",
                                "b", "{$0}",
                                "em", "{$0}",
                                "italic", "{$0}",
                                "i", "{$0}",
                                "link", "{$0}",
                                "br", "\n{$0}",
                                "strike", "{$0}",
                                "div", "{$0}",
                                "span", "{$0}",
                                "h1", "{$0}",
                                "h2", "{$0}",
                                "h3", "{$0}"
                        )
                )
        );
    }

    /**
     * Get default language, for when the application is not available
     *
     * @return
     */
    public Language getDefaultLanguage() {
        if (defaultLanguage == null) {
            try {
                String data = Utils.readFromInputStream(this.getClass().getClassLoader().getResourceAsStream("en.json"));
                @SuppressWarnings("unchecked")
                Map<String, Object> lang = (Map<String, Object>) Utils.parseJSON(data);
                defaultLanguage = new Language(lang);
            } catch (Exception ex) {
                Tml.getLogger().debug("Failed to load the default language: " + ex.getMessage());
            }
        }

        return defaultLanguage;
    }

    /**
     * <p>isEnabled.</p>
     *
     * @return a boolean.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * <p>Getter for the field <code>defaultLevel</code>.</p>
     *
     * @return a {@link Integer} object.
     */
    public Integer getDefaultLevel() {
        return defaultLevel;
    }

    /**
     * <p>Getter for the field <code>format</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getFormat() {
        return format;
    }

    /**
     * <p>Getter for the field <code>tokenClasses</code>.</p>
     *
     * @return a {@link List} object.
     */
    public List<String> getTokenClasses() {
        return tokenClasses;
    }

    /**
     * <p>Getter for the field <code>applicationClass</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getApplicationClass() {
        return applicationClass;
    }

    /**
     * <p>Getter for the field <code>application</code>.</p>
     *
     * @return a {@link Map} object.
     */
    public Map<String, Object> getApplication() {
        if (application == null) {
            return Utils.buildMap();
        }
        return application;
    }

    /**
     * <p>addDefaultTokenValue.</p>
     *
     * @param name   a {@link String} object.
     * @param type   a {@link String} object.
     * @param format a {@link String} object.
     * @param value  a {@link String} object.
     */
    public void addDefaultTokenValue(String name, String type, String format, String value) {
        if (this.defaultTokens == null) this.defaultTokens = new HashMap<String, Object>();
        Utils.setNestedMapValue(this.defaultTokens, format + "." + type + "." + name, value);
    }

    /**
     * <p>getDefaultTokenValue.</p>
     *
     * @param name   a {@link String} object.
     * @param type   a {@link String} object.
     * @param format a {@link String} object.
     * @return a {@link String} object.
     */
    public String getDefaultTokenValue(String name, String type, String format) {
        return (String) Utils.getNestedMapValue(this.defaultTokens, format + "." + type + "." + name);
    }

    /**
     * <p>getDefaultTokenValue.</p>
     *
     * @param name a {@link String} object.
     * @param type a {@link String} object.
     * @return a {@link String} object.
     */
    public String getDefaultTokenValue(String name, String type) {
        return getDefaultTokenValue(name, type, getFormat());
    }

    /**
     * <p>getDefaultTokenValue.</p>
     *
     * @param name a {@link String} object.
     * @return a {@link String} object.
     */
    public String getDefaultTokenValue(String name) {
        return getDefaultTokenValue(name, "data");
    }

    /**
     * <p>getDefaultFormat.</p>
     *
     * @param name a {@link String} object.
     * @return a {@link String} object.
     */
    public String getDefaultFormat(String name) {
        return (String) Utils.getNestedMapValue(localization, "custom_date_formats." + name);
    }


    /**
     * <p>getContextVariable.</p>
     *
     * @param contextKeyword a {@link String} object.
     * @param varName        a {@link String} object.
     * @return a {@link Variable} object.
     */
    public Variable getContextVariable(String contextKeyword, String varName) {
        return (Variable) Utils.getNestedMapValue(contextRules, contextKeyword + ".variables." + varName);
    }

    /**
     * <p>setContextVariable.</p>
     *
     * @param contextKeyword a {@link String} object.
     * @param varName        a {@link String} object.
     * @param var            a {@link Variable} object.
     */
    @SuppressWarnings("unchecked")
    public void setContextVariable(String contextKeyword, String varName, Variable var) {
        Map<String, Object> vars = (Map<String, Object>) Utils.getNestedMapValue(contextRules, contextKeyword + ".variables");
        vars.put(varName, var);
    }

    /**
     * <p>Getter for the field <code>enabled</code>.</p>
     *
     * @return a {@link Boolean} object.
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * <p>Setter for the field <code>enabled</code>.</p>
     *
     * @param enabled a {@link Boolean} object.
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * <p>Getter for the field <code>defaultLocale</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * <p>Setter for the field <code>defaultLocale</code>.</p>
     *
     * @param defaultLocale a {@link String} object.
     */
    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    /**
     * <p>Setter for the field <code>defaultLevel</code>.</p>
     *
     * @param defaultLevel a {@link Integer} object.
     */
    public void setDefaultLevel(Integer defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    /**
     * <p>Setter for the field <code>format</code>.</p>
     *
     * @param format a {@link String} object.
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * <p>Getter for the field <code>submitMissingKeysRealTime</code>.</p>
     *
     * @return a {@link Boolean} object.
     */
    public Boolean getSubmitMissingKeysRealTime() {
        return submitMissingKeysRealTime;
    }

    /**
     * <p>Setter for the field <code>submitMissingKeysRealTime</code>.</p>
     *
     * @param submitMissingKeysRealTime a {@link Boolean} object.
     */
    public void setSubmitMissingKeysRealTime(Boolean submitMissingKeysRealTime) {
        this.submitMissingKeysRealTime = submitMissingKeysRealTime;
    }

    /**
     * <p>Setter for the field <code>tokenClasses</code>.</p>
     *
     * @param tokenClasses a {@link List} object.
     */
    public void setTokenClasses(List<String> tokenClasses) {
        this.tokenClasses = tokenClasses;
    }

    /**
     * <p>Setter for the field <code>application</code>.</p>
     *
     * @param application a {@link Map} object.
     */
    public void setApplication(Map<String, Object> application) {
        this.application = application;
    }

    /**
     * <p>Setter for the field <code>applicationClass</code>.</p>
     *
     * @param applicationClass a {@link String} object.
     */
    public void setApplicationClass(String applicationClass) {
        this.applicationClass = applicationClass;
    }

    /**
     * <p>Getter for the field <code>contextRules</code>.</p>
     *
     * @return a {@link Map} object.
     */
    public Map<String, Object> getTranslatorOptions() {
        return translatorOptions;
    }

    /**
     * <p>Setter for the field <code>contextRules</code>.</p>
     *
     * @param translatorOptions a {@link Map} object.
     */
    public void setTranslatorOptions(Map<String, Object> translatorOptions) {
        this.translatorOptions = translatorOptions;
    }

    /**
     * <p>Getter for the field <code>contextRules</code>.</p>
     *
     * @return a {@link Map} object.
     */
    public Map<String, Object> getContextRules() {
        return contextRules;
    }

    /**
     * <p>Setter for the field <code>contextRules</code>.</p>
     *
     * @param contextRules a {@link Map} object.
     */
    public void setContextRules(Map<String, Object> contextRules) {
        this.contextRules = contextRules;
    }

    /**
     * <p>Getter for the field <code>cache</code>.</p>
     *
     * @return a {@link Map} object.
     */
    public Map<String, Object> getCache() {
        return cache;
    }

    /**
     * <p>Setter for the field <code>cache</code>.</p>
     *
     * @param cache a {@link Map} object.
     */
    public void setCache(Map<String, Object> cache) {
        this.cache = cache;
    }

    /**
     * <p>Getter for the field <code>logger</code>.</p>
     *
     * @return a {@link Map} object.
     */
    public Map<String, Object> getLogger() {
        return logger;
    }

    /**
     * <p>Setter for the field <code>logger</code>.</p>
     *
     * @param logger a {@link Map} object.
     */
    public void setLogger(Map<String, Object> logger) {
        this.logger = logger;
    }

    /**
     * <p>Getter for the field <code>defaultTokens</code>.</p>
     *
     * @return a {@link Map} object.
     */
    public Map<String, Object> getDefaultTokens() {
        return defaultTokens;
    }

    /**
     * <p>Setter for the field <code>defaultTokens</code>.</p>
     *
     * @param defaultTokens a {@link Map} object.
     */
    public void setDefaultTokens(Map<String, Object> defaultTokens) {
        this.defaultTokens = defaultTokens;
    }

    /**
     * <p>Getter for the field <code>localization</code>.</p>
     *
     * @return a {@link Map} object.
     */
    public Map<String, Object> getLocalization() {
        return localization;
    }

    /**
     * <p>Setter for the field <code>localization</code>.</p>
     *
     * @param localization a {@link Map} object.
     */
    public void setLocalization(Map<String, Object> localization) {
        this.localization = localization;
    }

    /**
     * <p>Setter for the field <code>decorator</code>.</p>
     *
     * @param type a {@link String} object.
     */
    public void setDecorator(String type) {
        if (type.equals("html"))
            decorator = new HtmlDecorator();
        else
            decorator = new PlainDecorator();
    }

    /**
     * <p>Setter for the field <code>decorator</code>.</p>
     *
     * @param decorator a {@link Decorator} object.
     */
    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
    }

    /**
     * <p>Getter for the field <code>decorator</code>.</p>
     *
     * @return a {@link Decorator} object.
     */
    public Decorator getDecorator() {
        return decorator;
    }

    /**
     * <p>isCacheEnabled.</p>
     *
     * @return a boolean.
     */
    public boolean isCacheEnabled() {
        if (this.cache == null) return false;
        if (this.cache.get("enabled") == null) return true;
        return (Boolean) this.cache.get("enabled");
    }

    /**
     * <p>getApplicationName.</p>
     *
     * @return a {@link String} object.
     */
    public String getApplicationName() {
        if (getApplication() == null || getApplication().get("name") == null)
            return "Tml";

        return (String) getApplication().get("name");
    }

    /**
     * <p>addTokenizerClass.</p>
     *
     * @param key            a {@link String} object.
     * @param tokenizerClass a {@link String} object.
     */
    public void addTokenizerClass(String key, String tokenizerClass) {
        if (this.tokenizerClasses == null)
            this.tokenizerClasses = new HashMap<String, String>();

        this.tokenizerClasses.put(key, tokenizerClass);
    }

    /**
     * <p>getTokenizerClass.</p>
     *
     * @param key a {@link String} object.
     * @return a {@link String} object.
     */
    public String getTokenizerClass(String key) {
        if (this.tokenizerClasses == null)
            return null;

        return this.tokenizerClasses.get(key);
    }


    /**
     * <p>Getter for the field <code>agent</code>.</p>
     *
     * @return a {@link Map} object.
     */
    public Map<String, Object> getAgent() {
        return agent;
    }

    /**
     * <p>Setter for the field <code>agent</code>.</p>
     *
     * @param agent a {@link Map} object.
     */
    public void setAgent(Map<String, Object> agent) {
        this.agent = agent;
    }

    public boolean isKeyRegistrationModeEnabled() {
        return keyRegistrationMode;
    }

    public void enableKeyRegistrationMode() {
        this.keyRegistrationMode = true;
    }

    public void disableKeyRegistrationMode() {
        this.keyRegistrationMode = false;
    }

    public boolean isAndroidApp() {
        return androidApp;
    }

    public void setAndroidApp(boolean androidApp) {
        this.androidApp = androidApp;
    }

    public TmlMode getTmlMode() {
        return tmlMode;
    }

    public void setTmlMode(TmlMode tmlMode) {
        this.tmlMode = tmlMode;
        if (tmlMode.equals(TmlMode.API_LIVE)) {
            Cache cache = Tml.getCache();
            cache.delete("live", Utils.buildMap("directory", true));
        }
    }
}
