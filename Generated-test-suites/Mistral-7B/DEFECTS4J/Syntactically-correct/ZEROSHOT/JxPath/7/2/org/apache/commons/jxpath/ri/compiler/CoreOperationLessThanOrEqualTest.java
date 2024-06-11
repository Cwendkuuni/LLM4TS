package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.apache.commons.jxpath.ri.expressions.VariableReference;
import org.junit.Test;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual() {
        // Test with double values
        Expression arg1 = new VariableReference("arg1");
        Expression arg2 = new VariableReference("arg2");
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        InfoSet infoSet = createInfoSet();
        EvalContext context = createContext();

        context.setVariable("arg1", 5.0);
        context.setVariable("arg2", 5.0);
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue("5 <= 5 should be true", result.equals(Boolean.TRUE));

        context.setVariable("arg1", 5.0);
        context.setVariable("arg2", 6.0);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse("5 <= 6 should be false", result.equals(Boolean.TRUE));

        context.setVariable("arg1", 5.0);
        context.setVariable("arg2", 5.0);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue("5 <= 5 should be true", result.equals(Boolean.TRUE));

        context.setVariable("arg1", 6.0);
        context.setVariable("arg2", 5.0);
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue("6 <= 5 should be false", !result.equals(Boolean.TRUE));

        // Test with NodeInfos
        arg1 = new VariableReference("arg1");
        arg2 = new VariableReference("arg2");
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        infoSet = createInfoSet();
        context = createContext();

        NodeInfo arg1NodeInfo = new NodeInfo(1, "arg1", null);
        NodeInfo arg2NodeInfo = new NodeInfo(1, "arg2", null);
        context.setVariable("arg1", arg1NodeInfo);
        context.setVariable("arg2", arg2NodeInfo);

        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse("Comparison of NodeInfos should fail", result instanceof Boolean);

        arg1NodeInfo.setValue("5.0");
        arg2NodeInfo.setValue("5.0");
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue("5.0 <= 5.0 should be true", result.equals(Boolean.TRUE));

        arg1NodeInfo.setValue("5.0");
        arg2NodeInfo.setValue("6.0");
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse("5.0 <= 6.0 should be false", result.equals(Boolean.TRUE));

        arg1NodeInfo.setValue("5.0");
        arg2NodeInfo.setValue("5.0");
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertTrue("5.0 <= 5.0 should be true", result.equals(Boolean.TRUE));

        arg1NodeInfo.setValue("6.0");
        arg2NodeInfo.setValue("5.0");
        result = coreOperationLessThanOrEqual.computeValue(context);
        assertFalse("6.0 <= 5.0 should be false", !result.equals(Boolean.TRUE));
    }

    private InfoSet createInfoSet() {
        return new InfoSet();
    }

    private EvalContext createContext() {
        return new EvalContext();
    }
}