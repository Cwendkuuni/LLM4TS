package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
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
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.evaluate(1.0);
        double expected = 1.648051701859880320606381572511;
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test evaluate method with custom epsilon.
     */
    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.evaluate(1.0, 1e-14);
        double expected = 1.648051701859880320606381572511;
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test evaluate method with custom maxIterations.
     */
    @Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.evaluate(1.0, 1e-10, 10); ;
        double expected = 1.6480517018598803206;
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test evaluate method with epsilon and maxIterations smaller than allowed.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateTooSmallEpsilon() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.evaluate(1.0, 1e-15);
    }

    /**
     * Test evaluate method with maxIterations smaller than allowed.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateTooSmallMaxIterations() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.evaluate(1.0, 1e-10, 5);
    }

    /**
     * Test evaluate method with infinite value.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteValue() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.evaluate(Double.POSITIVE_INFINITY);
    }

    /**
     * Test evaluate method with NaN value.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNaNValue() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.evaluate(Double.NaN);
    }

    /**
     * Test getA method with valid index and value.
     */
    @Test
    public void testGetAValid() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.getA(0, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test getA method with invalid index and value.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetAInvalidIndex() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.getA(-1, 1.0);
    }

    /**
     * Test getB method with valid index and value.
     */
    @Test
    public void testGetBValid() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.getB(0, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Test getB method with invalid index and value.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetBInvalidIndex() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        continuedFraction.getB(-1, 1.0);
    }
}

class MyContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return FastMath.sin(x) / (x + n);
    }

    @Override
    protected double getB(int n, double x) {
        return FastMath.cos(x) / (x + n);
    }
}