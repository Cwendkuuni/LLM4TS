package org.javex.Expression;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class ExpressionTest {

 private Expression expression;
 private FunctionExpression andFunction, orFunction;
 private FunctionExpression addFunction, subtractFunction, multiplyFunction, divideFunction, powerFunction, sqrtFunction, absFunction, lengthFunction;
 private FunctionExpression dateAddFunction, dateDiffFunction;
 private ExpressionStack stack;

 @Before
 public void setup() {
 expression = new Expression("(5 + 3) * (2 and 3)");
 andFunction = new AndFunction();
 orFunction = new OrFunction();
 addFunction = new AddFunction();
 subtractFunction = new SubtractFunction();
 multiplyFunction = new MultiplyFunction();
 divideFunction = new DivideFunction();
 powerFunction = new PowerFunction();
 sqrtFunction = new SqrtFunction();
 absFunction = new AbsFunction();
 lengthFunction = new LengthFunction();
 dateAddFunction = new DateAddFunction();
 dateDiffFunction = new DateDiffFunction();
 stack = new ExpressionStack();
 }

 @Test
 public void testConstructor() {
 Expression e = new Expression("");
 Assert.assertFalse(e.isPrepared());
 }

 @Test
 public void testGetExpression() {
 Assert.assertEquals("(5 + 3) * (2 and 3)", expression.getExpression());
 }

 @Test
 public void testIsReservedFunction() {
 assertTrue(expression.isReservedFunction("and"));
 assertTrue(expression.isReservedFunction("or"));
 assertTrue(expression.isReservedFunction("="));
 assertTrue(expression.isReservedFunction(">="));
 assertTrue(expression.isReservedFunction("<="));
 assertTrue(expression.isReservedFunction(">"));
 assertTrue(expression.isReservedFunction("<"));
 assertTrue(expression.isReservedFunction("!="));
 assertTrue(expression.isReservedFunction("not"));
 assertTrue(expression.isReservedFunction("isempty"));
 assertTrue(expression.isReservedFunction("isnull"));
 assertTrue(expression.isReservedFunction("isfunction"));
 assertTrue(expression.isReservedFunction("abs"));
 assertTrue(expression.isReservedFunction("divide"));
 assertTrue(expression.isReservedFunction("multiply"));
 assertTrue(expression.isReservedFunction("plus"));
 assertTrue(expression.isReservedFunction("sqrt"));
 assertTrue(expression.isReservedFunction("subtract"));
 assertTrue(expression.isReservedFunction("power"));
 assertTrue(expression.isReservedFunction("powerchar"));
 assertTrue(expression.isReservedFunction("dateadd"));
 assertTrue(expression.isReservedFunction("datediff"));
 assertTrue(expression.isReservedFunction("length"));
 assertTrue(expression.isReservedFunction("substring"));
 assertTrue(expression.isReservedFunction("tolower"));
 assertTrue(expression.isReservedFunction("toupper"));
 assertTrue(expression.isReservedFunction("inlist"));
 assertTrue(expression.isReservedFunction("matches"));
 }

 @Test
 public void testIsLogicalFunction() {
 assertFalse(expression.isLogicalFunction("abs"));
 assertFalse(expression.isLogicalFunction("divide"));
 assertFalse(expression.isLogicalFunction("multiply"));
 assertFalse(expression.isLogicalFunction("plus"));
 assertFalse(expression.isLogicalFunction("sqrt"));
 assertFalse(expression.isLogicalFunction("subtract"));
 assertFalse(expression.isLogicalFunction("power"));
 assertFalse(expression.isLogicalFunction("powerchar"));
 assertFalse(expression.isLogicalFunction("dateadd"));
 assertFalse(expression.isLogicalFunction("datediff"));
 assertFalse(expression.isLogicalFunction("length"));
 assertFalse(expression.isLogicalFunction("substring"));
 assertFalse(expression.isLogicalFunction("tolower"));
 assertFalse(expression.isLogicalFunction("toupper"));
 assertFalse(expression.isLogicalFunction("inlist"));
 assertFalse(expression.isLogicalFunction("matches"));
 assertTrue(expression.isLogicalFunction("and"));
 assertTrue(expression.isLogicalFunction("or"));
 }

 @Test
 public void testIsMathFunction() {
 assertFalse(expression.isMathFunction("and"));
 assertFalse(expression.isMathFunction("or"));
 assertFalse(expression.isMathFunction("="));
 assertFalse(expression.isMathFunction(">="));
 assertFalse(expression.isMathFunction("<="));
 assertFalse(expression.isMathFunction(">"));
 assertFalse(expression.isMathFunction("<"));
 assertFalse(expression.isMathFunction("!"));
 assertFalse(expression.isMathFunction("not"));
 assertFalse(expression.isMathFunction("isempty"));
 assertFalse(expression.isMathFunction("isnull"));
 assertFalse(expression.isMathFunction("isfunction"));
 assertFalse(expression.isMathFunction("abs"));
 assertFalse(expression.isMathFunction("divide"));
 assertFalse(expression.isMathFunction("multiply"));
 assertFalse(expression.isMathFunction("plus"));
 assertFalse(expression.isMathFunction("sqrt"));
 assertFalse(expression.isMathFunction("subtract"));
 assertFalse(expression.isMathFunction("power"));
 assertFalse(expression.isMathFunction("powerchar"));
 assertFalse(expression.isMathFunction("dateadd"));
 assertFalse(expression.isMathFunction("datediff"));
 assertFalse(expression.isMathFunction("length"));
 assertFalse(expression.isMathFunction("substring"));
 assertFalse(expression.isMathFunction("tolower"));
 assertFalse(expression.isMathFunction("toupper"));
 assertFalse(expression.isMathFunction("inlist"));
 assertFalse(expression.isMathFunction("matches"));
 assertTrue(expression.isMathFunction("abs"));
 assertTrue(expression.isMathFunction("divide"));
 assertTrue(expression.isMathFunction("multiply"));
 assertTrue(expression.isMathFunction("plus"));
 assertTrue(expression.isMathFunction("sqrt"));
 assertTrue(expression.isMathFunction("subtract"));
 assertTrue(expression.isMathFunction("power"));
 assertTrue(expression.isMathFunction("powerchar"));
 }

 @Test
 public void testIsDateFunction() {
 assertFalse(expression.isDateFunction("and"));
 assertFalse(expression.isDateFunction("or"));
 assertFalse(expression.isDateFunction("="));
 assertFalse(expression.isDateFunction(">="));
 assertFalse(expression.isDateFunction("<="));
 assertFalse(expression.isDateFunction(">"));
 assertFalse(expression.isDateFunction("<"));
 assertFalse(expression.isDateFunction("!"));
 assertFalse(expression.isDateFunction("not"));
 assertFalse(expression.isDateFunction("isempty"));
 assertFalse(expression.isDateFunction("isnull"));
 assertFalse(expression.isDateFunction("isfunction"));
 assertFalse(expression.isDateFunction("abs"));
 assertFalse(expression.isDateFunction("divide"));
 assertFalse(expression.isDateFunction("multiply"));
 assertFalse(expression.isDateFunction("plus"));
 assertFalse(expression.isDateFunction("sqrt"));
 assertFalse(expression.isDateFunction("subtract"));
 assertFalse(expression.isDateFunction("power"));
 assertFalse(expression.isDateFunction("powerchar"));
 assertFalse(expression.isDateFunction("dateadd"));
 assertFalse(expression.isDateFunction("datediff"));
 assertFalse(expression.isDateFunction("length"));
 assertFalse(expression.isDateFunction("substring"));
 assertFalse(expression.isDateFunction("tolower"));
 assertFalse(expression.isDateFunction("toupper"));
 assertFalse(expression.isDateFunction("inlist"));
 assertFalse(expression.isDateFunction("matches"));
 assertFalse(expression.isDateFunction("inlist"));
 assertTrue(expression.isDateFunction("dateadd"));
 assertTrue(expression.isDateFunction("datediff"));
 }

 @Test
 public void testIsStringFunction() {
 assertFalse(expression.isStringFunction("and"));
 assertFalse(expression.isStringFunction("or"));
 assertFalse(expression.isStringFunction("="));
 assertFalse(expression.isStringFunction(">="));
 assertFalse(expression.isStringFunction("<="));
 assertFalse(expression.isStringFunction(">"));
 assertFalse(expression.isStringFunction("<"));
 assertFalse(expression.isStringFunction("!"));
 assertFalse(expression.isStringFunction("not"));
 assertFalse(expression.isStringFunction("isempty"));
 assertFalse(expression.isStringFunction("isnull"));
 assertFalse(expression.isStringFunction("isfunction"));
 assertFalse(expression.isStringFunction("abs"));
 assertFalse(expression.isStringFunction("divide"));
 assertFalse(expression.isStringFunction("multiply"));
 assertFalse(expression.isStringFunction("plus"));
 assertFalse(expression.isStringFunction("sqrt"));
 assertFalse(expression.isStringFunction("subtract"));
 assertFalse(expression.isStringFunction("power"));
 assertFalse(expression.isStringFunction("powerchar"));
 assertFalse(expression.isStringFunction("dateadd"));
 assertFalse(expression.isStringFunction("datediff"));
 assertFalse(expression.isStringFunction("length"));
 assertFalse(expression.isStringFunction("substring"));
 assertFalse(expression.isStringFunction("tolower"));
 assertFalse(expression.isStringFunction("toupper"));
 assertFalse(expression.isStringFunction("inlist"));
 assertFalse(expression.isStringFunction("matches"));
 assertTrue(expression.isStringFunction("length"));
 assertTrue(expression.isStringFunction("substring"));
 assertTrue(expression.isStringFunction("tolower"));
 assertTrue(expression.isStringFunction("toupper"));
 }

 @Test
 public void testGetExternalFunction() {
 List<FunctionExpression> functions = new ArrayList<>();
 functions.add(andFunction);
 Expression e = new Expression("(2 and 3)", functions);
 FunctionExpression and = e.getExternalFunction("and");
 assertTrue(and.equals(andFunction));
 }

 @Test
 public void testPrepare() {
 expression.prepare();
 Stack<String> expectedStack = new ExpressionStack();
 expectedStack.add("5");
 expectedStack.add("3");
 expectedStack.add("+");
 expectedStack.add("2");
 expectedStack.add("3");
 expectedStack.add("and");
 expression.doEvaluationStack();
 Stack<String> actualStack = expression.getStackCopy();
 assertEquals(expectedStack, actualStack);
 }

 @Test(expected = RuntimeException.class)
 public void testPrepareWithInvalidExpression() {
 expression = new Expression("(5 + 3) * (2 and 3) +");
 expression.prepare();
 }

 @Test
 public void testEvaluate() throws Exception {
 expression.prepare();
 Object result = expression.evaluate();
 assertEquals(15.0, result);
 }

}