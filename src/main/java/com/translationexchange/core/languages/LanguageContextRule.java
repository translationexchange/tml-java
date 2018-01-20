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

package com.translationexchange.core.languages;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.translationexchange.core.Base;
import com.translationexchange.core.rulesengine.Evaluator;
import com.translationexchange.core.rulesengine.Parser;

public class LanguageContextRule extends Base {

  /**
   * Constant <code>TR8N_DEFAULT_RULE_KEYWORD="other"</code>
   */
  public static final String TR8N_DEFAULT_RULE_KEYWORD = "other";

  /**
   * Holds reference to the language context it belongs to
   */
  private LanguageContext languageContext;

  /**
   * Unique key of the context within the language
   */
  private String keyword;

  /**
   * Description of the rule
   */
  private String description;

  /**
   * Examples of the rule application
   */
  private String examples;

  /**
   * Conditions in symbolic notation
   */
  private String conditions;

  /**
   * Optimized conditions parsed into an array
   */
  private List<Object> conditionsExpression;

  /**
   * Default constructor
   */
  public LanguageContextRule() {
    super();
  }

  /**
   * Default constructor
   *
   * @param attributes a {@link java.util.Map} object.
   */
  public LanguageContextRule(Map<String, Object> attributes) {
    super(attributes);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void updateAttributes(Map<String, Object> attributes) {
    if (attributes.get("language_context") != null)
      setLanguageContext((LanguageContext) attributes.get("language_context"));

    setKeyword((String) attributes.get("keyword"));
    setDescription((String) attributes.get("description"));
    setExamples((String) attributes.get("examples"));
    setConditions((String) attributes.get("conditions"));

    if (attributes.get("operations_expression") instanceof List) {
      this.conditionsExpression = (List) attributes.get("conditions_expression");
    }
  }

  /**
   * <p>isFallback.</p>
   *
   * @return boolean
   */
  public boolean isFallback() {
    return getKeyword().equals(TR8N_DEFAULT_RULE_KEYWORD);
  }

  /**
   * <p>Getter for the field <code>conditionsExpression</code>.</p>
   *
   * @return List
   */
  @SuppressWarnings("unchecked")
  public List<Object> getConditionsExpression() {
    if (this.conditionsExpression == null) {
      Parser p = new Parser(this.conditions);
      this.conditionsExpression = (List<Object>) p.parse();
    }

    return this.conditionsExpression;
  }

  /**
   * <p>evaluate.</p>
   *
   * @param vars a {@link java.util.Map} object.
   * @return boolean
   */
  public boolean evaluate(Map<String, Object> vars) {
    if (isFallback())
      return true;

    Evaluator e = new Evaluator();

    Iterator<Entry<String, Object>> entries = vars.entrySet().iterator();
    while (entries.hasNext()) {
      Entry<String, Object> thisEntry = (Entry<String, Object>) entries.next();
      String key = (String) thisEntry.getKey();
      Object value = thisEntry.getValue();
      e.setVariable(key, value);
    }

    return (Boolean) e.evaluate(this.getConditionsExpression());
  }

  /**
   * <p>Getter for the field <code>keyword</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getKeyword() {
    return this.keyword;
  }

  /**
   * <p>Getter for the field <code>languageContext</code>.</p>
   *
   * @return a {@link com.translationexchange.core.languages.LanguageContext} object.
   */
  public LanguageContext getLanguageContext() {
    return languageContext;
  }

  /**
   * <p>Setter for the field <code>languageContext</code>.</p>
   *
   * @param languageContext a {@link com.translationexchange.core.languages.LanguageContext} object.
   */
  public void setLanguageContext(LanguageContext languageContext) {
    this.languageContext = languageContext;
  }

  /**
   * <p>Getter for the field <code>description</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getDescription() {
    return description;
  }

  /**
   * <p>Getter for the field <code>examples</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getExamples() {
    return examples;
  }

  /**
   * <p>Getter for the field <code>conditions</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getConditions() {
    return conditions;
  }

  /**
   * <p>Setter for the field <code>keyword</code>.</p>
   *
   * @param keyword a {@link java.lang.String} object.
   */
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  /**
   * <p>Setter for the field <code>description</code>.</p>
   *
   * @param description a {@link java.lang.String} object.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * <p>Setter for the field <code>examples</code>.</p>
   *
   * @param examples a {@link java.lang.String} object.
   */
  public void setExamples(String examples) {
    this.examples = examples;
  }

  /**
   * <p>Setter for the field <code>conditions</code>.</p>
   *
   * @param conditions a {@link java.lang.String} object.
   */
  public void setConditions(String conditions) {
    this.conditions = conditions;
  }
}
