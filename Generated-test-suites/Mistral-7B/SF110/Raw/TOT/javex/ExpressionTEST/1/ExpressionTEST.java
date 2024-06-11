import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;
 private ExpressionStack stack;
 private List<FunctionExpression> externalFunctions;

 @Before
 public void setup() {
 expression = new Expression("1 + 2 * (3 + 4) and (5 > 4) and (6 < 7)");
 stack = expression.getStackCopy();
 externalFunctions = new ArrayList<>();
 }

 @Test
 public void testIsFunction() {
 final String[] functions = {"+", "-", "*", "/", "and", "or", "(", ")"};
 for (final String function : functions) {
 assertTrue(expression.isFunction(function));
 }
 }

 @Test
 public void testIsReservedFunction() {
 final String[] reservedFunctions = {"and", "or", "not", "is", "isnot"};
 for (final String function : reservedFunctions) {
 assertTrue(expression.isReservedFunction(function));
 }
 }

 @Test
 public void testIsLogicalFunction() {
 assertTrue(expression.isLogicalFunction("and"));
 assertFalse(expression.isLogicalFunction("+"));
 }

 @Test
 public void testIsMathFunction() {
 assertTrue(expression.isMathFunction("+"));
 assertFalse(expression.isMathFunction("and"));
 }

 @Test
 public void testIsDateFunction() {
 assertFalse(expression.isDateFunction("and"));
 }

 @Test
 public void testIsStringFunction() {
 assertFalse(expression.isStringFunction("and"));
 }

 @Test
 public void testGetExternalFunction() {
 final FunctionExpression function = new FunctionExpression("myFunction", FunctionExpression.CUSTOM);
 externalFunctions.add(function);
 expression.setExternalFunctions(externalFunctions);
 assertEquals(function, expression.getExternalFunction("myFunction"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetExternalFunctionInvalid() {
 expression.setExternalFunctions(externalFunctions);
 assertNull(expression.getExternalFunction("invalidFunction"));
 }

 @Test
 public void testPrepare() throws Exception {
 expression.prepare();
 Stack<String> expectedStack = new ExpressionStack();
 expectedStack.push("1");
 expectedStack.push("2");
 expectedStack.push("*");
 expectedStack.push("3");
 expectedStack.push("4");
 expectedStack.push("+");
 expectedStack.push("5");
 expectedStack.push(">");
 expectedStack.push("4");
 expectedStack.push("and");
 Stack<String> actualStack = new ExpressionStack();
 for (int i = 0; i < stack.size(); ++i) {
 actualStack.add(stack.get(i));
 }
 assertEquals(expectedStack, actualStack);
 }

 @Test(expected = Exception.class)
 public void testPrepareInvalidInput() {
 expression.prepare();
 fail("Preparing an invalid input should throw an exception");
 }

 @Test
 public void testEvaluateSimpleExpression() throws Exception {
 expression = new Expression("5 + 3");
 expression.prepare();
 assertEquals(8.0, expression.evaluate(), 0.0);
 }

 @Test
 public void testEvaluateComplexExpression() throws Exception {
 expression = new Expression("1 + 2 * (3 + 4) and (5 > 4) and (6 < 7)");
 expression.prepare();
 assertTrue(expression.evaluate());
 }

 @Test
 public void testEvaluateInvalidExpression() throws Exception {
 expression = new Expression("a + b");
 expression.prepare();
 assertFalse(expression.evaluate());
 }

 @Test(expected = Exception.class)
 public void testEvaluateWithInvalidFunction() throws Exception {
 expression = new Expression("sqrt(-1)");
 expression.prepare();
 fail("Evaluating an expression with an invalid function should throw an exception");
 }

 @Test
 public void testEvaluateWithInvalidInput() throws Exception {
 expression = new Expression("1 + a");
 expression.prepare();
 assertFalse(expression.evaluate());
 }
}