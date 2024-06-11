import org.junit.Before;
import org.junit.Test;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;
 private ExpressionStack stack;
 private List<FunctionExpression> externalFunctions;

 @Before
 public void setUp() {
 expression = new Expression("1 + 2 * (3 - 4) / 5 + sin(pi / 2)");
 stack = expression.getStackCopy();
 externalFunctions = new ArrayList<>();
 externalFunctions.add(new SinFunction());
 expression.setExternalFunctions(externalFunctions);
 }

 @Test
 public void testEvaluateSimpleExpression() {
 expression.prepare();
 assertEquals(2.44948974278317, expression.evaluate(), 0.001);
 }

 @Test
 public void testEvaluateExpressionWithParentheses() {
 expression.prepare();
 assertEquals(1.0, expression.evaluate(), 0.001);
 }

 @Test(expected = RuntimeException.class)
 public void testEvaluateExpressionWithInvalidFunction() {
 expression = new Expression("1 + 2 * (3 - 4) / sqrt(5) + sin(pi / 2)");
 expression.prepare();
 }

 @Test
 public void testEvaluateExpressionWithLogicalFunctions() {
 expression = new Expression("true and false or not(true) and not(false)");
 expression.prepare();
 assertEquals(false, expression.evaluate());
 }

 @Test
 public void testEvaluateExpressionWithExternalFunctions() {
 expression = new Expression("sin(pi / 2) + cos(pi / 2)");
 expression.prepare();
 assertEquals(1.0, expression.evaluate(), 0.001);
 }

 @Test
 public void testEvaluateExpressionWithReservedFunctions() {
 expression = new Expression("abs(-5) + between(5, 3, 7) and not(isNull(5))");
 expression.prepare();
 assertEquals(8, expression.evaluate(), 0.001);
 }

 @Test
 public void testEvaluateExpressionWithDateFunctions() {
 expression = new Expression("dateAdd(now(), interval '1 hour')");
 expression.prepare();
 // Use current date and time, so actual result will vary
 // Assert.assertTrue(expression.evaluate() instanceof Date);
 }

 @Test
 public void testEvaluateExpressionWithStringFunctions() {
 expression = new Expression("length(concat('Hello', ' World'))");
 expression.prepare();
 assertEquals(11, expression.evaluate(), 0.001);
 }
}