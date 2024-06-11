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
import org.javex.List;
import org.junit.Test;

public class ExpressionTest {

 private Expression prepareExpression(String expression) {
 Expression expr = new Expression(expression);
 expr.prepare();
 return expr;
 }

 @Test
 public void testIsReservedFunction() {
 Expression expr = new Expression("");
 assertTrue(expr.isReservedFunction("and"));
 assertTrue(expr.isReservedFunction("or"));
 assertFalse(expr.isReservedFunction("test"));
 }

 @Test
 public void testIsLogicalFunction() {
 Expression expr = new Expression("");
 assertFalse(expr.isLogicalFunction("add"));
 assertTrue(expr.isLogicalFunction("and"));
 }

 @Test
 public void testIsMathFunction() {
 Expression expr = new Expression("");
 assertFalse(expr.isMathFunction("and"));
 assertTrue(expr.isMathFunction("abs"));
 }

 @Test
 public void testIsDateFunction() {
 Expression expr = new Expression("");
 assertFalse(expr.isDateFunction("and"));
 assertTrue(expr.isDateFunction("dateadd"));
 }

 @Test
 public void testIsStringFunction() {
 Expression expr = new Expression("");
 assertFalse(expr.isStringFunction("and"));
 assertTrue(expr.isStringFunction("length"));
 }

 @Test
 public void testGetFunction() {
 Expression expr = new Expression("");
 FunctionExpression function = new AndFunction();
 expr.putFunction(function, Expression.reservedFunctions);
 assertEquals(function, expr.getFunction("and"));
 }

 @Test
 public void testIsFunction() {
 Expression expr = new Expression("");
 assertTrue(expr.isFunction("and"));
 assertFalse(expr.isFunction("test"));
 }

 @Test
 public void testIsExternalFunction() {
 Expression expr = new Expression("");
 FunctionExpression function = new CustomFunction();
 List externalFunctions = new ArrayList();
 externalFunctions.add(function);
 expr.setExternalFunctions(externalFunctions);
 assertTrue(expr.isExternalFunction("customFunction"));
 assertFalse(expr.isExternalFunction("and"));
 }

 @Test
 public void testEvaluateSimpleExpression() throws Exception {
 Expression expr = prepareExpression("1 + 2");
 assertEquals(3, expr.evaluate());
 }

 @Test
 public void testEvaluateComplexExpression() throws Exception {
 Expression expr = prepareExpression("((1 + 2) * 3) - 4");
 assertEquals(5, expr.evaluate());
 }

 @Test
 public void testEvaluateLogicalExpression() throws Exception {
 Expression expr = prepareExpression("((1 > 2) and (2 > 1))");
 assertEquals(false, expr.evaluate());
 }

 @Test
 public void testEvaluateMathExpression() throws Exception {
 Expression expr = prepareExpression("sqrt(64)");
 assertEquals(8.0, expr.evaluate(), 0.001);
 }

 @Test
 public void testEvaluateDateExpression() throws Exception {
 Expression expr = prepareExpression("dateadd(day, 1, '2022-01-01')");
 assertEquals("2022-01-02", expr.evaluate().toString());
 }

 @Test
 public void testEvaluateStringExpression() throws Exception {
 Expression expr = prepareExpression("length('test')");
 assertEquals(5, expr.evaluate().intValue());
 }

 @Test
 public void testEvaluateFunctionWithArguments() throws Exception {
 Expression expr = prepareExpression("abs(-3)");
 assertEquals(3, expr.evaluate().intValue());
 }

 @Test
 public void testEvaluateFunctionWithMultipleArguments() throws Exception {
 Expression expr = prepareExpression("pow(2, 3)");
 assertEquals(8.0, expr.evaluate(), 0.001);
 }

 @Test
 public void testEvaluateCustomFunction() throws Exception {
 Expression expr = prepareExpression("customFunction(1, 2)");
 assertEquals(3, expr.evaluate().intValue());
 }

 @Test
 public void testEvaluateExpressionWithVariables() throws Exception {
 Expression expr = new Expression("x > y");
 expr.setVariableValue("x", 5);
 expr.setVariableValue("y", 3);
 assertTrue(expr.evaluate());
 }

 @Test
 public void testEvaluateExpressionWithNestedFunctions() throws Exception {
 Expression expr = prepareExpression("abs(sqrt(64))");
 assertEquals(8.0, expr.evaluate(), 0.001);
 }

 @Test
 public void testEvaluateExpressionWithComplexTokenization() throws Exception {
 Expression expr = prepareExpression("(1 + 2) * (3 + 4)");
 assertEquals(11, expr.evaluate().intValue());
 }

 @Test
 public void testEvaluateExpressionWithParentheses() throws Exception {
 Expression expr = prepareExpression("(1 + 2) * (3 + 4)");
 assertEquals(11, expr.evaluate().intValue());
 }

 @Test
 public void testEvaluateExpressionWithSpaces() throws Exception {
 Expression expr = prepareExpression("1 + 2");
 assertEquals(3, expr.evaluate().intValue());
 }

 @Test
 public void testEvaluateExpressionWithQuotes() throws Exception {
 Expression expr = prepareExpression("'1' + '2'");
 assertEquals("12", expr.evaluate().toString());
 }

 @Test
 public void testEvaluateExpressionWithReservedWords() throws Exception {
 Expression expr = prepareExpression("and(1, 2)");
 assertEquals(true, expr.evaluate());
 }

 @Test
 public void testEvaluateExpressionWithCustomFunctionsAndVariables() throws Exception {
 Expression expr = prepareExpression("customFunction(x, y) and x > y");
 expr.setVariableValue("x", 5);
 expr.setVariableValue("y", 3);
 assertTrue(expr.evaluate());
 }

 @Test
 public void testEvaluateExpressionWithComplexFunctionsAndVariables() throws Exception {
 Expression expr = prepareExpression("pow(x, 2) + pow(y, 2)");
 expr.setVariableValue("x", 5);
 expr.setVariableValue("y", 3);
 assertEquals(44.0, expr.evaluate(), 0.001);
 }

 @Test
 public void testEvaluateExpressionWithInvalidFunction() throws Exception {
 Expression expr = new Expression("invalidFunction(x)");
 assertNull(expr.evaluate());
 }

 @Test
 public void testEvaluateExpressionWithMissingFunctionArgument() throws Exception {
 Expression expr = new Expression("abs()");
 assertNull(expr.evaluate());
 }

 @Test
 public void testEvaluateExpressionWithInvalidVariableName() throws Exception {
 Expression expr = new Expression("x + y = 5");
 assertNull(expr.evaluate());
 }

 @Test
 public void testEvaluateExpressionWithEmptyString() throws Exception {
 Expression expr = new Expression("");
 assertEquals(0, expr.evaluate().intValue());
 }

 @Test
 public void testEvaluateExpressionWithNullExpression() throws Exception {
 Expression expr = null;
 assertNull(expr.evaluate());
 }
}