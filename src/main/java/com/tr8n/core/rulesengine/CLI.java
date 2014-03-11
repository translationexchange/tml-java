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

package com.tr8n.core.rulesengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean done = false;
		
		Parser parser = new Parser();
		Evaluator evaluator = new Evaluator();
		
		while (!done) {
	      System.out.print("$ ");
	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	      String expression = null;
	      try {
	    	  expression = br.readLine();
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read expression");
	         System.exit(1);
	      }
	
	      if (expression.equals("\\q") || expression.equals("\\quit"))
	    	  return;
	      
	      Object result = evaluator.evaluate(parser.parse(expression));
	      System.out.println(result.toString());
		}
	}
	
}
