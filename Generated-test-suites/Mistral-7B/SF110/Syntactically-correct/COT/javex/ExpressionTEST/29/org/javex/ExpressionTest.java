package org.javex.expression;

import static org.junit.Assert.*;
import org.javex.Expression;
import org.javex.functions.FunctionExpression;
import org.javex.utils.JavexUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setUp() {
 expression = new Expression("(1 + 2) and (3 < 4) and (5 > 2) and not (7 = 7)");
 expression.prepare();
 }

 @Test
 public void testGetExternalFunctions() {
 List<FunctionExpression> externalFunctions = expression.getExternalFunctions();
 assertTrue(externalFunctions.isEmpty());
 }

 @Test
 public void testConstructor() {
 assertNotNull(expression);
 assertEquals("(1 + 2) and (3 < 4) and (5 > 2) and not (7 = 7)", expression.expression);
 }

 @Test
 public void testDoEvaluationStack() {
 Stack<String> operands = new Stack<>();
 ExpressionStack values = new ExpressionStack();
 ExpressionStack expectedValues = new ExpressionStack(Arrays.asList(
 "1", "2", "+", "3", "<", "4", "<", "5", ">", "2", "not", "7", "=", "7"
 ));

 expression.doEvaluationStack();

 for (String stackElement : expression.stack) {
 operands.push(stackElement);
 String valuesElement = values.pop();
 assertEquals(valuesElement, operands.pop());
 }

 assertFalse(operands.isEmpty());
 assertEquals(expectedValues, values);
 }

 @Test
 public void testGetReservedFunctions() {
 FunctionExpression andFunction = Expression.getReservedFunctions("and");
 FunctionExpression orFunction = Expression.getReservedFunctions("or");

 assertTrue(andFunction instanceof AndFunction);
 assertTrue(orFunction instanceof OrFunction);
 }

 @Test
 public void testIsFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();
 FunctionExpression invalidFunction = new Object();

 assertTrue(expression.isFunction("and"));
 assertTrue(expression.isFunction("or"));
 assertFalse(expression.isFunction("invalid"));
 assertFalse(expression.isFunction(andFunction));
 assertFalse(expression.isFunction(orFunction));
 }

 @Test
 public void testIsLogicalFunction() {
 FunctionExpression andFunction = new AndFunction();
 FunctionExpression orFunction = new OrFunction();

 assertTrue(expression.isLogicalFunction("and"));
 assertTrue(expression.isLogicalFunction("or"));
 assertFalse(expression.isLogicalFunction("+"));
 assertFalse(expression.isLogicalFunction(new AddFunction()));
 }

 @Test
 public void testIsMathFunction() {
 FunctionExpression addFunction = new AddFunction();
 FunctionExpression subtractFunction = new SubtractFunction();

 assertFalse(expression.isMathFunction("and"));
 assertFalse(expression.isMathFunction("or"));
 assertTrue(expression.isMathFunction("+"));
 assertTrue(expression.isMathFunction("-"));
 assertFalse(expression.isMathFunction("not"));
 assertFalse(expression.isMathFunction(andFunction));
 assertFalse(expression.isMathFunction(orFunction));
 assertTrue(expression.isMathFunction(addFunction));
 assertTrue(expression.isMathFunction(subtractFunction));
 }

 @Test
 public void testIsDateFunction() {
 FunctionExpression dateAddFunction = new DateAddFunction();
 FunctionExpression dateDiffFunction = new DateDiffFunction();

 assertFalse(expression.isDateFunction("and"));
 assertFalse(expression.isDateFunction("or"));
 assertFalse(expression.isDateFunction("+"));
 assertFalse(expression.isDateFunction("-"));
 assertFalse(expression.isDateFunction("not"));
 assertFalse(expression.isDateFunction(andFunction));
 assertFalse(expression.isDateFunction(orFunction));
 assertTrue(expression.isDateFunction(dateAddFunction));
 assertTrue(expression.isDateFunction(dateDiffFunction));
 }

 @Test
 public void testIsStringFunction() {
 FunctionExpression lengthFunction = new LengthFunction();
 FunctionExpression subStringFunction = new SubStringFunction();

 assertFalse(expression.isStringFunction("and"));
 assertFalse(expression.isStringFunction("or"));
 assertFalse(expression.isStringFunction("+"));
 assertFalse(expression.isStringFunction("-"));
 assertFalse(expression.isStringFunction("not"));
 assertFalse(expression.isStringFunction(andFunction));
 assertFalse(expression.isStringFunction(orFunction));
 assertTrue(expression.isStringFunction(lengthFunction));
 assertTrue(expression.isStringFunction(subStringFunction));
 }

 @Test
 public void testIsQuotedString() {
 Expression quotedStringExpression = new Expression("\"Hello World\"");
 quotedStringExpression.prepare();
List<String> expectedValues = new ArrayList<>(Arrays.asList("Hello World"));
assertEquals(expectedValues, quotedStringExpression.stack);
}

 @Test(expected = AssertionError.class)
 public void testInvalidExpression() {
 Expression invalidExpression = new Expression("invalid");
 invalidExpression.prepare();
 fail("Invalid expression should throw an exception");
 }

 @Test
 public void testPrepare() {
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }

 assertTrue(result instanceof Boolean);
 }

 @Test
 public void testEvaluate() throws Exception {
 Object result = expression.evaluate();
 assertTrue((boolean) result);
 }

}