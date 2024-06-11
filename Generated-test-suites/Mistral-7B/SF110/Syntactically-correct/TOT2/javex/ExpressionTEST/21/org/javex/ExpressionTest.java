import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("x and y > 5");
        List externalFunctions = expression.getExternalFunctions();
        Assert.assertNotNull(externalFunctions);
        Assert.assertEquals(2, externalFunctions.size());
        Assert.assertTrue(externalFunctions.contains(new AndFunction()));
        // Add more assertions to test for other external functions if necessary
    }

    @Test
    public void testIsFunction() {
        Expression expression = new Expression("x + y");
        Assert.assertTrue(expression.isFunction("+"));
        Assert.assertTrue(expression.isFunction("x"));
        Assert.assertFalse(expression.isFunction("z"));
    }

    @Test
    public void testDoEvaluationStack() {
        Expression expression = new Expression("(3 + 5) * 2");
        expression.prepare();
        ExpressionStack stack = expression.getStackCopy();
        Assert.assertEquals(6, stack.size());
        // Add more assertions to test for different expressions and edge cases
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFunctionWithInvalidArgument() {
        Expression expression = new Expression("x + y");
        Assert.assertFalse(expression.isFunction("z"));
        expression.evaluate(); // This line should throw an exception
    }
}