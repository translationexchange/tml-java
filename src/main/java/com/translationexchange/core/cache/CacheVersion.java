/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
 * <p/>
 * _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 * | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 * | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 * | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 * |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 * __/ |
 * |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.translationexchange.core.Tml;
import com.translationexchange.core.TmlMode;
import com.translationexchange.core.Utils;

public class CacheVersion {

    /**
     * Constant <code>VERSION_KEY="current_version"</code>
     */
    public static String VERSION_KEY = "current_version";

    /**
     * Constant <code>UNRELEASED_VERSION="0"</code>
     */
    public static final String UNRELEASED_VERSION = "0";

    /**
     * Constant <code>KEY_PREFIX="tml"</code>
     */
    protected static String KEY_PREFIX = "tml";

    /**
     * Cache version
     */
    private String version;

    /**
     * Last time the cache was updated
     */
    private Long timestamp;

    /**
     * Expired time
     */
    private Long expiredIn;

    private TmlMode tmlMode = TmlMode.NONE;

    /**
     * Get timestamp
     *
     * @return
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Set timestamp
     *
     * @param timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getExpiredIn() {
        if (expiredIn == null) {
            expiredIn = new Date().getTime();
        }
        return expiredIn;
    }

    public void setExpiredIn(Long expiredIn) {
        this.expiredIn = expiredIn;
    }

    public TmlMode getTmlMode() {
        return tmlMode;
    }

    public void setTmlMode(TmlMode tmlMode) {
        this.tmlMode = tmlMode;
    }

    /**
     * Get version
     *
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set version
     *
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Default constructor, sets the version as unreleased
     */
    public CacheVersion() {
        setVersion(UNRELEASED_VERSION);
    }

    /**
     * Checks if the version has expired and needs a refresh from CDN
     *
     * @return
     */
    public boolean isExpired() {
        if (getTimestamp() == null)
            return true;

        long validWindow = getExpiredIn();
//        long validWindow = getTimestamp().longValue() + getVerificationInterval();
        long now = new Date().getTime();
        return validWindow <= now;
    }

    /**
     * Returns the key under which the current version is stored
     *
     * @return
     */
    public String getVersionKey() {
        List<String> fragments = new ArrayList<String>();
        fragments.add(KEY_PREFIX);

        if (Tml.getCache().getNamespace() != null)
            fragments.add(Tml.getCache().getNamespace());
        fragments.add(VERSION_KEY);

        return Utils.join(fragments, "_");
    }

    /**
     * Marks the version as updated
     */
    public void markAsUpdated() {
        setTimestamp(new Date().getTime());
    }

    /**
     * Convert the version object to JSON
     *
     * @return
     */
    public String toJSON() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("version", getVersion());
        data.put("t", getTimestamp());
        data.put("expired_in", getTimestamp() + getVerificationInterval());
        data.put("tml_mode", getTmlMode().name());
        return Utils.buildJSON(data);
    }

    /**
     * Fetches current version from cache
     *
     * @return a {@link java.lang.String} object.
     */
    public boolean fetchFromCache() {
        String data = (String) Tml.getCache().fetch(getVersionKey(), Utils.buildMap("cache_key", VERSION_KEY));

        // No version has ever been stored in the local cache
        if (data == null) {
            setVersion(UNRELEASED_VERSION);
            setTmlMode(TmlMode.NONE);
        } else {
            updateFromJSON(data);
        }
        return data != null;
    }

    /**
     * Update version from CDN
     *
     * @param data
     */
    public void updateFromCDN(String data) {
        // no release has been published yet on the CDN
        if (data == null) {
            setVersion(UNRELEASED_VERSION);
        } else {
            updateFromJSON(data);
        }

        markAsUpdated();

        Tml.getCache().store(getVersionKey(), toJSON(), Utils.buildMap());
    }

    /**
     * Update version from JSON
     *
     * @param data
     */
    private void updateFromJSON(String data) {
        if (data.indexOf("{") != -1) {
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonData = (Map<String, Object>) Utils.parseJSON(data);
            setVersion((String) jsonData.get("version"));
            if (jsonData.containsKey("t")) {
                setTimestamp((Long) jsonData.get("t"));
            }
            if (jsonData.containsKey("expired_in")) {
                setExpiredIn((Long) jsonData.get("expired_in"));
            }

            if (jsonData.containsKey("tml_mode")) {
                setTmlMode(TmlMode.valueOf(jsonData.get("tml_mode").toString()));
            }
        } else {
            setVersion(data);
        }
    }

    /**
     * Checks if version has not been released
     *
     * @return
     */
    public boolean isUnreleased() {
        return UNRELEASED_VERSION.equals(getVersion());
    }

    /**
     * Resets the version
     */
    public void reset() {
        Tml.getCache().delete(getVersionKey(), null);
    }

    /**
     * Returns verification interval
     *
     * @return
     */
    public static long getVerificationInterval() {
        Integer seconds = (Integer) Tml.getConfig().getCache().get("version_check_interval");

        if (seconds == null)
            seconds = new Integer(60 * 60);

        return seconds.intValue() * 1000;
    }

    /**
     * Gets expiration message
     *
     * @return
     */
    public String getExpirationMessage() {
        if (getTimestamp() == null)
            return "expired";

        long validWindow = getExpiredIn();
//        long validWindow = getTimestamp().longValue() + getVerificationInterval();
        long now = new Date().getTime();

        if (now > validWindow)
            return "expired " + ((now - validWindow) / 1000) + " s ago";

        return "expires in " + ((validWindow - now) / 1000) + "s";
    }

}