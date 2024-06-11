import org.junit.Test;
import org.junit.Assert.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setUp() {
 expression = new Expression("2 + 3 * (4 > 2 and 5 < 6) + 7");
 expression.prepare();
 }

 @Test
 public void testSimpleArithmeticExpression() {
 final Object result = expression.evaluate();
 assertEquals(11.0, result);
 }

 @Test
 public void testComplexLogicalExpression() {
 final Object result = expression.evaluate();
 assertTrue(result);
 }

 @Test(expected = Exception.class)
 public void testDivideByZero() {
 expression = new Expression("2 / 0");
 expression.prepare();
 expression.evaluate();
 }

 @Test(expected = Exception.class)
 public void testInvalidFunction() {
 expression = new Expression("invalidFunction(2, 3)");
 expression.prepare();
 expression.evaluate();
 }

 @Test
 public void testDateFunctions() {
 expression = new Expression("DateAdd(DateDiff(now(), '2022-01-01'), 5) > Date('2022-03-31')");
 expression.prepare();
 final Object result = expression.evaluate();
 assertTrue(result);
 }

 @Test
 public void testLogicalFunctions() {
 expression = new Expression("(5 > 3) and (7 < 10) and (false or true)");
 expression.prepare();
 final Object result = expression.evaluate();
 assertTrue(result);
 }

 @Test
 public void testStringFunctions() {
 expression = new Expression("ToUpper('Hello World') startsWith 'HE'");
 expression.prepare();
 final Object result = expression.evaluate();
 assertTrue(result);
 }

 @Test
 public void testExternalFunctions() {
 expression = new Expression("MyFunction('Hello') + MyFunction('World')");
 expression.getExternalFunctions().add(new MyFunction("MyFunction"));
 expression.prepare();
 final Object result = expression.evaluate();
 assertEquals("HelloWorld", result.toString());
 }
}

class MyFunction implements FunctionExpression {
 private final String simbol;

 public MyFunction(final String simbol) {
 this.simbol = simbol;
 }

 @Override
 public String getSimbol() {
 return simbol;
 }

 @Override
 public int getArity() {
 return 1;
 }

 @Override
 public int getType() {
 return FunctionExpression.STRING;
 }

 @Override
 public Object evaluate(final ExpressionStack stack, final List externalFunctions) throws Exception {
 return stack.pop().toString().toUpperCase();
 }
}