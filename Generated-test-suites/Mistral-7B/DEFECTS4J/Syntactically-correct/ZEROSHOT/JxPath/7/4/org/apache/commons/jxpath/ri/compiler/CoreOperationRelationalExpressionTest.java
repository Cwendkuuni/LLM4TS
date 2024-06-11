package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.NumberValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

 private ExpressionFactory factory;
 private JXPathContext context;

 @Before
 public void setup() {
 factory = new ExpressionFactory();
 context = new JXPathContext();
 }

 @Test
 public void testGreaterThan() {
 Expression exp1 = factory.createNumber(new NumberValue(5));
 Expression exp2 = factory.createNumber(new NumberValue(3));
 CoreOperationRelationalExpression gt = (CoreOperationRelationalExpression) factory.createGreaterThan(exp1, exp2);
 Assert.assertTrue(gt.evaluate(context));
 }

 @Test
 public void testGreaterThanFalse() {
 Expression exp1 = factory.createNumber(new NumberValue(3));
 Expression exp2 = factory.createNumber(new NumberValue(5));
 CoreOperationRelationalExpression gt = (CoreOperationRelationalExpression) factory.createGreaterThan(exp1, exp2);
 Assert.assertFalse(gt.evaluate(context));
 }

 @Test
 public void testGreaterThanEquals() {
 Expression exp1 = factory.createNumber(new NumberValue(5));
 Expression exp2 = factory.createNumber(new NumberValue(4));
 CoreOperationRelationalExpression gte = (CoreOperationRelationalExpression) factory.createGreaterThanOrEqual(exp1, exp2);
 Assert.assertTrue(gte.evaluate(context));
 }

 @Test
 public void testGreaterThanEqualsFalse() {
 Expression exp1 = factory.createNumber(new NumberValue(3));
 Expression exp2 = factory.createNumber(new NumberValue(5));
 CoreOperationRelationalExpression gte = (CoreOperationRelationalExpression) factory.createGreaterThanOrEqual(exp1, exp2);
 Assert.assertFalse(gte.evaluate(context));
 }

 @Test
 public void testLessThan() {
 Expression exp1 = factory.createNumber(new NumberValue(3));
 Expression exp2 = factory.createNumber(new NumberValue(5));
 CoreOperationRelationalExpression lt = (CoreOperationRelationalExpression) factory.createLessThan(exp1, exp2);
 Assert.assertFalse(lt.evaluate(context));
 }

 @Test
 public void testLessThanFalse() {
 Expression exp1 = factory.createNumber(new NumberValue(5));
 Expression exp2 = factory.createNumber(new NumberValue(3));
 CoreOperationRelationalExpression lt = (CoreOperationRelationalExpression) factory.createLessThan(exp1, exp2);
 Assert.assertFalse(lt.evaluate(context));
 }

 @Test
 public void testLessThanEquals() {
 Expression exp1 = factory.createNumber(new NumberValue(3));
 Expression exp2 = factory.createNumber(new NumberValue(4));
 CoreOperationRelationalExpression lte = (CoreOperationRelationalExpression) factory.createLessThanOrEqual(exp1, exp2);
 Assert.assertTrue(lte.evaluate(context));
 }

 @Test
 public void testLessThanEqualsFalse() {
 Expression exp1 = factory.createNumber(new NumberValue(5));
 Expression exp2 = factory.createNumber(new NumberValue(3));
 CoreOperationRelationalExpression lte = (CoreOperationRelationalExpression) factory.createLessThanOrEqual(exp1, exp2);
 Assert.assertFalse(lte.evaluate(context));
 }

}