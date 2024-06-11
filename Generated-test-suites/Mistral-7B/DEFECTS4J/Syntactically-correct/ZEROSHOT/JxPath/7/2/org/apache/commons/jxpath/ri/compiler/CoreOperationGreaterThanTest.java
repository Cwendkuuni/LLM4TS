package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CoreOperationGreaterThan.
 */
public class CoreOperationGreaterThanTest {

    private InfoSet infoSet;
    private EvalContext context;
    private NodeInfo nodeInfo;

    @Before
    public void setup() {
        infoSet = new InfoSet();
        context = new EvalContext(infoSet);
        nodeInfo = new NodeInfo("5.0", null, null, null, null);
        infoSet.setRootNode(nodeInfo);
    }

    @Test
    public void testCoreOperationGreaterThan() {
        Expression arg1 = new CoreNumber(5.0);
        Expression arg2 = new CoreNumber(3.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue("Expected true, but got " + result, result.booleanValue());
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        Expression arg1 = new CoreNumber(3.0);
        Expression arg2 = new CoreNumber(5.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse("Expected false, but got " + result, result.booleanValue());
    }
}