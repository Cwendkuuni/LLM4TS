package org.javex.expression;

import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PowerFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubstractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.SubStringFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.utils.JavexUtils;
import org.javex.functions.FunctionExpression;
import org.javex.functions.LogicalFunction;
import org.javex.functions.MathFunction;
import org.javex.functions.DateFunction;
import org.javex.functions.StringFunction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression expr = new Expression(expression);
 expr.prepare();
 return expr;
 }

 @Test
 public void testIsReservedFunction() {
 Expression expr = new Expression("");
 Assert.assertFalse(expr.isReservedFunction("and"));
 expr = createExpression("x and y");
 Assert.assertTrue(expr.isReservedFunction("and"));
 }

 @Test
 public void testIsLogicalFunction() {
 Expression expr = new Expression("");
 Assert.assertFalse(expr.isLogicalFunction("and"));
 expr = createExpression("x and y");
 Assert.assertFalse(expr.isLogicalFunction("and"));
 expr = createExpression("and(x, y)");
 Assert.assertTrue(expr.isLogicalFunction("and"));
 expr = createExpression("or(x, y)");
 Assert.assertTrue(expr.isLogicalFunction("or"));
 }

 @Test
 public void testIsMathFunction() {
 Expression expr = new Expression("");
 Assert.assertFalse(expr.isMathFunction("abs"));
 expr = createExpression("abs(x)");
 Assert.assertTrue(expr.isMathFunction("abs"));
 expr = createExpression("sqrt(x)");
 Assert.assertTrue(expr.isMathFunction("sqrt"));
 }

 @Test
 public void testIsDateFunction() {
 Expression expr = new Expression("");
 Assert.assertFalse(expr.isDateFunction("dateadd"));
 expr = createExpression("dateadd(day, 1, date)");
 Assert.assertTrue(expr.isDateFunction("dateadd"));
 }

 @Test
 public void testIsStringFunction() {
 Expression expr = new Expression("");
 Assert.assertFalse(expr.isStringFunction("length"));
 expr = createExpression("length(x)");
 Assert.assertTrue(expr.isStringFunction("length"));
 }

 @Test
 public void testGetReservedFunctions() {
 Expression expr = new Expression("");
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(andFunction);
 functions.add(orFunction);
 expr.setExternalFunctions(functions);
 Map<String, FunctionExpression> reservedFunctions = Expression.getReservedFunctions((String) null);
 Assert.assertNotNull(reservedFunctions);
 FunctionExpression reservedAndFunction = reservedFunctions.get(andFunction.getSimbol());
 Assert.assertNotNull(reservedAndFunction);
 Assert.assertEquals(andFunction, reservedAndFunction);
 FunctionExpression reservedOrFunction = reservedFunctions.get(orFunction.getSimbol());
 Assert.assertNotNull(reservedOrFunction);
 Assert.assertEquals(orFunction, reservedOrFunction);
 }

 @Test
 public void testEvaluateLogicalFunction() {
 Expression expr = new Expression("");
 expr.setExternalFunctions(new ArrayList<>());
 expr.setPrepared(true);
 LogicalFunction andFunction = new AndFunction();
 LogicalFunction orFunction = new OrFunction();
 expr.stack.push("true");
 expr.stack.push("true");
 expr.stack.push("and");
 List<Object> expectedValues = Arrays.asList(true, true, true);
 List<Object> actualValues = new ArrayList<>();
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("true");
 expr.stack.push("false");
 expr.stack.push("and");
 expectedValues = Arrays.asList(true, false, false);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("false");
 expr.stack.push("false");
 expr.stack.push("and");
 expectedValues = Arrays.asList(false, false, false);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("true");
 expr.stack.push("true");
 expr.stack.push("or");
 expectedValues = Arrays.asList(true, true, true);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("true");
 expr.stack.push("false");
 expr.stack.push("or");
 expectedValues = Arrays.asList(true, false, true);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("false");
 expr.stack.push("false");
 expr.stack.push("or");
 expectedValues = Arrays.asList(false, false, false);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testEvaluateMathFunction() {
 Expression expr = new Expression("");
 expr.setExternalFunctions(new ArrayList<>());
 expr.setPrepared(true);
 MathFunction absFunction = new AbsFunction();
 MathFunction addFunction = new AddFunction();
 MathFunction subtractFunction = new SubtractFunction();
 MathFunction multiplyFunction = new MultiplyFunction();
 MathFunction divideFunction = new DivideFunction();
 MathFunction powerFunction = new PowerFunction();
 MathFunction sqrtFunction = new SqrtFunction();
 expr.stack.push("5");
 expr.stack.push("2");
 expr.stack.push("+");
 List<Object> expectedValues = Arrays.asList(7.0);
 List<Object> actualValues = new ArrayList<>();
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("5");
 expr.stack.push("2");
 expr.stack.push("-");
 expectedValues = Arrays.asList(3.0);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("5");
 expr.stack.push("2");
 expr.stack.push("*");
 expectedValues = Arrays.asList(10.0);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("5");
 expr.stack.push("2");
 expr.stack.push("/");
 expectedValues = Arrays.asList(2.5);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("5");
 expr.stack.push("2");
 expr.stack.push("^");
 expectedValues = Arrays.asList(25.0);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("5");
 expr.stack.push("2");
 expr.stack.push("sqrt");
 expectedValues = Arrays.asList(2.23606797749979);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("5");
 expr.stack.push("2");
 expr.stack.push("abs");
 expectedValues = Arrays.asList(5.0);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testEvaluateDateFunction() {
 Expression expr = new Expression("");
 expr.setExternalFunctions(new ArrayList<>());
 expr.setPrepared(true);
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 Date addDate = null;
 try {
 addDate = dateFormat.parse("2022-01-01");
 } catch (ParseException e) {
 Assert.fail(e.getMessage());
 }
 Date subDate = null;
 try {
 subDate = dateFormat.parse("2021-12-31");
 } catch (ParseException e) {
 Assert.fail(e.getMessage());
 }
 expr.stack.push("2022-01-01");
 expr.stack.push("2021-12-31");
 expr.stack.push("dateadd");
 expr.stack.push("day");
 expr.stack.push("1");
 List<Object> expectedValues = Arrays.asList(addDate);
 List<Object> actualValues = new ArrayList<>();
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("2022-01-01");
 expr.stack.push(addDate);
 expr.stack.push("dateadd");
 expr.stack.push("month");
 expr.stack.push("1");
 expectedValues = Arrays.asList(new Date(System.currentTimeMillis() + 31L * 24L * 60L * 60L * 1000));
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("2022-01-01");
 expr.stack.push(addDate);
 expr.stack.push("dateadd");
 expr.stack.push("year");
 expr.stack.push("1");
 expectedValues = Arrays.asList(new Date(System.currentTimeMillis() + 365L * 24L * 60L * 60L * 1000));
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("2022-01-01");
 expr.stack.push(subDate);
 expr.stack.push("datediff");
 expr.stack.push("day");
 expectedValues = Arrays.asList(-1L);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("2022-01-01");
 expr.stack.push(addDate);
 expr.stack.push("datediff");
 expr.stack.push("month");
 expectedValues = Arrays.asList(1L);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("2022-01-01");
 expr.stack.push(addDate);
 expr.stack.push("datediff");
 expr.stack.push("year");
 expectedValues = Arrays.asList(1L);
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testEvaluateStringFunction() {
 Expression expr = new Expression("");
 expr.setExternalFunctions(new ArrayList<>());
 expr.setPrepared(true);
 String string1 = "Hello, World!";
 String string2 = "Java is a great programming language.";
 expr.stack.push(string1);
 expr.stack.push("length");
 List<Object> expectedValues = Arrays.asList(13);
 List<Object> actualValues = new ArrayList<>();
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push(string1);
 expr.stack.push("toUpper");
 expectedValues = Arrays.asList("HELLO, WORLD!");
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push(string1);
 expr.stack.push("tolower");
 expectedValues = Arrays.asList("hello, world!");
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("Hello, World!");
 expr.stack.push("substr");
 expr.stack.push("1");
 expr.stack.push("5");
 expectedValues = Arrays.asList("World!");
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 expr.stack.clear();
 expr.stack.push("Hello, World!");
 expr.stack.push("substr");
 expr.stack.push("0");
 expr.stack.push("5");
 expectedValues = Arrays.asList("Hello,");
 try {
 actualValues = expr.evaluate();
 } catch (Exception e) {
 Assert.fail(e.getMessage());
 }
 Assert.assertEquals(expectedValues, actualValues);
 }

}