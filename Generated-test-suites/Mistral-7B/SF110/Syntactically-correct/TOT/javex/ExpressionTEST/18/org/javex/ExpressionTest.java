import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testSimpleExpression() {
 Expression e = createExpression("1 + 2");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 Assert.fail("Evaluation should not throw an exception: " + ex.getMessage());
 }
 Assert.assertEquals(3.0, result);
 }

 @Test
 public void testComplexExpression() {
 Expression e = createExpression("1 + 2 * 3 - 4 / (5 + 6)");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 Assert.fail("Evaluation should not throw an exception: " + ex.getMessage());
 }
 Assert.assertEquals(2.4, result);
 }

 @Test
 public void testExpressionWithFunctions() {
 Expression e = createExpression("sin(pi / 2) + cos(pi / 2)");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 Assert.fail("Evaluation should not throw an exception: " + ex.getMessage());
 }
 Assert.assertEquals(Math.sqrt(2.0), result, 0.001);
 }

 @Test
 public void testExpressionWithVariables() {
 Expression e = new Expression("x + y");
 e.setExternalFunctions(Arrays.asList(new FunctionExpression("sin", FunctionExpression.MATH),
 new FunctionExpression("pi", FunctionExpression.MATH),
 new FunctionExpression("x", new ValueExpression("x")),
 new FunctionExpression("y", new ValueExpression("y"))));
 e.prepare();
 Object result = null;
 try {
 result = e.evaluate(new HashMap() {{
 put("x", 1.0);
 put("y", 2.0);
 }});
 } catch (Exception ex) {
 Assert.fail("Evaluation should not throw an exception: " + ex.getMessage());
 }
 Assert.assertEquals(3.0, result);
 }

 @Test
 public void testInvalidExpression() {
 Expression e = createExpression("1 + 2 * 3 )");
 try {
 e.prepare();
 } catch (Exception ex) {
 Assert.assertEquals("Error parsing expression: Syntax error at index 5: Unexpected character ')'", ex.getMessage());
 }
 }

 @Test
 public void testInvalidFunction() {
 Expression e = createExpression("sqrt(-1)");
 try {
 e.prepare();
 } catch (Exception ex) {
 Assert.assertEquals("Error parsing expression: Function 'sqrt' called with invalid argument(s): Argument '-1' is not a number", ex.getMessage());
 }
 }

 @Test
 public void testEmptyExpression() {
 Expression e = new Expression("");
 Object result = null;
 try {
 result = e.evaluate();
 } catch (Exception ex) {
 Assert.fail("Evaluation should not throw an exception: " + ex.getMessage());
 }
 Assert.assertEquals(0.0, result);
 }

 @Test
 public void testNullExpression() {
 Expression e = null;
 try {
 e.evaluate();
 Assert.fail("Evaluation should throw a NullPointerException");
 } catch (NullPointerException ex) {
 // Expected
 }
 }
}