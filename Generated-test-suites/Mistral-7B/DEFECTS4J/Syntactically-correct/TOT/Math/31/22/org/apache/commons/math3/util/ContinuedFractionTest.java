package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with default epsilon and maxIterations.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0);
        assertEquals(ExpectedValue.ONE, result, EPSILON);
    }

    /**
     * Test evaluate method with custom epsilon and maxIterations.
     */
    @Test
    public void testEvaluateCustom() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0, 1e-12, 100);
        assertEquals(ExpectedValue.ONE, result, 1e-12);
    }

    /**
     * Test evaluate method with maxIterations set to 1.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterations1() {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(1.0, 1e-9, 1);
    }

    /**
     * Test evaluate method with epsilon too small.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateEpsilonTooSmall() {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(1.0, 1e-15, Integer.MAX_VALUE);
    }

    /**
     * Test getA method with valid index.
     */
    @Test
    public void testGetAValidIndex() {
        ContinuedFraction cf = new MyContinuedFraction();
        double expected = 1.0;
        double result = cf.getA(0, 1.0);
        assertEquals(expected, result, EPSILON);
    }

    /**
     * Test getA method with invalid index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAInvalidIndex() {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.getA(-1, 1.0);
    }

    private static class MyContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return (n % 2 == 0) ? 1.0 / (double) (n + 1) : 1.0 + 1.0 / (double) (n + 1);
        }

        @Override
        protected double getB(int n, double x) {
            return n == 0 ? 1.0 : 1.0 - 1.0 / (double) (n + 1);
        }

        private static final double EPSILON = 1e-9;
        private static final double ExpectedValue = 2.0;
    }

}