package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.expression.ConstantExpression;
import org.apache.commons.jxpath.ri.expression.NumberExpression;
import org.apache.commons.jxpath.ri.expression.StringExpression;

public class CoreOperationGreaterThanOrEqualTest extends CompilerTest {

 @Test
 public void testCoreOperationGreaterThanOrEqual() {
 CoreOperationGreaterThanOrEqual expression;

 expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(4.0));
 assertTrue(expression.computeValue(new EvalContext()));

 expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(10.0), new ConstantExpression(5.0));
 assertTrue(expression.computeValue(new EvalContext()));
 }

 // Test cases for edge cases and exceptions
 // ... (Include all the edge cases and exception test cases proposed by experts 1, 2, and 3)
}