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

package com.translationexchange.core.cache;

import java.util.Map;

import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;

public abstract class CacheAdapter implements Cache {
	private Map<String, Object> config;
	protected static String VERSION_KEY = "current_version";
	protected static String KEY_PREFIX  = "tml";
	
	private String version;
	
	/**
	 * Initialized Cache Adapter
	 * 
	 * @param config
	 */
	public CacheAdapter(Map<String, Object> config) {
		this.config = config;
	}

	/**
	 * Fetches current version from cache
	 * 
	 * @return
	 */
	public String fetchVersion() {
		String version = (String) fetch(VERSION_KEY, Utils.buildMap());
		Tml.getLogger().debug("Fetched cache version: " + version);
		return version;
	}
	
	/**
	 * Stores current version in the cache
	 * 
	 * @param version
	 */
	public void storeVersion(String version) {
		setVersion(version);
		store(VERSION_KEY, version, null);
	}
	
	/**
	 * Returns current version
	 * 
	 * @return
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets current version
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version; 
	}

	/**
	 * Returns cache timeout
	 * 
	 * @return
	 */
	public int getTimeout() {
		if (getConfig().get("timeout") == null) 
			return 0;
		return (Integer) getConfig().get("timeout");
	}
	
	/**
	 * Returns key wrapped in version
	 * 
	 * @param key
	 * @return
	 */
	
	// TODO: move it out to HTTP Client - version is not thread safe!
	protected String getVersionedKey(String key) {
		String elements[] = {
	         KEY_PREFIX,
	         (getConfig().get("namespace") == null ? "" : (String) getConfig().get("namespace")),
	         (key == VERSION_KEY) ? "v" : "v" + (getVersion() == null ? "0" : getVersion().toString()),
	         key
		};
		
		return Utils.join(elements, "_");
	}
	
	/**
	 * Return cache configuration
	 * 
	 * @return
	 */
	public Map<String, Object> getConfig() {
		return config;
	}

	/**
	 * Sets cache configuration
	 * 
	 * @param config
	 */
	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	/**
	 * Writes debug info
	 * 
	 * @param msg
	 */
	protected void debug(String msg) {
		Tml.getLogger().debug(this.getClass().getName() + " - " + msg);
	}

    /**
     * Resets version
     */
    public void resetVersion() {
    	delete(VERSION_KEY, null);
    }
}
