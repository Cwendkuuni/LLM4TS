import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.ExpressionTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest extends ExpressionTestCase {

    @Test
    public void testLessThan() {
        CoreOperationLessThan op = new CoreOperationLessThan(new Const("5.0"), new Const("6.0"));
        EvalContext context = new EvalContext();
        InfoSet infoSet = createInfoSet();
        context.setInfoSet(infoSet);
        Object result = op.computeValue(context);

        assertTrue("5.0 < 6.0 should be true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperationLessThan op = new CoreOperationLessThan(new Const("5.0"), new Const("5.0"));
        EvalContext context = new EvalContext();
        InfoSet infoSet = createInfoSet();
        context.setInfoSet(infoSet);
        Object result = op.computeValue(context);

        assertTrue("5.0 <= 5.0 should be true", result instanceof Boolean && ((Boolean) result).booleanValue());
    }

    @Test
    public void testNotLessThan() {
        CoreOperationLessThan op = new CoreOperationLessThan(new Const("6.0"), new Const("5.0"));
        EvalContext context = new EvalContext();
        InfoSet infoSet = createInfoSet();
        context.setInfoSet(infoSet);
        Object result = op.computeValue(context);

        assertFalse("6.0 < 5.0 should be false", result instanceof Boolean && !((Boolean) result).booleanValue());
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidArgs() {
        CoreOperationLessThan op = new CoreOperationLessThan(new Const("5.0"), new StringConst("str"));
        EvalContext context = new EvalContext();
        InfoSet infoSet = createInfoSet();
        context.setInfoSet(infoSet);
        op.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidArgs2() {
        CoreOperationLessThan op = new CoreOperationLessThan(new StringConst("str"), new Const("5.0"));
        EvalContext context = new EvalContext();
        InfoSet infoSet = createInfoSet();
        context.setInfoSet(infoSet);
        op.computeValue(context);
    }
}