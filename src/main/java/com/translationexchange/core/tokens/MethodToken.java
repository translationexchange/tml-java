/*
 * Copyright (c) 2018 Translation Exchange, Inc. All rights reserved.
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
 *
 * @author Michael Berkovich
 * @version $Id: $Id
 */

package com.translationexchange.core.tokens;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

import com.translationexchange.core.Tml;
import com.translationexchange.core.decorators.Decorator;
import com.translationexchange.core.languages.Language;

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
   * <p>getExpression.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getExpression() {
    return "(%?\\{{1,2}\\s*[\\w]+\\.\\w*\\s*(:\\s*\\w+)*\\s*(::\\s*\\w+)*\\s*\\}{1,2})";
  }

  /**
   * <p>Constructor for MethodToken.</p>
   *
   * @param token a {@link java.lang.String} object.
   */
  public MethodToken(String token) {
    super(token);
  }

  /**
   * <p>Constructor for MethodToken.</p>
   *
   * @param token a {@link java.lang.String} object.
   * @param label a {@link java.lang.String} object.
   */
  public MethodToken(String token, String label) {
    super(token, label);
  }

  /**
   * <p>Getter for the field <code>objectName</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getObjectName() {
    if (this.objectName == null) {
      this.objectName = getParenslessName().split(":")[0].split("\\.")[0];
    }
    return this.objectName;
  }

  /**
   * <p>Getter for the field <code>methodName</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getMethodName() {
    if (this.methodName == null) {
      this.methodName = getName().split("\\.")[1];
    }
    return this.methodName;

  }

  /**
   * <p>getObjectValue.</p>
   *
   * @param object a {@link java.lang.Object} object.
   * @param method a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   */
  @SuppressWarnings("unchecked")
  public String getObjectValue(Object object, String method) {
    if (object == null) {
      logError("{" + getName() + "} in " + getOriginalLabel() + " : object is not provided}");
      return getFullName();
    }

    if (object instanceof Map) {
      Map<String, Object> obj = (Map<String, Object>) object;
      if (obj.get(method) == null) {
        logError("{" + getName() + "} in " + getOriginalLabel() + " : map attribute is missing}");
        return getFullName();
      }

      return obj.get(method).toString();
    }

    try {
      Method m = object.getClass().getMethod(method);
      if (m == null) {
        logError("{" + getName() + "} in " + getOriginalLabel() + " : object does not provide a method}");
        return getFullName();
      }
      return (String) m.invoke(object);
    } catch (Exception ex) {
      logError("{" + getName() + "} in " + getOriginalLabel() + " : failed to execute object method}");
      return getFullName();
    }
  }

  /**
   * Returns decoration name
   *
   * @return a {@link java.lang.String} object.
   */
  public String getDecorationName() {
    return "method";
  }

  /**
   * {@inheritDoc}
   */
  public String substitute(String translatedLabel, Map<String, Object> tokensData, Language language, Map<String, Object> options) {
    Object object = getContextObject(tokensData);
    String value = getObjectValue(object, getMethodName());
    value = applyLanguageCases(value, object, language, options);

    Decorator decorator = Tml.getConfig().getDecorator();
    return translatedLabel.replaceAll(Pattern.quote(getFullName()), decorator.decorateToken(this, value, options));
  }


}
