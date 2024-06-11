import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testEqual_whenBothExpressionAreNumbers_thenReturnTrue() {
        CoreOperationEqual operation = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression(1));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqual_whenBothExpressionAreStrings_thenReturnTrue() {
        CoreOperationEqual operation = new CoreOperationEqual(new CoreConstantExpression("hello"), new CoreConstantExpression("hello"));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqual_whenBothExpressionAreNull_thenReturnTrue() {
        CoreOperationEqual operation = new CoreOperationEqual(new CoreConstantExpression(null), new CoreConstantExpression(null));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqual_whenArgumentsAreNotEqual_thenReturnFalse() {
        CoreOperationEqual operation = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression(2));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testEqual_whenArgumentsAreDifferentTypes_thenThrowClassCastException() {
        CoreOperationEqual operation = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression("hello"));
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

    @Test
    public void testGetSymbol_whenCalled_thenReturnEqualSign() {
        CoreOperationEqual operation = new CoreOperationEqual(new CoreConstantExpression(1), new CoreConstantExpression(1));
        String symbol = operation.getSymbol();
        Assert.assertEquals("=", symbol);
    }
}