import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.IterateIter;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_true() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new MyExpression(), new MyExpression());
        EvalContext context = new EvalContext();
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_false() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new MyExpression(), new MyExpression());
        EvalContext context = new EvalContext();
        assertFalse(expression.computeValue(context).equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_nullInput1() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new MyExpression(), null);
        EvalContext context = new EvalContext();
        expression.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_nullInput2() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(null, new MyExpression());
        EvalContext context = new EvalContext();
        expression.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_wrongInputType1() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new MyExpression(), new Integer(1));
        EvalContext context = new EvalContext();
        expression.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_wrongInputType2() {
        CoreOperationRelationalExpression expression = new MyCoreOperationRelationalExpression(new Integer(1), new MyExpression());
        EvalContext context = new EvalContext();
        expression.computeValue(context);
    }
}