package org.javex.tests;

import org.junit.Test;
import org.junit.Assert.*;
import org.javex.Expression;
import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.utils.JavexUtils;

import java.util.*;

public class ExpressionTest {

 private static final Expression INSTANCE = new AddFunction();

 @Test
 public void testExpressionPreparation() {
 Expression expr = new AddFunction("1 + 2");
 expr.prepare();
 Stack<String> expectedStack = new ExpressionStack();
 expectedStack.push("1");
 expectedStack.push("2");
 expectedStack.push("+");
 Stack<String> actualStack = expr.getStackCopy();
 assertEquals(expectedStack, actualStack);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExpressionPreparationWithInvalidInput() {
 Expression expr = new AddFunction("1 a 2");
 expr.prepare();
 fail("Should throw IllegalArgumentException with message: 'Invalid expression: '1 a 2'");
 }

 @Test
 public void testExpressionPreparationWithParentheses() {
 Expression expr = new AddFunction("(1 + 2) * 3");
 expr.prepare();
 Stack<String> expectedStack = new ExpressionStack();
 expectedStack.push("1");
 expectedStack.push("2");
 expectedStack.push("+");
 expectedStack.push("3");
 expectedStack.push("*");
 Stack<String> actualStack = expr.getStackCopy();
 assertEquals(expectedStack, actualStack);
 }

 @Test
 public void testIsReservedFunction() {
 FunctionExpression andFunction = new AndFunction();
 assertTrue(INSTANCE.isReservedFunction(andFunction.getSimbol()));
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression andFunction = new AndFunction();
 assertTrue(INSTANCE.isLogicalFunction(andFunction.getSimbol()));
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression addFunction = new AddFunction();
 assertFalse(INSTANCE.isMathFunction(addFunction.getSimbol()));
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression dateAddFunction = new DateAddFunction();
 assertFalse(INSTANCE.isDateFunction(dateAddFunction.getSimbol()));
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression lengthFunction = new LengthFunction();
 assertFalse(INSTANCE.isStringFunction(lengthFunction.getSimbol()));
 }

 @Test
 public void testGetFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression actualFunction = INSTANCE.getFunction(andFunction.getSimbol());
 assertEquals(andFunction, actualFunction);
 }

 @Test
 public void testGetExternalFunction() {
 FunctionExpression addFunction = new AddFunction();
 FunctionExpression externalAddFunction = new AddFunction("x + y");
 assertNull(INSTANCE.getExternalFunction("x + y"));
 INSTANCE.setExternalFunctions(Arrays.asList(externalAddFunction));
 FunctionExpression actualFunction = INSTANCE.getExternalFunction("x + y");
 assertEquals(externalAddFunction, actualFunction);
 }

 @Test
 public void testIsFunction() {
 FunctionExpression andFunction = new AndFunction();
 assertTrue(INSTANCE.isFunction(andFunction.getSimbol()));
 }

 @Test
 public void testIsLogicalFunctionWithInvalidInput() {
 assertFalse(INSTANCE.isLogicalFunction("invalidSymbol"));
 }

 @Test
 public void testIsMathFunctionWithInvalidInput() {
 assertFalse(INSTANCE.isMathFunction("invalidSymbol"));
 }

 @Test
 public void testIsDateFunctionWithInvalidInput() {
 assertFalse(INSTANCE.isDateFunction("invalidSymbol"));
 }

 @Test
 public void testIsStringFunctionWithInvalidInput() {
 assertFalse(INSTANCE.isStringFunction("invalidSymbol"));
 }

 @Test
 public void testEvaluateWithValidInput() {
 Expression expr = new AddFunction("1 + 2");
 try {
 Object result = expr.evaluate();
 assertEquals(3.0, result);
 } catch (Exception e) {
 fail("Should not throw any exception: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateWithInvalidInput1() {
 Expression expr = new AddFunction("1 a 2");
 try {
 expr.evaluate();
 fail("Should throw IllegalArgumentException with message: 'Invalid expression: '1 a 2'");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateWithInvalidInput2() {
 Expression expr = new AddFunction("1 +");
 try {
 expr.evaluate();
 fail("Should throw IllegalArgumentException with message: 'Invalid expression: '1 +'");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateWithInvalidInput3() {
 Expression expr = new AddFunction("1 + a");
 try {
 expr.evaluate();
 fail("Should throw IllegalArgumentException with message: 'Invalid expression: '1 + a'");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateWithInvalidInput4() {
 Expression expr = new AddFunction("1 + (2 + x)");
 try {
 expr.evaluate();
 fail("Should throw IllegalArgumentException with message: 'Undefined function: x'");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateWithInvalidInput5() {
 Expression expr = new AddFunction("1 + (2 + x)");
 Map<String, FunctionExpression> functions = new HashMap<>();
 functions.put("x", new FunctionExpression("x", FunctionExpression.LOGICAL, "x -> x") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 throw new UnsupportedOperationException();
 }
 });
 INSTANCE.setReservedFunctions(functions);
 try {
 expr.evaluate();
 fail("Should throw IllegalArgumentException with message: 'Undefined function: x'");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testEvaluateWithValidInputWithVariables() {
 Expression expr = new AddFunction("x + y");
 Map<String, Object> variables = new HashMap<>();
 variables.put("x", 1.0);
 variables.put("y", 2.0);
 Map<String, FunctionExpression> functions = new HashMap<>();
 functions.put("x", new FunctionExpression("x", FunctionExpression.MATH, "x -> x") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 return variables.get("x");
 }
 });
 functions.put("y", new FunctionExpression("y", FunctionExpression.MATH, "y -> y") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 return variables.get("y");
 }
 });
 INSTANCE.setReservedFunctions(functions);
 INSTANCE.setExternalFunctions(Arrays.asList(new FunctionExpression("x + y", FunctionExpression.MATH, "x + y -> x + y") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 return variables.get("x") + variables.get("y");
 }
 }));
 try {
 Object result = expr.evaluate(variables);
 assertEquals(3.0, result);
 } catch (Exception e) {
 fail("Should not throw any exception: " + e.getMessage());
 }
 }

 @Test
 public void testEvaluateWithValidInputWithDateFunctions() {
 Expression expr = new AddFunction("DateAdd(DateDiff(CurrentDate(), '2022-01-01'), 1) + 1");
 Map<String, FunctionExpression> functions = new HashMap<>();
 functions.put("CurrentDate", new DateFunction("CurrentDate", FunctionExpression.DATE, "CurrentDate -> Current date") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 return JavexUtils.getCurrentDate();
 }
 });
 functions.put("DateAdd", new DateAddFunction("DateAdd", FunctionExpression.DATE, "DateAdd -> DateAdd(date1, interval)") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 Date date1 = (Date) variables.get("date1");
 int interval = ((Number) variables.get("interval")).intValue();
 return JavexUtils.addDays(date1, interval);
 }
 });
 functions.put("DateDiff", new DateDiffFunction("DateDiff", FunctionExpression.DATE, "DateDiff -> DateDiff(date1, date2)") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 Date date1 = (Date) variables.get("date1");
 Date date2 = (Date) variables.get("date2");
 return JavexUtils.daysBetween(date1, date2);
 }
 });
 INSTANCE.setReservedFunctions(functions);
 try {
 Object result = expr.evaluate();
 Date expected = JavexUtils.addDays(JavexUtils.getCurrentDate(), 2);
 assertEquals(expected.getTime(), result.longValue());
 } catch (Exception e) {
 fail("Should not throw any exception: " + e.getMessage());
 }
 }

 @Test
 public void testEvaluateWithInvalidInputWithDateFunctions() {
 Expression expr = new AddFunction("DateAdd(DateDiff(CurrentDate(), '2022-01-01'), 1) + 1");
 Map<String, FunctionExpression> functions = new HashMap<>();
 functions.put("CurrentDate", new DateFunction("CurrentDate", FunctionExpression.DATE, "CurrentDate -> Current date") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 throw new UnsupportedOperationException();
 }
 });
 INSTANCE.setReservedFunctions(functions);
 try {
 expr.evaluate();
 fail("Should throw IllegalArgumentException with message: 'Undefined function: CurrentDate'");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testEvaluateWithValidInputWithExternalFunctions() {
 Expression expr = new AddFunction("x * y");
 FunctionExpression externalFunction = new FunctionExpression("mul", FunctionExpression.MATH, "mul -> x * y") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 double x = ((Number) variables.get("x")).doubleValue();
 double y = ((Number) variables.get("y")).doubleValue();
 return x * y;
 }
 };
 Map<String, FunctionExpression> functions = new HashMap<>();
 functions.put("x", new FunctionExpression("x", FunctionExpression.MATH, "x -> x") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 return 5.0;
 }
 });
 functions.put("y", new FunctionExpression("y", FunctionExpression.MATH, "y -> y") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 return 3.0;
 }
 });
 INSTANCE.setReservedFunctions(functions);
 INSTANCE.setExternalFunctions(Arrays.asList(externalFunction));
 Map<String, Object> variables = new HashMap<>();
 variables.put("x", 5.0);
 variables.put("y", 3.0);
 try {
 Object result = expr.evaluate(variables);
 assertEquals(15.0, result);
 } catch (Exception e) {
 fail("Should not throw any exception: " + e.getMessage());
 }
 }

 @Test
 public void testEvaluateWithInvalidInputWithExternalFunctions() {
 Expression expr = new AddFunction("x * y");
 Map<String, FunctionExpression> functions = new HashMap<>();
 FunctionExpression externalFunction = new FunctionExpression("mul", FunctionExpression.MATH, "mul -> x * y") {
 @Override
 public Object evaluate(Map<String, Object> variables) {
 throw new UnsupportedOperationException();
 }
 };
 INSTANCE.setReservedFunctions(functions);
 INSTANCE.setExternalFunctions(Arrays.asList(externalFunction));
 try {
 expr.evaluate();
 fail("Should throw IllegalArgumentException with message: 'Undefined function: mul'");
 } catch (Exception e) {
 // Expected exception
 }
 }

}