import org.junit.Test;
import org.junit.Assert.*;
import org.javex.Expression;

public class ExpressionTest {
    @Test
    public void testGetExternalFunctions() {
        Expression expression = new Expression("test");
        List<Expression> externalFunctions = expression.getExternalFunctions();
        assertNotNull(externalFunctions);
        assertTrue(externalFunctions.isEmpty());
    }
}