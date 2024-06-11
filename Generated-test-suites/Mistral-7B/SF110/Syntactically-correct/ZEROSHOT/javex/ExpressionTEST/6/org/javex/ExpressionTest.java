package org.javex.expression;

import org.junit.Test;
import org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.SubstringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpressionTest {

 private Expression parseExpression(String expression) {
 Expression exp = new Expression(expression);
 exp.prepare();
 return exp;
 }

 @Test
 public void testSimpleEvaluate() throws Exception {
 Expression exp = parseExpression("5 + 3");
 Object result = exp.evaluate();
 Assert.assertEquals(8, result);
 }

 @Test
 public void testLogicalEvaluate() throws Exception {
 Expression exp = parseExpression("true and false");
 Object result = exp.evaluate();
 Assert.assertFalse(result);

 exp = parseExpression("true or false");
 result = exp.evaluate();
 Assert.assertTrue(result);
 }

 @Test
 public void testMathEvaluate() throws Exception {
 Expression exp = parseExpression("2 * 3 + 4");
 Object result = exp.evaluate();
 Assert.assertEquals(10, result);

 exp = parseExpression("sqrt(5) + 3 * 2");
 result = exp.evaluate();
 Assert.assertEquals(7.4833452369751775, result, 0.001);

 exp = parseExpression("pow(2, 3) + pow(3, 2)");
 result = exp.evaluate();
 Assert.assertEquals(32.0, result);
 }

 @Test
 public void testDateEvaluate() throws Exception {
 Expression exp = parseExpression("dateadd(day, 2, '2021-01-01')");
 Object result = exp.evaluate();
 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 Date date = (Date) result;
 Assert.assertEquals("2021-01-03", sdf.format(date));

 exp = parseExpression("datediff(day, '2021-01-01', '2021-01-03')");
 result = exp.evaluate();
 Assert.assertEquals(2, result);
 }

 @Test
 public void testStringEvaluate() throws Exception {
 Expression exp = parseExpression("length(substring('abcdef', 1, 3))");
 Object result = exp.evaluate();
 Assert.assertEquals(3, result);

 exp = parseExpression("toUpper('abc')");
 result = exp.evaluate();
 Assert.assertEquals("ABC", result);

 exp = parseExpression("contains('abc', 'a')");
 result = exp.evaluate();
 Assert.assertTrue(result);

 exp = parseExpression("startsWith('abc', 'ab')");
 result = exp.evaluate();
 Assert.assertFalse(result);
 }

 @Test
 public void testReservedFunctionEvaluate() throws Exception {
 Expression exp = parseExpression("and(true, false)");
 Object result = exp.evaluate();
 Assert.assertFalse(result);

 exp = parseExpression("or(true, false)");
 result = exp.evaluate();
 Assert.assertTrue(result);
 }

 @Test
 public void testExternalFunctionEvaluate() throws Exception {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AddFunction());
 functions.add(new MultiplyFunction());
 Expression exp = new Expression("") {
 @Override
 public List getExternalFunctions() {
 return functions;
 }
 };
 exp.stack = new ExpressionStack();
 exp.expression = "myFunction(2, 3)";
 exp.prepare();
 Object result = exp.evaluate();
 Assert.fail("External function evaluation should have thrown an exception");
 }
}