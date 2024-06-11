package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMStrings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual expression;
    private EvalContext context;
    private InfoSet infoSet;

    @Before
    public void setup() {
        Expression arg1 = new LiteralExpression(new Double(5.5));
        Expression arg2 = new LiteralExpression(new Double(4.5));
        expression = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        infoSet = new InfoSet();
        infoSet.setNodeInfo(new NodeInfo("root", null, null, null, null, DOMStrings.fromString("5"), null, null, null, null));
        context.setInfoSet(infoSet);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThan() {
        Expression arg1 = new LiteralExpression(new Double(4.5));
        Expression arg2 = new LiteralExpression(new Double(5.5));
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = greaterThanOrEqual.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqual() {
        Expression arg1 = new LiteralExpression(new Double(5.5));
        Expression arg2 = new LiteralExpression(new Double(5.5));
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result = greaterThanOrEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentMismatch() {
        Expression arg1 = new LiteralExpression(new Integer(5));
        Expression arg2 = new LiteralExpression(new Double(4.5));
        new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new Double(4.5));
        new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }
}