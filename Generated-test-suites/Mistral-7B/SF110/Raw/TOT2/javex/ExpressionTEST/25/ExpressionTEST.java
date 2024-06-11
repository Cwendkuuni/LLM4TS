import org.junit.Assert;
import org.junit.Test;
import org.javex.Expression;
import org.javex.ExpressionStack;
import org.javex.utils.JavexUtils;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("expression with functions");
        List externalFunctions = expression.getExternalFunctions();
        Assert.assertNotNull(externalFunctions);
    }

    @Test
    public void testConstructor() {
        String expression = "expression to be tested";
        Expression e = new Expression(expression);
        Assert.assertNotNull(e);
    }

    @Test
    public void testIsFunction() {
        Expression e = new Expression("expression with functions");
        String symbol = "sin";
        boolean result = e.isFunction(symbol);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression e = new Expression("expression with logical functions");
        String symbol = "and";
        boolean result = e.isLogicalFunction(symbol);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsMathFunction() {
        Expression e = new Expression("expression with math functions");
        String symbol = "+";
        boolean result = e.isMathFunction(symbol);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsDateFunction() {
        Expression e = new Expression("expression with date functions");
        String symbol = "DateAdd";
        boolean result = e.isDateFunction(symbol);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStringFunction() {
        Expression e = new Expression("expression with string functions");
        String symbol = "SubString";
        boolean result = e.isStringFunction(symbol);
        Assert.assertTrue(result);
    }
}