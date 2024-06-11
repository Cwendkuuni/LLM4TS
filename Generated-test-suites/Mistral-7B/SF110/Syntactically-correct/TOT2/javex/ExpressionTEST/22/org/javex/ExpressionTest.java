import org.javex.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expr = new Expression("myExpression");
        List<Expression> externalFunctions = expr.getExternalFunctions();
        assertEquals(0, externalFunctions.size());
    }

    @Test
    public void testGetExternalFunctionsWithDefinedFunctions() {
        Expression expr = new Expression("myExpression WITH myFunction1(x), myFunction2(y)");
        List<Expression> externalFunctions = expr.getExternalFunctions();
        assertEquals(2, externalFunctions.size());
        Expression function1 = externalFunctions.get(0);
        Expression function2 = externalFunctions.get(1);
        assertEquals("myFunction1", function1.getSimbol());
        assertEquals("myFunction2", function2.getSimbol());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetExternalFunctionsWithNullExpression() {
        Expression expr = null;
        List<Expression> externalFunctions = expr.getExternalFunctions();
    }
}