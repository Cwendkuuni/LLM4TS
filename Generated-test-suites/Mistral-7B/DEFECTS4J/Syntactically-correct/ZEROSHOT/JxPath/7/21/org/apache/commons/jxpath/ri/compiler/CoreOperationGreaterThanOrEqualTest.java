package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testCoreOperationGreaterThanOrEqual() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual( new ConstantExpression(5.0), new ConstantExpression(5.0) );
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationGreaterThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCoreOperationGreaterThanOrEqualWithUnequalValues() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual( new ConstantExpression(5.0), new ConstantExpression(4.0) );
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationGreaterThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCoreOperationGreaterThanOrEqualWithEqualValues() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual( new ConstantExpression(5.0), new ConstantExpression(5.0) );
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationGreaterThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCoreOperationGreaterThanOrEqualWithNotEqualValues() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual( new ConstantExpression(5.0), new ConstantExpression(6.0) );
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationGreaterThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCoreOperationGreaterThanOrEqualWithNegativeValues() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual( new ConstantExpression(-3.0), new ConstantExpression(-2.0) );
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 Object result = coreOperationGreaterThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCoreOperationGreaterThanOrEqualWithInvalidArguments() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual( new ConstantExpression(5.0), null );
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 coreOperationGreaterThanOrEqual.computeValue(context);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test(expected = ClassCastException.class)
 public void testCoreOperationGreaterThanOrEqualWithInvalidTypes() {
 CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual( new ConstantExpression("5.0"), new ConstantExpression('A') );
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(infoSet);
 coreOperationGreaterThanOrEqual.computeValue(context);
 fail("Expected ClassCastException not thrown");
 }
}