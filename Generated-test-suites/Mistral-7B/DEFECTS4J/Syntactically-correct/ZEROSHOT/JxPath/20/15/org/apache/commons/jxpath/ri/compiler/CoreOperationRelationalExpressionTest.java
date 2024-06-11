package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.Axes;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.iter.ArrayIterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;
import org.apache.commons.jxpath.ri.iter.SingleNodeIterator;
import org.apache.commons.jxpath.ri.iter.StaticCollectionIterator;
import org.apache.commons.jxpath.ri.iter.ValueIterable;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;

 @Before
 public void setUp() {
 expression = new GreaterThanExpression(new VariableExpression("a"), new VariableExpression("b"));
 }

 @Test
 public void testComputeValue() {
 EvalContext context = new EvalContext();
 context.setInput(new Object[]{"1", "2"});
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 context = new EvalContext();
 context.setInput(new Object[]{"3", "2"});
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGetPrecedence() {
 assertEquals(RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
 }

 @Test
 public void testIsSymmetric() {
 assertFalse(expression.isSymmetric());
 }

 @Test
 public void testEvaluateCompare() {
 assertTrue(expression.evaluateCompare(1));
 assertFalse(expression.evaluateCompare(0));
 assertFalse(expression.evaluateCompare(-1));
 }

 @Test
 public void testComputeWithInitialContext() {
 SingleNodeIterator node1 = new SingleNodeIterator(new Object[]{"1"});
 SingleNodeIterator node2 = new SingleNodeIter(Axes.SELF, new Object[]{}, new Object[0]);
 EvalContext context = new EvalContext();
 context.setInput(new Object[]{node1, node2});
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testComputeWithIterators() {
 ValueIterable iter1 = new ArrayIterator(new Object[]{1, 2, 3});
 ValueIterable iter2 = new ArrayIterator(new Object[]{2, 3, 4});
 EvalContext context = new EvalContext();
 context.setInput(new Object[]{iter1, iter2});
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 iter1 = new ArrayIterator(new Object[]{1, 2, 3});
 iter2 = new SingletonIterator(new Object[]{3});
 context = new EvalContext();
 context.setInput(new Object[]{iter1, iter2});
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 iter1 = new ArrayIterator(new Object[]{1, 2, 3});
 iter2 = new SingletonIterator(new Object[]{2.5});
 context = new EvalContext();
 context.setInput(new Object[]{iter1, iter2});
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testComputeWithCollections() {
 StaticCollectionIterator iter1 = new StaticCollectionIterator(new HashSet<Object>() {{
 add(1);
 add(2);
 add(3);
 }});
 StaticCollectionIterator iter2 = new StaticCollectionIterator(new HashSet<Object>() {{
 add(2);
 add(3);
 add(4);
 }});
 EvalContext context = new EvalContext();
 context.setInput(new Object[]{iter1, iter2});
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 iter1 = new StaticCollectionIterator(new HashSet<Object>() {{
 add(1.0);
 add(2.0);
 add(3.0);
 }});
 iter2 = new StaticCollectionIterator(new HashSet<Object>() {{
 add(2.0);
 add(3.0);
 add(4.0);
 }});
 context = new EvalContext();
 context.setInput(new Object[]{iter1, iter2});
 result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);

 iter1 = new StaticCollectionIterator(new HashSet<Object>() {{
 add(1.0);
 add(2.0);
 add(3.0);
 }});
 iter2 = new SingletonIterator(new Object[]{2.5});
 context = new EvalContext();
 context.setInput(new Object[]{iter1, iter2});
 result = expression.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

}