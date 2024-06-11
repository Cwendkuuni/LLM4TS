import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;
 private ExpressionStack stack;

 @Before
 public void setUp() {
 expression = new Expression("1 + 2 * (3 + 4) * 5");
 stack = expression.getStackCopy();
 }

 @Test
 public void testIsReservedFunction() {
 // Test with reserved function symbol
 assertTrue(expression.isReservedFunction("and"));

 // Test with non-reserved function symbol
 assertFalse(expression.isReservedFunction("myFunction"));
 }

 @Test
 public void testIsLogicalFunction() {
 // Test with logical function symbol
 assertTrue(expression.isLogicalFunction("and"));

 // Test with non-logical function symbol
 assertFalse(expression.isLogicalFunction("sin"));
 }

 @Test
 public void testIsMathFunction() {
 // Test with math function symbol
 assertTrue(expression.isMathFunction("sqrt"));

 // Test with non-math function symbol
 assertFalse(expression.isMathFunction("abs"));
 }

 @Test
 public void testIsDateFunction() {
 // Test with date function symbol
 assertFalse(expression.isDateFunction("now"));

 // Test with non-date function symbol
 assertFalse(expression.isDateFunction("substring"));
 }

 @Test
 public void testIsStringFunction() {
 // Test with string function symbol
 assertTrue(expression.isStringFunction("toUpper"));

 // Test with non-string function symbol
 assertFalse(expression.isStringFunction("abs"));
 }

 @Test
 public void testIsFunction() {
 // Test with function symbol
 assertTrue(expression.isFunction("sqrt"));

 // Test with non-function symbol
 assertFalse(expression.isFunction("1"));
 }

 @Test
 public void testGetFunction() {
 // Test with valid function symbol
 FunctionExpression function = expression.getFunction("sqrt");
 assertEquals("sqrt", function.getSimbol());

 // Test with invalid function symbol
 FunctionExpression function2 = expression.getFunction("myFunction");
 assertNull(function2);
 }

 @Test
 public void testIsExternalFunction() {
 // Test with external function symbol
 Expression externalExpression = new Expression("myFunction(1, 2)");
 assertTrue(externalExpression.isExternalFunction("myFunction"));

 // Test with non-external function symbol
 assertFalse(expression.isExternalFunction("sqrt"));
 }

 @Test
 public void testTokenize() {
 // Test with valid expression
 assertEquals("[1, +, 2, *, (, 3, +, 4, ), *, 5]", expression.tokenizeToString());

 // Test with invalid expression
 expression = new Expression("1 + 2 * 3 + (4");
 assertEquals("[1, +, 2, *, 3, +, (, 4]", expression.tokenizeToString());
 }

 @Test
 public void testDoEvaluationStack() {
 // Test with valid expression
 Expression evaluatedExpression = new Expression(stack.toString());
 assertEquals(15d, evaluatedExpression.evaluate(), 0.01);

 // Test with invalid expression
 expression = new Expression("1 + 2 * )");
 try {
 evaluatedExpression = new Expression(stack.toString());
 evaluatedExpression.evaluate();
 fail("Expected EvaluationException");
 } catch (Exception e) {
 assertTrue(e instanceof EvaluationException);
 }
 }

 // Add more test cases as needed
}