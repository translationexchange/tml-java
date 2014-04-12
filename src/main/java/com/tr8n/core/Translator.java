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

public class Translator extends Base {

    private String name;

    private String email;

    private String gender;

    private String mugshot;

    private Boolean inline;

    private Map<String, Boolean> features;

    private Long votingPower;

    private Long rank;

    private Long level;

    private String locale;

    private Boolean manager;

    private String code;

    private String accessToken;

    public Translator() {
        super();
    }

    public Translator(Map<String, Object> attributes) {
        super(attributes);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void updateAttributes(Map<String, Object> attributes) {
    	setName((String)attributes.get("name"));
    	setEmail((String)attributes.get("email"));
    	setManager((Boolean)attributes.get("manager"));
    	setInline((Boolean)attributes.get("inline"));
    	setFeatures((Map<String, Boolean>)attributes.get("features"));
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMugshot() {
		return mugshot;
	}

	public void setMugshot(String mugshot) {
		this.mugshot = mugshot;
	}

	public Boolean isInline() {
		return inline;
	}

	public void setInline(Boolean inline) {
		this.inline = inline;
	}

	public Map<String, Boolean> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, Boolean> features) {
		this.features = features;
	}

	public Long getVotingPower() {
		return votingPower;
	}

	public void setVotingPower(Long votingPower) {
		this.votingPower = votingPower;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Boolean isManager() {
		if (manager == null)
			return false;
		return manager;
	}

	public void setManager(Boolean manager) {
		this.manager = manager;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
    
    public boolean isFeatureEnabled(String feature) {
    	if (getFeatures() == null)
    		return false;
    	return getFeatures().get(feature);
    }
}
