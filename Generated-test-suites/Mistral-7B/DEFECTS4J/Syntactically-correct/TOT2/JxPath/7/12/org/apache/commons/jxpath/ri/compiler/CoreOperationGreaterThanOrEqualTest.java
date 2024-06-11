
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestSupport;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Constant(5.0), new Constant(5.0));
        EqualEvaluator evaluator = new EqualEvaluator();
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet(TestSupport.createSequence(new Object[] { 5.0, 5.0 })));
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Constant(5.0), new Constant(6.0));
        EqualEvaluator evaluator = new EqualEvaluator();
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet(TestSupport.createSequence(new Object[] { 5.0, 6.0 })));
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationInvalidArgs() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Constant("string"), new Constant(5.0));
        EvalContext context = new EvalContext();
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNull() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Constant(5.0), new Constant(null));
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet(TestSupport.createSequence(new Object[] { 5.0, null })));
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertFalse(result.equals(Boolean.TRUE));
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNaN() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Constant(Double.NaN), new Constant(Double.NaN));
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet(TestSupport.createSequence(new Object[] { Double.NaN, Double.NaN })));
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithNegativeValues() {
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new Constant(-5.0), new Constant(-4.0));
        EqualEvaluator evaluator = new EqualEvaluator();
        EvalContext context = new EvalContext();
        context.setInfoSet(new InfoSet(TestSupport.createSequence(new Object[] { -5.0, -4.0 })));
        Object result = coreOperationGreaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}