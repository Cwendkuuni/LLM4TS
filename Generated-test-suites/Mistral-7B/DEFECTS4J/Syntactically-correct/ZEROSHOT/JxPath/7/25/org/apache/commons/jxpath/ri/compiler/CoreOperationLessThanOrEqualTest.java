package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.AbstractJXPathTest;
import org.apache.commons.jxpath.ri.test.TestUtils;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.nodes.NodeInfo;

/**
 * Test class for CoreOperationLessThanOrEqual.
 */
public class CoreOperationLessThanOrEqualTest extends AbstractJXPathTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link CoreOperationLessThanOrEqual#CoreOperationLessThanOrEqual(Expression, Expression)}.
     */
    @Test
    public void testCoreOperationLessThanOrEqual() {
        Expression arg1 = new CoreNumber(1.0);
        Expression arg2 = new CoreNumber(2.0);
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals(Boolean.CLASS, expr.getClass());
        assertEquals(2, expr.size());
    }

    /**
     * Test method for {@link CoreOperationLessThanOrEqual#computeValue(EvalContext)}.
     */
    @Test
    public void testComputeValue() {
        Expression arg1 = new CoreNumber(1.0);
        Expression arg2 = new CoreNumber(2.0);
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(arg1, arg2);

        InfoSet infoSet = TestUtils.createSimpleInfoSet(new NodeInfo(1));
        EvalContext context = createContext(infoSet);

        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        infoSet = TestUtils.createSimpleInfoSet(new NodeInfo(1.5));
        context = createContext(infoSet);

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        infoSet = TestUtils.createSimpleInfoSet(new NodeInfo(1.5));
        context = createContext(infoSet);

        arg1 = new CoreNumber(1.5);
        expr = new CoreOperationLessThanOrEqual(arg1, arg2);

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        infoSet = TestUtils.createSimpleInfoSet(new NodeInfo(2.0));
        context = createContext(infoSet);

        arg1 = new CoreNumber(1.5);
        expr = new CoreOperationLessThanOrEqual(arg1, arg2);

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        infoSet = TestUtils.createSimpleInfoSet(new NodeInfo(1.5));
        context = createContext(infoSet);

        arg1 = new CoreNumber(1.5);
        arg2 = new CoreNumber(1.5);
        expr = new CoreOperationLessThanOrEqual(arg1, arg2);

        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    /**
     * Test method for {@link CoreOperationLessThanOrEqual#getSymbol()}.
     */
    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreNumber(1.0);
        Expression arg2 = new CoreNumber(2.0);
        CoreOperationLessThanOrEqual expr = new CoreOperationLessThanOrEqual(arg1, arg2);

        String symbol = expr.getSymbol();
        assertEquals("<=", symbol);
    }
}