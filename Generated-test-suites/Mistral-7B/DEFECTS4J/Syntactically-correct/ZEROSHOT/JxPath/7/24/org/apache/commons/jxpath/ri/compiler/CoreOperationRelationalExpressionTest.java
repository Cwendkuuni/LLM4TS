package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.*;
import org.apache.commons.jxpath.ri.compiler.test.TestHelper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

 private CoreOperationRelationalExpression expr;

 @Test
 public void testCoreOperationRelationalExpressionInstance() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(1), new NumberLiteral(2) });
 assertNotNull(expr);
 assertEquals(CoreOperationType.RELATIONAL, expr.getType());
 assertEquals(3, expr.getPrecedence());
 assertFalse(expr.isSymmetric());
 }

 @Test
 public void testGetPrecedence() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(1), new NumberLiteral(2) });
 assertTrue(expr.getPrecedence() == 3);
 }

 @Test
 public void testIsSymmetric() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(1), new NumberLiteral(2) });
 assertFalse(expr.isSymmetric());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClone() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(1), new NumberLiteral(2) });
 expr.clone();
 }

 @Test
 public void testEvaluate() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(1), new NumberLiteral(2) });
 Expression left = expr.getOperands()[0];
 Expression right = expr.getOperands()[1];
 Object leftValue = TestHelper.eval(left);
 Object rightValue = TestHelper.eval(right);
 Object result = TestHelper.eval(expr);
 assertEquals(TestHelper.compare(leftValue, rightValue, TestHelper.GT), result);
 }

 @Test
 public void testEvaluateWithNullLeftOperand() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(2), new NumberLiteral(1) });
 Expression left = expr.getOperands()[0];
 Expression right = expr.getOperands()[1];
 Object leftValue = TestHelper.eval(left);
 Object rightValue = TestHelper.eval(right);
 Object result = TestHelper.eval(expr);
 assertNull(result);
 }

 @Test
 public void testEvaluateWithNullRightOperand() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(1), new NumberLiteral(2) });
 Expression left = expr.getOperands()[0];
 Expression right = expr.getOperands()[1];
 Object leftValue = TestHelper.eval(left);
 Object rightValue = TestHelper.eval(right);
 Object result = TestHelper.eval(expr);
 assertNull(result);
 }

 @Test
 public void testEvaluateWithBothNullOperands() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NullLiteral(), new NullLiteral() });
 Expression left = expr.getOperands()[0];
 Expression right = expr.getOperands()[1];
 Object leftValue = TestHelper.eval(left);
 Object rightValue = TestHelper.eval(right);
 Object result = TestHelper.eval(expr);
 assertNull(result);
 }

 @Test
 public void testEvaluateWithDifferentTypes() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new StringLiteral("a"), new NumberLiteral(1) });
 Expression left = expr.getOperands()[0];
 Expression right = expr.getOperands()[1];
 Object leftValue = TestHelper.eval(left);
 Object rightValue = TestHelper.eval(right);
 Object result = TestHelper.eval(expr);
 assertNull(result);
 }

 @Test
 public void testEvaluateWithSameValueTypesButDifferentValues() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(1), new NumberLiteral(2) });
 Expression left = expr.getOperands()[0];
 Expression right = expr.getOperands()[1];
 Object leftValue = TestHelper.eval(left);
 Object rightValue = TestHelper.eval(right);
 Object result = TestHelper.eval(expr);
 assertFalse(TestHelper.compare(leftValue, rightValue, TestHelper.EQ));
 assertTrue(TestHelper.compare(leftValue, rightValue, TestHelper.GT));
 }

 @Test
 public void testEvaluateWithSameValueTypesAndValues() {
 expr = new CoreOperationRelationalExpression(new Expression[] { new NumberLiteral(1), new NumberLiteral(1) });
 Expression left = expr.getOperands()[0];
 Expression right = expr.getOperands()[1];
 Object leftValue = TestHelper.eval(left);
 Object rightValue = TestHelper.eval(right);
 Object result = TestHelper.eval(expr);
 assertTrue(TestHelper.compare(leftValue, rightValue, TestHelper.EQ));
 assertFalse(TestHelper.compare(leftValue, rightValue, TestHelper.GT));
 assertFalse(TestHelper.compare(leftValue, rightValue, TestHelper.LT));
 }

}