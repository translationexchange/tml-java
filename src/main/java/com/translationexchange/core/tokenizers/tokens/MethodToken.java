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

package com.translationexchange.core.tokenizers.tokens;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

import com.translationexchange.core.Language;
import com.translationexchange.core.Tml;

public class MethodToken extends DataToken {

    /**
     * Object name
     */
    String objectName;

    /**
     * Name of the method
     */
    String methodName;


    /**
     *
     * @return
     */
    public static String getExpression() {
        return "(%?\\{{1,2}\\s*[\\w]*\\.\\w*\\s*(:\\s*\\w+)*\\s*(::\\s*\\w+)*\\s*\\}{1,2})";
    }

    /**
     *
     * @param token
     */
    public MethodToken(String token) {
        super(token);
    }

    /**
     *
     * @param token
     * @param label
     */
    public MethodToken(String token, String label) {
        super(token, label);
    }

    /**
     *
     * @return
     */
    public String getObjectName() {
        if (this.objectName == null) {
            this.objectName = getParenslessName().split(":")[0].split("\\.")[0];
        }
        return  this.objectName;
    }

    /**
     *
     * @return
     */
    public String getMethodName() {
        if (this.methodName == null) {
            this.methodName = getName().split("\\.")[1];
        }
        return  this.methodName;

    }

    /**
     *
     * @param object
     * @param method
     * @return
     */
    @SuppressWarnings("unchecked")
	public String getObjectValue(Object object, String method) {
        if (object == null) {
            Tml.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : object is not provided}");
            return getFullName();
        }

        if (object instanceof Map) {
            Map<String, Object> obj = (Map<String, Object>) object;
            if (obj.get(method) == null) {
                Tml.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : map attribute is missing}");
                return getFullName();
            }

            return obj.get(method).toString();
        }

        try {
            Method m = object.getClass().getMethod(method);
            if (m == null) {
                Tml.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : object does not provide a method}");
                return getFullName();
            }
            return (String) m.invoke(object);
        } catch (Exception ex) {
            Tml.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : failed to execute object method}");
            return getFullName();
        }
    }

    /**
     *
     * @param translatedLabel   label in which the substitution happens
     * @param tokensData        map of data tokens
     * @param language          language in which the substitution happens
     * @param options           options for substitutions
     * @return
     */
    public String substitute(String translatedLabel, Map<String, Object> tokensData, Language language, Map<String, Object> options) {
        Object object = getContextObject(tokensData);
        String value = getObjectValue(object, getMethodName());
        value = applyLanguageCases(value, object, language, options);
        return translatedLabel.replaceAll(Pattern.quote(getFullName()), value);
    }

}
