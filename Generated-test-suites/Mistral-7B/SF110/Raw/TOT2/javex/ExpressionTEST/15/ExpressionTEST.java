import org.javex.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ExpressionTest
{
    @Test
    public void TestExpressionConstructor()
    {
        String expression = "1 + 2";
        Expression exp = new Expression(expression);
        assertEquals("1 + 2", exp.getExpression());
    }

    // Test cases for other methods and edge cases follow here

    @Test
    public void TestExpressionConstructorNull()
    {
        try
        {
            new Expression(null);
            fail("Expected IllegalArgumentException: expression cannot be null.");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    // Test cases for other methods and edge cases follow here
}