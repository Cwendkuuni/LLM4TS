package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.test.ExpressionTestBase;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest extends ExpressionTestBase {

    @Test
    public void testCoreOperationNotEqual() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = createContext();
        Object result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(2);
        arg2 = new LiteralExpression(2);
        notEqual = new CoreOperationNotEqual(arg1, arg2);

        result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression("foo");
        arg2 = new LiteralExpression("bar");
        notEqual = new CoreOperationNotEqual(arg1, arg2);

        result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        arg1 = new LiteralExpression(1);
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        notEqual = new CoreOperationNotEqual(compare, new LiteralExpression(true));

        result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        notEqual = new CoreOperationNotEqual(new LiteralExpression(true), compare);

        result = notEqual.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualWithInvalidArguments() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = null;
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(arg1, arg2);
    }
}