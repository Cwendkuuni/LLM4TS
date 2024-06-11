package org.javex.expression;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javex.functions.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;

public class ExpressionTest {

 private Expression expression;
 private ExpressionStack stack;
 private List<FunctionExpression> externalFunctions;
 private static Map<String, FunctionExpression> reservedFunctions;

 @BeforeClass
 public static void setUpBeforeClass() {
 // Here you can initialize any static variables or objects needed by your tests
 Expression.putFunction(new AndFunction(), Expression.reservedFunctions);
 putFunction(new BetweenFunction(), Expression.reservedFunctions);
 putFunction(new ContainsFunction(), Expression.reservedFunctions);
 putFunction(new EndsWithFunction(), Expression.reservedFunctions);
 putFunction(new EqualsFunction(), Expression.reservedFunctions);
 putFunction(new GreaterEqThanFunction(), Expression.reservedFunctions);
 putFunction(new GreaterThanFunction(), Expression.reservedFunctions);
 putFunction(new IsEmptyFunction(), Expression.reservedFunctions);
 putFunction(new IsFunction(), Expression.reservedFunctions);
 putFunction(new IsNullFunction(), Expression.reservedFunctions);
 putFunction(new LessEqThanFunction(), Expression.reservedFunctions);
 putFunction(new LessThanFunction(), Expression.reservedFunctions);
 putFunction(new NotEqualsFunction(), Expression.reservedFunctions);
 putFunction(new NotFunction(), Expression.reservedFunctions);
 putFunction(new NotCharFunction(), Expression.reservedFunctions);
 putFunction(new OrFunction(), Expression.reservedFunctions);
 putFunction(new StartsWithFunction(), Expression.reservedFunctions);
 putFunction(new MatchesFunction(), Expression.reservedFunctions);
 putFunction(new InListFunction(), Expression.reservedFunctions);
 putFunction(new AbsFunction(), Expression.reservedFunctions);
 putFunction(new DivideFunction(), Expression.reservedFunctions);
 putFunction(new MultiplyFunction(), Expression.reservedFunctions);
 putFunction(new PlusFunction(), Expression.reservedFunctions);
 putFunction(new SqrtFunction(), Expression.reservedFunctions);
 putFunction(new SubstractFunction(), Expression.reservedFunctions);
 putFunction(new PowerFunction(), Expression.reservedFunctions);
 putFunction(new PowerCharFunction(), Expression.reservedFunctions);
 putFunction(new DateAddFunction(), Expression.reservedFunctions);
 putFunction(new DateDiffFunction(), Expression.reservedFunctions);
 putFunction(new LengthFunction(), Expression.reservedFunctions);
 putFunction(new SubStringFunction(), Expression.reservedFunctions);
 putFunction(new ToLowerFunction(), Expression.reservedFunctions);
 putFunction(new ToUpperFunction(), Expression.reservedFunctions);
 }

 @Before
 public void setUp() {
 // Initialize your test object here
 expression = new Expression("(1 + 2) * (3 > 4) and (5 = 5) or (6 <> 7) and (8 isnull null) and (contains('abc', 'bc') and startsWith('hello', 'hel') and length(substring('abcde', 2, 3)) > 3)");
 stack = new ExpressionStack();
 externalFunctions = new ArrayList<>();
 }

 @Test
 public void testIsReservedFunction() {
 // Test method isReservedFunction
 assertTrue(expression.isReservedFunction("and"));
 assertTrue(expression.isReservedFunction("or"));
 assertFalse(expression.isReservedFunction("test"));
 }

 @Test
 public void testIsLogicalFunction() {
 // Test method isLogicalFunction
 assertTrue(expression.isLogicalFunction("and"));
 assertTrue(expression.isLogicalFunction("or"));
 assertFalse(expression.isLogicalFunction("test"));
 }

 @Test
 public void testIsMathFunction() {
 // Test method isMathFunction
 assertFalse(expression.isMathFunction("and"));
 assertFalse(expression.isMathFunction("or"));
 assertTrue(expression.isMathFunction("abs"));
 }

 @Test
 public void testIsDateFunction() {
 // Test method isDateFunction
 assertFalse(expression.isDateFunction("and"));
 assertFalse(expression.isDateFunction("or"));
 assertTrue(expression.isDateFunction("dateadd"));
 }

 @Test
 public void testIsStringFunction() {
 // Test method isStringFunction
 assertFalse(expression.isStringFunction("and"));
 assertFalse(expression.isStringFunction("or"));
 assertTrue(expression.isStringFunction("substring"));
 }

 @Test
 public void testGetExternalFunction() {
 // Test method getExternalFunction
 FunctionExpression externalFunction = new TestFunction();
 expression.setExternalFunctions(externalFunctions);
 externalFunctions.add(externalFunction);
 assertTrue(expression.isExternalFunction("test"));
 assertEquals(externalFunction, expression.getExternalFunction("test"));
 }

 @Test
 public void testPrepare() {
 // Test method prepare
 expression.prepare();
 ExpressionStack expectedStack = new ExpressionStack();
 expectedStack.add("1");
 expectedStack.add("+");
 expectedStack.add("2");
 expectedStack.add("*");
 expectedStack.add("3");
 expectedStack.add(">");
 expectedStack.add("4");
 expectedStack.add(">");
 expectedStack.add("5");
 expectedStack.add("=");
 expectedStack.add("5");
 expectedStack.add("or");
 expectedStack.add("6");
 expectedStack.add("<>");
 expectedStack.add("7");
 expectedStack.add("and");
 expectedStack.add("8");
 expectedStack.add("isnull");
 expectedStack.add("null");
 expectedStack.add("and");
 expectedStack.add("contains");
 expectedStack.add("'abc'");
 expectedStack.add("'bc'");
 expectedStack.add("and");
 expectedStack.add("startsWith");
 expectedStack.add("'hello'");
 expectedStack.add("'hel'");
 expectedStack.add("and");
 expectedStack.add("length");
 expectedStack.add("substring");
 expectedStack.add("'abcde'");
 expectedStack.add("2");
 expectedStack.add("3");
 expectedStack.add(">");
 expectedStack.add("3");
 Stack<String> actualStack = expression.getStackCopy();
 assertEquals(expectedStack, actualStack);
 }

 @Test
 public void testEvaluate() throws Exception {
 // Test method evaluate
 ExpressionEvaluator evaluator = new ExpressionEvaluator(expression);
 Object result = evaluator.evaluate();
 assertTrue(result instanceof Boolean);
 assertEquals(true, result);
 }

 private static void putFunction(FunctionExpression f, Map<String, FunctionExpression> map) {
 map.put(f.getSymbol(), f);
 }
}