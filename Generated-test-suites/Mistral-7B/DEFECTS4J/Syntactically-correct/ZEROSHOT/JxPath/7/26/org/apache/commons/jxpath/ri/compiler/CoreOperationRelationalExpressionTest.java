package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expression;
 private CoreOperationRelationalExpressionarg arg1;
 private CoreOperationRelationalExpressionarg arg2;
 private ExpressionFactory factory;

 @Before
 public void setUp() {
 factory = new ExpressionFactory();
 arg1 = new CoreOperationRelationalExpressionarg(factory.createNumber(1));
 arg2 = new CoreOperationRelationalExpressionarg(factory.createNumber(2));
 expression = new CoreOperationGreaterThan(new Expression[]{arg1, arg2});
 }

 @Test
 public void testGetterPrecedence() {
 assertEquals(3, expression.getPrecedence());
 }

 @Test
 public void testIsSymmetricReturnsFalse() {
 assertFalse(expression.isSymmetric());
 }

 @Test
 public void testOperationWithSameValues() {
 JXPathContext context = new JXPathContext(new Object[]{arg1.getValue()});
 assertFalse(expression.evaluate(context).booleanValue());

 context = new JXPathContext(new Object[]{arg2.getValue()});
 assertFalse(expression.evaluate(context).booleanValue());

 context = new JXPathContext(new Object[]{arg1.getValue(), arg2.getValue()});
 assertFalse(expression.evaluate(context).booleanValue());
 }

 @Test
 public void testOperationWithDifferentValues() {
 JXPathContext context = new JXPathContext(new Object[]{arg1.getValue(), arg2.getValue()});
 assertTrue(expression.evaluate(context).booleanValue());
 }

 @Test
 public void testOperationWithFirstValueGreater() {
 JXPathContext context = new JXPathContext(new Object[]{arg1.getValue(), arg2.getValue()});
 assertTrue(expression.evaluate(context).booleanValue());

 arg1 = new CoreOperationRelationalExpressionarg(factory.createNumber(3));
 context = new JXPathContext(new Object[]{arg1.getValue(), arg2.getValue()});
 assertTrue(expression.evaluate(context).booleanValue());

 arg1 = new CoreOperationRelationalExpressionarg(factory.createNumber(5));
 context = new JXPathContext(new Object[]{arg1.getValue(), arg2.getValue()});
 assertTrue(expression.evaluate(context).booleanValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOperationWithInvalidNumberOfArguments() {
 new CoreOperationGreaterThan(new Expression[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOperationWithNullArguments() {
 new CoreOperationGreaterThan(new Expression[]{null});
 }

}