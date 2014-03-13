package com.tr8n.core;

import java.util.Map;

/**
 * Created by michael on 3/11/14.
 */
public class Component extends Base {

    /**
     * Reference back to the application it belongs to
     */
    Application application;

    /**
     * Source key
     */
    String key;

    /**
     * Component name given by the admin or developer
     */
    String name;

    /**
     * Component description
     */
    String description;

    /**
     * State of the component
     */
    String state;

    /**
     *
     * @param attributes
     */
    public Component(Map attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    public void updateAttributes(Map attributes) {
        if (attributes.get("application") != null)
            this.application = (Application) attributes.get("application");

        this.key = (String) attributes.get("key");
        this.name = (String) attributes.get("name");
        this.description = (String) attributes.get("description");
        this.state = (String) attributes.get("state");
    }

}
