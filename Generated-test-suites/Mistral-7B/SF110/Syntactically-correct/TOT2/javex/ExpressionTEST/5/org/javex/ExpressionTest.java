import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExpressionTest {

    @Test
    public void testExpressionEvaluation() {
        Expression expression = new Expression("(5 + 3) and (4 > 3) and (7 = 7)");
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(true, result);
    }

    @Test
    public void testExpressionWithReservedFunctions() {
        Expression expression = new Expression("(5 + 3) and (4 > 3) and (7 = 5) and (3 < 5)");
        expression.prepare();
        Object result = expression.evaluate();
        assertEquals(false, result);
    }

    @Test
    public void testExpressionWithInvalidFunction() {
        Expression expression = new Expression("(5 + 3) and (x = 5) and (3 < 5)");
        expression.prepare();
        try {
            expression.evaluate();
            fail("Exception expected");
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testExpressionWithEmptyString() {
        Expression expression = new Expression("");
        expression.prepare();
        try {
            expression.evaluate();
            fail("Exception expected");
        } catch (Exception e) {
            // Expected exception
        }
    }
}