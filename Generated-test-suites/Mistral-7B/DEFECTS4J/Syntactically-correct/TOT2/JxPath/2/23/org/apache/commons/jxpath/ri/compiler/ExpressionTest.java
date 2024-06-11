import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new ConcreteExpression(); // Replace ConcreteExpression with an actual subclass
        boolean contextDependent = expression.isContextDependent();
        boolean expectedContextDependent = expression.computeContextDependent();
        assertEquals(expectedContextDependent, contextDependent);
    }
}