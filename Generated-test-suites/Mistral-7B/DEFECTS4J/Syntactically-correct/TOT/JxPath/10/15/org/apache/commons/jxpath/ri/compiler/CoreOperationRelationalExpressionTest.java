package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.IteratorIter;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThan() {
 CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(5), new LiteralExpression(4));
 InfoSet infoSet = new InfoSet();
 infoSet.setUnderNode(new SingletonIter(new Object[]{5}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new LiteralExpression(5), new LiteralExpression(4));
 InfoSet infoSet = new InfoSet();
 infoSet.setUnderNode(new SingletonIter(new Object[]{5, 5, 6}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThan() {
 CoreOperationRelationalExpression expression = new LessThanExpression(new LiteralExpression(4), new LiteralExpression(5));
 InfoSet infoSet = new InfoSet();
 infoSet.setUnderNode(new SingletonIter(new Object[]{5}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new LiteralExpression(4), new LiteralExpression(5));
 InfoSet infoSet = new InfoSet();
 infoSet.setUnderNode(new SingletonIter(new Object[]{4, 4, 5}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testDoubleGreaterThan() {
 CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(5.5), new LiteralExpression(4.5));
 InfoSet infoSet = new InfoSet();
 infoSet.setUnderNode(new SingletonIter(new Object[]{5.6}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testDoubleGreaterThanOrEqual() {
 CoreOperationRelationalExpression expression = new GreaterThanOrEqualExpression(new LiteralExpression(5.5), new LiteralExpression(4.5));
 InfoSet infoSet = new InfoSet();
 infoSet.setUnderNode(new SingletonIter(new Object[]{5.5, 5.5, 5.6}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testDoubleLessThan() {
 CoreOperationRelationalExpression expression = new LessThanExpression(new LiteralExpression(4.5), new LiteralExpression(5.5));
 InfoSet infoSet = new InfoSet();
 infoSet.setUnderNode(new SingletonIter(new Object[]{5.6}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testDoubleLessThanOrEqual() {
 CoreOperationRelationalExpression expression = new LessThanOrEqualExpression(new LiteralExpression(4.5), new LiteralExpression(5.5));
 InfoSet infoSet = new InfoSet();
 infoSet.setUnderNode(new SingletonIter(new Object[]{4.5, 4.5, 5.5}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testDoubleNaN() {
 CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(Double.NaN), new LiteralExpression(Double.NaN));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testIterators() {
 CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
 InfoSet infoSet = new InfoSet();
 IteratorIter left = new IteratorIter(new ArrayIter(new Object[]{1, 2, 3, 4, 5}));
 IteratorIter right = new IteratorIter(new ArrayIter(new Object[]{1, 2, 3, 4}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 context.setVariable("x", left);
 context.setVariable("y", right);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testIteratorsMatch() {
 CoreOperationRelationalExpression expression = new GreaterThanExpression(new VariableExpression("x"), new VariableExpression("y"));
 InfoSet infoSet = new InfoSet();
 IteratorIter left = new IteratorIter(new ArrayIter(new Object[]{1, 2, 3, 4, 5}));
 IteratorIter right = new IteratorIter(new ArrayIter(new Object[]{1, 2, 3, 4, 5}));
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 context.setVariable("x", left);
 context.setVariable("y", right);
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments() {
 CoreOperationRelationalExpression expression = new GreaterThanExpression(new LiteralExpression(null), new LiteralExpression(5));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext(new SelfContext(infoSet), new InitialContext(infoSet));
 try {
 expression.computeValue(context);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid arguments for relational expression: null", e.getMessage());
 throw e;
 }
 }

}