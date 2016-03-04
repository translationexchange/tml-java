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

package com.translationexchange.core.notifications;

import java.util.Map;
public class Notification {
	private String topic;
	private Map<String, Object> data;
	
	/**
	 * <p>Constructor for Notification.</p>
	 *
	 * @param topic a {@link java.lang.String} object.
	 */
	public Notification(String topic) {
		this(topic, null);
	}
	
	/**
	 * <p>Constructor for Notification.</p>
	 *
	 * @param topic a {@link java.lang.String} object.
	 * @param data a {@link java.util.Map} object.
	 */
	public Notification(String topic, Map<String, Object> data) {
		setTopic(topic);
		setData(data);
	}
	
	/**
	 * <p>Getter for the field <code>topic</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTopic() {
		return topic;
	}
	
	/**
	 * <p>Setter for the field <code>topic</code>.</p>
	 *
	 * @param topic a {@link java.lang.String} object.
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	/**
	 * <p>Getter for the field <code>data</code>.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, Object> getData() {
		return data;
	}
	
	/**
	 * <p>Setter for the field <code>data</code>.</p>
	 *
	 * @param data a {@link java.util.Map} object.
	 */
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
