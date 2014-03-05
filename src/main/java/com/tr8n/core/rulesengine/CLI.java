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
