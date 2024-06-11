package org.javex.test;

import static org.junit.Assert.*;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.junit.Test;
import java.util.*;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testIsReservedFunction() {
 Expression e = createExpression("and(x, y)");
 assertTrue(e.isReservedFunction("and"));
 e = createExpression("or(x, y)");
 assertTrue(e.isReservedFunction("or"));
 e = createExpression("greaterThan(x, y)");
 assertTrue(e.isReservedFunction("greaterThan"));
 e = createExpression("isFunction(x)");
 assertTrue(e.isReservedFunction("isFunction"));
 }

 @Test
 public void testIsLogicalFunction() {
 Expression e = createExpression("and(x, y)");
 assertTrue(e.isLogicalFunction("and"));
 e = createExpression("or(x, y)");
 assertTrue(e.isLogicalFunction("or"));
 e = createExpression("not(x)");
 assertTrue(e.isLogicalFunction("not"));
 e = createExpression("x and y");
 assertFalse(e.isLogicalFunction("and"));
 }

 @Test
 public void testIsMathFunction() {
 Expression e = createExpression("power(x, y)");
 assertTrue(e.isMathFunction("power"));
 e = createExpression("abs(x)");
 assertTrue(e.isMathFunction("abs"));
 e = createExpression("sqrt(x)");
 assertTrue(e.isMathFunction("sqrt"));
 e = createExpression("x + y");
 assertFalse(e.isMathFunction("+"));
 }

 @Test
 public void testIsDateFunction() {
 Expression e = createExpression("dateAdd(x, y, 'day')");
 assertTrue(e.isDateFunction("dateAdd"));
 e = createExpression("dateDiff(x, y, 'day')");
 assertTrue(e.isDateFunction("dateDiff"));
 e = createExpression("x > y");
 assertFalse(e.isDateFunction(">"));
 }

 @Test
 public void testIsStringFunction() {
 Expression e = createExpression("substring(x, 0, 2)");
 assertTrue(e.isStringFunction("substring"));
 e = createExpression("length(x)");
 assertTrue(e.isStringFunction("length"));
 e = createExpression("upper(x)");
 assertTrue(e.isStringFunction("upper"));
 e = createExpression("x = 'foo'");
 assertFalse(e.isStringFunction("="));
 }

 @Test
 public void testGetExternalFunction() {
 Expression e = new Expression("myFunction(x)");
 e.setExternalFunctions(Arrays.asList(new MyFunction()));
 assertEquals("myFunction", e.getExternalFunction("myFunction").getSimbol());
 }

 @Test
 public void testEvaluateSimpleExpressions() {
 Expression e = createExpression("2 + 3");
 assertEquals(5, e.evaluate());
 e = createExpression("2 * 3");
 assertEquals(6, e.evaluate());
 e = createExpression("2 - 3");
 assertEquals(-1, e.evaluate());
 e = createExpression("2 / 3");
 assertEquals(0.6666666805288059, e.evaluate(), 0.00000001);
 e = createExpression("sqrt(16)");
 assertEquals(4, e.evaluate().intValue());
 e = createExpression("abs(-5)");
 assertEquals(5, e.evaluate().intValue());
 e = createExpression("sin(PI / 2)");
 assertEquals(1, e.evaluate().intValue());
 e = createExpression("not(true)");
 assertEquals(false, e.evaluate());
 e = createExpression("and(true, true)");
 assertEquals(true, e.evaluate());
 e = createExpression("or(true, false)");
 assertEquals(true, e.evaluate());
 e = createExpression("length('hello')");
 assertEquals(5, e.evaluate().intValue());
 e = createExpression("upper('hello')");
 assertEquals("HELLO", e.evaluate().toString());
 e = createExpression("substring('hello', 1, 3)");
 assertEquals("hell", e.evaluate().toString());
 e = createExpression("isFunction(sin)");
 assertTrue(e.evaluate());
 e = createExpression("isNull(null)");
 assertTrue(e.evaluate());
 e = createExpression("5 > 3");
 assertTrue(e.evaluate());
 e = createExpression("5 < 3");
 assertFalse(e.evaluate());
 e = createExpression("5 >= 3");
 assertTrue(e.evaluate());
 e = createExpression("5 <= 3");
 assertFalse(e.evaluate());
 e = createExpression("'foo' = 'foo'");
 assertTrue(e.evaluate());
 e = createExpression("'foo' = 'bar'");
 assertFalse(e.evaluate());
 e = createExpression("'foo' != 'bar'");
 assertTrue(e.evaluate());
 e = createExpression("'foo' == 'foo'");
 assertTrue(e.evaluate());
 e = createExpression("'foo' < 'bar'");
 assertFalse(e.evaluate());
 e = createExpression("'foo' > 'bar'");
 assertFalse(e.evaluate());
 e = createExpression("'foo' <= 'bar'");
 assertFalse(e.evaluate());
 e = createExpression("'foo' >= 'bar'");
 assertFalse(e.evaluate());
 e = createExpression("'hello world' contains 'world'");
 assertTrue(e.evaluate());
 e = createExpression("'hello world' contains 'hello'");
 assertTrue(e.evaluate());
 e = createExpression("'hello world' endsWith 'world'");
 assertTrue(e.evaluate());
 e = createExpression("'hello world' startsWith 'hello'");
 assertTrue(e.evaluate());
 e = createExpression("'hello world' equals 'hello world'");
 assertTrue(e.evaluate());
 e = createExpression("'hello world' equals 'hello world'");
 assertTrue(e.evaluate());
 e = createExpression("dateAdd('2022-01-01', 1, 'day')");
 assertEquals("2022-01-02", e.evaluate().toString());
 e = createExpression("dateDiff('2022-01-01', '2022-01-02', 'day')");
 assertEquals(-1, e.evaluate().intValue());
 }

 @Test
 public void testEvaluateComplexExpressions() {
 Expression e = createExpression("2 + 3 * 4 / 5");
 assertEquals(6.8, e.evaluate(), 0.00001);
 e = createExpression("(2 + 3) * 4 / 5");
 assertEquals(6.8, e.evaluate(), 0.00001);
 e = createExpression("(2 + 3) * (4 + 5) / 5");
 assertEquals(13.2, e.evaluate(), 0.00001);
 e = createExpression("(2 + 3) * (4 + 5) / (5 + 6)");
 assertEquals(8.470588235294118, e.evaluate(), 0.000001);
 e = createExpression("(2 + 3) * (4 + 5) / (5 + 6) + 1");
 assertEquals(9.470588235294118, e.evaluate(), 0.000001);
 e = createExpression("(2 + 3) * (4 + 5) / (5 + 6) + 1 * 2");
 assertEquals(11.470588235294119, e.evaluate(), 0.000001);
 e = createExpression("(2 + 3) * (4 + 5) / (5 + 6) + 1 * 2 + 'hello'");
 assertEquals("hello11.470588235294119", e.evaluate().toString());
 e = createExpression("(2 + 3) * (4 + 5) / (5 + 6) + 1 * 2 + 'hello' + dateAdd('2022-01-01', 1, 'day')");
 assertEquals("hello2022-01-03", e.evaluate().toString());
 }

 @Test
 public void testEvaluateWithExternalFunctions() {
 Expression e = new Expression("myFunction(x) + 2");
 e.setExternalFunctions(Arrays.asList(new MyFunction()));
 assertEquals(5, e.evaluate(new Object[]{"3"}).intValue());
 }

 @Test
 public void testEvaluateWithInvalidFunctions() {
 Expression e = createExpression("myFunction(x)");
 try {
 e.evaluate();
 fail();
 } catch (Exception ex) {
 assertTrue(ex instanceof IllegalArgumentException);
 }
 }
}

class MyFunction implements FunctionExpression {
 private static final long serialVersionUID = 1L;

 @Override
 public String getSimbol() {
 return "myFunction";
 }

 @Override
 public int getType() {
 return FunctionExpression.USER_DEFINED;
 }

 @Override
 public Object evaluate(Object[] args) {
 return Integer.parseInt((String) args[0]) + 2;
 }
}