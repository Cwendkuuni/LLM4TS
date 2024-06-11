import org.junit.Test;
import org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testComputeContextDependent() {
        Expression expression = new ConcreteExpression(); // Replace this with an actual subclass
        boolean contextDependency = expression.isContextDependent();
        assertTrue(contextDependency); // Assuming that concrete Expression subclasses are context-dependent
    }
}