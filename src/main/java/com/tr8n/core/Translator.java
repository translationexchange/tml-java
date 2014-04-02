package com.tr8n.core;

import java.util.Map;

/**
 * Created by michael on 3/14/14.
 */
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
