import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setup() {
 expression = new Expression("(5 > 3) and (7 < 10)");
 expression.prepare();
 }

 @Test
 public void testSimpleLogicalExpression() {
 final boolean result = expression.evaluate();
 assertTrue(result);
 }

 @Test
 public void testComplexLogicalExpression() {
 expression = new Expression("(5 < 3) or ((7 > 10) and (3 > 2))");
 expression.prepare();
 final boolean result = expression.evaluate();
 assertFalse(result);
 }

 @Test(expected = Exception.class)
 public void testInvalidExpression() {
 expression = new Expression("5 + ( )");
 expression.prepare();
 }

 @Test(expected = Exception.class)
 public void testInvalidFunction() {
 expression = new Expression("invalidFunction(5, 3)");
 expression.prepare();
 }

 @Test
 public void testExternalFunction() {
 final Expression externalFunctionExpression = new Expression("sqrt(25)");
 expression = new Expression("sqrt(25) + 3");
 expression.setExternalFunctions(Arrays.asList(externalFunctionExpression));
 expression.prepare();
 final double result = (Double) expression.evaluate();
 assertEquals(8.0, result, 0.001);
 }

 @Test
 public void testPrecedence() {
 expression = new Expression("2 + 3 * 4");
 expression.prepare();
 final int result = (Integer) expression.evaluate();
 assertEquals(10, result);
 }

 @Test
 public void testEmptyString() {
 expression = new Expression("");
 expression.prepare();
 final Object result = expression.evaluate();
 assertNull(result);
 }

 @Test
 public void testNullInput() {
 expression = new Expression("null");
 expression.prepare();
 final Object result = expression.evaluate();
 assertNull(result);
 }

 @Test(expected = Exception.class)
 public void testDivisionByZero() {
 expression = new Expression("10 / 0");
 expression.prepare();
 }

 @Test
 public void testNestedParentheses() {
 expression = new Expression("(5 * 3) + (4 + 2)");
 expression.prepare();
 final int result = (Integer) expression.evaluate();
 assertEquals(23, result);
 }

 @Test
 public void testNegativeNumber() {
 expression = new Expression("-5");
 expression.prepare();
 final int result = (Integer) expression.evaluate();
 assertEquals(-5, result);
 }
}