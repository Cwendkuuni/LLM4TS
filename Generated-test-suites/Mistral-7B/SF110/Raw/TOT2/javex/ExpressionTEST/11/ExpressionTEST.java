import org.junit.Test;
import org.junit.Assert.*;
import org.javex.Expression;
import org.javex.utils.JavexUtils;
import org.javex.functions.logical.AndFunction;
import org.javex.functions.logical.OrFunction;

public class ExpressionTest {

    @Test
    public void testConstructor() {
        Expression expression = new Expression("1 + 2");
        Assert.assertNotNull(expression);
    }

    // Add test cases for other constructors and methods here
}