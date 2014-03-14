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

package com.tr8n.core.tokenizers.tokens;

import com.tr8n.core.Language;
import com.tr8n.core.Tr8n;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
        return "(\\{[^_:.][\\w]*(\\.[\\w]+)(:[\\w]+)*(::[\\w]+)*\\})";
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
    public String getObjectValue(Object object, String method) {
        if (object == null) {
            Tr8n.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : object is not provided}");
            return getFullName();
        }

        if (object instanceof Map) {
            Map obj = (Map) object;
            if (obj.get(method) == null) {
                Tr8n.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : map attribute is missing}");
                return getFullName();
            }

            return obj.get(method).toString();
        }

        try {
            Class tokenClass = object.getClass();
            Method m = tokenClass.getMethod(method);
            if (m == null) {
                Tr8n.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : object does not provide a method}");
                return getFullName();
            }
            return (String) m.invoke(object);
        } catch (Exception ex) {
            Tr8n.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : failed to execute object method}");
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
    public String substitute(String translatedLabel, Map tokensData, Language language, Map options) {
        Object object = getContextObject(tokensData);
        String value = getObjectValue(object, getMethodName());
        value = applyLanguageCases(value, object, language, options);
        return translatedLabel.replaceAll(Pattern.quote(getFullName()), value);
    }

}
