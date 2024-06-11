package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.TestCase;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Number;

public class CoreOperationRelationalExpressionTest extends TestCase {

    // Test case for > method
    public void testGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(5), new Number(3)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() > this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertTrue(expression.evaluate(new JXPathContext()));
    }

    // Test case for >= method with equal values
    public void testGreaterThanOrEqual_equal() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(5), new Number(5)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() >= this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertTrue(expression.evaluate(new JXPathContext()));
    }

    // Test case for >= method with different values
    public void testGreaterThanOrEqual_different() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(5), new Number(3)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() >= this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertTrue(expression.evaluate(new JXPathContext()));
    }

    // Test case for > method with different values
    public void testGreaterThan_different() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(5), new Number(3)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() > this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertFalse(expression.evaluate(new JXPathContext()));
    }

    // Test case for <= method with equal values
    public void testLessThanOrEqual_equal() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(5), new Number(5)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() <= this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertTrue(expression.evaluate(new JXPathContext()));
    }

    // Test case for <= method with different values
    public void testLessThanOrEqual_different() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(3), new Number(5)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() <= this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertFalse(expression.evaluate(new JXPathContext()));
    }

    // Test case for < method with different values
    public void testLessThan_different() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(3), new Number(5)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() < this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertFalse(expression.evaluate(new JXPathContext()));
    }

    // Test case for equals method with equal values
    public void testEquals_equal() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(5), new Number(5)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() == this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertTrue(expression.evaluate(new JXPathContext()));
    }

    // Test case for equals method with different values
    public void testEquals_different() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(5), new Number(3)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context).intValue() == this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertFalse(expression.evaluate(new JXPathContext()));
    }

    // Test case for null left expression
    public void testGreaterThanWithNull() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant(null), new Number(3)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context) != null && this.getLeftExpression().evaluate(context).intValue() > this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertFalse(expression.evaluate(new JXPathContext()));
    }

    // Test case for null right expression
    public void testGreaterThanWithNullRight() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(3), new Constant(null)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context) != null && this.getLeftExpression().evaluate(context).intValue() > this.getRightExpression().evaluate(context) == false;
            }
        };

        assertFalse(expression.evaluate(new JXPathContext()));
    }

    // Test case for equals with null left expression
    public void testEqualsWithNullLeft() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Constant(null), new Number(3)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context) == null || this.getLeftExpression().evaluate(context).intValue() == this.getRightExpression().evaluate(context).intValue();
            }
        };

        assertTrue(expression.evaluate(new JXPathContext()));
    }

    // Test case for equals with null right expression
    public void testEqualsWithNullRight() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Number(3), new Constant(null)) {
            @Override
            public boolean evaluate(JXPathContext context) {
                return this.getLeftExpression().evaluate(context) != null && this.getLeftExpression().evaluate(context).intValue() == this.getRightExpression().evaluate(context).intValue() || this.getRightExpression().evaluate(context) == null;
            }
        };

        assertTrue(expression.evaluate(new JXPathContext()));
    }
}