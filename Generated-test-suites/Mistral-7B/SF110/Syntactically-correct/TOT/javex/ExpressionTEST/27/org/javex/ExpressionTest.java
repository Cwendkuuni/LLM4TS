import org.javex.Expression;
import org.javex.ExpressionStack;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setUp() {
 expression = new Expression("(2 + 3) * 4");
 expression.prepare();
 }

 @Test
 public void testEvaluateSimpleAddition() {
 ExpressionStack expectedResult = new ExpressionStack();
 expectedResult.push("6");
 ExpressionStack actualResult = new ExpressionStack(expression.getStackCopy());
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testEvaluateComplexExpression() {
 ExpressionStack expectedResult = new ExpressionStack();
 expectedResult.push("24");
 ExpressionStack actualResult = new ExpressionStack(expression.getStackCopy());
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = Exception.class)
 public void testEvaluateInvalidExpression() {
 Expression invalidExpression = new Expression("2 + 3 4");
 invalidExpression.prepare();
 }

 @Test
 public void testIsFunction() {
 assertTrue(expression.isFunction("*"));
 assertTrue(expression.isFunction("-"));
 assertTrue(expression.isFunction("("));
 assertTrue(expression.isFunction("+"));
 assertFalse(expression.isFunction("x"));
 }

 @Test
 public void testIsLogicalFunction() {
 assertFalse(expression.isLogicalFunction("*"));
 assertFalse(expression.isLogicalFunction("-"));
 assertFalse(expression.isLogicalFunction("("));
 assertFalse(expression.isLogicalFunction("+"));
 assertFalse(expression.isLogicalFunction("x"));
 assertFalse(expression.isLogicalFunction("and"));
 assertTrue(expression.isLogicalFunction("or"));
 }

 @Test
 public void testIsMathFunction() {
 assertFalse(expression.isMathFunction("and"));
 assertFalse(expression.isMathFunction("or"));
 assertFalse(expression.isMathFunction("("));
 assertFalse(expression.isMathFunction(")"));
 assertFalse(expression.isMathFunction("x"));
 assertTrue(expression.isMathFunction("*"));
 assertTrue(expression.isMathFunction("-"));
 assertTrue(expression.isMathFunction("+"));
 assertTrue(expression.isMathFunction("Sqrt"));
 }

 @Test
 public void testIsDateFunction() {
 assertFalse(expression.isDateFunction("*"));
 assertFalse(expression.isDateFunction("-"));
 assertFalse(expression.isDateFunction("+"));
 assertFalse(expression.isDateFunction("Sqrt"));
 assertFalse(expression.isDateFunction("and"));
 assertFalse(expression.isDateFunction("or"));
 assertFalse(expression.isDateFunction("("));
 assertFalse(expression.isDateFunction(")"));
 assertFalse(expression.isDateFunction("x"));
 assertFalse(expression.isDateFunction("DateAdd"));
 assertTrue(expression.isDateFunction("DateDiff"));
 }

 @Test
 public void testIsStringFunction() {
 assertFalse(expression.isStringFunction("*"));
 assertFalse(expression.isStringFunction("-"));
 assertFalse(expression.isStringFunction("+"));
 assertFalse(expression.isStringFunction("Sqrt"));
 assertFalse(expression.isStringFunction("and"));
 assertFalse(expression.isStringFunction("or"));
 assertFalse(expression.isStringFunction("("));
 assertFalse(expression.isStringFunction(")"));
 assertFalse(expression.isStringFunction("x"));
 assertTrue(expression.isStringFunction("Length"));
 assertTrue(expression.isStringFunction("SubString"));
 assertTrue(expression.isStringFunction("ToUpper"));
 assertTrue(expression.isStringFunction("ToLower"));
 }

 @Test
 public void testGetExternalFunction() {
 FunctionExpression externalFunction = new AddFunction();
 expression.setExternalFunctions(Arrays.asList(externalFunction));
 Expression externalExpression = new Expression("x + y");
 externalExpression.setExternalFunctions(Arrays.asList(externalFunction));
 externalExpression.prepare();
 FunctionExpression result = expression.getFunction("x + y");
 assertEquals(externalFunction, result);
 }

 @Test
 public void testGetReservedFunction() {
 FunctionExpression result = Expression.getReservedFunctions("*");
 assertTrue(result instanceof MultiplyFunction);
 }
}