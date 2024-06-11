import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testSimpleExpression() {
 Expression e = createExpression("5 + 3");
 FunctionExpression add = Expression.getReservedFunctions("+");
 Object result = e.evaluate();
 Assert.assertEquals(add.apply(8.0, 3.0), result);
 }

 @Test(expected = Exception.class)
 public void testMalformedExpression() {
 Expression e = new Expression("5 + 3) ");
 e.prepare();
 e.evaluate();
 }

 @Test
 public void testComplexExpression() {
 Expression e = createExpression("(5 + 3) * (7 - 2)");
 FunctionExpression add = Expression.getReservedFunctions("+");
 FunctionExpression subtract = Expression.getReservedFunctions("-");
 FunctionExpression multiply = Expression.getReservedFunctions("*");
 Object result = e.evaluate();
 Assert.assertEquals(multiply.apply(add.apply(8.0, 3.0), subtract.apply(7.0, 2.0)), result);
 }

 @Test
 public void testLogicalExpression() {
 Expression e = createExpression("(5 > 3) and (7 < 5)");
 FunctionExpression greaterThan = Expression.getReservedFunctions(">");
 FunctionExpression lessThan = Expression.getReservedFunctions("<");
 FunctionExpression and = Expression.getReservedFunctions("and");
 Object result = e.evaluate();
 Assert.assertEquals(and.apply(false, false), result);
 }

 @Test
 public void testFunctionExpression() {
 Expression e = createExpression("abs(-5)");
 FunctionExpression abs = Expression.getReservedFunctions("abs");
 Object result = e.evaluate();
 Assert.assertEquals(5.0, result);
 }

 @Test
 public void testFunctionWithParameters() {
 Expression e = createExpression("dateAdd(date('2022-01-01'), interval '1 month')");
 FunctionExpression dateAdd = Expression.getReservedFunctions("dateAdd");
 FunctionExpression date = Expression.getReservedFunctions("date");
 FunctionExpression interval = Expression.getReservedFunctions("interval");
 Map<String, Object> parameters = new HashMap<>();
 parameters.put("date", "2022-01-01");
 Object result = dateAdd.apply(date.apply(parameters), interval.apply("1 month"));
 Assert.assertEquals("2022-02-01", result.toString());
 }

 @Test
 public void testEmptyExpression() {
 Expression e = new Expression("");
 Object result = e.evaluate();
 Assert.assertEquals(null, result);
 }

 @Test(expected = Exception.class)
 public void testNonReservedFunction() {
 Expression e = new Expression("myFunction()");
 e.prepare();
 e.evaluate();
 }
}