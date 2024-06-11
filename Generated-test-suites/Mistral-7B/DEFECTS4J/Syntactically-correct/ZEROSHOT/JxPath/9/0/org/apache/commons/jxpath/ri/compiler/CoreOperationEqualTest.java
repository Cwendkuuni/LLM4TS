package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri. expressions.Expression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationEqual coreOperationEqual;

 @Before
 public void setUp() {
 arg1 = new LiteralExpression(1);
 arg2 = new LiteralExpression(1);
 coreOperationEqual = new CoreOperationEqual(arg1, arg2);
 }

 @Test
 public void testComputeValueWithEqualValues() {
 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testComputeValueWithNotEqualValues() {
 arg2 = new LiteralExpression(2);
 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((boolean) result);
 }

 @Test
 public void testComputeValueWithNullAndNotNullValues() {
 arg2 = new LiteralExpression(null);
 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((boolean) result);

 arg1 = new LiteralExpression(new Integer(1));
 arg2 = new LiteralExpression(new Integer(1));
 result = coreOperationEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test
 public void testComputeValueWithNullAndNullValues() {
 arg1 = new LiteralExpression(null);
 arg2 = new LiteralExpression(null);
 EvalContext context = new EvalContext();
 Object result = coreOperationEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testThrowsIllegalArgumentExceptionWithInvalidArguments() {
 coreOperationEqual = new CoreOperationEqual(null, arg2);
 EvalContext context = new EvalContext();
 coreOperationEqual.computeValue(context);
 }

 @Test
 public void testGetSymbol() {
 String symbol = coreOperationEqual.getSymbol();
 assertEquals("=", symbol);
 }
}