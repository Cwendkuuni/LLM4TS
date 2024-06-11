package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;
 private Expression arg1;
 private Expression arg2;
 private JXPathContext jxpathContext;

 @Before
 public void setUp() {
 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("10");
 expression = new CoreOperationRelationalExpression(new CoreOperation[] {arg1, arg2});
 jxpathContext = new JXPathContext();
 }

 @Test
 public void testGreaterThan() {
 expression = new GreaterThanExpression(new CoreOperation[] {arg1, arg2});
 assertFalse(expression.evaluate(jxpathContext));

 arg1 = new LiteralExpression("10");
 arg2 = new LiteralExpression("5");
 expression = new GreaterThanExpression(new CoreOperation[] {arg1, arg2});
 assertTrue(expression.evaluate(jxpathContext));
 }

 @Test
 public void testGreaterThanOrEqual() {
 expression = new GreaterThanOrEqualExpression(new CoreOperation[] {arg1, arg2});
 assertFalse(expression.evaluate(jxpathContext));

 arg1 = new LiteralExpression("10");
 arg2 = new LiteralExpression("5");
 expression = new GreaterThanOrEqualExpression(new CoreOperation[] {arg1, arg2});
 assertTrue(expression.evaluate(jxpathContext));

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("5");
 expression = new GreaterThanOrEqualExpression(new CoreOperation[] {arg1, arg2});
 assertTrue(expression.evaluate(jxpathContext));
 }

 @Test
 public void testLessThan() {
 expression = new LessThanExpression(new CoreOperation[] {arg1, arg2});
 assertTrue(expression.evaluate(jxpathContext));

 arg1 = new LiteralExpression("10");
 arg2 = new LiteralExpression("5");
 expression = new LessThanExpression(new CoreOperation[] {arg1, arg2});
 assertFalse(expression.evaluate(jxpathContext));
 }

 @Test
 public void testLessThanOrEqual() {
 expression = new LessThanOrEqualExpression(new CoreOperation[] {arg1, arg2});
 assertTrue(expression.evaluate(jxpathContext));

 arg1 = new LiteralExpression("10");
 arg2 = new LiteralExpression("5");
 expression = new LessThanOrEqualExpression(new CoreOperation[] {arg1, arg2});
assertTrue(expression.evaluate(jxpathContext));

 arg1 = new LiteralExpression("5");
 arg2 = new LiteralExpression("5");
 expression = new LessThanOrEqualExpression(new CoreOperation[] {arg1, arg2});
 assertTrue(expression.evaluate(jxpathContext));
 }
}