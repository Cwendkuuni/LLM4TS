package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.expressions.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.apache.commons.jxpath.ri.expressions.VariableExpression;

public class CoreOperationGreaterThanTest {

 private Expression arg1, arg2, operation;
 private InfoSet infoSet;
 private EvalContext context;

 @Before
 public void setUp() {
 infoSet = new org.apache.commons.jxpath.ri.mock.SimpleAxisIterate();
 context = new EvalContext(infoSet);

 arg1 = new VariableExpression("arg1");
 arg2 = new VariableExpression("arg2");
 operation = new CoreOperationGreaterThan(arg1, arg2);
 }

 @Test
 public void testComputeValueTrue() {
 infoSet.setDouble("arg1", 5.5);
 infoSet.setDouble("arg2", 3.3);

 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueFalse() {
 infoSet.setDouble("arg1", 3.3);
 infoSet.setDouble("arg2", 5.5);

 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueEquals() {
 infoSet.setDouble("arg1", 5.5);
 infoSet.setDouble("arg2", 5.5);

 Object result = operation.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = RuntimeException.class)
 public void testComputeValueNullArg1() {
 infoSet.setDouble("arg2", 5.5);

 try {
 operation.computeValue(context);
 fail("Expected exception not thrown");
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test(expected = RuntimeException.class)
 public void testComputeValueNullArg2() {
 infoSet.setDouble("arg1", 5.5);

 try {
 operation.computeValue(context);
 fail("Expected exception not thrown");
 } catch (RuntimeException e) {
 // Expected
 }
 }
}