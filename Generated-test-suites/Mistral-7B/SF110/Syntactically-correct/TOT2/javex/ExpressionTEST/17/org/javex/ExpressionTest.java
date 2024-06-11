import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.FunctionExpression;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List externalFunctions = expression.getExternalFunctions();
        Assert.assertNotNull(externalFunctions);
        Assert.assertEquals(0, externalFunctions.size());
    }

    @Test
    public void testExpressionConstructor() {
        Expression expression = new Expression("1 + 2");
        ExpressionStack stack = expression.getStackCopy();
        Assert.assertArrayEquals(new String[]{"1", "+", "2"}, stack.toArray());
    }

    @Test(expected = Exception.class)
    public void testIsFunctionWithInvalidSymbol() {
        Expression expression = new Expression("test");
        Assert.assertFalse(expression.isFunction("test"));
    }

    @Test
    public void testIsFunctionWithValidLogicalSymbol() {
        Expression expression = new Expression("and(true, true)");
        Assert.assertTrue(expression.isFunction("and"));
    }

    @Test(expected = Exception.class)
    public void testEvaluateWithInvalidExpression() {
        Expression expression = new Expression("test");
        try {
            expression.evaluate();
            Assert.fail();
        } catch (Exception e) {
            // Expected exception
        }
    }

    @Test
    public void testEvaluateWithSimpleAddition() {
        Expression expression = new Expression("1 + 2");
        Object result = null;
        try {
            result = expression.evaluate();
        } catch (Exception e) {
            Assert.fail();
        }
        Assert.assertEquals(3, result);
    }
}