package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestSupport;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 private CoreExpression arg1, arg2;
 private CoreOperationLessThanOrEqual lessThanOrEqual;

 @Test
 public void testCoreOperationLessThanOrEqual() {
 arg1 = new CoreNumberExpression(new InfoSet(), new Double(5.0));
 arg2 = new CoreNumberExpression(new InfoSet(), new Double(10.0));
 lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 }

 @Test
 public void testComputeValueTrue() {
 EvaluationContext context = new EvaluationContext(new InfoSet());
 InfoSet infoSet = new InfoSet();
 infoSet.addNode(new DoubleNode(infoSet, 5.0));
 infoSet.addNode(new DoubleNode(infoSet, 10.0));
 context.setInfoSet(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueFalse() {
 EvaluationContext context = new EvaluationContext(new InfoSet());
 InfoSet infoSet = new InfoSet();
 infoSet.addNode(new DoubleNode(infoSet, 10.0));
 infoSet.addNode(new DoubleNode(infoSet, 5.0));
 context.setInfoSet(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeValueBothZero() {
 EvaluationContext context = new EvaluationContext(new InfoSet());
 InfoSet infoSet = new InfoSet();
 arg1 = new CoreNumberExpression(new InfoSet(), new Double(0.0));
 arg2 = new CoreNumberExpression(new InfoSet(), new Double(0.0));
 lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 context.setInfoSet(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testComputeValueOneIsNaN() {
 EvaluationContext context = new EvaluationContext(new InfoSet());
 InfoSet infoSet = new InfoSet();
 arg1 = new CoreNumberExpression(new InfoSet(), new Double(Double.NaN));
 arg2 = new CoreNumberExpression(new InfoSet(), new Double(5.0));
 lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 context.setInfoSet(infoSet);
 Object result = lessThanOrEqual.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueNullArg() {
 EvaluationContext context = new EvaluationContext(new InfoSet());
 InfoSet infoSet = new InfoSet();
 arg1 = new CoreNumberExpression(new InfoSet(), null);
 arg2 = new CoreNumberExpression(new InfoSet(), new Double(5.0));
 lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 context.setInfoSet(infoSet);
 lessThanOrEqual.computeValue(context);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeValueNullArg2() {
 EvaluationContext context = new EvaluationContext(new InfoSet());
 InfoSet infoSet = new InfoSet();
 arg1 = new CoreNumberExpression(new InfoSet(), new Double(5.0));
 arg2 = new CoreNumberExpression(new InfoSet(), null);
 lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
 context.setInfoSet(infoSet);
 lessThanOrEqual.computeValue(context);
 }

 @Test
 public void testSymbol() {
 String symbol = lessThanOrEqual.getSymbol();
 assertEquals("<=", symbol);
 }
}