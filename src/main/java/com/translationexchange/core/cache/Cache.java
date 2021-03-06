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

package com.translationexchange.core.cache;

import com.translationexchange.core.Application;

import java.util.Map;

public interface Cache {

  /**
   * <p>fetch.</p>
   *
   * @param key     a {@link java.lang.String} object.
   * @param options a {@link java.util.Map} object.
   * @return a {@link java.lang.Object} object.
   */
  Object fetch(String key, Map<String, Object> options);

  /**
   * <p>store.</p>
   *
   * @param key     a {@link java.lang.String} object.
   * @param data    a {@link java.lang.Object} object.
   * @param options a {@link java.util.Map} object.
   */
  void store(String key, Object data, Map<String, Object> options);

  /**
   * <p>delete.</p>
   *
   * @param key     a {@link java.lang.String} object.
   * @param options a {@link java.util.Map} object.
   */
  void delete(String key, Map<String, Object> options);

  /**
   * Returns cache name space
   *
   * @return String
   */
  String getNamespace();

  /**
   * Verify that the current cache version is correct
   * Check it against the API
   */
  CacheVersion verifyCacheVersion(Application application) throws Exception;
}
