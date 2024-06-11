package org.javex.expression;

import static org.junit.Assert.*;
import org.javex.functions.*;
import org.javex.utils.JavexUtils;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.Stack;
import java.util.*;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setup() {
 expression = new Expression("(2 + 3) * (4 > 2) and (5 > 4)");
 expression.prepare();
 }

 @Test
 public void testIsReservedFunction() {
 assertTrue(expression.isReservedFunction("and"));
 assertTrue(expression.isReservedFunction(">"));
 assertFalse(expression.isReservedFunction("+"));
 }

 @Test
 public void testIsLogicalFunction() {
 assertTrue(expression.isLogicalFunction("and"));
 assertFalse(expression.isLogicalFunction("+"));
 }

 @Test
 public void testIsMathFunction() {
 assertFalse(expression.isMathFunction("and"));
 assertFalse(expression.isMathFunction(">"));
 assertFalse(expression.isMathFunction("+"));
 }

 @Test
 public void testIsDateFunction() {
 assertFalse(expression.isDateFunction("and"));
 assertFalse(expression.isDateFunction(">"));
 assertFalse(expression.isDateFunction("+"));
 }

 @Test
 public void testIsStringFunction() {
 assertFalse(expression.isStringFunction("and"));
 assertFalse(expression.isStringFunction(">"));
 assertFalse(expression.isStringFunction("+"));
 }

 @Test
 public void testGetExternalFunction() {
 Expression externalFunctionExpression = new Expression("myFunction(5)");
 externalFunctionExpression.prepare();
 FunctionExpression myFunction = new MyFunctionExpression();
 externalFunctionExpression.setExternalFunctions(Arrays.asList(myFunction));
 assertTrue(expression.isExternalFunction("myFunction"));
 assertEquals(myFunction, expression.getExternalFunction("myFunction"));
 }

 @Test
 public void testIsFunction() {
 assertTrue(expression.isFunction("+"));
 assertTrue(expression.isFunction(">"));
 assertTrue(expression.isFunction("and"));
 }

 @Test
 public void testDoEvaluationStack() {
 List<String> expectedStack = Arrays.asList(
 "2", "+", "3", "*", ">", "4", "2", "F", "T"
 );
 Stack<String> stack = expression.getStackCopy();
 List<String> actualStack = new ArrayList<>();
 while (!stack.isEmpty()) {
 actualStack.add(stack.pop());
 }
 Collections.reverse(actualStack);
 assertEquals(expectedStack, actualStack);
 }

 @Test(expected = Exception.class)
 public void testEvaluateException() {
 expression = new Expression("2 + 3 * 4");
 expression.prepare();
 expression.evaluate();
 fail("Expected exception thrown");
 }

 @Test
 public void testEvaluate() {
 Object result = expression.evaluate();
 assertEquals(12.0, result, 0.0);
 }

 @Test
 public void testIsEmptyFunction() {
 FunctionExpression isEmptyFunction = new IsEmptyFunctionExpression();
 assertTrue(isEmptyFunction.evaluate(new Object()));
 }

 @Test
 public void testEqualsFunction() {
 FunctionExpression equalsFunction = new EqualsFunctionExpression();
 Object a = new Object();
 Object b = new Object();
 assertFalse(equalsFunction.evaluate(a, b));
 Object c = new Object();
 assertTrue(equalsFunction.evaluate(c, c));
 }

 @Test
 public void testNotFunction() {
 FunctionExpression notFunction = new NotFunctionExpression();
 assertTrue(notFunction.evaluate(new Boolean(true)));
 assertFalse(notFunction.evaluate(new Boolean(false)));
 }

 @Test
 public void testNotCharFunction() {
 FunctionExpression notCharFunction = new NotCharFunctionExpression();
 char expected = (char) 0x5F;
 char actual = notCharFunction.evaluate('~').charValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testAndFunction() {
 FunctionExpression andFunction = new AndFunctionExpression();
 assertTrue(andFunction.evaluate(new Boolean(true), new Boolean(true)));
 assertFalse(andFunction.evaluate(new Boolean(false), new Boolean(true)));
 assertFalse(andFunction.evaluate(new Boolean(true), new Boolean(false)));
 }

 @Test
 public void testOrFunction() {
 FunctionExpression orFunction = new OrFunctionExpression();
 assertTrue(orFunction.evaluate(new Boolean(true), new Boolean(true)));
 assertTrue(orFunction.evaluate(new Boolean(false), new Boolean(true)));
 assertTrue(orFunction.evaluate(new Boolean(true), new Boolean(false)));
 }

 @Test
 public void testGreaterThanFunction() {
 FunctionExpression greaterThanFunction = new GreaterThanFunctionExpression();
 assertTrue(greaterThanFunction.evaluate(new Integer(5), new Integer(4)));
 assertFalse(greaterThanFunction.evaluate(new Integer(4), new Integer(5)));
 }

 @Test
 public void testGreaterEqThanFunction() {
 FunctionExpression greaterEqThanFunction = new GreaterEqThanFunctionExpression();
 assertTrue(greaterEqThanFunction.evaluate(new Integer(5), new Integer(4)));
 assertTrue(greaterEqThanFunction.evaluate(new Integer(5), new Integer(5)));
 assertFalse(greaterEqThanFunction.evaluate(new Integer(4), new Integer(5)));
 }

 @Test
 public void testLessThanFunction() {
 FunctionExpression lessThanFunction = new LessThanFunctionExpression();
 assertFalse(lessThanFunction.evaluate(new Integer(5), new Integer(4)));
 assertTrue(lessThanFunction.evaluate(new Integer(4), new Integer(5)));
 }

 @Test
 public void testLessEqThanFunction() {
 FunctionExpression lessEqThanFunction = new LessEqThanFunctionExpression();
 assertTrue(lessEqThanFunction.evaluate(new Integer(5), new Integer(5)));
 assertTrue(lessEqThanFunction.evaluate(new Integer(4), new Integer(4)));
 assertFalse(lessEqThanFunction.evaluate(new Integer(5), new Integer(4)));
 }

 @Test
 public void testIsFunction() {
 FunctionExpression isFunctionExpression = new IsFunctionExpression();
 Object value = new Object();
 assertTrue(isFunctionExpression.evaluate(value));
 }

 @Test
 public void testIsNullFunction() {
 FunctionExpression isNullFunction = new IsNullFunctionExpression();
 Object value = null;
 assertTrue(isNullFunction.evaluate(value));
 }

 @Test
 public void testInListFunction() {
 FunctionExpression inListFunction = new InListFunctionExpression();
 Object[] values = {3, 4, 5};
 Object valueToCheck = new Integer(4);
 assertTrue(inListFunction.evaluate(valueToCheck, values));
 assertFalse(inListFunction.evaluate(new Integer(2), values));
 }

 @Test
 public void testMatchesFunction() {
 FunctionExpression matchesFunction = new MatchesFunctionExpression();
 String pattern = ".a.";
 String valueToCheck = "the cat is on the mat";
 assertTrue(matchesFunction.evaluate(valueToCheck, pattern));
 assertFalse(matchesFunction.evaluate("the cat is not on the mat", pattern));
 }

 @Test
 public void testStartsWithFunction() {
 FunctionExpression startsWithFunction = new StartsWithFunctionExpression();
 String prefix = "the cat";
 String valueToCheck = "the cat is on the mat";
 assertTrue(startsWithFunction.evaluate(valueToCheck, prefix));
 assertFalse(startsWithFunction.evaluate("the dog is on the mat", prefix));
 }

 @Test
 public void testEndsWithFunction() {
 FunctionExpression endsWithFunction = new EndsWithFunctionExpression();
 String suffix = "mat";
 String valueToCheck = "the cat is on the mat";
 assertTrue(endsWithFunction.evaluate(valueToCheck, suffix));
 assertFalse(endsWithFunction.evaluate("the cat is on the floor", suffix));
 }

 @Test
 public void testContainsFunction() {
 FunctionExpression containsFunction = new ContainsFunctionExpression();
 String valueToSearch = "the cat is on the mat";
 String valueToFind = "cat";
 assertTrue(containsFunction.evaluate(valueToSearch, valueToFind));
 assertFalse(containsFunction.evaluate("the dog is on the mat", valueToFind));
 }

 @Test
 public void testLengthFunction() {
 FunctionExpression lengthFunction = new LengthFunctionExpression();
 String value = "hello world";
 assertEquals(11, lengthFunction.evaluate(value), 0);
 }

 @Test
 public void testSubStringFunction() {
 FunctionExpression subStringFunction = new SubStringFunctionExpression();
 String value = "hello world";
 assertEquals("world", subStringFunction.evaluate(value, 6, 5).toString());
 }

 @Test
 public void testToLowerFunction() {
 FunctionExpression toLowerFunction = new ToLowerFunctionExpression();
 String value = "Hello world";
 assertEquals("hello world", toLowerFunction.evaluate(value).toString());
 }

 @Test
 public void testToUpperFunction() {
 FunctionExpression toUpperFunction = new ToUpperFunctionExpression();
 String value = "hello world";
 assertEquals("HELLO WORLD", toUpperFunction.evaluate(value).toString());
 }

 @Test
 public void testAbsFunction() {
 FunctionExpression absFunction = new AbsFunctionExpression();
 assertEquals(3.0, absFunction.evaluate(-3.0), 0.0);
 }

 @Test
 public void testDivideFunction() {
 FunctionExpression divideFunction = new DivideFunctionExpression();
 assertEquals(2.5, divideFunction.evaluate(5.0, 2.0), 0.0);
 }

 @Test
 public void testMultiplyFunction() {
 FunctionExpression multiplyFunction = new MultiplyFunctionExpression();
 assertEquals(12.0, multiplyFunction.evaluate(3.0, 4.0), 0.0);
 }

 @Test
 public void testPlusFunction() {
 FunctionExpression plusFunction = new PlusFunctionExpression();
 assertEquals(5.0, plusFunction.evaluate(2.0, 3.0), 0.0);
 }

 @Test
 public void testSqrtFunction() {
 FunctionExpression sqrtFunction = new SqrtFunctionExpression();
 assertEquals(2.0, sqrtFunction.evaluate(4.0), 0.0);
 }

 @Test
 public void testSubtractFunction() {
 FunctionExpression subtractFunction = new SubtractFunctionExpression();
 assertEquals(2.0, subtractFunction.evaluate(4.0, 2.0), 0.0);
 }

 @Test
 public void testPowerFunction() {
 FunctionExpression powerFunction = new PowerFunctionExpression();
 assertEquals(256.0, powerFunction.evaluate(2.0, 8.0), 0.0);
 }

 @Test
 public void testPowerCharFunction() {
 FunctionExpression powerCharFunction = new PowerCharFunctionExpression();
 char base = 'A';
 char exponent = '2';
 assertEquals("AA", powerCharFunction.evaluate(base, exponent).toString());
 }

 @Test
 public void testDateAddFunction() {
 FunctionExpression dateAddFunction = new DateAddFunctionExpression();
 Date date = new Date(2022, 11, 30);
 assertEquals(new Date(2022, 12, 31), dateAddFunction.evaluate(date, 30).getValue(), 0);
 }

 @Test
 public void testDateDiffFunction() {
 FunctionExpression dateDiffFunction = new DateDiffFunctionExpression();
 Date date1 = new Date(2022, 11, 30);
 Date date2 = new Date(2022, 12, 31);
 assertEquals(-30, dateDiffFunction.evaluate(date2, date1).getValue(), 0);
 }
}