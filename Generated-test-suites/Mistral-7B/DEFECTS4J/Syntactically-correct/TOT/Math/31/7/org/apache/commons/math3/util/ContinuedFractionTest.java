package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

/**
 * Test cases for the ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluating the golden ratio continued fraction at the default epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testGoldenRatio() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double result = goldenRatio.evaluate(1.0);
        double expected = (1.0 + Math.sqrt(5.0)) / 2.0;
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test evaluating the golden ratio continued fraction with a small epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testGoldenRatioSmallEpsilon() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double result = goldenRatio.evaluate(1.0, 1e-15);
        double expected = (1.0 + Math.sqrt(5.0)) / 2.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test evaluating the golden ratio continued fraction with a large epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testGoldenRatioLargeEpsilon() {
        ContinuedFraction goldenRatio = new GoldenRatioContinuedFraction();
        double result = goldenRatio.evaluate(1.0, 1e-2);
        double expected = (1.0 + Math.sqrt(5.0)) / 2.0;
        assertEquals(expected, result, 1e-2);
    }

    /**
     * Test evaluating the square root of 2 continued fraction at the default epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testSquareRootOfTwo() {
        ContinuedFraction sqrt2 = new SquareRootOfTwoContinuedFraction();
        double result = sqrt2.evaluate(1.0);
        double expected = Math.sqrt(2.0);
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test evaluating the square root of 2 continued fraction with a small epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testSquareRootOfTwoSmallEpsilon() {
        ContinuedFraction sqrt2 = new SquareRootOfTwoContinuedFraction();
        double result = sqrt2.evaluate(1.0, 1e-15);
        double expected = Math.sqrt(2.0);
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test evaluating the square root of 2 continued fraction with a large epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testSquareRootOfTwoLargeEpsilon() {
        ContinuedFraction sqrt2 = new SquareRootOfTwoContinuedFraction();
        double result = sqrt2.evaluate(1.0, 1e-2);
        double expected = Math.sqrt(2.0);
        assertEquals(expected, result, 1e-2);
    }

    /**
     * Test evaluating the square root of -1 continued fraction at the default epsilon
     * and maximum iterations, which should throw a ConvergenceException.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testSquareRootOfMinusOne() {
        ContinuedFraction sqrtMinusOne = new SquareRootOfMinusOneContinuedFraction();
        sqrtMinusOne.evaluate(1.0);
    }

    /**
     * Test evaluating the logarithm base e continued fraction at the default epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseE() {
        ContinuedFraction logBaseE = new LogarithmBaseEContinuedFraction();
        double result = logBaseE.evaluate(1.0);
        double expected = Math.log(1.0) / Math.log(Math.E);
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test evaluating the logarithm base e continued fraction with a small epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseESmallEpsilon() {
        ContinuedFraction logBaseE = new LogarithmBaseEContinuedFraction();
        double result = logBaseE.evaluate(1.0, 1e-15);
        double expected = Math.log(1.0) / Math.log(Math.E);
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test evaluating the logarithm base e continued fraction with a large epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseELargeEpsilon() {
        ContinuedFraction logBaseE = new LogarithmBaseEContinuedFraction();
        double result = logBaseE.evaluate(1.0, 1e-2);
        double expected = Math.log(1.0) / Math.log(Math.E);
        assertEquals(expected, result, 1e-2);
    }

    /**
     * Test evaluating the logarithm base e continued fraction with a negative argument,
     * which should throw an IllegalArgumentException.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLogarithmBaseENegativeArgument() {
        ContinuedFraction logBaseE = new LogarithmBaseEContinuedFraction();
        logBaseE.evaluate(-1.0);
    }

    /**
     * Test evaluating the logarithm base e continued fraction with a very large positive
     * argument, which should throw a ConvergenceException.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testLogarithmBaseELargeArgument() {
        ContinuedFraction logBaseE = new LogarithmBaseEContinuedFraction();
        logBaseE.evaluate(FastMath.nextDouble(1000000.0));
    }

    /**
     * Test evaluating the logarithm base 2 continued fraction at the default epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseTwo() {
        ContinuedFraction logBaseTwo = new LogarithmBaseTwoContinuedFraction();
        double result = logBaseTwo.evaluate(1.0);
        double expected = Math.log(1.0) / Math.log(2.0);
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test evaluating the logarithm base 2 continued fraction with a small epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseTwoSmallEpsilon() {
        ContinuedFraction logBaseTwo = new LogarithmBaseTwoContinuedFraction();
        double result = logBaseTwo.evaluate(1.0, 1e-15);
        double expected = Math.log(1.0) / Math.log(2.0);
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test evaluating the logarithm base 2 continued fraction with a large epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseTwoLargeEpsilon() {
        ContinuedFraction logBaseTwo = new LogarithmBaseTwoContinuedFraction();
        double result = logBaseTwo.evaluate(1.0, 1e-2);
        double expected = Math.log(1.0) / Math.log(2.0);
        assertEquals(expected, result, 1e-2);
    }

    /**
     * Test evaluating the logarithm base 2 continued fraction with a negative argument,
     * which should throw an IllegalArgumentException.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLogarithmBaseTwoNegativeArgument() {
        ContinuedFraction logBaseTwo = new LogarithmBaseTwoContinuedFraction();
        logBaseTwo.evaluate(-1.0);
    }

    /**
     * Test evaluating the logarithm base 2 continued fraction with a very large positive
     * argument, which should throw a ConvergenceException.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testLogarithmBaseTwoLargeArgument() {
        ContinuedFraction logBaseTwo = new LogarithmBaseTwoContinuedFraction();
        logBaseTwo.evaluate(FastMath.nextDouble(1000000.0));
    }

    /**
     * Test evaluating the logarithm base 10 continued fraction at the default epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseTen() {
        ContinuedFraction logBaseTen = new LogarithmBaseTenContinuedFraction();
        double result = logBaseTen.evaluate(1.0);
        double expected = Math.log(1.0) / Math.log(10.0);
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test evaluating the logarithm base 10 continued fraction with a small epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseTenSmallEpsilon() {
        ContinuedFraction logBaseTen = new LogarithmBaseTenContinuedFraction();
        double result = logBaseTen.evaluate(1.0, 1e-15);
        double expected = Math.log(1.0) / Math.log(10.0);
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test evaluating the logarithm base 10 continued fraction with a large epsilon
     * and maximum iterations.
     */
    @org.junit.Test
    public void testLogarithmBaseTenLargeEpsilon() {
        ContinuedFraction logBaseTen = new LogarithmBaseTenContinuedFraction();
        double result = logBaseTen.evaluate(1.0, 1e-2);
        double expected = Math.log(1.0) / Math.log(10.0);
        assertEquals(expected, result, 1e-2);
    }

    /**
     * Test evaluating the logarithm base 10 continued fraction with a negative argument,
     * which should throw an IllegalArgumentException.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLogarithmBaseTenNegativeArgument() {
        ContinuedFraction logBaseTen = new LogarithmBaseTenContinuedFraction();
        logBaseTen.evaluate(-1.0);
    }

    /**
     * Test evaluating the logarithm base 10 continued fraction with a very large positive
     * argument, which should throw a ConvergenceException.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testLogarithmBaseTenLargeArgument() {
        ContinuedFraction logBaseTen = new LogarithmBaseTenContinuedFraction();
        logBaseTen.evaluate(FastMath.nextDouble(1000000.0));
    }

    /**
     * Test evaluating the e continued fraction at the default epsilon and maximum iterations.
     */
    @org.junit.Test
    public void testEContinuedFraction() {
        ContinuedFraction e = new EContinuedFraction();
        double result = e.evaluate(1.0);
        double expected = Math.E;
        assertEquals(expected, result, 1e-8);
    }

    /**
     * Test evaluating the e continued fraction with a small epsilon and maximum iterations.
     */
    @org.junit.Test
    public void testEContinuedFractionSmallEpsilon() {
        ContinuedFraction e = new EContinuedFraction();
        double result = e.evaluate(1.0, 1e-15);
        double expected = Math.E;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Test evaluating the e continued fraction with a large epsilon and maximum iterations.
     */
    @org.junit.Test
    public void testEContinuedFractionLargeEpsilon() {
        ContinuedFraction e = new EContinuedFraction();
        double result = e.evaluate(1.0, 1e-2);
        double expected = Math.E;
        assertEquals(expected, result, 1e-2);
    }

    /**
     * Test evaluating the e continued fraction with a very large positive argument,
     * which should throw a ConvergenceException.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEContinuedFractionLargeArgument() {
        ContinuedFraction e = new EContinuedFraction();
        e.evaluate(FastMath.nextDouble(1000000.0));
    }
}