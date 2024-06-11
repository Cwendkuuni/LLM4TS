package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

/**
 * Test class for the CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Object[] args = new Object[] { new SelfContext(), new SelfContext() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        assertEquals("Expected instance of CoreOperationRelationalExpression",
                CoreOperationRelationalExpression.class, expression.getClass());
    }

    /**
     * Test computeValue method with Boolean values.
     */
    @Test
    public void testComputeValueWithBooleans() {
        Object[] args = new Object[] { new Boolean(true), new Boolean(false) };
        CoreOperationRelationalExpression expression = new CoreOperationGt(args);
        EvalContext context = new EvalContext(Collections.emptyList(), new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue("Expected Boolean.FALSE", result instanceof Boolean);
        assertEquals("Expected Boolean.FALSE", Boolean.FALSE, result);

        args = new Object[] { new Boolean(true), new Boolean(true) };
        expression = new CoreOperationGtEquals(args);
        result = expression.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertEquals("Expected Boolean.TRUE", Boolean.TRUE, result);

        args = new Object[] { new Boolean(false), new Boolean(false) };
        expression = new CoreOperationGt(args);
        result = expression.computeValue(context);
        assertFalse("Expected Boolean.FALSE", result instanceof Boolean);

        args = new Object[] { new Boolean(false), new Boolean(true) };
        expression = new CoreOperationGtEquals(args);
        result = expression.computeValue(context);
        assertFalse("Expected Boolean.FALSE", result instanceof Boolean);
    }

    /**
     * Test computeValue method with numbers.
     */
    @Test
    public void testComputeValueWithNumbers() {
        Object[] args = new Object[] { 3.0, 4.0 };
        CoreOperationRelationalExpression expression = new CoreOperationGt(args);
        EvalContext context = new EvalContext(Collections.emptyList(), new InitialContext());
        Object result = expression.computeValue(context);
        assertTrue("Expected Boolean.FALSE", result instanceof Boolean);
        assertEquals("Expected Boolean.FALSE", Boolean.FALSE, result);

        args = new Object[] { 3.0, 3.0 };
        expression = new CoreOperationGtEquals(args);
        result = expression.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertEquals("Expected Boolean.TRUE", Boolean.TRUE, result);

        args = new Object[] { 1.5, 2.5 };
        expression = new CoreOperationGt(args);
        result = expression.computeValue(context);
        assertFalse("Expected Boolean.FALSE", result instanceof Boolean);

        args = new Object[] { 1.5, 1.5 };
        expression = new CoreOperationGtEquals(args);
        result = expression.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertEquals("Expected Boolean.TRUE", Boolean.TRUE, result);

        args = new Object[] { Double.NaN, Double.NaN };
        expression = new CoreOperationGt(args);
        result = expression.computeValue(context);
        assertFalse("Expected Boolean.FALSE", result instanceof Boolean);
    }

    /**
     * Test computeValue method with Iterators.
     */
    @Test
    public void testComputeValueWithIterators() {
        Object left = Arrays.asList(1, 2, 3).iterator();
        Object right = Arrays.asList(2, 3, 4).iterator();
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Object[] { left, right });
        EvalContext context = new EvalContext(Collections.emptyList(), new InitialContext());
        Object result = expression.computeValue(context);
        assertFalse("Expected Boolean.FALSE", result instanceof Boolean);

        left = Arrays.asList(1, 2, 3).iterator();
        right = Arrays.asList(1, 2, 3).iterator();
        expression = new CoreOperationGtEquals(new Object[] { left, right });
        result = expression.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertEquals("Expected Boolean.TRUE", Boolean.TRUE, result);

        left = Arrays.asList(1, 2, 3).iterator();
        right = Arrays.asList(2, 3, 4).iterator();
        expression = new CoreOperationGt(new Object[] { left, right });
        result = expression.computeValue(context);
        assertFalse("Expected Boolean.FALSE", result instanceof Boolean);

        left = Arrays.asList(1, 2, 3).iterator();
        right = Arrays.asList(1, 2, 3).iterator();
        expression = new CoreOperationGtEquals(new Object[] { left, right });
        result = expression.computeValue(context);
        assertTrue("Expected Boolean.TRUE", result instanceof Boolean);
        assertEquals("Expected Boolean.TRUE", Boolean.TRUE, result);
    }

    /**
     * Test getPrecedence method.
     */
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Object[0][0]);
        int precedence = expression.getPrecedence();
        assertEquals("Expected precedence 3", 3, precedence);
    }

    /**
     * Test isSymmetric method.
     */
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGt(new Object[0][0]);
        assertFalse("Expected method to return false", expression.isSymmetric());
    }
}