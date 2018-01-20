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

package com.translationexchange.core.decorators;

import java.util.Map;

import com.translationexchange.core.TranslationKey;
import com.translationexchange.core.languages.Language;
import com.translationexchange.core.languages.LanguageCase;
import com.translationexchange.core.languages.LanguageCaseRule;
import com.translationexchange.core.tokens.Token;

public interface Decorator {

  /**
   * <p>decorate.</p>
   *
   * @param translatedLabel a {@link java.lang.String} object.
   * @param translationLanguage a {@link com.translationexchange.core.languages.Language} object.
   * @param targetLanguage a {@link com.translationexchange.core.languages.Language} object.
   * @param translationKey a {@link com.translationexchange.core.TranslationKey} object.
   * @param options a {@link java.util.Map} object.
   * @return a {@link java.lang.String} object.
   */
  String decorate(String translatedLabel, Language translationLanguage, Language targetLanguage, TranslationKey translationKey, Map<String, Object> options);

  /**
   * <p>decorateLanguageCase.</p>
   *
   * @param languageCase a {@link com.translationexchange.core.languages.LanguageCase} object.
   * @param rule a {@link com.translationexchange.core.languages.LanguageCaseRule} object.
   * @param original a {@link java.lang.String} object.
   * @param transformed a {@link java.lang.String} object.
   * @param options a {@link java.util.Map} object.
   * @return a {@link java.lang.String} object.
   */
  String decorateLanguageCase(LanguageCase languageCase, LanguageCaseRule rule, String original, String transformed, Map<String, Object> options);

  /**
   * <p>decorateToken.</p>
   *
   * @param token a {@link com.translationexchange.core.tokens.Token} object.
   * @param value a {@link java.lang.String} object.
   * @param options a {@link java.util.Map} object.
   * @return a {@link java.lang.String} object.
   */
  String decorateToken(Token token, String value, Map<String, Object> options);

  /**
   * <p>decorateElement.</p>
   *
   * @param token a {@link com.translationexchange.core.tokens.Token} object.
   * @param value a {@link java.lang.String} object.
   * @param options a {@link java.util.Map} object.
   * @return a {@link java.lang.String} object.
   */
  String decorateElement(Token token, String value, Map<String, Object> options);

}
