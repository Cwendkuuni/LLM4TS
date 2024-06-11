package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.test.AbstractJxPathTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CoreOperationEqual}.
 */
public class CoreOperationEqualTest extends AbstractJxPathTest {

    @Test
    public void testCoreOperationEqual() {
        // create expressions
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new LiteralExpression(1), new LiteralExpression(2));

        // test getSymbol
        assertEquals("=", coreOperationEqual.getSymbol());
        assertNotSame("=", coreOperationCompare.getSymbol());

        // test computeValue
        EvalContext context = createEvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        result = coreOperationCompare.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertNull(result);

        // test equals
        assertNotNull(coreOperationEqual);
        assertSame(coreOperationEqual, coreOperationEqual);
        assertNotSame(coreOperationEqual, null);
        assertNotSame(coreOperationEqual, new Object());

        CoreOperationEqual coreOperationEqual1 = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        assertSame(coreOperationEqual, coreOperationEqual1);

        // test hashCode
        int hashCode = coreOperationEqual.hashCode();
        assertTrue(hashCode >= 0);
        int hashCode1 = coreOperationEqual1.hashCode();
        assertEquals(hashCode, hashCode1);

        // test toString
        String toString = coreOperationEqual.toString();
        assertTrue(toString.contains("="));
        assertTrue(toString.contains("CoreOperationEqual"));
    }
}