import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        Expression[] args = new Expression[1];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        CoreOperation coreOperation = coreOperationRelationalExpression;
        assertEquals(CoreOperation.TYPE_RELAIONAL, coreOperation.getType());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(3, coreOperationRelationalExpression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]);
        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }
}