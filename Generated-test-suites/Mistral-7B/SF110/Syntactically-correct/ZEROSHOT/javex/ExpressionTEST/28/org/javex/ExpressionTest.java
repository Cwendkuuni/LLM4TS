package org.javex.expression;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

 private Expression expression;
 private static final String EXPRESSION_1 = "1 + 2 * (3 + 4) and 5 > 3";
 private static final String EXPRESSION_2 = "isFunction('and')";
 private static final String EXPRESSION_3 = "isFunction('not') and length(1) > 2";
 private static final String EXPRESSION_4 = "isFunction('abs') and abs(-1) = 1";
 private static final String EXPRESSION_5 = "isFunction('dateAdd') and dateAdd('2022-01-01', '1 day') = '2022-01-02'";

 @Before
 public void setUp() {
 expression = new Expression(EXPRESSION_1);
 expression.prepare();
 }

 @Test
 public void testGetExpression() {
 assertEquals("1 + 2 * (3 + 4) and 5 > 3", expression.getExpression());
 }

 @Test
 public void testIsReservedFunction() {
 assertTrue(expression.isReservedFunction("and"));
 assertTrue(expression.isReservedFunction("isFunction"));
 assertFalse(expression.isReservedFunction("not"));
 }

 @Test
 public void testIsLogicalFunction() {
 assertTrue(expression.isLogicalFunction("and"));
 assertFalse(expression.isLogicalFunction("isFunction"));
 }

 @Test
 public void testIsMathFunction() {
 assertFalse(expression.isMathFunction("and"));
 assertFalse(expression.isMathFunction("isFunction"));
 }

 @Test
 public void testIsDateFunction() {
 assertFalse(expression.isDateFunction("and"));
 assertFalse(expression.isDateFunction("isFunction"));
 }

 @Test
 public void testIsStringFunction() {
 assertFalse(expression.isStringFunction("and"));
 assertFalse(expression.isStringFunction("isFunction"));
 }

 @Test
 public void testGetFunction() {
 FunctionExpression function = expression.getFunction("and");
 assertEquals(FunctionExpression.LOGICAL, function.getType());
 }

 @Test(expected = NullPointerException.class)
 public void testGetNonExistentFunction() {
 expression.getFunction("notFunction");
 }

 @Test
 public void testIsFunction() {
 assertTrue(expression.isFunction("and"));
 assertTrue(expression.isFunction("isFunction"));
 assertFalse(expression.isFunction("not"));
 }

 @Test
 public void testGetExternalFunctions() {
 List<FunctionExpression> externalFunctions = expression.getExternalFunctions();
 assertTrue(externalFunctions.isEmpty());
 }

 @Test
 public void testPrepare() {
 expression = new Expression(EXPRESSION_2);
 expression.prepare();
 FunctionExpression function = expression.getFunction("and");
 assertNull(function);

 expression = new Expression(EXPRESSION_3);
 expression.prepare();
 FunctionExpression lengthFunction = expression.getFunction("length");
 assertSame(FunctionExpression.STRING, lengthFunction.getType());
 }

 @Test
 public void testGetStack() {
 Stack<String> stack = expression.getStack();
 List<String> expected = new ArrayList<>(Collections.singletonList("1"));
 expected.addAll(Collections.singletonList("2"));
 expected.addAll(Collections.singletonList("*"));
 expected.addAll(Collections.singletonList("("));
 expected.addAll(Collections.singletonList("3"));
 expected.addAll(Collections.singletonList("+"));
 expected.addAll(Collections.singletonList("4"));
 expected.addAll(Collections.singletonList(")"));
 expected.addAll(Collections.singletonList("and"));
 expected.addAll(Collections.singletonList("5"));
 expected.addAll(Collections.singletonList(">"));
 expected.addAll(Collections.singletonList("3"));
 assertSame(expected, stack);
 }

 @After
 public void tearDown() {
 expression = null;
 }

 @Test
 public void testExpression2() {
 Expression e2 = new Expression(EXPRESSION_2);
 e2.prepare();
 assertTrue(e2.evaluate() instanceof Boolean);
 }

 @Test(expected = RuntimeException.class)
 public void testExpression3_False() {
 Expression e3 = new Expression(EXPRESSION_3);
 e3.prepare();
 assertFalse(e3.evaluate());
 }

 @Test
 public void testExpression3_True() {
 Expression e3 = new Expression(EXPRESSION_3);
 e3.prepare();
 Expression lengthFunction = e3.getFunction("length");
 FunctionExpression eval = lengthFunction.evaluate(Collections.singletonList("1"));
 assertTrue(eval.evaluate());
 }

 @Test
 public void testExpression4() {
 Expression e4 = new Expression(EXPRESSION_4);
 e4.prepare();
 FunctionExpression absFunction = e4.getFunction("abs");
 FunctionExpression valueFunction = e4.getFunction("-1");
 FunctionExpression equalsFunction = e4.getFunction("=");
 Object abs = absFunction.evaluate();
 Object value = valueFunction.evaluate();
 Object result = equalsFunction.evaluate(Collections.singletonList(abs), Collections.singletonList(value));
 assertTrue(result);
 }

 @Test
 public void testExpression5() {
 Expression e5 = new Expression(EXPRESSION_5);
 e5.prepare();
 FunctionExpression dateAddFunction = e5.getFunction("dateAdd");
 FunctionExpression dateFunction = e5.getFunction("'2022-01-01'");
 FunctionExpression stringFunction = e5.getFunction("'1 day'");
 FunctionExpression equalsFunction = e5.getFunction("=");
 Object date = dateFunction.evaluate();
 Object dayFunction = stringFunction.evaluate();
 Object result = equalsFunction.evaluate(Collections.singletonList(dateAddFunction.evaluate(date, dayFunction)), Collections.singletonList("'2022-01-02'"));
 assertTrue(result);
 }
}