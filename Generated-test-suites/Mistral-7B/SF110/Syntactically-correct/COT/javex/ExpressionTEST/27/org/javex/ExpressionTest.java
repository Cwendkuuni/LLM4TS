import org.junit.Test;
import org.javex.Expression;
import static org.junit.Assert.assertEquals;

public class ExpressionTest {

    @Test
    public void testExpressionConstructor() {
        String expression = "1 + 2";
        Expression expr = new Expression(expression);
        assertEquals("1 + 2", expr.getExpression());
    }
}