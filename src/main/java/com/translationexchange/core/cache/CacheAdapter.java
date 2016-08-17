/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
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
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core.cache;

import java.util.Map;

import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;
public abstract class CacheAdapter implements Cache {

	private Map<String, Object> config;
		
	/**
	 * Initialized Cache Adapter
	 *
	 * @param config a {@link java.util.Map} object.
	 */
	public CacheAdapter(Map<String, Object> config) {
		this.config = config;
	}
	
	/**
	 * Return cache configuration
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, Object> getConfig() {
		return config;
	}

	/**
	 * Sets cache configuration
	 *
	 * @param config a {@link java.util.Map} object.
	 */
	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	/**
	 * Writes debug info
	 *
	 * @param msg a {@link java.lang.String} object.
	 */
	protected void debug(String msg) {
		Tml.getLogger().debug(this.getClass().getName() + " - " + msg);
	}
	
	/**
	 * Returns cache name space
	 */
	public String getNamespace() {
		return getConfigProperty("namespace");
	}

	/**
	 * Returns configuration property
	 * 
	 * @param key
	 * @return
	 */
	protected String getConfigProperty(String key) {
		return getConfigProperty(key, null);
	}

	/**
	 * Returns configuration property with default value
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected String getConfigProperty(String key, String defaultValue) {
		if (getConfig() == null || getConfig().get(key) == null)
			return defaultValue;
		
		return (String) getConfig().get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param options
	 * @return
	 */
	protected String getVersionedKey(String key, Map<String, Object> options) {
		
		String version = (String) options.get(CacheVersion.VERSION_KEY);
		
		String elements[] = {
				CacheVersion.KEY_PREFIX,
	         (Tml.getCache().getNamespace() == null ? "" : Tml.getCache().getNamespace()),
	         (key == CacheVersion.VERSION_KEY) ? "v" : "v" + (version == null ? "0" : version),
	         key
		};
		
		return Utils.join(elements, "_");
	}
}
