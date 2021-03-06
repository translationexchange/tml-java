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

package com.translationexchange.core.tokenizers;

import com.translationexchange.core.Utils;
import com.translationexchange.core.languages.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StyledTokenizer extends DecorationTokenizer {

  /**
   * Constant <code>ATTRIBUTE_RANGE_ORIGIN="origin"</code>
   */
  public static final String ATTRIBUTE_RANGE_ORIGIN = "origin";
  /**
   * Constant <code>ATTRIBUTE_RANGE_LENGTH="length"</code>
   */
  public static final String ATTRIBUTE_RANGE_LENGTH = "length";

  protected Map<String, List<Map<String, Object>>> attributes;

  /**
   * <p>Constructor for StyledTokenizer.</p>
   *
   * @param label a {@link java.lang.String} object.
   */
  public StyledTokenizer(String label) {
    this(label, null);
  }

  /**
   * <p>Constructor for StyledTokenizer.</p>
   *
   * @param label             a {@link java.lang.String} object.
   * @param allowedTokenNames a {@link java.util.List} object.
   */
  public StyledTokenizer(String label, List<String> allowedTokenNames) {
    super(label, allowedTokenNames);
  }

  /**
   * <p>evaluate.</p>
   *
   * @param expr     a {@link java.lang.Object} object.
   * @param location a int.
   * @return a {@link java.lang.String} object.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  protected String evaluate(Object expr, int location) {
    if (!(expr instanceof List))
      return expr.toString();

    List<Object> args = new ArrayList<Object>((List) expr);
    String token = (String) args.remove(0);

    List<Map<String, Object>> attributedSet = (List<Map<String, Object>>) this.attributes.get(token);
    if (attributedSet == null) {
      attributedSet = new ArrayList<Map<String, Object>>();
      this.attributes.put(token, attributedSet);
    }

    Map<String, Object> attribute = new HashMap<String, Object>();
    attribute.put(ATTRIBUTE_RANGE_ORIGIN, location);

    List<String> processedValues = new ArrayList<String>();
    for (Object arg : args) {
      String value = evaluate(arg, location);
      location += value.length();
      processedValues.add(value);
    }

    String value = Utils.join(processedValues.toArray(), "");

    attribute.put(ATTRIBUTE_RANGE_LENGTH, value.length());
    attributedSet.add(attribute);

    return applyToken(token, value);
  }


  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Object substitute(Map<String, Object> tokensData, Language language, Map<String, Object> options) {
    this.tokensData = tokensData;
    this.options = options;
    this.attributes = new HashMap<String, List<Map<String, Object>>>();

    String result = evaluate(this.expression, 0);

    Object styledString = createStyledString(result);

    for (String tokenName : this.tokenNames) {
      if (!isTokenAllowed(tokenName))
        continue;

      if (tokensData == null) {
        continue;
      }

      Map<String, Object> styles = (Map<String, Object>) tokensData.get(tokenName);
      if (styles == null)
        continue;

      List<Map<String, Object>> ranges = this.attributes.get(tokenName);
      if (ranges == null)
        continue;

      applyStyles(styledString, styles, ranges);
    }

    return styledString;
  }

  /**
   * Creates a styled string AtrributedString or SpannedString or whatever else...
   *
   * @param label a {@link java.lang.String} object.
   * @return a {@link java.lang.Object} object.
   */
  protected abstract Object createStyledString(String label);

  /**
   * Applies styles to the styled string
   *
   * @param styledString a {@link java.lang.Object} object.
   * @param styles       a {@link java.util.Map} object.
   * @param ranges       a {@link java.util.List} object.
   */
  protected abstract void applyStyles(Object styledString, Map<String, Object> styles, List<Map<String, Object>> ranges);

}
