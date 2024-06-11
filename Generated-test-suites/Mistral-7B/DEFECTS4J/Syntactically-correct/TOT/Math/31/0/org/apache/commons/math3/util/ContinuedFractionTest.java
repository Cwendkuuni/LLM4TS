package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Tests the evaluation method with default epsilon and maxIterations.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0);
        double expected = 3.0;
        assertEquals(expected, result, 1e-9);
    }

    /**
     *Tests the evaluation method with epsilon less than default value.
     */
    @Test
    public void testEvaluateEpsilonLess() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0, 1e-12);
        double expected = 3.0;
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Tests the evaluation method with maxIterations less than default value.
     */
    @Test
    public void testEvaluateMaxIterationsLess() {
        ContinuedFraction cf = new MyContinuedFraction();
        try {
            cf.evaluate(1.0, 1e-9, 10);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    /**
     * Tests the evaluation method with negative x value.
     */
    @Test
    public void testEvaluateNegativeX() {
        ContinuedFraction cf = new MyContinuedFraction();
        try {
            cf.evaluate(-1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Tests the evaluation method with infinite x value.
     */
    @Test
    public void testEvaluateInfiniteX() {
        ContinuedFraction cf = new MyContinuedFraction();
        try {
            cf.evaluate(Double.POSITIVE_INFINITY);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Tests the evaluation method with NaN x value.
     */
    @Test
    public void testEvaluateNaNX() {
        ContinuedFraction cf = new MyContinuedFraction();
        try {
            cf.evaluate(Double.NaN);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Tests the getA and getB methods.
     */
    @Test
    public void testGetAAndB() {
        ContinuedFraction cf = new MyContinuedFraction();
        double a = cf.getA(1, 1.0);
        double b = cf.getB(1, 1.0);
        double expectedA = 2.0;
        double expectedB = 1.0;
        assertEquals(expectedA, a, 1e-9);
        assertEquals(expectedB, b, 1e-9);
    }

    /**
     * Tests the evaluate method with a multiplication by zero.
     */
    @Test
    public void testEvaluateMultiplyByZero() {
        ContinuedFraction cf = new MyContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                if (n == 1) {
                    return 0.0;
                }
                return super.getA(n, x);
            }
        };
        try {
            cf.evaluate(1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Tests the evaluate method with a division by zero.
     */
    @Test
    public void testEvaluateDivideByZero() {
        ContinuedFraction cf = new MyContinuedFraction() {
            @Override
            protected double getB(int n, double x) {
                if (n == 1) {
                    return 0.0;
                }
                return super.getB(n, x);
            }
        };
        try {
            cf.evaluate(1.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test class for the continued fraction.
     */
    private static class MyContinuedFraction extends ContinuedFraction {
        // Empty implementation
    }
}