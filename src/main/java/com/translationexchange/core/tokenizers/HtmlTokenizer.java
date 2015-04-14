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

package com.translationexchange.core.tokenizers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.translationexchange.core.Tml;

public class HtmlTokenizer extends DecorationTokenizer {

	/**
	 * Default constructor
	 */
    public HtmlTokenizer() {
        super();
    }
	
    /**
     *
     * @param label
     */
    public HtmlTokenizer(String label) {
        this(label, null);
    }

    /**
     *
     * @param label
     * @param allowedTokenNames
     */
    public HtmlTokenizer(String label, List<String> allowedTokenNames) {
        super(label, allowedTokenNames);
    }

    /**
     *
     * @param token
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
	protected String applyToken(String token, String value) {
        if (token.equals(RESERVED_TOKEN) || !isTokenAllowed(token))
            return value;

        if (this.tokensData == null || this.tokensData.get(token) == null || this.tokensData.get(token) instanceof Map) {
            String defaultValue = Tml.getConfig().getDefaultTokenValue(token, "decoration", "html");
            if (defaultValue == null) return value;

            defaultValue = defaultValue.replaceAll(Pattern.quote(PLACEHOLDER), value);

            if (this.tokensData != null && this.tokensData.get(token) instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) this.tokensData.get(token);
                Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, Object> entry = entries.next();
                    String param = "{$" + entry.getKey() + "}";
                    defaultValue = defaultValue.replaceAll(Pattern.quote(param), (String) entry.getValue());
                }
            }

            return defaultValue;
        }

        Object object = this.tokensData.get(token);

        if (object instanceof DecorationTokenValue) {
            DecorationTokenValue dtv = (DecorationTokenValue) object;
            return dtv.getSubstitutionValue(value);
        }

        if (object instanceof String) {
            String str = (String) object;
            return str.replaceAll(Pattern.quote(PLACEHOLDER), value);
        }

        return value;
    }

}
