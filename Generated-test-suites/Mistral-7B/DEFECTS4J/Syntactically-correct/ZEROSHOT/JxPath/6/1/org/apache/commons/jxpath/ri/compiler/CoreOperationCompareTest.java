package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationConstant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNumber;
import org.apache.commons.jxpath.ri.compiler.CoreOperationString;
import org.apache.commons.jxpath.ri.compiler.Compiler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for CoreOperationCompare class.
 */
public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        Compiler compiler = new Compiler();
        Expression left = null;
        Expression right = null;

        // Test for equal operation with boolean values
        left = new CoreOperationBoolean(true);
        right = new CoreOperationBoolean(true);
        CoreOperationCompare compare = (CoreOperationCompare) compiler.compile("Boolean(true) = Boolean(true)", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for equal operation with number values
        left = new CoreOperationNumber(1);
        right = new CoreOperationNumber(1);
        compare = (CoreOperationCompare) compiler.compile("1 = 1", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for equal operation with string values
        left = new CoreOperationString("abc");
        right = new CoreOperationString("abc");
        compare = (CoreOperationCompare) compiler.compile("'abc' = 'abc'", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for equal operation with pointers to the same node
        left = new CoreOperationVariable("$node");
        right = new CoreOperationVariable("$node");
        compare = (CoreOperationCompare) compiler.compile("$node = $node", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for equal operation with pointers to different nodes
        left = new CoreOperationVariable("$node1");
        right = new CoreOperationVariable("$node2");
        compare = (CoreOperationCompare) compiler.compile("$node1 = $node2", null, null);
        assertFalse(compare.evaluate(new ExpressionContext()));

        // Test for equal operation with Collection values
        left = new CoreOperationConstant(new Object[] { 1, 2, 3 });
        right = new CoreOperationConstant(new Object[] { 1, 2, 3 });
        compare = (CoreOperationCompare) compiler.compile("(1, 2, 3) = (1, 2, 3)", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for equals operation with different Collection values
        left = new CoreOperationConstant(new Object[] { 1, 2, 3 });
        right = new CoreOperationConstant(new Object[] { 4, 5, 6 });
        compare = (CoreOperationCompare) compiler.compile("(1, 2, 3) = (4, 5, 6)", null, null);
        assertFalse(compare.evaluate(new ExpressionContext()));

        // Test for not equal operation with boolean values
        left = new CoreOperationBoolean(true);
        right = new CoreOperationBoolean(false);
        compare = (CoreOperationCompare) compiler.compile("Boolean(true) != Boolean(false)", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for not equal operation with number values
        left = new CoreOperationNumber(1);
        right = new CoreOperationNumber(2);
        compare = (CoreOperationCompare) compiler.compile("1 != 2", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for not equal operation with string values
        left = new CoreOperationString("abc");
        right = new CoreOperationString("def");
        compare = (CoreOperationCompare) compiler.compile("'abc' != 'def'", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for not equal operation with pointers to the same node
        left = new CoreOperationVariable("$node");
        right = new CoreOperationVariable("$node");
        compare = (CoreOperationCompare) compiler.compile("$node != $node", null, null);
        assertFalse(compare.evaluate(new ExpressionContext()));

        // Test for not equal operation with pointers to different nodes
        left = new CoreOperationVariable("$node1");
        right = new CoreOperationVariable("$node2");
        compare = (CoreOperationCompare) compiler.compile("$node1 != $node2", null, null);
        assertTrue(compare.evaluate(new ExpressionContext()));

        // Test for not equal operation with Collection values
        left = new CoreOperationConstant(new Object[] { 1, 2, 3 });
        right = new CoreOperationConstant(new Object[] { 4, 5, 6 });
        compare = (CoreOperationCompare) compiler.compile("(1, 2, 3) != (4, 5, 6)", null, null);
        assertFalse(compare.evaluate(new ExpressionContext()));

        // Test for not equal operation with different Collection values containing the same elements
        left = new CoreOperationConstant(new Object[] { 1, 2, 3 });
        right = new CoreOperationConstant(new Object[] { 3, 2, 1 });
        compare = (CoreOperationCompare) compiler.compile("(1, 2, 3) != (3, 2, 1)", null, null);
        assertFalse(compare.evaluate(new ExpressionContext()));

        // Test for not equal operation with different Collection values
        left = new CoreOperationConstant(new Object[] { 1, 2, 3 });
        right = new CoreOperationConstant(new Object[] { 4, 5, 6 });
        compare = (CoreOperationCompare) compiler.compile("(1, 2, 3) != (4, 5, 6)", null, null);
        assertFalse(compare.evaluate(new ExpressionContext()));
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null);
    }

}