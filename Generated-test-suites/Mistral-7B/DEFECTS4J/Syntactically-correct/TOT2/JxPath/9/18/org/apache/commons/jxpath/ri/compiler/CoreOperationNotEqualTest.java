package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Constant("5"), new Constant("6"));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("5.0"), new Constant("5"));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("5"), new Constant("5.0"));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("null"), new Constant("5"));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("5"), new Constant("null"));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("5"), null); // NPE test
        try {
            coreOperationNotEqual.computeValue(new EvalContext());
        } catch (NullPointerException e) {
            // Pass
        }

        coreOperationNotEqual = new CoreOperationNotEqual(null, new Constant("5")); // NPE test
        try {
            coreOperationNotEqual.computeValue(new EvalContext());
        } catch (NullPointerException e) {
            // Pass
        }

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("5"), new Constant("5"));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("5"), new Constant("5"));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("abc"), new Constant("5.0")); // Type Mismatch
        try {
            coreOperationNotEqual.computeValue(new EvalContext());
        } catch (ClassCastException e) {
            // Pass
        }

        coreOperationNotEqual = new CoreOperationNotEqual(new Constant("5"), new Constant("abc")); // Type Mismatch
        try {
            coreOperationNotEqual.computeValue(new EvalContext());
        } catch (ClassCastException e) {
            // Pass
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationNotEqualNullArgs() {
        new CoreOperationNotEqual(null, null);
    }
}