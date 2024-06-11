import org.javex.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1 + 2");
        assertTrue(expression instanceof Expression);
    }

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("myFunction(5)");
        List<FunctionExpression> externalFunctions = expression.getExternalFunctions();
        assertEquals(1, externalFunctions.size());
        assertTrue(externalFunctions.get(0) instanceof FunctionExpression);
    }

    @Test
    public void testPrepare() {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        // Add assertions for the ExpressionStack and evaluated result here
    }
}