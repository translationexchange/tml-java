/*
 * Copyright (c) 2018 Translation Exchange, Inc. All rights reserved.
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
 * @author Michael Berkovich
 * @version $Id: $Id
 */

package com.translationexchange.core.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationCenter {
  Map<String, List<NotificationListener>> listeners;

  /**
   * <p>subscribe.</p>
   *
   * @param listener a {@link com.translationexchange.core.notifications.NotificationListener} object.
   */
  public void subscribe(NotificationListener listener) {
    subscribe("*", listener);
  }

  /**
   * <p>subscribe.</p>
   *
   * @param topic    a {@link java.lang.String} object.
   * @param listener a {@link com.translationexchange.core.notifications.NotificationListener} object.
   */
  public void subscribe(String topic, NotificationListener listener) {
    List<NotificationListener> topicListeners = getListeners(topic);
    topicListeners.add(listener);
  }

  /**
   * <p>publish.</p>
   *
   * @param notification a {@link com.translationexchange.core.notifications.Notification} object.
   */
  public void publish(Notification notification) {
    List<NotificationListener> topicListeners = getListeners(notification.getTopic());
    for (NotificationListener listener : topicListeners) {
      listener.onNotification(notification);
    }

    topicListeners = getListeners("*");
    for (NotificationListener listener : topicListeners) {
      listener.onNotification(notification);
    }
  }

  private List<NotificationListener> getListeners(String topic) {
    if (this.listeners == null) {
      this.listeners = new HashMap<String, List<NotificationListener>>();
    }

    List<NotificationListener> topicListeners = this.listeners.get(topic);
    if (topicListeners == null) {
      topicListeners = new ArrayList<NotificationListener>();
      this.listeners.put(topic, topicListeners);
    }
    return topicListeners;
  }

}
