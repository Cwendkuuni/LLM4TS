
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        assertNotNull(expr);
        assertEquals(3, expr.getPrecedence());
        assertFalse(expr.isSymmetric());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArgs() {
        new CoreOperationRelationalExpression(null);
    }

    @Test
    public void testTypicalUsage() {
        Expression lhs = new Constant("10");
        Expression rhs = new Constant("5");
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[] { lhs, rhs });
        assertEquals(Boolean.FALSE, expr.evaluate(new Context()));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPrecedenceWithNullObject() {
        new CoreOperationRelationalExpression(null);
    }

    @Test
    public void testGetPrecedenceWithTypicalUsage() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(3, expr.getPrecedence());
    }

}
