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

package com.tr8n.core;

import java.util.Map;


public abstract class Cache {
	private Map<String, Object> config;
	
	public Cache(Map<String, Object> config) {
		this.config = config;
	}
	
	public Integer getVersion() {
		return (Integer) getConfig().get("version");
	}
	
	public void setVersion(Integer version) {
		getConfig().put("version", version);
	}

	public void incrementVersion() {
		setVersion(getVersion() + 1);
	}

	protected String getVersionedKey(String key) {
		return key;
	}
	
    public boolean isInlineMode(Map<String, Object> options) {
		if (options == null || options.get("session") == null) 
			return false;
		
		Session session = (Session) options.get("session"); 
		if (session.getCurrentTranslator() == null)
			return false;
		
		return session.getCurrentTranslator().isInline();
    }
    
    public abstract Object fetch(String key, Map<String, Object> options);
    
    public abstract void store(String key, Object data, Map<String, Object> options);

    public abstract void delete(String key, Map<String, Object> options);

	public Map<String, Object> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

}
