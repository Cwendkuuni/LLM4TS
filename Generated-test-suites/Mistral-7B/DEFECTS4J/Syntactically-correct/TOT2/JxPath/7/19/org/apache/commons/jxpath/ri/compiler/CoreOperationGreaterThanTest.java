
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        // Test method: CoreOperationGreaterThan(Expression arg1, Expression arg2)
        Expression arg1 = new LiteralExpression(new Double(5.0), null);
        Expression arg2 = new LiteralExpression(new Double(3.0), null);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        NodeInfo nodeInfo = new NodeInfo("", null, null, null, null, null, null);
        infoSet.setNodeInfo(0, nodeInfo);
        context.setInfoSet(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue() {
        // Test method: computeValue(EvalContext context)
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new LiteralExpression(new Double(5.0), null), new LiteralExpression(new Double(3.0), null));
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        NodeInfo nodeInfo = new NodeInfo("", null, null, null, null, null, null);
        infoSet.setNodeInfo(0, nodeInfo);
        context.setInfoSet(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanWithZero() {
        // Test case for edge case: comparing zero with another value
        Expression arg1 = new LiteralExpression(new Double(0.0), null);
        Expression arg2 = new LiteralExpression(new Double(3.0), null);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        NodeInfo nodeInfo = new NodeInfo("", null, null, null, null, null, null);
        infoSet.setNodeInfo(0, nodeInfo);
        context.setInfoSet(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanWithNegativeValues() {
        // Test case for edge case: comparing negative values
        Expression arg1 = new LiteralExpression(new Double(-5.0), null);
        Expression arg2 = new LiteralExpression(new Double(-3.0), null);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        NodeInfo nodeInfo = new NodeInfo("", null, null, null, null, null, null);
        infoSet.setNodeInfo(0, nodeInfo);
        context.setInfoSet(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(!((boolean) result).equals(Boolean.FALSE));
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationGreaterThanWithInvalidExpressionType() {
        // Test error scenario: comparing an integer with a double
        Expression arg1 = new LiteralExpression(new Integer(5), null);
        Expression arg2 = new CoreOperationGreaterThan(new LiteralExpression(new Double(3.0), null), new LiteralExpression(new Integer(3), null));
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        NodeInfo nodeInfo = new NodeInfo("", null, null, null, null, null, null);
        infoSet.setNodeInfo(0, nodeInfo);
        context.setInfoSet(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
    }
}
