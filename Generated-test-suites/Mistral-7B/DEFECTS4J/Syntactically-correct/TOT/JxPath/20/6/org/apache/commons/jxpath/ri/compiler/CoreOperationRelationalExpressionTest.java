package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test for greater than (>) expression.
     */
    @Test
    public void testGreaterThanExpression() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanExpression(
                new CoreNumberExpression(new ConstantExpression(5)),
                new CoreNumberExpression(new ConstantExpression(3)));

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), new Object[] {});
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(4)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(5.5)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3.1))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(5.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(5.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3.1))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(5.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3.5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(5.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3.6))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test for greater than or equal to (>=) expression.
     */
    @Test
    public void testGreaterThanOrEqualExpression() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanOrEqualExpression(
                new CoreNumberExpression(new ConstantExpression(5)),
                new CoreNumberExpression(new ConstantExpression(3)));

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), new Object[] {});
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(4)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(5)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(5.5)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3.1))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(5.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(5.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3.1))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(5.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3.5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(5.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(3.6))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test for less than (<) expression.
     */
    @Test
    public void testLessThanExpression() {
        CoreOperationRelationalExpression expression = new CoreLessThanExpression(
                new CoreNumberExpression(new ConstantExpression(3)),
                new CoreNumberExpression(new ConstantExpression(5)));

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), new Object[] {});
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(3)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(2)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(3.5)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(3)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(5.1))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(3)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(5.5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(3.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(5.5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(3.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(3.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(4.5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(3.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(4.6))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test for less than or equal to (<=) expression.
     */
    @Test
    public void testLessThanOrEqualExpression() {
        CoreOperationRelationalExpression expression = new CoreLessThanOrEqualExpression(
                new CoreNumberExpression(new ConstantExpression(3)),
                new CoreNumberExpression(new ConstantExpression(5)));

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), new Object[] {});
        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(3)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(4)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(3.5)}), new Object[] {});
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(3)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(5.1))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Integer(3)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(5.5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(3.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(5.5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(3.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(3.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(4.5))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        context = new EvalContext(new InitialContext(new Object[] {new Double(3.5)}), new Object[] {
                new CoreNumberExpression(new ConstantExpression(4.6))
        });
        result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    /**
     * Test for left operand being null and right operand not null.
     */
    @Test(expected = NullPointerException.class)
    public void testLeftOperandIsNull() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanExpression(
                new CoreNullExpression(),
                new CoreNumberExpression(new ConstantExpression(5)));

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), new Object[] {});
        expression.computeValue(context);
    }

    /**
     * Test for left operand not null and right operand being null.
     */
    @Test(expected = NullPointerException.class)
    public void testRightOperandIsNull() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanExpression(
                new CoreNumberExpression(new ConstantExpression(5)),
                new CoreNullExpression());

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), new Object[] {});
        expression.computeValue(context);
    }

    /**
     * Test for both operands being null.
     */
    @Test(expected = NullPointerException.class)
    public void testBothOperandsIsNull() {
        CoreOperationRelationalExpression expression = new CoreGreaterThanExpression(
                new CoreNullExpression(),
                new CoreNullExpression());

        EvalContext context = new EvalContext(new InitialContext(new Object[] {}), new Object[] {});
        expression.computeValue(context);
    }
}