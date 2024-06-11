package org.javex.expression;

import org.junit.Assert;
import org.junit.Test;
import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.EndsWithFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.StartsWithFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.utils.JavexUtils;
import org.javex.utils.ExpressionStack;
import org.javex.Expression;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testSimpleExpression() {
 String expression = "1 + 2 * 3";
 Expression e = createExpression(expression);
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(7.0, result);
 }

 @Test
 public void testComplexExpression() {
 String expression = "5 * (6 + (7 - 1))";
 Expression e = createExpression(expression);
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(36.0, result);
 }

 @Test
 public void testReservedFunctionExpression() {
 String expression = "5 > 3";
 Expression e = createExpression(expression);
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(true, result);
 }

 @Test
 public void testLogicalFunctionExpression() {
 String expression = "5 > 3 and 10 > 5";
 Expression e = createExpression(expression);
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(true, result);
 }

 @Test
 public void testLogicalOperatorExpression() {
 String expression = "5 > 3 or 10 > 5";
 Expression e = createExpression(expression);
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(true, result);
 }

 @Test
 public void testStringFunctionExpression() {
 String expression = "length(upper('abc'))";
 Expression e = createExpression(expression);
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(3, result);
 }

 @Test
 public void testDateFunctionExpression() {
 Date date1 = new Date(2022, 1, 1);
 Date date2 = new Date(2022, 1, 5);
 String expression = "dateadd(day, 3, date('2022-01-01'))";
 Expression e = createExpression(expression);
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(date2, result);
 }

 @Test
 public void testMathFunctionExpression() {
 String expression = "sqrt(16)";
 Expression e = createExpression(expression);
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(4.0, result);
 }

 @Test
 public void testExternalFunctionExpression() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AndFunction());
 functions.add(new OrFunction());
 Expression e = new Expression("", functions);
 e.setExpression("myFunction(true, 5) or 10 > 5");
 e.prepare();
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertTrue(result instanceof Boolean);
 }

 @Test
 public void testReservedFunctionsMap() {
 Map<String, FunctionExpression> functionsMap = new HashMap<>();
 Expression.putFunction(new AndFunction(), functionsMap);
 Expression.putFunction(new OrFunction(), functionsMap);
 Expression e = new Expression("", functionsMap);
 e.setExpression("and(true, false)");
 e.prepare();
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception e1) {
 e1.printStackTrace();
 }
 Assert.assertEquals(false, result);
 }

 @Test
 public void testTokenize() {
 String expression = "5 + 6 * (7 - 1) and 10 > 5";
 Expression e = new Expression(expression);
 e.tokenize();
 List<String> expectedTokens = new ArrayList<>();
 expectedTokens.add("5");
 expectedTokens.add("+");
 expectedTokens.add("6");
 expectedTokens.add("*");
 expectedTokens.add("(");
 expectedTokens.add("7");
 expectedTokens.add("-");
 expectedTokens.add("1");
 expectedTokens.add(")");
 expectedTokens.add("and");
 expectedTokens.add("10");
 expectedTokens.add(">");
 expectedTokens.add("5");
 List<String> actualTokens = new ArrayList<>();
 for (int i = 0; i < e.getStack().size(); ++i) {
 actualTokens.add(e.getStack().get(i).toString().trim());
 }
 Assert.assertEquals(expectedTokens, actualTokens);
 }

 @Test
 public void testIsFunction() {
 Expression e = new Expression("");
 Assert.assertFalse(e.isFunction("and"));
 e.prepare();
 e.setExpression("and(true, false)");
 Assert.assertTrue(e.isFunction("and"));
 }

 @Test
 public void testIsReservedFunction() {
 Expression e = new Expression("");
 Assert.assertFalse(e.isReservedFunction("and"));
 e.prepare();
 e.setExpression("and(true, false)");
 Assert.assertFalse(e.isReservedFunction("and"));
 Expression.putFunction(new AndFunction(), new HashMap<>());
 e.prepare();
 e.setExpression("and(true, false)");
 Assert.assertTrue(e.isReservedFunction("and"));
 }

 @Test
 public void testGetFunction() {
 Expression e = new Expression("");
 FunctionExpression function = new AndFunction();
 Expression.putFunction(function, new HashMap<>());
 e.prepare();
 e.setExpression("and(true, false)");
 FunctionExpression result = e.getFunction("and");
 Assert.assertEquals(function, result);
 }

 @Test
 public void testGetExternalFunction() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AndFunction());
 functions.add(new OrFunction());
 Expression e = new Expression("", functions);
 e.prepare();
 e.setExpression("myFunction(true, 5)");
 FunctionExpression result = e.getExternalFunction("myFunction");
 Assert.assertNull(result);
 }

 @Test
 public void testIsExternalFunction() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(new AndFunction());
 functions.add(new OrFunction());
 Expression e = new Expression("", functions);
 e.prepare();
 e.setExpression("myFunction(true, 5)");
 Assert.assertFalse(e.isExternalFunction("myFunction"));
 }
}