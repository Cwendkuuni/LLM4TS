package org.javex.expression;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javex.functions.FunctionExpression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PlusFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.ToLowerFunction;
import org.javex.functions.string.ToUpperFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.utils.ExpressionStack;
import org.javex.utils.JavexUtils;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setUp() {
 expression = new Expression("1 + 2 * (3 + 4) * (5 and 6)");
 expression.prepare();
 }

 @Test
 public void testIsReservedFunction() {
 final String[] testCases = new String[]{"and", "or", "not", "is", "isnot", "equals", "greaterthan", "lessthan", "lessorequal", "greaterorequal", "notchar", "contains", "startswith", "endswith", "isEmpty", "inlist", "abs", "divide", "multiply", "plus", "sqrt", "subtract", "power", "powerchar", "dateadd", "datediff", "length", "substring", "tolower", "toupper"};
 for (String testCase : testCases) {
 assertTrue(expression.isReservedFunction(testCase));
 }
 }

 @Test
 public void testIsLogicalFunction() {
 final String[] testCases = new String[]{"and", "or"};
 for (String testCase : testCases) {
 assertTrue(expression.isLogicalFunction(testCase));
 }
 final List<FunctionExpression> externalFunctions = new ArrayList<>();
 externalFunctions.add(new AndFunction());
 expression = new Expression("1 + 2 * (3 and 4)", externalFunctions);
 expression.prepare();
 assertFalse(expression.isLogicalFunction("and")); // Test for new instance with external functions
 }

 @Test
 public void testIsMathFunction() {
 final String[] testCases = new String[]{"abs", "divide", "multiply", "plus", "sqrt", "subtract", "power", "powerchar"};
 for (String testCase : testCases) {
 assertTrue(expression.isMathFunction(testCase));
 }
 }

 @Test
 public void testIsDateFunction() {
 final String[] testCases = new String[]{"dateadd", "datediff"};
 for (String testCase : testCases) {
 assertTrue(expression.isDateFunction(testCase));
 }
 }

 @Test
 public void testIsStringFunction() {
 final String[] testCases = new String[]{"length", "substring", "tolower", "toupper"};
 for (String testCase : testCases) {
 assertTrue(expression.isStringFunction(testCase));
 }
 }

 @Test
 public void testIsFunction() {
 final String[] testCases = new String[]{"abs", "and", "dateadd", "divide", "length", "multiply", "or", "power", "tolower"};
 for (String testCase : testCases) {
 assertTrue(expression.isFunction(testCase));
 }
 }

 @Test
 public void testGetReservedFunctions() {
 final Map<String, FunctionExpression> functions = new HashMap<>();
 Expression.putFunction(new AndFunction(), functions);
 Expression.putFunction(new OrFunction(), functions);
 FunctionExpression function = Expression.getReservedFunctions("and");
 assertEquals(AndFunction.class, function.getClass());
 }

 @Test
 public void testGetFunction() {
 final Map<String, FunctionExpression> functions = new HashMap<>();
 Expression.putFunction(new AndFunction(), functions);
 Expression.putFunction(new OrFunction(), functions);
 FunctionExpression function = expression.getFunction("and");
 assertEquals(AndFunction.class, function.getClass());
 }

 @Test
 public void testGetExternalFunction() {
 List<FunctionExpression> externalFunctions = new ArrayList<>();
 externalFunctions.add(new AndFunction());
 expression = new Expression("1 + 2 * (3 and 4)", externalFunctions);
 expression.prepare();
 FunctionExpression function = expression.getExternalFunction("and");
 assertEquals(AndFunction.class, function.getClass());
 }

 @Test
 public void testEvaluate() throws Exception {
 assertEquals(6.0, expression.evaluate());
 }

 @Test
 public void testTokenize() {
 Expression expression = new Expression("1 + 2 * (3 + 4) * (5 and 6)");
 expression.prepare();
 ExpressionStack expectedStack = new ExpressionStack();
 expectedStack.add("1");
 expectedStack.add("+");
 expectedStack.add("2");
 expectedStack.add("*");
 expectedStack.add("(");
 expectedStack.add("3");
 expectedStack.add("+");
 expectedStack.add("4");
 expectedStack.add(")");
 expectedStack.add("*");
 expectedStack.add("5");
 expectedStack.add("and");
 expectedStack.add("6");
 List<String> expectedTokens = new ArrayList<>();
 expectedTokens.add("1");
 expectedTokens.add("+");
 expectedTokens.add("2");
 expectedTokens.add("*");
 expectedTokens.add("(");
 expectedTokens.add("3");
 expectedTokens.add("+");
 expectedTokens.add("4");
 expectedTokens.add(")");
 expectedTokens.add("*");
 expectedTokens.add("5");
 expectedTokens.add("and");
 expectedTokens.add("6");
 List<String> actualTokens = expression.getTokens();
 assertEquals(expectedTokens, actualTokens);
 assertEquals(expectedStack.size(), expression.getStack().size());
 for (int i = 0; i < expectedStack.size(); i++) {
 assertEquals(expectedStack.get(i), expression.getStack().get(i));
 }
 }

 @Test
 public void testPrepare() {
 Expression expression = new Expression("1 + 2 * (3 + 4) * (5 and 6)");
 expression.prepare();
 }

 @Test(expected = Exception.class)
 public void testEvaluateWithException() throws Exception {
 Expression expression = new Expression("1 + 2 * (3 + 4) * (5 and x)");
 expression.prepare();
 expression.evaluate();
 }

 @After
 public void tearDown() {
 expression = null;
 }
}