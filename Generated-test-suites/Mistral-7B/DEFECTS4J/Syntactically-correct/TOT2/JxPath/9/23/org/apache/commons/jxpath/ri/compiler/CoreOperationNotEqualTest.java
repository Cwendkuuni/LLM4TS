import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        EvalContext context = mock(EvalContext.class);

        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(context);
        assertNull(result);

        when(arg1.computeValue(context)).thenReturn(Boolean.TRUE);
        when(arg2.computeValue(context)).thenReturn(Boolean.TRUE);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse(result);

        when(arg1.computeValue(context)).thenReturn(1);
        when(arg2.computeValue(context)).thenReturn(1);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse(result);

        when(arg1.computeValue(context)).thenReturn(true);
        when(arg2.computeValue(context)).thenReturn(false);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse(result);

        when(arg1.computeValue(context)).thenReturn(false);
        when(arg2.computeValue(context)).thenReturn(true);
        result = coreOperationNotEqual.computeValue(context);
        assertFalse(result);

        when(arg1.computeValue(context)).thenThrow(new RuntimeException());
        when(arg2.computeValue(context)).thenReturn(Boolean.TRUE);
        try {
            result = coreOperationNotEqual.computeValue(context);
        } catch (Exception e) {
            result = e;
        }
        assertNotNull(result);
        assertInstanceOf(RuntimeException.class, result);

        when(arg1.computeValue(context)).thenReturn(Boolean.TRUE);
        when(arg2.computeValue(context)).thenThrow(new RuntimeException());
        try {
            result = coreOperationNotEqual.computeValue(context);
        } catch (Exception e) {
            result = e;
        }
        assertNotNull(result);
        assertInstanceOf(RuntimeException.class, result);
    }
}