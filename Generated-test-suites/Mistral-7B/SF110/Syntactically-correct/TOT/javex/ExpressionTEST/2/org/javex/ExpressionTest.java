package org.javex.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.Expression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.ExpressionStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testSimpleAddition() {
 Expression e = createExpression("1+2");
 Object result = e.evaluate();
 assertEquals(3, result);
 }

 @Test
 public void testSimpleSubtraction() {
 Expression e = createExpression("2-1");
 Object result = e.evaluate();
 assertEquals(1, result);
 }

 @Test
 public void testSimpleMultiplication() {
 Expression e = createExpression("2*3");
 Object result = e.evaluate();
 assertEquals(6, result);
 }

 @Test
 public void testSimpleDivision() {
 Expression e = createExpression("4/2");
 Object result = e.evaluate();
 assertEquals(2.0, result);
 }

 @Test
 public void testComplexExpressionWithParentheses() {
 Expression e = createExpression("(1+2)*3");
 Object result = e.evaluate();
 assertEquals(9, result);
 }

 @Test
 public void testComplexExpressionWithoutParentheses() {
 Expression e = createExpression("1+2*3");
 Object result = e.evaluate();
 assertEquals(7, result);
 }

 @Test
 public void testLogicalAND() {
 Expression e = createExpression("true and true");
 Object result = e.evaluate();
 assertTrue(result);
 }

 @Test
 public void testLogicalOR() {
 Expression e = createExpression("true or false");
 Object result = e.evaluate();
 assertTrue(result);
 }

 @Test
 public void testLogicalNOT() {
 Expression e = createExpression("not true");
 Object result = e.evaluate();
 assertFalse(result);
 }

 @Test
 public void testStringFunctions() {
 Expression e = createExpression("length(upper(\"abcdefg\"))");
 Object result = e.evaluate();
 assertEquals(7, result);

 Expression e2 = createExpression("substring(\"hello\", 1, 3)");
 Object result2 = e2.evaluate();
 assertEquals("hell", result2.toString());
 }

 @Test
 public void testDateFunctions() {
 Expression e = createExpression("dateadd(new date(2022, 1, 1), 30)");
 Object result = e.evaluate();
 assertEquals("2022-02-01", result.toString());

 Expression e2 = createExpression("datediff(new date(2022, 1, 1), new date(2022, 12, 31))");
 Object result2 = e2.evaluate();
 assertEquals(364, result2);
 }

 @Test
 public void testMathFunctions() {
 Expression e = createExpression("abs(-5)");
 Object result = e.evaluate();
 assertEquals(5, result);

 Expression e2 = createExpression("sqrt(16)");
 Object result2 = e2.evaluate();
 assertEquals(4.0, result2);
 }

 @Test(expected = RuntimeException.class)
 public void testNonExistentFunction() {
 Expression e = new Expression("nonExistentFunction()");
 e.prepare();
 e.evaluate();
 }

 @Test(expected = RuntimeException.class)
 public void testDivideByZero() {
 Expression e = new Expression("5/0");
 e.prepare();
 e.evaluate();
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidExpression() {
 Expression e = new Expression("invalidExpression()");
 e.prepare();
 e.evaluate();
 }

 @Test(expected = RuntimeException.class)
 public void testMissingFunctionArgument() {
 Expression e = new Expression("length(abc)");
 e.prepare();
 e.evaluate();
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidFunctionArgumentType() {
 Expression e = new Expression("toUpper(5)");
 e.prepare();
 e.evaluate();
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidDateFormat() {
 Expression e = new Expression("dateadd(new date(\"2022-1-1\"), 30)");
 e.prepare();
 e.evaluate();
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidStringFormat() {
 Expression e = new Expression("substring(\"abcd\", 5, 2)");
 e.prepare();
 e.evaluate();
 }
}