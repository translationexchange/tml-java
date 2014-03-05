package com.tr8n.core.rulesengine;

import java.util.List;

public interface Expression {
	public Object evaluate(Evaluator evaluator, List<Object> objects);
}
   
