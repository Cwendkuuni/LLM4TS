import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.constants.CoreListConstant;
import org.apache.commons.jxpath.ri.constants.CoreNumberConstant;
import org.apache.commons.jxpath.ri.constants.CoreStringConstant;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreStringConstant("5"), new CoreNumberConstant(5));
        EvalContext context = new EvalContext(new InitialContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithDifferentTypes() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreStringConstant("5"), new CoreNumberConstant("6.5"));
        EvalContext context = new EvalContext(new InitialContext());
        assertFalse(expr.computeValue(context) instanceof Boolean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullLeft() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(null, new CoreNumberConstant(5));
        EvalContext context = new EvalContext(new InitialContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithNullRight() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreStringConstant("5"), null);
        EvalContext context = new EvalContext(new InitialContext());
    }

    @Test
    public void testComputeValueWithEmptyCollections() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreListConstant(new Object[0]), new CoreListConstant(new Object[0]));
        EvalContext context = new EvalContext(new InitialContext());
        assertEquals(Boolean.FALSE, expr.computeValue(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithIncorrectOperation() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreNumberConstant(5), new CoreNumberConstant(5), new CoreStringConstant("="));
        EvalContext context = new EvalContext(new InitialContext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testComputeValueWithOutOfBoundsException() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreListConstant(new Object[]{1, 2, 3}), new CoreListConstant(new Object[]{1, 2}), new CoreNumberConstant(3));
        EvalContext context = new EvalContext(new InitialContext());
    }

}