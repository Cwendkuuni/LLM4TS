package org.javex.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.utils.JavexUtils;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.FunctionExpression;
import java.util.*;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testIsReservedFunction() {
 final Expression e = createExpression("and(5, 10)");
 assertEquals(true, e.isReservedFunction("and"));
 }

 @Test
 public void testIsLogicalFunction() {
 final Expression e = createExpression("and(5, 10)");
 assertEquals(false, e.isLogicalFunction("and"));
 assertEquals(true, e.isLogicalFunction("and", FunctionExpression.LOGICAL));
 }

 @Test
 public void testIsMathFunction() {
 final Expression e = createExpression("sqrt(25)");
 assertEquals(true, e.isMathFunction("sqrt"));
 }

 @Test
 public void testIsDateFunction() {
 final Expression e = createExpression("dateAdd(now(), '1 day')");
 assertEquals(true, e.isDateFunction("dateAdd"));
 }

 @Test
 public void testIsStringFunction() {
 final Expression e = createExpression("length(\"hello world\")");
 assertEquals(true, e.isStringFunction("length"));
 }

 @Test
 public void testGetExternalFunction() {
 Expression e = new Expression("");
 e.externalFunctions = Arrays.asList(new AddFunction(), new SubtractFunction());
 assertEquals(new AddFunction(), e.getExternalFunction("add"));
 assertEquals(new SubtractFunction(), e.getExternalFunction("subtract"));
 assertEquals(null, e.getExternalFunction("not"));
 }

 @Test
 public void testPrepare() {
 final Expression e = createExpression("5 + 10 / 2");
 assertEquals(20.0, e.evaluate(), 0.01);
 }

 @Test
 public void testPrepareWithExternalFunctions() {
 Expression e = new Expression("");
 e.externalFunctions = Arrays.asList(new AddFunction(), new SubtractFunction());
 e.prepare();
 final String expression = "add(5, subtract(10, 2))";
 e = new Expression(expression);
 e.prepare();
 assertEquals(7.0, e.evaluate(), 0.01);
 }

 @Test
 public void testTokenize() {
 final Expression e = new Expression("");
 String[] expected = {"5", "+", "10", "/", "2"};
 e.tokenize();
 assertEquals(Arrays.asList(expected), e.getStack());
 }

 @Test
 public void testEvaluationStack() {
 final Expression e = new Expression("");
 final double[] values = {5.0, 10.0, 2.0};
 final String[] operands = {"5", "+", "10", "/", "2"};
 e.stack = new ExpressionStack();
 for (int i = 0; i < operands.length; ++i) {
 e.stack(operands[i], e.stack);
 }
 e.doEvaluationStack();
 assertEquals(Arrays.asList(values), e.stack);
 }

 @Test
 public void testGetFunction() {
 final Expression e = new Expression("");
 e.prepare();
 assertEquals(new AndFunction(), e.getFunction("and"));
 assertEquals(new AndFunction(), e.getFunction("and", FunctionExpression.LOGICAL));
 assertEquals(null, e.getFunction("not"));
 }

 @Test
 public void testIsFunction() {
 final Expression e = new Expression("");
 e.prepare();
 assertTrue(e.isFunction("and")); // Logical function
 assertTrue(e.isFunction("sqrt")); // Math function
 assertTrue(e.isFunction("dateAdd")); // Date function
 assertTrue(e.isFunction("length")); // String function
 }

 @Test
 public void testIsFunctionWithInvalidSymbol() {
 final Expression e = new Expression("");
 assertFalse(e.isFunction("invalidFunction"));
 }

 @Test
 public void testStackCopy() {
 final Expression e = new Expression("5 + 10");
 Expression eCopy = e.getStackCopy();
 assertEquals(Arrays.asList(Arrays.asList("5", "+", "10")), Arrays.asList(e.getStack(), eCopy.getStack()));
 }

 @Test
 public void testEvaluateWithInvalidExpression() {
 assertThrows(Exception.class, () -> createExpression(""));
 }

 @Test
 public void testEvaluateWithEmptyStack() {
 final Expression e = new Expression("");
 e.prepare();
 assertThrows(Exception.class, () -> e.evaluate());
 }

 @Test
 public void testEvaluateWithOverflow() {
 final Expression e = new Expression("");
 e.prepare();
 e.stack.push("2147483647");
 e.stack.push("1");
 e.stack.push("+");
 assertThrows(Exception.class, () -> e.evaluate());
 }

}