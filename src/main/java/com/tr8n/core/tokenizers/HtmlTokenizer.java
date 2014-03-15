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

package com.tr8n.core.tokenizers;

import com.tr8n.core.Tr8n;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HtmlTokenizer extends DecorationTokenizer {

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
    protected String applyToken(String token, String value) {
        if (token.equals(RESERVED_TOKEN) || !isTokenAllowed(token))
            return value;

        if (this.tokensData == null || this.tokensData.get(token) == null || this.tokensData.get(token) instanceof Map) {
            String defaultValue = Tr8n.getConfig().getDefaultTokenValue(token, "decoration", "html");
            if (defaultValue == null) return value;

            defaultValue = defaultValue.replaceAll(Pattern.quote(PLACEHOLDER), value);

            if (this.tokensData != null && this.tokensData.get(token) instanceof Map) {
                Map map = (Map) this.tokensData.get(token);
                Iterator entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
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
