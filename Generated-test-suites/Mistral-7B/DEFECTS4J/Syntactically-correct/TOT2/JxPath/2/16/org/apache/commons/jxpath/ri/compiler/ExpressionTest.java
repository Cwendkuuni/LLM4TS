import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExpressionTest {
    @Test
    public void testIsContextDependent() {
        Expression expression = new SpecificExpression(); // Assuming SpecificExpression is a subclass of Expression
        boolean contextDependent = expression.isContextDependent();
        assertEquals(true, contextDependent);

        expression = new ContextIndependentExpression(); // Assuming ContextIndependentExpression is a subclass of Expression
        contextDependent = expression.isContextDependent();
        assertEquals(false, contextDependent);
    }
}