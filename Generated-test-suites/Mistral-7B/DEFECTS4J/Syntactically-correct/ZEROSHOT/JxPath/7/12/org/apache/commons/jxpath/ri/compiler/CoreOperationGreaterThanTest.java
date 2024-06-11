package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.apache.commons.jxpath.ri.expressions.VariableExpression;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThanWithNumericValues() {
        // Test with numeric values greater than 0
        Expression arg1 = new VariableExpression("arg1");
        Expression arg2 = new VariableExpression("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        context.setVariable("arg1", new Double(5.0));
        context.setVariable("arg2", new Double(3.0));

        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);

        // Test with numeric values equal
        context.setVariable("arg1", new Double(3.0));
        context.setVariable("arg2", new Double(3.0));

        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);

        // Test with numeric values less than
        context.setVariable("arg1", new Double(3.0));
        context.setVariable("arg2", new Double(5.0));

        result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationGreaterThanWithNullValues() {
        // Test with null values
        Expression arg1 = new VariableExpression("arg1");
        Expression arg2 = new VariableExpression("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        context.setVariable("arg1", null);
        context.setVariable("arg2", new NodeInfo(new Object()));

        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected IllegalArgumentException when at least one operand is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        context.setVariable("arg1", new NodeInfo(new Object()));
        context.setVariable("arg2", null);

        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected IllegalArgumentException when at least one operand is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        context.setVariable("arg1", null);
        context.setVariable("arg2", null);

        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected IllegalArgumentException when at least one operand is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCoreOperationGreaterThanWithMixedValues() {
        // Test with mixed values
        Expression arg1 = new VariableExpression("arg1");
        Expression arg2 = new VariableExpression("arg2");
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext(new InfoSet());
        context.setVariable("arg1", new Double(5.0));
        context.setVariable("arg2", new String("3"));

        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected IllegalArgumentException when operands have incompatible types.");
        } catch (ClassCastException e) {
            // Expected
        }

        context.setVariable("arg1", new String("5"));
        context.setVariable("arg2", new Double(3.0));

        try {
            coreOperationGreaterThan.computeValue(context);
            fail("Expected IllegalArgumentException when operands have incompatible types.");
        } catch (ClassCastException e) {
            // Expected
        }
    }
}