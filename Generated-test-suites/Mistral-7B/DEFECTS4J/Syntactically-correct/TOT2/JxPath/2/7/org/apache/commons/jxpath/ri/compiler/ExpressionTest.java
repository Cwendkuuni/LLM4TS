import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new ConstantExpression(ONE); // Replace with an actual instance
        assertEquals(false, expression.isContextDependent());
    }
}