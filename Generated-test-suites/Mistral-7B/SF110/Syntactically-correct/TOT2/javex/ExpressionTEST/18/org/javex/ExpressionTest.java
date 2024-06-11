import org.junit.Test;
import org.junit.Assert.*;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.javex.utils.*;

public class ExpressionTest {

    @Test
    public void testExpressionEmptyConstructor() {
        Expression expression = new Expression();
        assertTrue(expression.prepared == false);
    }

    // Test the remaining methods with their respective test cases

    // ...

    @Test
    public void testExpressionEvaluateWithProperlyFormattedExpression() {
        Expression expression = new Expression("1 + 2");
        expression.prepare();
        assertEquals(3, expression.evaluate());
    }

    // ...

    @Test
    public void testTokenizeImproperlyFormattedExpressions() {
        Expression expression = new Expression("1 + 2 +");
        expression.tokenize();
        List<String> expected = Arrays.asList("1", "+", "2");
        Assert.assertEquals(expected, expression.stack);
    }

    // ...

}