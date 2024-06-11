package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestSupport;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    private CoreOperationLessThan op;
    private EvalContext context;
    private Expression arg1;
    private Expression arg2;

    @Before
    public void setUp() {
        arg1 = new LiteralExpression(new Double(10.0));
        arg2 = new LiteralExpression(new Double(5.0));
        op = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext();
    }

    @Test
    public void testLessThan() {
        // Test positive case
        InfoSet result = new InfoSet();
        context.setInfoSet(result);
        Object value = op.computeValue(context);
        assertTrue((Boolean) value);
    }

    @Test
    public void testGreaterThan() {
        // Test negative case
        InfoSet result = new InfoSet();
        context.setInfoSet(result);
        arg1 = new LiteralExpression(new Double(15.0));
        op = new CoreOperationLessThan(arg1, arg2);
        Object value = op.computeValue(context);
        assertFalse((Boolean) value);
    }

    @Test
    public void testEqual() {
        // Test equal case
        InfoSet result = new InfoSet();
        context.setInfoSet(result);
        arg1 = new LiteralExpression(new Double(5.0));
        op = new CoreOperationLessThan(arg1, arg2);
        Object value = op.computeValue(context);
        assertFalse((Boolean) value);
    }

    @Test
    public void testNullArg1() {
        // Test null arg1
        arg1 = null;
        op = new CoreOperationLessThan(arg1, arg2);
        assertNull(op.computeValue(context));
    }

    @Test
    public void testNullArg2() {
        // Test null arg2
        arg2 = null;
        op = new CoreOperationLessThan(arg1, arg2);
        assertNull(op.computeValue(context));
    }
}