import org.javex.Expression;
import org.javex.utils.JavexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;

 @Test
 public void testGetExternalFunctions() {
 expression = new Expression("expressionWithFunctions()");
 List externalFunctions = expression.getExternalFunctions();
 assertTrue(externalFunctions.isEmpty());
 }

 @Test
 public void testExpressionWithFunctions() {
 expression = new Expression("expressionWithFunctions() { function1(); function2(); }");
 List externalFunctions = expression.getExternalFunctions();
 assertEquals(2, externalFunctions.size());
 assertEquals("function1", externalFunctions.get(0).getSimbol());
 assertEquals("function2", externalFunctions.get(1).getSimbol());
 }

 @Test
 public void testIsFunction() {
 expression = new Expression("expressionWithFunctions()");
 assertTrue(expression.isFunction("function1"));
 assertTrue(expression.isFunction("function2"));
 assertFalse(expression.isFunction("notAFunction"));
 }

 @Test
 public void testIsLogicalFunction() {
 expression = new Expression("expressionWithFunctions()");
 assertFalse(expression.isLogicalFunction("function1"));
 assertFalse(expression.isLogicalFunction("function2"));
 assertFalse(expression.isLogicalFunction("notALogicalFunction"));
 expression = new Expression("expressionWithLogicalFunctions() { and(1, 2); or(1, 2); }");
 assertTrue(expression.isLogicalFunction("and"));
 assertTrue(expression.isLogicalFunction("or"));
 }

 @Test
 public void testIsMathFunction() {
 expression = new Expression("expressionWithFunctions()");
 assertFalse(expression.isMathFunction("function1"));
 assertFalse(expression.isMathFunction("function2"));
 assertFalse(expression.isMathFunction("notAMathFunction"));
 expression = new Expression("expressionWithMathFunctions() { abs(1); sqrt(4); }");
 assertTrue(expression.isMathFunction("abs"));
 assertTrue(expression.isMathFunction("sqrt"));
 }

 @Test
 public void testIsDateFunction() {
 expression = new Expression("expressionWithFunctions()");
 assertFalse(expression.isDateFunction("function1"));
 assertFalse(expression.isDateFunction("function2"));
 assertFalse(expression.isDateFunction("notADateFunction"));
 expression = new Expression("expressionWithDateFunctions() { dateAdd(date1, interval '1 day'); }");
 assertTrue(expression.isDateFunction("dateAdd"));
 }

 @Test
 public void testIsStringFunction() {
 expression = new Expression("expressionWithFunctions()");
 assertFalse(expression.isStringFunction("function1"));
 assertFalse(expression.isStringFunction("function2"));
 assertFalse(expression.isStringFunction("notAStringFunction"));
 expression = new Expression("expressionWithStringFunctions() { toUpper(text1); }");
 assertTrue(expression.isStringFunction("toUpper"));
 }

 @Test
 public void testDoEvaluationStack() {
 expression = new Expression("expressionWithFunctions()");
 expression.prepare();
 assertFalse(expression.prepared);
 expression.doEvaluationStack();
 assertTrue(expression.prepared);
 }

 @Test(expected = Exception.class)
 public void testDoEvaluationStackWithException() {
 expression = new Expression("expressionWithInvalidFunctions()");
 expression.prepare();
 expression.doEvaluationStack();
 fail("Exception expected but not thrown");
 }

 @Test
 public void testGetStackCopy() {
 expression = new Expression("expressionWithFunctions()");
 expression.prepare();
 ExpressionStack stack = expression.getStackCopy();
 assertNotNull(stack);
 assertEquals(expression.stack.size(), stack.size());
 for (int i = 0; i < expression.stack.size(); i++) {
 assertEquals(expression.stack.get(i), stack.get(i));
 }
 }

 @Test
 public void testEvaluate() {
 expression = new Expression("expressionWithValidFunctions() { and(or(1, 2), not(3)); }");
 expression.prepare();
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 fail("Exception thrown while evaluating expression");
 }
 assertTrue(result instanceof Boolean);
 }

 @Test
 public void testEvaluateWithInvalidFunctions() {
 expression = new Expression("expressionWithInvalidFunctions() { functionNotDefined(); }");
 expression.prepare();
 try {
 expression.evaluate();
 fail("Exception not thrown while evaluating invalid expression");
 } catch (Exception e) {
 assertTrue(e.getMessage().contains("FunctionFunctionNotDefined is not a valid function"));
 }
 }

 @Test
 public void testEvaluateWithReservedFunction() {
 expression = new Expression("expressionWithReservedFunction() { and(1, 2); }");
 expression.prepare();
 try {
 expression.evaluate();
 fail("Exception not thrown while evaluating expression with reserved word as function name");
 } catch (Exception e) {
 assertTrue(e.getMessage().contains("Invalid token 'and'"));
 }
 }

 @Test
 public void testEvaluateWithInvalidCharacters() {
 expression = new Expression("expressionWithInvalidCharacters()");
 expression.prepare();
 try {
 expression.evaluate();
 fail("Exception not thrown while evaluating expression with invalid characters");
 } catch (Exception e) {
 assertTrue(e.getMessage().contains("Invalid token ''"));
 }
 }

 @Test
 public void testEvaluateWithMismatchedParentheses() {
 expression = new Expression("expressionWithMismatchedParentheses() { (1 + 2); }");
 expression.prepare();
 try {
 expression.evaluate();
 fail("Exception not thrown while evaluating expression with mismatched parentheses");
 } catch (Exception e) {
 assertTrue(e.getMessage().contains("Mismatched parentheses"));
 }
 }

 @Test
 public void testEvaluateWithEmptyExpression() {
 expression = new Expression("");
 expression.prepare();
 try {
 expression.evaluate();
 fail("Exception not thrown while evaluating empty expression");
 } catch (Exception e) {
 assertTrue(e.getMessage().contains("Empty expression"));
 }
 }

 @Test
 public void testEvaluateWithNumber() {
 expression = new Expression("5");
 expression.prepare();
 try {
 Object result = expression.evaluate();
 assertEquals(5.0, result);
 } catch (Exception e) {
 fail("Exception thrown while evaluating number");
 }
 }

 @Test
 public void testEvaluateWithVariable() {
 expression = new Expression("x");
 expression.prepare();
 expression.setVariable("x", 5);
 try {
 Object result = expression.evaluate();
 assertEquals(5.0, result);
 } catch (Exception e) {
 fail("Exception thrown while evaluating variable");
 }
 }

 @Test
 public void testEvaluateWithStringVariable() {
 expression = new Expression("text");
 expression.prepare();
 expression.setVariable("text", "Hello, World!");
 try {
 Object result = expression.evaluate();
 assertEquals("Hello, World!", result);
 } catch (Exception e) {
 fail("Exception thrown while evaluating string variable");
 }
 }

 @Test
 public void testEvaluateWithStringFunction() {
 expression = new Expression("expressionWithStringFunctions() { toUpper(text1); }");
 expression.prepare();
 expression.setVariable("text1", "Hello, World!");
 try {
 Object result = expression.evaluate();
 assertEquals("HELLO, WORLD!", result);
 } catch (Exception e) {
 fail("Exception thrown while evaluating string function");
 }
 }

 @Test
 public void testEvaluateWithLogicalFunction() {
 expression = new Expression("expressionWithLogicalFunctions() { and(1, 2); }");
 expression.prepare();
 Object[] values = {true, false};
 for (boolean value1 : values) {
 for (boolean value2 : values) {
 try {
 Object result = expression.evaluate();
 assertEquals(value1 && value2, result);
 } catch (Exception e) {
 fail("Exception thrown while evaluating logical function");
 }
 expression.setVariable("x", value1);
 expression.setVariable("y", value2);
 }
 }
 }

 @Test
 public void testEvaluateWithMathFunction() {
 expression = new Expression("expressionWithMathFunctions() { abs(-3); }");
 expression.prepare();
 try {
 Object result = expression.evaluate();
 assertEquals(3.0, result);
 } catch (Exception e) {
 fail("Exception thrown while evaluating math function");
 }
 }
}