package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.util.SimpleNode;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanTest {

 @Test
 public void testGreaterThanPositive() {
 InfoSet infoSet = new InfoSet();
 infoSet.setNode(new SimpleNode("root", 5.0));
 infoSet.setNode(new SimpleNode("child1", 3.0));
 infoSet.setNode(new SimpleNode("child2", 7.0));

 EvalContext context = new EvalContext(infoSet);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new SimpleExpression(new SimpleNode("root", 5.0)),
 new SimpleExpression(new SimpleNode("child1", 3.0))
 );

 Object result = expression.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanZero() {
 InfoSet infoSet = new InfoSet();
 infoSet.setNode(new SimpleNode("root", 0.0));
 infoSet.setNode(new SimpleNode("child", 5.0));

 EvalContext context = new EvalContext(infoSet);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new SimpleExpression(new SimpleNode("root", 0.0)),
 new SimpleExpression(new SimpleNode("child", 5.0))
 );

 Object result = expression.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanEqual() {
 InfoSet infoSet = new InfoSet();
 infoSet.setNode(new SimpleNode("root", 5.0));
 infoSet.setNode(new SimpleNode("child", 5.0));

 EvalContext context = new EvalContext(infoSet);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new SimpleExpression(new SimpleNode("root", 5.0)),
 new SimpleExpression(new SimpleNode("child", 5.0))
 );

 Object result = expression.computeValue(context);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanNegative() {
 InfoSet infoSet = new InfoSet();
 infoSet.setNode(new SimpleNode("root", -5.0));
 infoSet.setNode(new SimpleNode("child", 3.0));

 EvalContext context = new EvalContext(infoSet);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new SimpleExpression(new SimpleNode("root", -5.0)),
 new SimpleExpression(new SimpleNode("child", 3.0))
 );

 Object result = expression.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testGreaterThanNaN() {
 InfoSet infoSet = new InfoSet();
 infoSet.setNode(new SimpleNode("root", Double.NaN));
 infoSet.setNode(new SimpleNode("child", 3.0));

 EvalContext context = new EvalContext(infoSet);
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new SimpleExpression(new SimpleNode("root", Double.NaN)),
 new SimpleExpression(new SimpleNode("child", 3.0))
 );

 Object result = expression.computeValue(context);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidExpression() {
 CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
 new SimpleExpression(new SimpleNode("root")),
 null
 );
 }
}