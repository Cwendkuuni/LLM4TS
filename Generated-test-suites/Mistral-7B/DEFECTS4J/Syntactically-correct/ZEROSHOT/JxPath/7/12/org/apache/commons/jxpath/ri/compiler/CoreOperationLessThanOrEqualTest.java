package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NumberTest;

public class CoreOperationLessThanOrEqualTest extends TestCase {

    /**
     * Test constructor.
     */
    public void testConstructor() {
        Expression arg1 = ExpressionFactory.createNodeTest(NodeTest.CHILD);
        Expression arg2 = ExpressionFactory.createNumberTest(NumberTest.NUMBER, 5.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("Expected 2 arguments", 2, coreOperationLessThanOrEqual.getArgs().length);
        assertInstanceOf("Expected CoreOperationRelationalExpression", CoreOperationRelationalExpression.class, coreOperationLessThanOrEqual);
    }

    /**
     * Test computeValue method with NodeTest and NumberTest as arguments.
     */
    public void testComputeValueNodeTestAndNumberTest() {
        Expression arg1 = ExpressionFactory.createNodeTest(NodeTest.CHILD);
        Expression arg2 = ExpressionFactory.createNumberTest(NumberTest.NUMBER, 5.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet(new Object[]{1.0, 2.0, 3.0, 4.0, 5.0});
        EvalContext context = new EvalContext(null, infoSet);
        Object value = coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertTrue("Expected Boolean.FALSE", Boolean.FALSE.equals(value));

        infoSet = new InfoSet(new Object[]{1.0, 2.0, 3.0, 4.5, 5.0});
        context = new EvalContext(null, infoSet);
        value = coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertTrue("Expected Boolean.TRUE", Boolean.TRUE.equals(value));
    }

    /**
     * Test computeValue method with NumberTest and NumberTest as arguments.
     */
    public void testComputeValueNumberTestAndNumberTest() {
        Expression arg1 = ExpressionFactory.createNumberTest(NumberTest.NUMBER, 5.0);
        Expression arg2 = ExpressionFactory.createNumberTest(NumberTest.NUMBER, 5.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        InfoSet infoSet = new InfoSet(new Object[]{1.0, 2.0, 3.0, 4.5, 5.0});
        EvalContext context = new EvalContext(null, infoSet);
        Object value = coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertTrue("Expected Boolean.TRUE", Boolean.TRUE.equals(value));

        infoSet = new InfoSet(new Object[]{1.0, 2.0, 3.0, 4.5, 6.0});
        context = new EvalContext(null, infoSet);
        value = coreOperationLessThanOrEqual.computeValue(context);
        Assert.assertTrue("Expected Boolean.FALSE", Boolean.FALSE.equals(value));
    }

    /**
     * Test getSymbol method.
     */
    public void testGetSymbol() {
        Expression arg1 = ExpressionFactory.createNumberTest(NumberTest.NUMBER, 5.0);
        Expression arg2 = ExpressionFactory.createNumberTest(NumberTest.NUMBER, 5.0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        assertEquals("Expected '<='", "<=", symbol);
    }
}