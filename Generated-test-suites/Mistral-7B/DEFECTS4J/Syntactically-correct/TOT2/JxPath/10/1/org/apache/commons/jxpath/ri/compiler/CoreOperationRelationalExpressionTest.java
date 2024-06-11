import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testConstructor() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args);
        assertNull(coreOperationRelationalExpression.getLeftExpression());
        assertNull(coreOperationRelationalExpression.getRightExpression());
    }
}