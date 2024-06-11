import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.FunctionExpression;
import org.javex.functions.LogicalFunction;
import org.javex.functions.MathFunction;
import org.javex.functions.StringFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;
 private ExpressionStack stack;
 private List<FunctionExpression> externalFunctions;

 @Before
 public void setup() {
 expression = new Expression("2 + 3 * (4 - 1) and (5 > 4) or (10 < 9)");
 stack = expression.getStackCopy();
 externalFunctions = new ArrayList<>();
 }

 @Test
 public void testIsReservedFunction() {
 assertEquals(true, expression.isReservedFunction("and"));
 assertEquals(true, expression.isReservedFunction("or"));
 assertEquals(false, expression.isReservedFunction("x"));
 }

 @Test
 public void testIsFunction() {
 assertEquals(true, expression.isFunction("+"));
 assertEquals(true, expression.isFunction("*"));
 assertEquals(true, expression.isFunction("-"));
 assertEquals(true, expression.isFunction("/"));
 assertEquals(true, expression.isFunction("*"));
 assertEquals(true, expression.isFunction("sqrt"));
 assertEquals(true, expression.isFunction("abs"));
 assertEquals(true, expression.isFunction("power"));
 assertEquals(true, expression.isFunction("powerchar"));
 assertEquals(true, expression.isFunction("dateadd"));
 assertEquals(true, expression.isFunction("datediff"));
 assertEquals(true, expression.isFunction("length"));
 assertEquals(true, expression.isFunction("substr"));
 assertEquals(true, expression.isFunction("tolower"));
 assertEquals(true, expression.isFunction("toupper"));
 assertEquals(false, expression.isFunction("x"));
 }

 @Test
 public void testIsLogicalFunction() {
 assertEquals(true, expression.isLogicalFunction("and"));
 assertEquals(true, expression.isLogicalFunction("or"));
 assertEquals(false, expression.isLogicalFunction("+"));
 }

 @Test
 public void testIsMathFunction() {
 assertEquals(true, expression.isMathFunction("+"));
 assertEquals(true, expression.isMathFunction("-"));
 assertEquals(true, expression.isMathFunction("*"));
 assertEquals(true, expression.isMathFunction("/"));
 assertEquals(true, expression.isMathFunction("sqrt"));
 assertEquals(true, expression.isMathFunction("abs"));
 assertEquals(true, expression.isMathFunction("power"));
 assertEquals(false, expression.isMathFunction("and"));
 }

 @Test
 public void testIsDateFunction() {
 assertEquals(true, expression.isDateFunction("dateadd"));
 assertEquals(true, expression.isDateFunction("datediff"));
 assertEquals(false, expression.isDateFunction("and"));
 }

 @Test
 public void testIsStringFunction() {
 assertEquals(true, expression.isStringFunction("length"));
 assertEquals(true, expression.isStringFunction("substr"));
 assertEquals(true, expression.isStringFunction("tolower"));
 assertEquals(true, expression.isStringFunction("toupper"));
 assertEquals(false, expression.isStringFunction("and"));
 }

 @Test
 public void testGetExternalFunction() {
 FunctionExpression function = new LogicalFunction("myFunction", FunctionExpression.LOGICAL);
 expression.externalFunctions.add(function);
 assertEquals(function, expression.getExternalFunction("myFunction"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetExternalFunctionNotFound() {
 assertEquals(null, expression.getExternalFunction("notFoundFunction"));
 }

 @Test
 public void testPrepare() {
 expression.prepare();
 assertEquals(11, expression.stack.size());
 }

 @Test(expected = RuntimeException.class)
 public void testPrepareMalformedExpression() {
 final String malformedExpression = "2 + 3 * (4 - 1) and (5 > 4) or (10 < 9 x";
 Expression malformedExpressionExpression = new Expression(malformedExpression);
 malformedExpressionExpression.prepare();
 }

 @Test
 public void testEvaluate() throws Exception {
 assertEquals(6.0, expression.evaluate(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateWithMalformedExpression() {
 expression.stack = new ExpressionStack();
 expression.stack.add("2 + 3 * (4 - 1) and (5 > 4) or (10 < 9 x");
 expression.prepare();
 assertEquals(0, expression.evaluate().hashCode());
 }

}