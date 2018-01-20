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

package com.translationexchange.core.tools;

import com.translationexchange.core.Base;

import java.util.Map;

/**
 * Created by ababenko on 9/8/16.
 */
public class Tools extends Base {
  private String translationCenter;
  private String mobileTranslationCenter;
  private String mobileTranslationCenterKey;

  /**
   * Constructor from attributes
   *
   * @param attributes a {@link Map} object.
   */
  public Tools(Map<String, Object> attributes) {
    super(attributes);
  }

  public String getTranslationCenter() {
    return translationCenter;
  }

  public void setTranslationCenter(String translationCenter) {
    this.translationCenter = translationCenter;
  }

  public String getMobileTranslationCenter() {
    return mobileTranslationCenter;
  }

  public void setMobileTranslationCenter(String mobileTranslationCenter) {
    this.mobileTranslationCenter = mobileTranslationCenter;
  }

  public String getMobileTranslationCenterKey() {
    return mobileTranslationCenterKey;
  }

  public void setMobileTranslationCenterKey(String mobileTranslationCenterKey) {
    this.mobileTranslationCenterKey = mobileTranslationCenterKey;
  }

  /**
   * Updates object's attributes
   *
   * @param attributes a {@link Map} object.
   */
  @Override
  public void updateAttributes(Map<String, Object> attributes) {
    if (attributes == null || attributes.isEmpty())
      return;
    setTranslationCenter((String) attributes.get("translation_center"));
    setMobileTranslationCenter((String) attributes.get("mobile_translation_center"));
    setMobileTranslationCenterKey((String) attributes.get("mobile_translation_center_key"));
  }
}
