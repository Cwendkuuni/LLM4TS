import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.FunctionExpression;
import org.javex.functions.LogicalFunction;
import org.javex.functions.MathFunction;
import org.javex.functions.DateFunction;
import org.javex.functions.StringFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 @Test
 public void testSimpleArithmeticExpression() {
 Expression e = new ArithmeticExpression("2 + 3");
 e.prepare();
 ExpressionStack stack = e.getStackCopy();
 assertEquals("2.0", stack.get(0));
 assertEquals("3.0", stack.get(1));
 assertEquals("+", stack.get(2));
 assertEquals("5.0", stack.get(3));
 }

 @Test
 public void testComplexArithmeticExpression() {
 Expression e = new ArithmeticExpression("3 * ( 2 + 5 )");
 e.prepare();
 ExpressionStack stack = e.getStackCopy();
 assertEquals("3.0", stack.get(0));
 assertEquals("2.0", stack.get(1));
 assertEquals("+", stack.get(2));
 assertEquals("5.0", stack.get(3));
 assertEquals("*", stack.get(4));
 assertEquals("3.0", stack.get(5));
 assertEquals("13.0", stack.get(6));
 }

 @Test
 public void testLogicalExpressionWithAnd() {
 Expression e = new LogicalExpression("a > 5 and b < 10");
 e.prepare();
 ExpressionStack stack = e.getStackCopy();
 // Assuming a and b are set up as external functions or variables
 FunctionExpression a = new VariableFunction("a");
 FunctionExpression b = new VariableFunction("b");
 e.setExternalFunctions(new ArrayList<>());
 e.setExternalFunctions(Arrays.asList(a, b));
 assertEquals(a, stack.get(0));
 assertEquals(">", stack.get(1));
 assertEquals(5, stack.get(2));
 assertEquals("and", stack.get(3));
 assertEquals(b, stack.get(4));
 assertEquals("<", stack.get(5));
 assertEquals(10, stack.get(6));
 }

 @Test
 public void testLogicalExpressionWithOr() {
 Expression e = new LogicalExpression("a > 5 or b < 10");
 e.prepare();
 ExpressionStack stack = e.getStackCopy();
 // Assuming a and b are set up as external functions or variables
 FunctionExpression a = new VariableFunction("a");
 FunctionExpression b = new VariableFunction("b");
 e.setExternalFunctions(new ArrayList<>());
 e.setExternalFunctions(Arrays.asList(a, b));
 assertEquals(a, stack.get(0));
 assertEquals(">", stack.get(1));
 assertEquals(5, stack.get(2));
 assertEquals("or", stack.get(3));
 // Assuming b is false
 stack.set(4, new BooleanExpression(false));
 assertEquals(true, e.evaluate());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidExpression() {
 Expression e = new Expression("a = b +");
 e.prepare();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingClosingParenthesis() {
 Expression e = new Expression("( a > b + c");
 e.prepare();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFunctionCall() {
 Expression e = new Expression("sqrt(a + b - )");
 e.prepare();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingOperandForFunctionCall() {
 Expression e = new Expression("sqrt()");
 e.prepare();
 }
}