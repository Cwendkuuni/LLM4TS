import org.junit.Test;
import static org.junit.Assert.*;
import org.javex.Expression;

public class ExpressionTest {
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List<Expression> expectedExternalFunctions = new ArrayList<>();
        Expression externalFunction = new Expression("testFunction");
        expectedExternalFunctions.add(externalFunction);
        expression.setExternalFunctions(expectedExternalFunctions);
        List<Expression> actualExternalFunctions = expression.getExternalFunctions();
        assertEquals(expectedExternalFunctions, actualExternalFunctions);
    }
}