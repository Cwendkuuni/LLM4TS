package org.apache.commons.math3.util;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with valid input and default epsilon and maxIterations.
     */
    @Test
    public void testEvaluateValidInput() {
        ContinuedFraction cf = new TestContinuedFraction();
        double result = cf.evaluate(1.0);
        assertEquals(4.0, result, 1e-8);
    }

    /**
     * Test evaluate method with valid input, custom epsilon and maxIterations.
     */
    @Test
    public void testEvaluateCustomInput() {
        ContinuedFraction cf = new TestContinuedFraction();
        double result = cf.evaluate(1.0, 1e-10, 100);
        assertEquals(4.0, result, 1e-11);
    }

    /**
     * Test evaluate method with negative input.
     */
    @Test
    public void testEvaluateNegativeInput() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertThrows(ConvergenceException.class, () -> cf.evaluate(-1.0));
    }

    /**
     * Test evaluate method with infinite input.
     */
    @Test
    public void testEvaluateInfiniteInput() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertThrows(ConvergenceException.class, () -> cf.evaluate(Double.POSITIVE_INFINITY));
    }

    /**
     * Test evaluate method with NaN input.
     */
    @Test
    public void testEvaluateNaNInput() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertThrows(ConvergenceException.class, () -> cf.evaluate(Double.NaN));
    }

    /**
     * Test evaluate method with maxIterations reached.
     */
    @Test
    public void testEvaluateMaxIterations() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertThrows(MaxCountExceededException.class, () -> cf.evaluate(1.0, 0.0, 0));
    }

}

class TestContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        switch (n) {
            case 0: return 1.0;
            case 1: return 1.0;
            case 2: return x;
            default: return -x;
        }
    }

    @Override
    protected double getB(int n, double x) {
        switch (n) {
            case 0: return 0.0;
            case 1: return 1.0;
            default: return 1.0;
        }
    }
}