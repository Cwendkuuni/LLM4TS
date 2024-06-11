import org.junit.Before;
import org.junit.Test;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;
 private ExpressionStack stack;

 @Before
 public void setup() {
 expression = new Expression("(2 + 3) * 4");
 stack = expression.getStackCopy();
 }

 @Test
 public void testIsReservedFunction() {
 // Test case for isReservedFunction method
 assertTrue(expression.isReservedFunction("and"));
 }

 @Test
 public void testIsLogicalFunction() {
 // Test case for isLogicalFunction method
 assertFalse(expression.isLogicalFunction("sin"));
 }

 @Test
 public void testIsMathFunction() {
 // Test case for isMathFunction method
 assertTrue(expression.isMathFunction("sqrt"));
 }

 @Test
 public void testIsDateFunction() {
 // Test case for isDateFunction method
 assertFalse(expression.isDateFunction("abs"));
 }

 @Test
 public void testIsStringFunction() {
 // Test case for isStringFunction method
 assertFalse(expression.isStringFunction("abs"));
 }

 @Test
 public void testEvaluateSimpleExpression() {
 // Test case for evaluate method with simple expression
 assertEquals(14, expression.evaluate());
 }

 @Test(expected = Exception.class)
 public void testEvaluateInvalidExpression() {
 // Test case for evaluate method with invalid expression
 expression = new Expression("2 + (3 * 4) + sin(5)");
 try {
 expression.evaluate();
 fail("Expected Exception");
 } catch (Exception e) {
 // Expected exception, test continues
 }
 }

 @Test
 public void testGetFunction() {
 // Test case for getFunction method
 FunctionExpression function = expression.getFunction("*");
 assertEquals("*", function.getSimbol());
 }

 @Test
 public void testIsFunction() {
 // Test case for isFunction method
 assertTrue(expression.isFunction("*"));
 }

 @Test
 public void testIsExternalFunction() {
 // Test case for isExternalFunction method
 assertFalse(expression.isExternalFunction("*"));
 }

 @Test
 public void testTokenize() {
 // Test case for tokenize method
 Expression tokenizedExpression = new Expression(expression.getExpression());
 assertEquals(stack, tokenizedExpression.getStackCopy());
 }

 @Test
 public void testPrepare() {
 // Test case for prepare method
 expression.prepare();
 Expression preparedExpression = new Expression(expression.getExpression());
 assertEquals(stack, preparedExpression.getStackCopy());
 }

 @Test
 public void testGetExternalFunctions() {
 // Test case for getExternalFunctions method
 Expression externalFunctionsExpression = new Expression("myFunction(5)");
 List<FunctionExpression> externalFunctions = externalFunctionsExpression.getExternalFunctions();
 assertNotNull(externalFunctions);
 }
}