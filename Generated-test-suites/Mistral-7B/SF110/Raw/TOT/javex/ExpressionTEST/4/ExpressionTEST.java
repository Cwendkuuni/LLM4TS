import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.javex.functions.math.AddFunction;
import org.javex.functions.math.SubtractFunction;
import org.javex.functions.date.DateAddFunction;
import org.javex.functions.date.DateDiffFunction;
import org.javex.functions.string.ContainsFunction;
import org.javex.functions.string.LengthFunction;
import org.javex.functions.string.ToLowerFunction;

public class ExpressionTest {

 private Expression expression1;
 private Expression expression2;

 @Before
 public void setup() {
 expression1 = new Expression("2 + 3 * (4 + 5) and (6 > 5) or (7 < 8)");
 expression2 = new Expression("(1 + 2) * 3 + 4 and (true or false) and not 'abc' contains 'a' and length(substr('abcdefg', 1, 3)) > 5 and dateadd(now(), 1, 'day') = date('2022-03-05') and pow(2, 3) > 100");
 expression1.prepare();
 expression2.prepare();
 }

 @Test
 public void testSimpleExpressionEvaluation() {
 final Object result1 = expression1.evaluate();
 final Object result2 = expression2.evaluate();
 Assert.assertEquals(13, result1);
 Assert.assertTrue(result2);
 }

 @Test(expected = Exception.class)
 public void testExpressionWithInvalidFunction() {
 final String invalidFunction = "invalidFunction";
 final Expression invalidExpression = new Expression("1 + " + invalidFunction + "(2)");
 invalidExpression.prepare();
 }

 @Test(expected = Exception.class)
 public void testExpressionWithEmptyStack() {
 final Expression emptyExpression = new Expression("");
 emptyExpression.prepare();
 }

 @Test(expected = Exception.class)
 public void testExpressionWithMismatchedParentheses() {
 final Expression mismatchedParenthesisExpression = new Expression("(1 + 2) * (3 + 4) + ");
 mismatchedParenthesisExpression.prepare();
 }

 @Test
 public void testLogicalFunctions() {
 final boolean andResult = expression1.isLogicalFunction("and");
 final boolean orResult = expression1.isLogicalFunction("or");
 Assert.assertTrue(andResult);
 Assert.assertTrue(orResult);
 }

 @Test
 public void testIsFunction() {
 final boolean containsResult = expression2.isFunction("contains");
 final boolean dateAddResult = expression2.isFunction("dateadd");
 Assert.assertTrue(containsResult);
 Assert.assertTrue(dateAddResult);
 }

 @Test
 public void testGetReservedFunctions() {
 final FunctionExpression andFunction = Expression.getReservedFunctions("and");
 final FunctionExpression andFunctionExpected = new AndFunction();
 Assert.assertEquals(andFunctionExpected.getClass(), andFunction.getClass());
 }

 @Test
 public void testEvaluateExternalFunction() {
 final FunctionExpression addFunction = new AddFunction();
 final Expression addExpression = new Expression("1 + 2");
 addExpression.setExternalFunctions(Arrays.asList(addFunction));
 addExpression.prepare();
 final Object result = addExpression.evaluate();
 Assert.assertEquals(3, result);
 }

}