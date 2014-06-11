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

If you are using Maven:

Add the following dependency to your pom.xml:

```xml
<dependency>
  <groupId>com.tr8nhub</groupId>
  <artifactId>core</artifactId>
  <version>0.1.0</version>
</dependency>
```


Integration
==================

If you are building an application using any of the following frameworks, you should use the SDKs for those frameworks instead:

* Android applications: [tr8n_android_clientsdk](https://github.com/tr8n/tr8n_android_clientsdk)
* Swing applications: [tr8n_swing_clientsdk](https://github.com/tr8n/tr8n_swing_clientsdk)
* J2EE web applications (standard): [tr8n_j2ee_clientsdk](https://github.com/tr8n/tr8n_j2ee_clientsdk)
* J2EE web applications (Struts 2): [tr8n_struts2_clientsdk](https://github.com/tr8n/tr8n_struts2_clientsdk)

The SDKs provide extensions that allow for quicker and smoother integration with your application. For example, Android SDK provides SpannableStringTokenizer that you can use for decorating strings.
At the same time, Swing SDK provides AttributedStringTokenizer for the same purposes, but using AWT classes. J2EE SDK provides tag libraries that can be used in JSPs and Struts SDK provides action extensions. 

If you are not using any of the above frameworks, you can use Tr8n core directly and customize functionality based on what you need.


Links
==================

* Register on TranslationExchange.com: https://translationexchange.com

* Read TranslationExchange's documentation: http://wiki.translationexchange.com

* Visit TranslationExchange's blog: http://blog.translationexchange.com

* Follow TranslationExchange on Twitter: https://twitter.com/translationx

* Connect with TranslationExchange on Facebook: https://www.facebook.com/translationexchange

* If you have any questions or suggestions, contact us: info@translationexchange.com


Copyright and license
==================

Copyright (c) 2014 Michael Berkovich, TranslationExchange.com

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


