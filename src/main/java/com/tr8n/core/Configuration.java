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

import java.util.Map;

public class Configuration {

    Boolean enabled = true;
    String defaultLocale = "en-US";
    Integer defaultLevel = 0;
    String format = "html";
    Boolean submitMissingKeysRealTime = false;
    Map<String, Object> application;
    Map<String, Object> contextRules;
    Map<String, Object> logger;
    Map<String, Object> cache;
    Map<String, Object> defaultTokens;
    Map<String, Object> localization;

    public Configuration() {
//        this.application = Utils.buildMap(
//            "host", "https://localhost:3000",
//            "key", "default",
//            "secret", "12345"
//        );

        this.contextRules = Utils.buildMap(
            "number", Utils.buildMap(
                "variables", Utils.buildMap()
            ),
            "gender", Utils.buildMap(
                "variables", Utils.buildMap(
                    "@gender", "gender"
                )
             ),
            "genders", Utils.buildMap(
                "variables", Utils.buildMap(
                    "@genders", "gender",
                    "@size", "size"
                )
            ),
            "date", Utils.buildMap(
                "variables", Utils.buildMap()
            ),
            "list", Utils.buildMap(
                "variables", Utils.buildMap(
                    "@count", "count"
                )
            )
        );

        this.logger = Utils.buildMap(
            "enabled", false,
            "path", "./log/tr8n.log",
            "level", "debug"
        );

        this.cache = Utils.buildMap(
            "enabled", true,
            "host", "localhost:11211",
            "adapter", "memcache",
            "version", 1,
            "timeout", 3600
        );


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

    public Object getNestedMapValue(Map map, String key) {
        String[] parts = key.split(".");

        for (String part : parts) {
            Object obj = map.get(part);
            if (obj instanceof Map)
                map = (Map) obj;
            else
                return obj;
        }

        return map;
    }

    public String getDefaultFormat(String name) {
        return (String) getNestedMapValue(localization, "custom_date_formats." + name);
    }


}
