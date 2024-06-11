import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.FunctionExpression;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExpressionTest {

 private Expression createExpression(String expression) {
 Expression e = new Expression(expression);
 e.prepare();
 return e;
 }

 @Test
 public void testSimpleExpression() {
 Expression e = createExpression("3 + 4");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("3", "4", "+", "7");
 Assert.assertEquals(expected, stack);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidExpression() {
 Expression e = createExpression("3 + 4)");
 }

 @Test
 public void testStringFunction() {
 Expression e = new StringExpression("Len(Hello World)");
 e.prepare();
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("Hello World", "Len", "11");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testLogicalFunction() {
 Expression e = new Expression("(3 > 2) and (4 < 5)");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("3 > 2", "4 < 5", "and", "true");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testDateFunction() {
 Expression e = new Expression("DateDiff(Day, '2022-01-01', '2022-01-05')");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("2022-01-01", "DateDiff", "Day", "3");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testExternalFunction() {
 Expression e = new ExternalFunctionExpression("MyFunction(1, 2)");
 e.prepare();
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("1", "2", "MyFunction");
 Assert.assertEquals(expected, stack);
 }

 @Test(expected = RuntimeException.class)
 public void testMissingFunction() {
 Expression e = new Expression("MissingFunction(1, 2)");
 }

 @Test
 public void testReservedFunction() {
 Expression e = new Expression("Equals('abc', 'def')");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("abc", "Equals", "def", "false");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testInvalidFunctionCall() {
 Expression e = new Expression("Equals('abc', 'def') + 5");
 }

 @Test
 public void testEmptyList() {
 Expression e = new Expression("IsEmpty(())");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("()", "IsEmpty", "true");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testNullValue() {
 Expression e = new Expression("IsNull(null)");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("null", "IsNull", "true");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testNonEmptyList() {
 Expression e = new Expression("IsEmpty((1, 2, 3))");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("(1, 2, 3)", "IsEmpty", "false");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testMatchesFunction() {
 Expression e = new Expression("Matches('abc', '.*c.*')");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("abc", "Matches", "true");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testInListFunction() {
 Expression e = new Expression("InList(3, (1, 2, 3, 4, 5))");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("3", "InList", "(1, 2, 3, 4, 5)", "true");
 Assert.assertEquals(expected, stack);
 }

 @Test
 public void testInListFunctionWithMissingValue() {
 Expression e = new Expression("InList(6, (1, 2, 3, 4, 5))");
 List<String> stack = e.getStackCopy();
 List<String> expected = Arrays.asList("6", "InList", "(1, 2, 3, 4, 5)", "false");
 Assert.assertEquals(expected, stack);
 }
}