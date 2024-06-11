import org.javex.Expression;
import org.javex.utils.JavexUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExpressionTest {

 @Test
 public void testSimpleExpression() {
 Expression expression = new SimpleExpression("2 + 3");
 expression.prepare();
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 Assert.fail("Exception thrown: " + e.getMessage());
 }
 Assert.assertEquals(5, result);
 }

 @Test
 public void testExpressionWithParentheses() {
 Expression expression = new SimpleExpression("(2 + 3) * 4");
 expression.prepare();
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 Assert.fail("Exception thrown: " + e.getMessage());
 }
 Assert.assertEquals(20, result);
 }

 @Test
 public void testExpressionWithLogicalOperators() {
 Expression expression = new SimpleExpression("(2 > 1) and (3 > 2)");
 expression.prepare();
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 Assert.fail("Exception thrown: " + e.getMessage());
 }
 Assert.assertTrue(result);
 }

 @Test
 public void testExpressionWithMathFunctions() {
 Expression expression = new SimpleExpression("sqrt(16 + 4) * 2");
 expression.prepare();
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 Assert.fail("Exception thrown: " + e.getMessage());
 }
 Assert.assertEquals(8.0, result, 0.01);
 }

 @Test
 public void testExpressionWithDateFunctions() {
 Expression expression = new SimpleExpression("DateAdd(DateDiff(now(), '2022-01-01'), 3)");
 expression.prepare();
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 Assert.fail("Exception thrown: " + e.getMessage());
 }
 Assert.assertNotNull(result);
 Assert.assertTrue(JavexUtils.isValidDate((String) result));
 }

 @Test
 public void testExpressionWithStringFunctions() {
 Expression expression = new SimpleExpression("ToUpper('hello world')");
 expression.prepare();
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 Assert.fail("Exception thrown: " + e.getMessage());
 }
 Assert.assertEquals("HELLO WORLD", result);
 }

 @Test
 public void testExpressionWithExternalFunctions() {
 Expression expression = new SimpleExpression("sin(pi/2)");
 expression.prepare();
 expression.setExternalFunctions(Arrays.asList(new TrigonometricFunction("sin")));
 Object result = null;
 try {
 result = expression.evaluate();
 } catch (Exception e) {
 Assert.fail("Exception thrown: " + e.getMessage());
 }
 Assert.assertEquals(1, result, 0.01);
 }

 @Test
 public void testExpressionWithInvalidSyntax() {
 Expression expression = new SimpleExpression("2 + 3 4");
 Assert.assertThrows(RuntimeException.class, () -> expression.prepare());
 }

 @Test
 public void testExpressionWithMissingParentheses() {
 Expression expression = new SimpleExpression("2 + 3 * 4");
 Assert.assertThrows(RuntimeException.class, () -> expression.prepare());
 }

 @Test
 public void testExpressionWithMissingFunction() {
 Expression expression = new SimpleExpression("sqrt(16 + 4) * 2 * asin(1)");
 expression.prepare();
 expression.setExternalFunctions(Collections.emptyList());
 Assert.assertThrows(RuntimeException.class, () -> expression.evaluate());
 }
}