package org.apache.commons.math3.dfp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpDec;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.dfp.DfpFieldContext;
import org.apache.commons.math3.dfp.DfpRoundingMode;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.junit.Test;

/**
 * Tests for the DfpDec class.
 */
public class DfpDecTest {

    /**
     * Test constructor with factory and int.
     *
     * @throws MathIllegalArgumentException if the given int is not supported by the DfpField
     */
    @Test
    public void testConstructorInt() throws MathIllegalArgumentException {
        final DfpFieldContext context = new DfpFieldContext(43, 18, 3, 10000);
        final DfpField factory = new DfpField(context);
        final DfpDec d = new DfpDec(factory, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, d.doubleValueExact(), 0.0);

        try {
            new DfpDec(factory, Integer.MIN_VALUE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test constructor with factory and double.
     */
    @Test
    public void testConstructorDouble() {
        final DfpFieldContext context = new DfpFieldContext(43, 18, 3, 10000);
        final DfpField factory = new DfpField(context);
        final DfpDec d = new DfpDec(factory, Math.PI);
        assertEquals(Math.PI, d.doubleValueExact(), 0.0);
    }

    /**
     * Test method newInstance.
     */
    @Test
    public void testNewInstance() {
        final DfpFieldContext context = new DfpFieldContext(43, 18, 3, 10000);
        final DfpField factory = new DfpField(context);
        final DfpDec d = new DfpDec(factory, Math.PI);
        final DfpDec d2 = d.newInstance();
        assertEquals(Math.PI, d2.doubleValueExact(), 0.0);
    }

    /**
     * Test method newInstance with int argument.
     */
    @Test
    public void testNewInstanceInt() {
        final DfpFieldContext context = new DfpFieldContext(43, 18, 3, 10000);
        final DfpField factory = new DfpField(context);
        final DfpDec d = new DfpDec(factory, Math.PI);
        final DfpDec d2 = d.newInstance(42);
        assertEquals(42, d2.intValueExact(), 0);
    }

    /**
     * Test method round with in = 0.
     */
    @Test
    public void testRoundZero() {
        final DfpFieldContext context = new DfpFieldContext(43, 18, 3, 10000);
        final DfpField factory = new DfpField(context);
        final DfpDec d = new DfpDec(factory, Math.PI);
        final DfpDec zero = d.newInstance(0);
        final int result = d.round(zero.getField(), DfpRoundingMode.ROUND_DOWN);
        assertEquals(0, result);
    }

    /**
     * Test method round with in > 0.
     */
    @Test
    public void testRoundPositive() {
        final DfpFieldContext context = new DfpFieldContext(43, 18, 3, 10000);
        final DfpField factory = new DfpField(context);
        final DfpDec d = new DfpDec(factory, Math.PI);
        final DfpDec one = d.newInstance(1);
        final int result = d.round(one.getField(), DfpRoundingMode.ROUND_UP);
        assertEquals(1, result);
    }

    /**
     * Test method round with in < 0.
     */
    @Test
    public void testRoundNegative() {
        final DfpFieldContext context = new DfpFieldContext(43, 18, 3, 10000);
        final DfpField factory = new DfpField(context);
        final DfpDec d = new DfpDec(factory, -Math.PI);
        final DfpDec one = d.newInstance(1);
        final int result = d.round(one.getField(), DfpRoundingMode.ROUND_DOWN);
        assertEquals(-1, result);
    }

    /**
     * Test method nextAfter.
     */
    @Test
    public void testNextAfter() {
        final DfpFieldContext context = new DfpFieldContext(43, 18, 3, 10000);
        final DfpField factory = new DfpField(context);
        final DfpDec d = new DfpDec(factory, 0.123456789);
        final DfpDec next = d.nextAfter(d.newInstance(0.5));
        assertEquals(0.1235, next.doubleValueExact(), 0.00001);

        try {
            d.nextAfter(null);
            fail("Expected MathUnsupportedOperationException");
        } catch (MathUnsupportedOperationException e) {
            // expected
        }
    }
}