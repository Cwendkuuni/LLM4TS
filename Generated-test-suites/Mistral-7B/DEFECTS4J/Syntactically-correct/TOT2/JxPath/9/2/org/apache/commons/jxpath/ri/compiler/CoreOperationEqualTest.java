import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqualConstructor() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        // Add checks for constructor arguments if necessary
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        arg1 = new Expression(1);
        arg2 = new Expression(1);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        arg1 = new Expression(1);
        arg2 = new Expression(2);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArg1() {
        EvalContext context = new EvalContext();
        Expression arg1 = null;
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArg2() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression(1);
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    @Test
    public void testComputeValueWithDifferentDataTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression("str");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueWithExceptionalExpression1() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                throw new RuntimeException("Exception from arg1");
            }
        };
        Expression arg2 = new Expression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValueWithExceptionalExpression2() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression(1);
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                throw new RuntimeException("Exception from arg2");
            }
        };
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        coreOperationEqual.computeValue(context);
    }
}