package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test case for CoreOperationGreaterThanOrEqual class.
 */
public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationRelationalExpression arg1;
    private CoreOperationRelationalExpression arg2;
    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        Expression arg1Value = new ConstantExpression(new NumberValue(5.0));
        Expression arg2Value = new ConstantExpression(new NumberValue(4.0));

        arg1 = new CoreOperationRelationalExpression(arg1Value, null);
        arg2 = new CoreOperationRelationalExpression(arg2Value, null);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueTrue() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());
    }

    @Test
    public void testComputeValueFalse() {
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext(new Path("/"), infoSet);

        arg1 = new CoreOperationRelationalExpression(new ConstantExpression(new NumberValue(4.0)), null);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse(((Boolean) result).booleanValue());
    }

    @Test
    public void testSymbol() {
        String expected = ">=";

        String result = operation.getSymbol();

        assertEquals(expected, result);
    }
}