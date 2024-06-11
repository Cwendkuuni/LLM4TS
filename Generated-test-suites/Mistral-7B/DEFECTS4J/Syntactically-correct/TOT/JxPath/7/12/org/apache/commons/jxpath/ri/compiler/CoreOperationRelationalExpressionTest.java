package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.Context;
import org.apache.commons.jxpath.ri.parser.JXPathParser;
import org.junit.Test;

/**
 * Test for {@link CoreOperationRelationalExpression} class.
 *
 * @author John Doe
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression expr = new CoreOperationRelationalExpression(new CoreOperation(">"), new Expression[] { new Constant("5"), new Constant("3") });
        JXPathContext context = new JXPathContext(new Context());
        double result = expr.evaluateNumber(context);
        assertTrue(result > 0);
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression expr = new CoreOperationRelationalExpression(new CoreOperation(">="), new Expression[] { new Constant("5"), new Constant("5") });
        JXPathContext context = new JXPathContext(new Context());
        double result = expr.evaluateNumber(context);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    public void testLessThan() {
        Expression expr = new CoreOperationRelationalExpression(new CoreOperation("<"), new Expression[] { new Constant("3"), new Constant("5") });
        JXPathContext context = new JXPathContext(new Context());
        double result = expr.evaluateNumber(context);
        assertTrue(result < 0);
    }

    @Test
    public void testLessThanOrEqual() {
        Expression expr = new CoreOperationRelationalExpression(new CoreOperation("<="), new Expression[] { new Constant("5"), new Constant("5") });
        JXPathContext context = new JXPathContext(new Context());
        double result = expr.evaluateNumber(context);
        assertEquals(1.0, result, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs() {
        Expression expr = new CoreOperationRelationalExpression(new CoreOperation(">"), new Expression[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOperator() {
        Expression expr = new CoreOperationRelationalExpression(new CoreOperation("INVALID"), new Expression[] { new Constant("5"), new Constant("3") });
    }

}