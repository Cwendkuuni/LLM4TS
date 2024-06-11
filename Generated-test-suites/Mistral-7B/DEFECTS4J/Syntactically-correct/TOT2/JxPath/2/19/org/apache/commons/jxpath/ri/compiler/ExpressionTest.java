import org.junit.Test;
import org.apache.commons.jxpath.EvalContext;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new FooExpression(); // replace with a valid subclass of Expression
        assertTrue(expression.isContextDependent()); // expected result is true
    }
}