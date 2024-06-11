import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 private Expression expression;

 @Before
 public void setUp() {
 expression = new Expression("2 + 3 * (4 - 5) and 7 > 5 and (10 > 5 and 10 < 11) or (5 < 6 and 5 > 4)");
 expression.prepare();
 }

 @Test
 public void testEvaluateBasicArithmetic() {
 ExpressionStack expectedStack = new ExpressionStack();
 expectedStack.add("2");
 expectedStack.add("3");
 expectedStack.add("*");
 expectedStack.add("4");
 expectedStack.add("-");
 expectedStack.add("5");
 expectedStack.add(")");
 expectedStack.add("and");
 expectedStack.add("7");
 expectedStack.add(">");
 expectedStack.add("5");
 expectedStack.add("and");
 expectedStack.add("(");
 expectedStack.add("10");
 expectedStack.add(">");
 expectedStack.add("5");
 expectedStack.add("and");
 expectedStack.add("10");
 expectedStack.add("<");
 expectedStack.add("11");
 expectedStack.add(")");
 expectedStack.add("or");
 expectedStack.add("(");
 expectedStack.add("5");
 expectedStack.add("<");
 expectedStack.add("6");
 expectedStack.add("and");
 expectedStack.add("5");
 expectedStack.add(">");
 expectedStack.add("4");
 expectedStack.add(")");
 Object result = expression.evaluate();
 ExpressionStack actualStack = expression.getStackCopy();
 assertEquals(expectedStack.toString(), actualStack.toString());
 assertEquals(1, result.hashCode()); // The result of the expression should be 1 for true
 }

 @Test(expected = Exception.class)
 public void testEvaluateErrorScenario() {
 expression = new Expression("2 + 3 * (4 - 5) and 7 > 5 and (10 > 5 and 10 < z) or (5 < 6 and 5 > 4)");
 expression.prepare();
 }

 @After
 public void tearDown() {
 expression = null;
 }

}