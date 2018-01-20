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

import com.translationexchange.core.Session;
import com.translationexchange.core.TranslationKey;
import com.translationexchange.core.Utils;
import com.translationexchange.core.languages.Language;
import com.translationexchange.core.languages.LanguageCase;
import com.translationexchange.core.languages.LanguageCaseRule;
import com.translationexchange.core.tokens.Token;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HtmlDecorator implements Decorator {

  /**
   * Checks if decorator is enabled
   *
   * @param options
   * @return
   */
  private boolean isEnabled(Map<String, Object> options) {
    if (options == null || options.isEmpty() || options.get("skip_decorations") != null)
      return false;

    Session session = (Session) options.get(Session.SESSION_KEY);

    if (session == null || !session.getApplication().isLoaded())
      return false;

    return (session.getCurrentTranslator() != null && session.getCurrentTranslator().isInline());
  }

  /**
   * @param defaultName
   * @param options
   * @return
   */
  private String getDecorationElement(String defaultName, Map<String, Object> options) {
    if (options.get("use_span") != null)
      return "span";

    if (options.get("use_div") != null)
      return "div";

    return defaultName;
  }

  /**
   * @param translatedLabel     a {@link java.lang.String} object.
   * @param translationLanguage a {@link com.translationexchange.core.languages.Language} object.
   * @param targetLanguage      a {@link com.translationexchange.core.languages.Language} object.
   * @param translationKey      a {@link com.translationexchange.core.TranslationKey} object.
   * @param options             a {@link java.util.Map} object.
   * @return
   */
  public String decorate(String translatedLabel, Language translationLanguage, Language targetLanguage, TranslationKey translationKey, Map<String, Object> options) {
//        Tml.getLogger().debug(translatedLabel + " : " + translationLanguage.getLocale() + " :: " + translationKey.getLocale() + " => " + targetLanguage.getLocale());

    if (!isEnabled(options))
      return translatedLabel;

    if (translationKey.getApplication() == null || (translationKey.getApplication().isFeatureEnabled("lock_original_content") && translationKey.getLocale() != null && translationKey.getLocale().equals(targetLanguage.getLocale())))
      return translatedLabel;

    String element = getDecorationElement("tml:label", options);

    StringBuilder sb = new StringBuilder();

    List<String> classes = new ArrayList<String>();
    classes.add("tml_translatable");

    if (options.get("locked") != null && options.get("locked").equals("true")) {
      classes.add("tml_locked");
    } else if (translationLanguage.getLocale().equals(translationKey.getLocale())) {
      if (options.get("pending") != null && options.get("pending").equals("true")) {
        classes.add("tml_pending");
      } else {
        classes.add("tml_not_translated");
      }
    } else if (translationLanguage.getLocale().equals(targetLanguage.getLocale())) {
      classes.add("tml_translated");
    } else {
      classes.add("tml_fallback");
    }

    sb.append("<").append(element);
    sb.append(" class='").append(Utils.join(classes.toArray(), " ")).append("'");
    sb.append(" data-translation_key='").append(translationKey.getKey()).append("'");
    sb.append(" data-target_locale='").append(targetLanguage.getLocale()).append("'");
    sb.append(">")
        .append(translatedLabel)
        .append("</").append(element).append(">");

    return sb.toString();
  }

  /**
   * @param languageCase a {@link com.translationexchange.core.languages.LanguageCase} object.
   * @param rule         a {@link com.translationexchange.core.languages.LanguageCaseRule} object.
   * @param original     a {@link java.lang.String} object.
   * @param transformed  a {@link java.lang.String} object.
   * @param options      a {@link java.util.Map} object.
   * @return
   */
  public String decorateLanguageCase(LanguageCase languageCase, LanguageCaseRule rule, String original, String transformed, Map<String, Object> options) {
    if (!isEnabled(options))
      return transformed;

    Map<String, Object> data = Utils.map(
        "keyword", languageCase.getKeyword(),
        "language_name", languageCase.getLanguage().getEnglishName(),
        "latin_name", languageCase.getLatinName(),
        "native_name", languageCase.getNativeName(),
        "conditions", (rule != null ? rule.getConditions() : ""),
        "operations", (rule != null ? rule.getOperations() : ""),
        "original", original,
        "transformed", transformed
    );

    String payload = Utils.buildJSON(data);
    payload = Base64.encodeBase64String(StringUtils.getBytesUtf8(payload));
    payload.replaceAll("\n", "");

    Map<String, Object> attributes = Utils.map(
        "class", "tml_language_case",
        "data-locale", languageCase.getLanguage().getLocale(),
        "data-rule", payload
    );

    String element = getDecorationElement("tml:case", options);
    StringBuilder html = new StringBuilder();

    try {
      html.append("<" + element + " " + Utils.buildQueryString(attributes) + ">");
      html.append(transformed);
      html.append("</" + element + ">");
    } catch (Exception ex) {
      html.append(transformed);
    }

    return html.toString();
  }

  /**
   * @param token   a {@link com.translationexchange.core.tokens.Token} object.
   * @param value   a {@link java.lang.String} object.
   * @param options a {@link java.util.Map} object.
   * @return
   */
  public String decorateToken(Token token, String value, Map<String, Object> options) {
    if (!isEnabled(options))
      return value;

    String element = getDecorationElement("tml:token", options);

    List<String> classes = new ArrayList<String>();
    classes.add("tml_token");
    classes.add("tml_token_" + token.getDecorationName());

    StringBuilder html = new StringBuilder();
    html.append("<")
        .append(element)
        .append(" class='")
        .append(Utils.join(classes.toArray(), " "))
        .append("' data-name='")
        .append(token.getName())
        .append("'");

    if (!token.getLanguageContextKeys().isEmpty())
      html.append(" data-context='")
          .append(Utils.join(token.getLanguageContextKeys(), " "))
          .append("'");

    if (!token.getLanguageCaseKeys().isEmpty())
      html.append(" data-case='")
          .append(Utils.join(token.getLanguageCaseKeys(), " "))
          .append("'");

    html.append(">")
        .append(value)
        .append("</").append(element).append(">");

    return html.toString();
  }

  /**
   * @param token   a {@link com.translationexchange.core.tokens.Token} object.
   * @param value   a {@link java.lang.String} object.
   * @param options a {@link java.util.Map} object.
   * @return
   */
  public String decorateElement(Token token, String value, Map<String, Object> options) {
    if (!isEnabled(options))
      return value;

    String element = getDecorationElement("tml:element", options);

    return "<" + element + ">" + value + "</" + element + ">";
  }

}
