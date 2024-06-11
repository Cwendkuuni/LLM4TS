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

 private Expression createExpression(String expression) {
 Expression expr = new Expression(expression);
 expr.prepare();
 return expr;
 }

 @Test
 public void testIsReservedFunction() {
 FunctionExpression[] functions = {new AndFunction(), new BetweenFunction(), new ContainsFunction(),
 new EndsWithFunction(), new EqualsFunction(), new GreaterEqThanFunction(), new GreaterThanFunction(),
 new IsEmptyFunction(), new IsFunction(), new IsNullFunction(), new LessEqThanFunction(),
 new LessThanFunction(), new NotEqualsFunction(), new NotFunction(), new NotCharFunction(),
 new OrFunction(), new StartsWithFunction(), new MatchesFunction(), new InListFunction(),
 new AbsFunction(), new DivideFunction(), new MultiplyFunction(), new PlusFunction(), new SqrtFunction(),
 new SubstractFunction(), new PowerFunction(), new PowerCharFunction(), new DateAddFunction(),
 new DateDiffFunction(), new LengthFunction(), new SubStringFunction(), new ToLowerFunction(),
 new ToUpperFunction()};
 String[] symbols = {"and", "between", "contains", "endsWith", "equals", "greaterEqThan", "greaterThan",
 "isEmpty", "isFunction", "isNull", "lessEqThan", "lessThan", "notEquals", "not", "notChar",
 "or", "startsWith", "matches", "inList", "abs", "divide", "multiply", "plus", "sqrt", "subtract",
 "power", "powerChar", "dateAdd", "dateDiff", "length", "substring", "toLower", "toUpper"};
 for (int i = 0; i < functions.length; i++) {
 Expression expr = createExpression("" + symbols[i] + "(");
 assertTrue(expr.isReservedFunction(symbols[i]));
 expr = createExpression("" + symbols[i] + ")");
 assertTrue(expr.isReservedFunction(symbols[i]));
 }
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression[] functions = {new AndFunction(), new OrFunction()};
 String[] symbols = {"and", "or"};
 for (int i = 0; i < functions.length; i++) {
 Expression expr = createExpression("" + symbols[i]);
 assertTrue(expr.isLogicalFunction(symbols[i]));
 }
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression[] functions = {new AbsFunction(), new SqrtFunction(), new SubtractFunction(),
 new PowerFunction(), new PowerCharFunction()};
 String[] symbols = {"abs", "sqrt", "subtract", "power", "powerChar"};
 for (int i = 0; i < functions.length; i++) {
 Expression expr = createExpression("" + symbols[i] + "(");
 assertTrue(expr.isMathFunction(symbols[i]));
 expr = createExpression("" + symbols[i] + "(" + "1" + ")");
 assertTrue(expr.isMathFunction(symbols[i]));
 }
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression[] functions = {new DateAddFunction(), new DateDiffFunction()};
 String[] symbols = {"dateAdd", "dateDiff"};
 for (int i = 0; i < functions.length; i++) {
 Expression expr = createExpression("" + symbols[i] + "(");
 assertTrue(expr.isDateFunction(symbols[i]));
 expr = createExpression("" + symbols[i] + "(" + "2022-01-01" + "," + "2022-01-02" + ")");
 assertTrue(expr.isDateFunction(symbols[i]));
 }
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression[] functions = {new LengthFunction(), new SubStringFunction(), new ToLowerFunction(),
 new ToUpperFunction()};
 String[] symbols = {"length", "substring", "toLower", "toUpper"};
 for (int i = 0; i < functions.length; i++) {
 Expression expr = createExpression("" + symbols[i] + "(");
 assertTrue(expr.isStringFunction(symbols[i]));
 expr = createExpression("" + symbols[i] + "(" + "\"" + "foo" + "\"" + ")");
 assertTrue(expr.isStringFunction(symbols[i]));
 }
 }

 @Test
 public void testGetFunction() {
 FunctionExpression andFunc = new AndFunction();
 FunctionExpression orFunc = new OrFunction();
 FunctionExpression invalidFunc = new Object() {};
 String[] symbols = {"and", "or"};
 Expression expr = createExpression("" + symbols[0] + "(");
 FunctionExpression func = expr.getFunction(symbols[0]);
 assertEquals(andFunc, func);
 expr = createExpression("" + symbols[1] + "(");
 func = expr.getFunction(symbols[1]);
 assertEquals(orFunc, func);
 expr = createExpression("invalid");
 func = expr.getFunction("invalid");
 assertNull(func);
 }

 @Test
 public void testGetExternalFunction() {
 FunctionExpression customFunc = new AddNumberFunction();
 Expression expr = createExpression("custom");
 expr.setExternalFunctions(Arrays.asList(customFunc));
 FunctionExpression func = expr.getExternalFunction("custom");
 assertEquals(customFunc, func);
 expr.setExternalFunctions(null);
 func = expr.getExternalFunction("custom");
 assertNull(func);
 }

 @Test
 public void testIsFunction() {
 FunctionExpression[] functions = {new AndFunction(), new OrFunction(), new AddNumberFunction()};
 String[] symbols = {"and", "or", "custom"};
 for (int i = 0; i < functions.length; i++) {
 Expression expr = createExpression("" + symbols[i]);
 assertTrue(expr.isFunction(symbols[i]));
 }
 expr = createExpression("invalid");
 assertFalse(expr.isFunction("invalid"));
 expr.setExternalFunctions(Arrays.asList(functions[2]));
 assertTrue(expr.isFunction("custom"));
 expr.setExternalFunctions(null);
 assertFalse(expr.isFunction("custom"));
 }

 @Test
 public void testEvaluate() throws Exception {
 Expression expr = createExpression("1 + 2");
 assertEquals(3.0, expr.evaluate());
 expr = createExpression("3 > 2");
 assertTrue(expr.evaluate());
 expr = createExpression("\"foo\" = \"foo\"");
 assertTrue(expr.evaluate());
 expr = createExpression("\"foo\" = \"bar\"");
 assertFalse(expr.evaluate());
 expr = createExpression("length(\"foo\")");
 assertEquals(3, expr.evaluate());
 expr = createExpression("substring(\"foo\", 1, 2)");
 assertEquals("fo", expr.evaluate());
 expr = createExpression("toUpper(\"foo\")");
 assertEquals("FOO", expr.evaluate());
 expr = createExpression("toLower(\"FOO\")");
 assertEquals("foo", expr.evaluate());
 expr = createExpression("abs(-3)");
 assertEquals(3.0, expr.evaluate());
 expr = createExpression("sqrt(9)");
 assertEquals(3.0, expr.evaluate());
 expr = createExpression("power(2, 3)");
 assertEquals(8.0, expr.evaluate());
 expr = createExpression("powerChar('a', 2)");
 assertEquals("aa", expr.evaluate());
 expr = createExpression("dateAdd('2022-01-01', 1, 'day')");
 assertEquals("2022-01-02", expr.evaluate());
 expr = createExpression("dateDiff('2022-01-01', '2022-01-02', 'day')");
 assertEquals(-1.0, expr.evaluate());
 }

 @Test
 public void testPrepare() {
 Expression expr = new Expression("");
 assertFalse(expr.isPrepared());
 expr.prepare();
 assertTrue(expr.isPrepared());
 }

 @Test
 public void testGetStackCopy() {
 Expression expr = createExpression("1 + 2");
 ExpressionStack stack = expr.getStackCopy();
 assertArrayEquals(new String[]{"1", "+", "2"}, stack.toArray());
 }

 @Test
 public void testTokenize() {
 String expression = "1 + 2 * 3";
 Expression expr = new Expression(expression);
 expr.tokenize();
 ExpressionStack expectedStack = new ExpressionStack();
 expectedStack.add("1");
 expectedStack.add("+");
 expectedStack.add("2");
 expectedStack.add("*");
 expectedStack.add("3");
 ExpressionStack actualStack = expr.getStackCopy();
 assertArrayEquals(expectedStack.toArray(), actualStack.toArray());
 }

}