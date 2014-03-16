package com.tr8n.core;

import java.util.Map;

/**
 * Created by michael on 3/14/14.
 */
public class Translator extends Base {

    private Application application;

    private String name;

    private String email;

    private String gender;

    private String mugshot;

    private String link;

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

    @Override
    public void updateAttributes(Map<String, Object> attributes) {

    }
}
