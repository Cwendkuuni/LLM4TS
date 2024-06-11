import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {
    @Test
    public void testIsContextDependent() {
        Expression expression = new MyExpression(); // Assuming MyExpression is a subclass of Expression
        Assert.assertFalse(expression.isContextDependent());
    }
}