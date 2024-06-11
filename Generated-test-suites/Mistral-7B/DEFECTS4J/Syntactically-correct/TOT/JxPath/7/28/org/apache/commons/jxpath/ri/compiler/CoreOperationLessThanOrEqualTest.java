import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqualWithBothNumbersLessThanResult() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);

        when(arg1.computeValue(context)).thenReturn(new Double(3.0));
        when(arg2.computeValue(context)).thenReturn(new Double(5.0));

        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        Boolean result = expression.computeValue(context);

        Assert.assertTrue(result);
    }

    @Test
    public void testLessThanOrEqualWithFirstNumberGreaterThanSecondNumber() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);

        when(arg1.computeValue(context)).thenReturn(new Double(5.0));
        when(arg2.computeValue(context)).thenReturn(new Double(3.0));

        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        Boolean result = expression.computeValue(context);

        Assert.assertFalse(result);
    }

    @Test
    public void testLessThanOrEqualWithBothNumbersEqual() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);

        when(arg1.computeValue(context)).thenReturn(new Double(3.0));
        when(arg2.computeValue(context)).thenReturn(new Double(3.0));

        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        Boolean result = expression.computeValue(context);

        Assert.assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThanOrEqualWithInvalidArgument() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);

        when(arg1.computeValue(context)).thenReturn(InfoSet.getEmptyInfoSet());
        when(arg2.computeValue(context)).thenReturn(InfoSet.getEmptyInfoSet());

        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        expression.computeValue(context);
    }
}