
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
 *
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core;

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

    /**
     * <p>Constructor for Translator.</p>
     */
    public Translator() {
        super();
    }

    /**
     * <p>Constructor for Translator.</p>
     *
     * @param attributes a {@link java.util.Map} object.
     */
    public Translator(Map<String, Object> attributes) {
        super(attributes);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
	@Override
    public void updateAttributes(Map<String, Object> attributes) {
    	setName((String)attributes.get("name"));
    	setEmail((String)attributes.get("email"));
    	setManager((Boolean)attributes.get("manager"));
    	setInline((Boolean)attributes.get("inline"));
    	setFeatures((Map<String, Boolean>)attributes.get("features"));
    }

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>Getter for the field <code>email</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * <p>Setter for the field <code>email</code>.</p>
	 *
	 * @param email a {@link java.lang.String} object.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * <p>Getter for the field <code>gender</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * <p>Setter for the field <code>gender</code>.</p>
	 *
	 * @param gender a {@link java.lang.String} object.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * <p>Getter for the field <code>mugshot</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMugshot() {
		return mugshot;
	}

	/**
	 * <p>Setter for the field <code>mugshot</code>.</p>
	 *
	 * @param mugshot a {@link java.lang.String} object.
	 */
	public void setMugshot(String mugshot) {
		this.mugshot = mugshot;
	}

	/**
	 * <p>isInline.</p>
	 *
	 * @return a {@link java.lang.Boolean} object.
	 */
	public Boolean isInline() {
		if (inline == null) return false;
		return inline;
	}

	/**
	 * <p>Setter for the field <code>inline</code>.</p>
	 *
	 * @param inline a {@link java.lang.Boolean} object.
	 */
	public void setInline(Boolean inline) {
		this.inline = inline;
	}

	/**
	 * <p>Getter for the field <code>features</code>.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, Boolean> getFeatures() {
		return features;
	}

	/**
	 * <p>Setter for the field <code>features</code>.</p>
	 *
	 * @param features a {@link java.util.Map} object.
	 */
	public void setFeatures(Map<String, Boolean> features) {
		this.features = features;
	}

	/**
	 * <p>Getter for the field <code>votingPower</code>.</p>
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	public Long getVotingPower() {
		return votingPower;
	}

	/**
	 * <p>Setter for the field <code>votingPower</code>.</p>
	 *
	 * @param votingPower a {@link java.lang.Long} object.
	 */
	public void setVotingPower(Long votingPower) {
		this.votingPower = votingPower;
	}

	/**
	 * <p>Getter for the field <code>rank</code>.</p>
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	public Long getRank() {
		return rank;
	}

	/**
	 * <p>Setter for the field <code>rank</code>.</p>
	 *
	 * @param rank a {@link java.lang.Long} object.
	 */
	public void setRank(Long rank) {
		this.rank = rank;
	}

	/**
	 * <p>Getter for the field <code>level</code>.</p>
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	public Long getLevel() {
		return level;
	}

	/**
	 * <p>Setter for the field <code>level</code>.</p>
	 *
	 * @param level a {@link java.lang.Long} object.
	 */
	public void setLevel(Long level) {
		this.level = level;
	}

	/**
	 * <p>Getter for the field <code>locale</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * <p>Setter for the field <code>locale</code>.</p>
	 *
	 * @param locale a {@link java.lang.String} object.
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * <p>isManager.</p>
	 *
	 * @return a {@link java.lang.Boolean} object.
	 */
	public Boolean isManager() {
		if (manager == null)
			return false;
		return manager;
	}

	/**
	 * <p>Setter for the field <code>manager</code>.</p>
	 *
	 * @param manager a {@link java.lang.Boolean} object.
	 */
	public void setManager(Boolean manager) {
		this.manager = manager;
	}

	/**
	 * <p>Getter for the field <code>code</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * <p>Setter for the field <code>code</code>.</p>
	 *
	 * @param code a {@link java.lang.String} object.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * <p>Getter for the field <code>accessToken</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * <p>Setter for the field <code>accessToken</code>.</p>
	 *
	 * @param accessToken a {@link java.lang.String} object.
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
    
    /**
     * <p>isFeatureEnabled.</p>
     *
     * @param feature a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isFeatureEnabled(String feature) {
    	if (getFeatures() == null)
    		return false;
    	return getFeatures().get(feature);
    }
}
