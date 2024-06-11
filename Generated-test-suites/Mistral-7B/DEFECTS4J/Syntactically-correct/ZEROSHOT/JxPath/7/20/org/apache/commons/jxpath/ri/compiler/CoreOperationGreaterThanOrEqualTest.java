package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationGreaterThanOrEqualTest {

    @Mock
    private EvalContext context;

    @Mock
    private Expression arg1;

    @Mock
    private Expression arg2;

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setup() {
        when(context.createNumber(ArgumentMatchers.doubleValue(1.0))).thenReturn(new NumberNodeInfo(1.0));
        when(context.createNumber(ArgumentMatchers.doubleValue(2.0))).thenReturn(new NumberNodeInfo(2.0));
        when(context.createNumber(ArgumentMatchers.doubleValue(3.0))).thenReturn(new NumberNodeInfo(3.0));

        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueWithDoubleValues() {
        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(1.0));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(1.0));

        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(1.0));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(2.0));

        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(2.0));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(1.0));

        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(2.0));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(2.0));

        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithBigDecimalValues() {
        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("1.0")));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("1.0")));

        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("1.0")));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("2.0")));

        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("2.0")));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("1.0")));

        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("2.0")));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("2.0")));

        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithMixedValues() {
        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(1.0));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("2.0")));

        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("1.0")));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(1.0));

        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        when(arg1.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("2.0")));
        when(arg2.computeValue(context)).thenReturn(new NumberNodeInfo(new BigDecimal("1.0")));

        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        assertEquals(">=", operation.getSymbol());
    }
}