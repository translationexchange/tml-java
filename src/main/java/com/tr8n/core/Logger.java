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

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;

public class Logger {
    private org.apache.log4j.Logger theLogger;

    public Logger() {
        this.theLogger = LogManager.getLogger("Tr8n");
        theLogger.setLevel(Level.DEBUG);
        PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");
        
        // TODO: disable console writer
        theLogger.addAppender(new ConsoleAppender(layout));
        
        if (Tr8n.getConfig().getLogger().get("enabled").equals(Boolean.TRUE)) {
	        try {
	            FileAppender fileAppender = new FileAppender(layout, "log/tr8n.log");
	            fileAppender.setName("FileLogger");
	            fileAppender.setImmediateFlush(true);
	            fileAppender.setThreshold(Level.DEBUG);
	            fileAppender.setAppend(true);
	            fileAppender.activateOptions();
	            theLogger.addAppender(fileAppender);
	        } catch (IOException e) {
	            System.out.println("Failed to add appender !!");
	        }
        }
    }

    public void logException(String message, Exception ex) {
        if (message != null) error(message);
        error(ex);
        error(Utils.join(ex.getStackTrace(), "\n"));
    }

    public void logException(Exception ex) {
        this.logException(null, ex);
    }

    public void trace(Object message) {
        theLogger.trace(message);
    }
    public void debug(Object message) {
        theLogger.debug(message);
    }
    public void info(Object message) {
        theLogger.info(message);
    }
    public void warn(Object message) {
        theLogger.warn(message);
    }
    public void error(Object message) {
        theLogger.error(message);
    }
    public void fatal(Object message) {
        theLogger.fatal(message);
    }
}
