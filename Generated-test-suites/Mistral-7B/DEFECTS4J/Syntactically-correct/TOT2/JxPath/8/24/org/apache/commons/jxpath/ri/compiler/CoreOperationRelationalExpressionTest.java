import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLiteral;

public class CoreOperationRelationalExpressionTest {

    CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new CoreOperation[0]);

    @Test
    public void testComputeValue() {
        CoreOperation op1 = new CoreOperationLiteral(Boolean.TRUE);
        CoreOperation op2 = new CoreOperationLiteral(Boolean.FALSE);

        EvalContext context1 = new EvalContext();
        EvalContext context2 = new EvalContext();

        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(context1, op1, op2));
        assertEquals(Boolean.FALSE, coreOperationRelationalExpression.computeValue(context2, op1, op2));

        //Add more test cases with different input types
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullEvalContext() {
        CoreOperation op1 = new CoreOperationLiteral(Boolean.TRUE);
        CoreOperation op2 = new CoreOperationLiteral(Boolean.FALSE);

        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(null, op1, op2)); //should throw exception
    }

    //Add test cases for other edge cases and exception handling scenarios

}