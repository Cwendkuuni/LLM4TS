package org.apache.commons.jxpath.ri.compiler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.literals.LiteralExpression;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(1.5);
        Expression arg2 = new LiteralExpression(1.0);
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        Assertions.assertTrue(coreOperationGreaterThan.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        double l = 5.5;
        double r = 2.0;
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(l), new LiteralExpression(r)
        );
        Assertions.assertTrue(coreOperationGreaterThan.computeValue(context).booleanValue());
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(
                new LiteralExpression(1.0), new LiteralExpression(2.0)
        );
        Assertions.assertEquals(">", coreOperationGreaterThan.getSymbol());
    }

    // Add test cases for edge cases and exceptions here

}