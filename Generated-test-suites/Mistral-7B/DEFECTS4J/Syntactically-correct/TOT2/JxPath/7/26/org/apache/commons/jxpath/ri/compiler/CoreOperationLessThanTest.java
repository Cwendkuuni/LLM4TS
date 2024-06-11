package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.info.NumberInfo;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationLessThanTest {

    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setup() {
        arg1 = Mockito.mock(Expression.class);
        arg2 = Mockito.mock(Expression.class);
        context = Mockito.mock(EvalContext.class);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(arg1, arg2, context);
    }

    @org.junit.Test
    public void testLessThan() {
        NumberInfo numberInfo1 = new NumberInfo(1.0);
        NumberInfo numberInfo2 = new NumberInfo(2.0);
        NumberInfo numberInfoNaN1 = new NumberInfo(Double.NaN);
        NumberInfo numberInfoNaN2 = new NumberInfo(Double.NaN);

        when(arg1.computeValue(context)).thenReturn(numberInfo1);
        when(arg2.computeValue(context)).thenReturn(numberInfo2);
        when(context.getInfoSet()).thenReturn(new InfoSet() {
            @Override
            public Info getValueInfo(int index) {
                return numberInfo1;
            }
        });

        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        Object result = coreOperationLessThan.computeValue(context);

        assertEquals(Boolean.FALSE, result);

        // Test with true condition
        when(arg1.computeValue(context)).thenReturn(numberInfo2);
        when(arg2.computeValue(context)).thenReturn(numberInfo1);

        result = coreOperationLessThan.computeValue(context);

        assertEquals(Boolean.TRUE, result);

        // Test with NaN values
        when(arg1.computeValue(context)).thenReturn(numberInfoNaN1);
        when(arg2.computeValue(context)).thenReturn(numberInfoNaN2);

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        result = coreOperationLessThan.computeValue(context);

        assertNull(result);

        // Test with Infinity and negative Infinity
        when(arg1.computeValue(context)).thenReturn(new NumberInfo(Double.POSITIVE_INFINITY));
        when(arg2.computeValue(context)).thenReturn(new NumberInfo(Double.NEGATIVE_INFINITY));

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        result = coreOperationLessThan.computeValue(context);

        assertFalse(result);

        // Test with null inputs
        when(arg1.computeValue(context)).thenReturn(null);

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        try {
            coreOperationLessThan.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with different data types (String, Integer, Long, etc.)
        when(arg1.computeValue(context)).thenReturn(new StringInfo("1"));
        when(arg2.computeValue(context)).thenReturn(new NumberInfo(2.0));

        coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        try {
            coreOperationLessThan.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }
}