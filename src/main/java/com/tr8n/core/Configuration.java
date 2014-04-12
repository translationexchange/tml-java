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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tr8n.core.decorators.Decorator;
import com.tr8n.core.decorators.HtmlDecorator;
import com.tr8n.core.decorators.PlainDecorator;
import com.tr8n.core.rulesengine.Variable;

public class Configuration {

    /**
     * Enables/Disables Tr8n
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
     * Application configuration
     */
    private Map<String, String> application;

    /**
     * Context rules configuration and variable mapping
     */
    private Map<String, Object> contextRules;

    /**
     * Logger configuration
     */
    private Map<String, Object> logger;

    /**
     * Cache configuration
     */
    private Map<String, Object> cache;

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
    
    public Configuration() {
    	this.decorator = new PlainDecorator();
    	
        this.tokenizerClasses = Utils.buildStringMap(
			"data", "com.tr8n.core.tokenizers.DataTokenizer",
			"html", "com.tr8n.core.tokenizers.HtmlTokenizer"
        );
    	
        this.tokenClasses = Utils.buildStringList(
		    "com.tr8n.core.tokenizers.tokens.DataToken",
		    "com.tr8n.core.tokenizers.tokens.MethodToken",
		    "com.tr8n.core.tokenizers.tokens.PipedToken"
        );

        this.logger = Utils.buildMap(
            "enabled", false,
            "path", "./log/tr8n.log",
            "level", "debug"
        );

        this.cache = Utils.buildMap(
            "enabled", true,
        	"class", "com.tr8n.core.cache.FileCache",
            "host", "localhost:11211",
            "adapter", "memcache",
            "version", 1,
            "timeout", 3600
        );

        buildDefaultContextRulesConfiguration();
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
                                Tr8n.getLogger().error("Object " + object.getClass().getName() + " does not support gender method");
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
                                            genders.add((String)method.invoke(obj));
                                    } catch (Exception ex) {
                                        Tr8n.getLogger().error("Object " + obj.getClass().getName() + " does not support gender method");
                                    }
                                }
                            }
                            return genders;
                        }
                    },
                    "@count",   new Variable() {
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
                    "@date",   new Variable() {
                        public Object getValue(LanguageContext context, Object object) {
                            return object;
                        }
                    }
                )
            ),
            "list", Utils.buildMap(
                "variables", Utils.buildMap(
                    "@count",   new Variable() {
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

    private void buildDefaultLocalizationConfiguration() {
        this.localization = Utils.buildMap(
            "default_day_names",        Utils.buildList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
            "default_abbr_day_names",   Utils.buildList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
            "default_month_names",      Utils.buildList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"),
            "default_abbr_month_names", Utils.buildList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"),
            "custom_date_formats", Utils.buildMap(
                "default",                  "%m/%d/%Y",            // 07/4/2008
                "short_numeric",            "%m/%d",               // 07/4
                "short_numeric_year",       "%m/%d/%y",            // 07/4/08
                "long_numeric",             "%m/%d/%Y",            // 07/4/2008
                "verbose",                  "%A, %B %d, %Y",       // Friday, July  4, 2008
                "monthname",                "%B %d",               // July 4
                "monthname_year",           "%B %d, %Y",           // July 4, 2008
                "monthname_abbr",           "%b %d",               // Jul 4
                "monthname_abbr_year",      "%b %d, %Y",           // Jul 4, 2008
                "date_time",                "%m/%d/%Y at %H:%M"   // 01/03/1010 at 5:30
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
                    "ndash",    "&ndash;",       // –
                    "mdash",    "&mdash;",       // —
                    "iexcl",    "&iexcl;",       // ¡
                    "iquest",   "&iquest;",      // ¿
                    "quot",     "&quot;",        // "
                    "ldquo",    "&ldquo;",       // “
                    "rdquo",    "&rdquo;",       // ”
                    "lsquo",    "&lsquo;",       // ‘
                    "rsquo",    "&rsquo;",       // ’
                    "laquo",    "&laquo;",       // «
                    "raquo",    "&raquo;",       // »
                    "nbsp",     "&nbsp;",        // space
                    "lsaquo",   "&lsaquo;",      // ‹
                    "rsaquo",   "&rsaquo;",      // ›
                    "br",       "<br/>",         // line break
                    "lbrace",   "{",
                    "rbrace",   "}",
                    "trade",    "&trade;"        // TM
                ),
                "decoration", Utils.buildMap(
                    "strong",   "<strong>{$0}</strong>",
                    "bold",     "<strong>{$0}</strong>",
                    "b",        "<strong>{$0}</strong>",
                    "em",       "<em>{$0}</em>",
                    "italic",   "<i>{$0}</i>",
                    "i",        "<i>{$0}</i>",
                    "link",     "<a href='{$href}'>{$0}</a>",
                    "br",       "<br>{$0}",
                    "strike",   "<strike>{$0}</strike>",
                    "div",      "<div id='{$id}' class='{$class}' style='{$style}'>{$0}</div>",
                    "span",     "<span id='{$id}' class='{$class}' style='{$style}'>{$0}</span>",
                    "h1",       "<h1>{$0}</h1>",
                    "h2",       "<h2>{$0}</h2>",
                    "h3",       "<h3>{$0}</h3>"
                )
            ),

            "text", Utils.buildMap(
                "data", Utils.buildMap(
                    "ndash",    "–",        // –
                    "mdash",    "–",        // —
                    "iexcl",    "¡",        // ¡
                    "iquest",   "¿",        // ¿
                    "quot",     "\"",       // "
                    "ldquo",    "“",        // “
                    "rdquo",    "”",        // ”
                    "lsquo",    "‘",        // ‘
                    "rsquo",    "’",        // ’
                    "laquo",    "«",        // «
                    "raquo",    "»",        // »
                    "nbsp",     " ",        // space
                    "lsaquo",   "‹",        // ‹
                    "rsaquo",   "›",        // ›
                    "br",       "\n",       // line break
                    "lbrace",   "{",
                    "rbrace",   "}",
                    "trade",    "™"         // TM
                ),
                "decoration", Utils.buildMap(
                    "strong",   "{$0}",
                    "bold",     "{$0}",
                    "b",        "{$0}",
                    "em",       "{$0}",
                    "italic",   "{$0}",
                    "i",        "{$0}",
                    "link",     "{$0}",
                    "br",       "\n{$0}",
                    "strike",   "{$0}",
                    "div",      "{$0}",
                    "span",     "{$0}",
                    "h1",       "{$0}",
                    "h2",       "{$0}",
                    "h3",       "{$0}"
                )
            )
        );
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Integer getDefaultLevel() {
        return defaultLevel;
    }

    public String getFormat() {
        return format;
    }

    public List<String> getTokenClasses() {
        return tokenClasses;
    }

    public Map<String, String> getApplication() {
        return application;
    }

    public void addDefaultTokenValue(String name, String type, String format, String value) {
        if (this.defaultTokens == null) this.defaultTokens = new HashMap<String, Object>();
        Utils.setNestedMapValue(this.defaultTokens, format + "." + type + "." + name, value);
    }

    public String getDefaultTokenValue(String name, String type, String format) {
        return (String) Utils.getNestedMapValue(this.defaultTokens, format + "." + type + "." + name);
    }

    public String getDefaultTokenValue(String name, String type) {
        return getDefaultTokenValue(name, type, getFormat());
    }

    public String getDefaultTokenValue(String name) {
        return getDefaultTokenValue(name, "data");
    }

    public String getDefaultFormat(String name) {
        return (String) Utils.getNestedMapValue(localization, "custom_date_formats." + name);
    }

    public Variable getContextVariable(String contextKeyword, String varName) {
        return (Variable) Utils.getNestedMapValue(contextRules, contextKeyword + ".variables." + varName);
    }

	@SuppressWarnings("unchecked")
	public void setContextVariable(String contextKeyword, String varName, Variable var) {
        Map<String, Object> vars = (Map<String, Object>) Utils.getNestedMapValue(contextRules, contextKeyword + ".variables");
        vars.put(varName, var);
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public void setDefaultLevel(Integer defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Boolean getSubmitMissingKeysRealTime() {
        return submitMissingKeysRealTime;
    }

    public void setSubmitMissingKeysRealTime(Boolean submitMissingKeysRealTime) {
        this.submitMissingKeysRealTime = submitMissingKeysRealTime;
    }

    public void setTokenClasses(List<String> tokenClasses) {
        this.tokenClasses = tokenClasses;
    }

    public void setApplication(Map<String, String> application) {
        this.application = application;
    }

    public Map<String, Object> getContextRules() {
        return contextRules;
    }

    public void setContextRules(Map<String, Object> contextRules) {
        this.contextRules = contextRules;
    }

    public Map<String, Object> getLogger() {
        return logger;
    }

    public void setLogger(Map<String, Object> logger) {
        this.logger = logger;
    }

    public Map<String, Object> getCache() {
        return cache;
    }

    public void setCache(Map<String, Object> cache) {
        this.cache = cache;
    }

    public Map<String, Object> getDefaultTokens() {
        return defaultTokens;
    }

    public void setDefaultTokens(Map<String, Object> defaultTokens) {
        this.defaultTokens = defaultTokens;
    }

    public Map<String, Object> getLocalization() {
        return localization;
    }

    public void setLocalization(Map<String, Object> localization) {
        this.localization = localization;
    }

    public void setDecorator(String type) {
    	if (type.equals("html"))
    		decorator = new HtmlDecorator();
    	else
    		decorator = new PlainDecorator();
    }
    
    public void setDecorator(Decorator decorator) {
    	this.decorator = decorator;
    }
    
    public Decorator getDecorator() {
    	return decorator;
    }
    
    public boolean isCacheEnabled() {
    	if (this.cache == null) return false;
    	if (this.cache.get("enabled") == null) return true;
    	return (Boolean)this.cache.get("enabled");
    }
    
    public String getApplicationName() {
    	if (getApplication() == null || getApplication().get("name") == null)
    		return "Tr8n";
    	
    	return (String) getApplication().get("name");
    }
    
    public void addTokenizerClass(String key, String tokenizerClass) {
    	if (this.tokenizerClasses == null)
    		this.tokenizerClasses = new HashMap<String, String>();
    	
    	this.tokenizerClasses.put(key, tokenizerClass);
    }
    
    public String getTokenizerClass(String key) {
    	if (this.tokenizerClasses == null)
    		return null;
    	
    	return this.tokenizerClasses.get(key);
    }
    
}
