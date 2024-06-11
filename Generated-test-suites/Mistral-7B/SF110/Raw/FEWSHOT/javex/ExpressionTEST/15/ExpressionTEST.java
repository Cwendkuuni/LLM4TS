package org.javex.test;

import static org.junit.Assert.*;
import org.javex.utils.*;
import org.javex.functions.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;
import java.util.*;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setup() {
 expression = new Expression("(5 + (3 * 4)) and (10 > 5) and (2 = 2) and (abs(-5) = 5) and (sqrt(16) = 4) and (power(2, 2) = 16) and (dateAdd(now(), interval '1 day') = now() + interval '1 day') and (substring(concat('hello ', 'world'), 1, 5) = 'hello') and (toLower('Hello') = 'hello') and (toUpper('world') = 'WORLD') and (length(concat('test', 'ing')) = 6) and (contains('test', 'test'))");
 expression.prepare();
 }

 @Test
 public void testIsReservedFunction() {
 FunctionExpression function = Expression.getReservedFunctions("and");
 assertTrue(function != null);
 function = Expression.getReservedFunctions("or");
 assertTrue(function != null);
 function = Expression.getReservedFunctions("not");
 assertTrue(function != null);
 function = Expression.getReservedFunctions("xor");
 assertNull(function);
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression function = expression.getFunction("and");
 assertTrue(function != null && function.getType() == FunctionExpression.LOGICAL);
 function = expression.getFunction("or");
 assertTrue(function != null && function.getType() == FunctionExpression.LOGICAL);
 function = expression.getFunction("not");
 assertTrue(function != null && function.getType() == FunctionExpression.LOGICAL);
 function = expression.getFunction("xor");
 assertNull(function);
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression function = expression.getFunction("abs");
 assertTrue(function != null && function.getType() == FunctionExpression.MATH);
 function = expression.getFunction("sqrt");
 assertTrue(function != null && function.getType() == FunctionExpression.MATH);
 function = expression.getFunction("power");
 assertTrue(function != null && function.getType() == FunctionExpression.MATH);
 function = expression.getFunction("sin");
 assertNull(function);
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression function = expression.getFunction("dateAdd");
 assertTrue(function != null && function.getType() == FunctionExpression.DATE);
 function = expression.getFunction("dateDiff");
 assertTrue(function != null && function.getType() == FunctionExpression.DATE);
 function = expression.getFunction("now");
 assertNull(function);
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression function = expression.getFunction("concat");
 assertTrue(function != null && function.getType() == FunctionExpression.STRING);
 function = expression.getFunction("substr");
 assertTrue(function != null && function.getType() == FunctionExpression.STRING);
 function = expression.getFunction("length");
 assertTrue(function != null && function.getType() == FunctionExpression.STRING);
 function = expression.getFunction("toUpper");
 assertTrue(function != null && function.getType() == FunctionExpression.STRING);
 function = expression.getFunction("toLowerCase");
 assertTrue(function != null && function.getType() == FunctionExpression.STRING);
 function = expression.getFunction("substring");
 assertTrue(function != null && function.getType() == FunctionExpression.STRING);
 function = expression.getFunction("contains");
 assertTrue(function != null && function.getType() == FunctionExpression.STRING);
 function = expression.getFunction("trim");
 assertNull(function);
 }

 @Test
 public void testGetExternalFunction() {
 Expression externalExpression = new Expression("myFunction(5)");
 externalExpression.setExternalFunctions(Arrays.asList(new MyFunctionExpression()));
 assertTrue(externalExpression.isExternalFunction("myFunction"));
 FunctionExpression function = externalExpression.getFunction("myFunction");
 assertTrue(function != null && function.getType() == FunctionExpression.MATH);
 }

 @Test
 public void testIsFunction() {
 FunctionExpression function = expression.getFunction("and");
 assertTrue(function != null);
 assertTrue(expression.isFunction("and"));
 function = expression.getFunction("myFunction");
 assertFalse(function != null);
 assertTrue(expression.isFunction("myFunction"));
 }

 @Test
 public void testEvaluate() throws Exception {
 Object result = expression.evaluate();
 assertTrue(result != null);
 assertTrue(result instanceof Boolean);
 }

 @Test(expected = Exception.class)
 public void testEvaluateWithError() throws Exception {
 Expression errorExpression = new Expression("5 * not(5)");
 errorExpression.prepare();
 errorExpression.evaluate();
 fail("An exception should have been thrown.");
 }

 @Test
 public void testTokenize() {
 List<String> tokens = new ArrayList<>(expression.getStack().size());
 for (String token : expression.getStack()) {
 tokens.add(token.trim());
 }
 List<String> expectedTokens = Arrays.asList("(", "5", "+", "3", "*", "4", ")", "and", "(", "10", ">", "5", ")", "and", "(", "2", "=", "2", ")", "and", "(", "abs", "-5", "=", "5", ")", "and", "(", "sqrt", "16", "=", "4", ")", "and", "(", "power", "2", "2", "=", "16", ")", "and", "(", "dateAdd", "now()", "interval", "'1 day'", "=", "now()", "+", "interval", "'1 day'", ")", "and", "(", "substring", "concat", "'hello'", "' ", "'world'", ")", "1", ",", "5", ")", "and", "(", "toLower", "Hello", "=", "'hello'", ")", "and", "(", "toUpper", "'world'", "=", "'WORLD'", ")", "and", "(", "length", "concat", "'test'", "'ing'", "=", "6", ")", "and", "(contains", "test", "test", ")", ")");
 assertEquals(expectedTokens, tokens);
 }

}