<p align="center">
  <img src="https://raw.github.com/tr8n/tr8n/master/doc/screenshots/tr8nlogo.png">
</p>

Tr8n for Java
===

[![Build Status](https://travis-ci.org/tr8n/tr8n_java_core.png?branch=master)](https://travis-ci.org/tr8n/tr8n_java_core)
[![Coverage Status](https://coveralls.io/repos/tr8n/tr8n_java_core/badge.png?branch=master)](https://coveralls.io/r/tr8n/tr8n_java_core?branch=master)
[![Project status](http://stillmaintained.com/tr8n/tr8n_java_core.png)](http://stillmaintained.com/tr8n/tr8n_java_core.png)

This java package provides the core classes for the Tr8n translation service.

Installation
==================

Add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>tr8n</groupId>
  <artifactId>tr8n_core</artifactId>
  <version>0.1.0</version>
</dependency>
```


Integration
==================

If you are building an application using any of the following frameworks, you should use the SDKs for those frameworks:

* Android applications: [tr8n_android_clientsdk](https://github.com/tr8n/tr8n_android_clientsdk)
* Swing applications: [tr8n_swing_clientsdk](https://github.com/tr8n/tr8n_swing_clientsdk)
* J2EE web applications (standard): [tr8n_j2ee_clientsdk](https://github.com/tr8n/tr8n_j2ee_clientsdk)
* J2EE web applications (Struts 2): [tr8n_struts2_clientsdk](https://github.com/tr8n/tr8n_struts2_clientsdk)

The SDKs provide extensions that allow for quicker and smoother integration with your application. For example, Android SDK provides SpannableStringTokenizer that you can use for decorating strings.
At the same time, Swing SDK provides AttributedStringTokenizer for the same purposes, but using AWT classes. J2EE SDK provides tag libraries that can be used in JSPs and Struts SDK provides action extensions. 

If you are not using any of the above frameworks, you can use Tr8n core directly and customize functionality based on what you need.


Where can I get more information?
==================

* Visit Tr8nHub's documentation: http://wiki.tr8nhub.com/

* Follow Tr8nHub on Twitter: https://twitter.com/Tr8nHub

* Connect with Tr8nHub on Facebook: https://www.facebook.com/pages/tr8nhubcom/138407706218622

* If you have any questions, contact: michael@tr8nhub.com


