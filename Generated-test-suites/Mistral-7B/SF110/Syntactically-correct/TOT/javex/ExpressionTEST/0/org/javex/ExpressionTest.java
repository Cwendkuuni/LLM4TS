import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.JavexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ExpressionTest {

 private Expression expression;
 private FunctionExpression andFunction, orFunction;

 @Before
 public void setUp() {
 expression = new Expression("(1 + 2) and (3 > 2 and 4 < 5)");
 andFunction = Expression.getReservedFunctions("and");
 orFunction = Expression.getReservedFunctions("or");
 }

 @Test
 public void testIsReservedFunction() {
 assertTrue(expression.isReservedFunction("and"));
 assertTrue(expression.isReservedFunction("or"));
 assertFalse(expression.isReservedFunction("add"));
 }

 @Test
 public void testIsLogicalFunction() {
 assertFalse(expression.isLogicalFunction("1"));
 assertFalse(expression.isLogicalFunction("("));
 assertTrue(expression.isLogicalFunction("and"));
 }

 @Test
 public void testGetFunction() {
 assertEquals(andFunction, expression.getFunction("and"));
 assertNull(expression.getFunction("add"));
 }

 @Test
 public void testIsFunction() {
 assertTrue(expression.isFunction("and"));
 assertFalse(expression.isFunction("add"));
 assertFalse(expression.isFunction("1"));
 }

 @Test
 public void testEvaluateWithPreparedExpression() throws Exception {
 expression.prepare();
 assertTrue(expression.evaluate().booleanValue());
 }

 @Test(expected = Exception.class)
 public void testEvaluateWithUnpreparedExpression() throws Exception {
 expression.evaluate();
 }

 @Test
 public void testGetExternalFunctions() {
 List<FunctionExpression> functions = expression.getExternalFunctions();
 assertFalse(functions.isEmpty());
 assertTrue(functions.contains(orFunction));
 }

 @Test
 public void testIsExternalFunction() {
 assertFalse(expression.isExternalFunction("and"));
 assertTrue(expression.isExternalFunction("or"));
 }

 @Test
 public void testStack() {
 ExpressionStack stack = expression.getStackCopy();
 String[] expected = {"1", "+", "2", "", "and", "(", "3", ">", "2", "<", "5", ")"};
 assertArrayEquals(expected, stack.toArray(new String[stack.size()]));
 }

 @Test
 public void testTokenize() {
 ExpressionStack stack = new ExpressionStack();
 expression.tokenize();
 Object[] expected = new Object[]{"1 + 2", "and", "(", "3", ">", "2", "<", "5", ")"}.toArray();
 for (int i = 0; i < stack.size(); ++i) {
 assertEquals(expected[i], stack.get(i));
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
 assertFalse(JavexUtils.isSpace('+'));
 }

 @Test
 public void testIsReservedChar() {
 assertTrue(JavexUtils.isReservedChar("("));
 assertFalse(JavexUtils.isReservedChar("+"));
 }

 @Test
 public void testIsNumber() {
 assertTrue(JavexUtils.isNumber("1"));
 assertFalse(JavexUtils.isNumber("+"));
 }

 @Test
 public void testStackPushAndPop() {
 ExpressionStack stack = new ExpressionStack();
 stack.push("1");
 stack.push("2");
 assertEquals("2", stack.pop());
 assertEquals("1", stack.pop());
 assertEquals(0, stack.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testStackAddNull() {
 ExpressionStack stack = new ExpressionStack();
 stack.add(null);
 }

 @Test
 public void testStackToString() {
 ExpressionStack stack = new ExpressionStack();
 stack.add("1");
 stack.add("2");
 stack.add("3");
 assertEquals("[1, 2, 3]", stack.toString());
 }

 @Test
 public void testEvaluateWithPreparedExpressionAndFunctions() throws Exception {
 expression = new Expression("(1 + 2) or (3 > 2 and 4 < 5)");
 expression.prepare();
 assertTrue(expression.evaluate().booleanValue());
 }

 @Test
 public void testEvaluateWithPreparedExpressionAndInvalidFunction() throws Exception {
 expression = new Expression("(1 + 2) add (3 > 2 and 4 < 5)");
 expression.prepare();
 assertThrows(Exception.class, () -> expression.evaluate());
 }

 @Test
 public void testEvaluateWithPreparedExpressionAndDateFunctions() throws Exception {
 expression = new Expression("(DateDiffFunction(\"2022-01-01\", \"2022-01-03\") > 1)");
 expression.prepare();
 assertTrue(expression.evaluate().booleanValue());
 }

 @Test
 public void testEvaluateWithPreparedExpressionAndStringFunctions() throws Exception {
 expression = new Expression("(SubStringFunction(\"Hello World\", 5, 2) = \"or\")");
 expression.prepare();
 assertFalse(expression.evaluate().booleanValue());
 }

 @Test
 public void testEvaluateWithPreparedExpressionAndMathFunctions() throws Exception {
 expression = new Expression("(SqrtFunction(16) = 4)");
 expression.prepare();
 assertTrue(expression.evaluate().booleanValue());
 }

 @Test
 public void testEvaluateWithPreparedExpressionAndLogicalFunctions() throws Exception {
 expression = new Expression("(LengthFunction(\"Hello World\") > 10 and LessThanFunction(5, 3))");
 expression.prepare();
 assertFalse(expression.evaluate().booleanValue());
 }
}