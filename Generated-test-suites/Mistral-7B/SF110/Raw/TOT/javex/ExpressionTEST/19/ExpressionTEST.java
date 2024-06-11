import org.javex.Expression;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AbsFunction;
import org.javex.functions.math.DivideFunction;
import org.javex.functions.math.MultiplyFunction;
import org.javex.functions.math.PlusFunction;
import org.javex.functions.math.SqrtFunction;
import org.javex.functions.math.SubtractFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testSimpleAddition() {
 Expression e = createExpression("1 + 2");
 Object result = e.evaluate();
 assertEquals(3, result);
 }

 @Test
 public void testComplexAddition() {
 Expression e = createExpression("5 + 2 * (3 + 4)");
 Object result = e.evaluate();
 assertEquals(14, result);
 }

 @Test
 public void testSubtraction() {
 Expression e = createExpression("5 - 2");
 Object result = e.evaluate();
 assertEquals(3, result);
 }

 @Test
 public void testMultiplication() {
 Expression e = createExpression("5 * 2");
 Object result = e.evaluate();
 assertEquals(10, result);
 }

 @Test
 public void testDivision() {
 Expression e = createExpression("5 / 2");
 Object result = e.evaluate();
 assertEquals(2.5, result);
 }

 @Test
 public void testSquareRoot() {
 Expression e = createExpression("SQRT(16)");
 Object result = e.evaluate();
 assertEquals(4.0, result);
 }

 @Test
 public void testAbsoluteValue() {
 Expression e = createExpression("ABS(-3)");
 Object result = e.evaluate();
 assertEquals(3.0, result);
 }

 @Test
 public void testLogicalAnd() {
 Expression e1 = new AndFunction();
 Expression e2 = createExpression("1 = 1");
 Expression e = new Expression("" + e1.getSimbol() + " " + e2.getExpression());
 Object result = e.evaluate();
 assertTrue(result);
 }

 @Test
 public void testLogicalOr() {
 Expression e1 = new OrFunction();
 Expression e2 = createExpression("1 = 1");
 Expression e3 = createExpression("1 = 2");
 Expression e = new Expression("" + e1.getSimbol() + " " + e2.getExpression() + " " + e3.getExpression());
 Object result = e.evaluate();
 assertTrue(result);
 }

 @Test(expected = Exception.class)
 public void testInvalidExpression() {
 Expression e = createExpression("ABC");
 }

 @Test(expected = Exception.class)
 public void testDivisionByZero() {
 Expression e = createExpression("5 / 0");
 }
}