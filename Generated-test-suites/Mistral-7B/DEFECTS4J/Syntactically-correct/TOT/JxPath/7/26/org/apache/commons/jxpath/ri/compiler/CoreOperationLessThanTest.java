package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for CoreOperationLessThan class.
 */
public class CoreOperationLessThanTest {

    @Test
    public void testLessThan() {
        // Test case 1: Normal operation with double values
        CoreOperationLessThan op = new CoreOperationLessThan(
                new CoreExpressionNumber(new NumberNode(1.5)),
                new CoreExpressionNumber(new NumberNode(2.5))
        );
        EvaluationContext context = new EvaluationContext(new InfoSet());
        Boolean result = (Boolean) op.computeValue(context);
        assertTrue(result);

        // Test case 2: Edge case with equal double values
        op = new CoreOperationLessThan(
                new CoreExpressionNumber(new NumberNode(1.5)),
                new CoreExpressionNumber(new NumberNode(1.5))
        );
        context = new EvaluationContext(new InfoSet());
        result = (Boolean) op.computeValue(context);
        assertFalse(result);

        // Test case 3: Error case with non-double values
        op = new CoreOperationLessThan(
                new CoreExpressionNumber(new NumberNode(1)),
                new CoreExpressionNumber(new NumberNode("five"))
        );
        context = new EvaluationContext(new InfoSet());
        try {
            op.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases if needed

    // Test case 4: ...
    // Test case 5: ...
    // Test case 6: ...

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new CoreExpressionNumber(new NumberNode(1.5)),
                new CoreExpressionNumber(new NumberNode(2.5))
        );
        String symbol = op.getSymbol();
        assertEquals("<", symbol);
    }
}