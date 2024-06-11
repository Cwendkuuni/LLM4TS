import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private Expression expression;
 private ExpressionStack stack;

 @Before
 public void setUp() {
 expression = new Expression("1 + (2 * 3)");
 stack = expression.getStackCopy();
 }

 @Test
 public void testIsReservedFunction() {
 assertEquals(true, expression.isReservedFunction("and"));
 assertEquals(true, expression.isReservedFunction("="));
 assertEquals(true, expression.isReservedFunction("not"));
 }

 @Test
 public void testIsFunction() {
 assertEquals(true, expression.isFunction("*"));
 assertEquals(true, expression.isFunction("+"));
 assertFalse(expression.isFunction("1"));
 }

 @Test
 public void testIsLogicalFunction() {
 assertEquals(false, expression.isLogicalFunction("*"));
 assertEquals(false, expression.isLogicalFunction("+"));
 assertEquals(false, expression.isLogicalFunction("and"));
 }

 @Test
 public void testIsMathFunction() {
 assertEquals(true, expression.isMathFunction("*"));
 assertEquals(true, expression.isMathFunction("+"));
 assertFalse(expression.isMathFunction("and"));
 }

 @Test
 public void testIsDateFunction() {
 assertFalse(expression.isDateFunction("*"));
 assertFalse(expression.isDateFunction("+"));
 assertFalse(expression.isDateFunction("and"));
 }

 @Test
 public void testIsStringFunction() {
 assertFalse(expression.isStringFunction("*"));
 assertFalse(expression.isStringFunction("+"));
 assertFalse(expression.isStringFunction("and"));
 }

 @Test
 public void testGetFunction() {
 FunctionExpression function = expression.getFunction("*");
 assertEquals("*", function.getSimbol());
 assertEquals(FunctionExpression.MATH, function.getType());
 }

 @Test
 public void testGetExternalFunction() {
 assertNull(expression.getExternalFunction("abc"));
 }

 @Test
 public void testIsExternalFunction() {
 assertFalse(expression.isExternalFunction("abc"));
 }

 @Test
 public void testEvaluate() {
 expression.prepare();
 try {
 assertEquals(7.0, expression.evaluate(), 0.001);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception occurred during expression evaluation");
 }
 }

 @Test
 public void testPrepare() {
 expression.prepare();
 assertTrue(expression.isPrepared());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateUndprepared() {
 expression.evaluate();
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testTokenize() {
 Expression e = new Expression("1 + (2 * 3)");
 assertEquals(11, e.stack.size());
 String[] expected = {"1", "+", "(", "2", "*", "3", ")", ")"};
 for (int i = 0; i < e.stack.size(); i++) {
 assertEquals(expected[i], e.stack.get(i));
 }
 }

 @Test
 public void testIsQuote() {
 assertTrue(JavexUtils.isQuote('"'));
 assertFalse(JavexUtils.isQuote('('));
 }

 @Test
 public void testIsSpace() {
 assertTrue(JavexUtils.isSpace(' '));
 assertFalse(JavexUtils.isSpace('('));
 }

 @Test
 public void testIsNumber() {
 assertTrue(JavexUtils.isNumber("123"));
 assertFalse(JavexUtils.isNumber("abc"));
 }

 @Test
 public void testIsReservedChar() {
 assertTrue(JavexUtils.isReservedChar("+"));
 assertFalse(JavexUtils.isReservedChar("1"));
 }
}