package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Test for the {@link ContinuedFraction#evaluate(double)} method with a simple continued fraction.
     */
    @Test
    public void testSquareRootOfTwo() {
        ContinuedFraction sqrtTwoCF = new SqrtTwoContinuedFraction();
        double result = sqrtTwoCF.evaluate(2.0);
        double expected = 1.4142135623730955;
        Assert.assertEquals(expected, result, 1e-6);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, double)} method with a simple continued fraction.
     */
    @Test
    public void testSquareRootOfTwoWithEpsilon() {
        ContinuedFraction sqrtTwoCF = new SqrtTwoContinuedFraction();
        double result = sqrtTwoCF.evaluate(2.0, 1e-9);
        double expected = 1.4142135623730955;
        Assert.assertEquals(expected, result, 1e-10);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, int)} method with a simple continued fraction.
     */
    @Test
    public void testSquareRootOfTwoWithMaxIterations() {
        ContinuedFraction sqrtTwoCF = new SqrtTwoContinuedFraction();
        double result = sqrtTwoCF.evaluate(2.0, 10);
        double expected = 1.4142135623730955;
        Assert.assertEquals(expected, result, 1e-6);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double)} method with a non-convergent continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergentContinuedFraction() {
        NonConvergentContinuedFraction nonConvergentCF = new NonConvergentContinuedFraction();
        nonConvergentCF.evaluate(0.5);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, double)} method with a non-convergent continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergentContinuedFractionWithEpsilon() {
        NonConvergentContinuedFraction nonConvergentCF = new NonConvergentContinuedFraction();
        nonConvergentCF.evaluate(0.5, 1e-6);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, int)} method with a non-convergent continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergentContinuedFractionWithMaxIterations() {
        NonConvergentContinuedFraction nonConvergentCF = new NonConvergentContinuedFraction();
        nonConvergentCF.evaluate(0.5, 5);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double)} method with a divergent continued fraction.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivergentContinuedFraction() {
        DivergentContinuedFraction divergentCF = new DivergentContinuedFraction();
        divergentCF.evaluate(1.0);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, double)} method with a divergent continued fraction.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivergentContinuedFractionWithEpsilon() {
        DivergentContinuedFraction divergentCF = new DivergentContinuedFraction();
        divergentCF.evaluate(1.0, 1e-6);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, int)} method with a divergent continued fraction.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivergentContinuedFractionWithMaxIterations() {
        DivergentContinuedFraction divergentCF = new DivergentContinuedFraction();
        divergentCF.evaluate(1.0, 10);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double)} method with a very large number.
     */
    @Test
    public void testEvaluateWithVeryLargeNumber() {
        ContinuedFraction veryLargeContinuedFraction = new VeryLargeContinuedFraction();
        double result = veryLargeContinuedFraction.evaluate(1e308);
        Assert.assertEquals(1.0, result, 1e-10);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, double)} method with a very large number.
     */
    @Test
    public void testEvaluateWithVeryLargeNumberAndEpsilon() {
        ContinuedFraction veryLargeContinuedFraction = new VeryLargeContinuedFraction();
        double result = veryLargeContinuedFraction.evaluate(1e308, 1e-12);
        Assert.assertEquals(1.0, result, 1e-13);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, int)} method with a very large number.
     */
    @Test
    public void testEvaluateWithVeryLargeNumberAndMaxIterations() {
        ContinuedFraction veryLargeContinuedFraction = new VeryLargeContinuedFraction();
        double result = veryLargeContinuedFraction.evaluate(1e308, 100000);
        Assert.assertEquals(1.0, result, 1e-10);
    }

    /**
     * Test for the {@link ContinuedFraction#getA(int, double)} method of a simple continued fraction.
     */
    @Test
    public void testGetA() {
        SqrtTwoContinuedFraction sqrtTwoCF = new SqrtTwoContinuedFraction();
        double result = sqrtTwoCF.getA(1, 2.0);
        double expected = 1.0;
        Assert.assertEquals(expected, result, 1e-6);
    }

    /**
     * Test for the {@link ContinuedFraction#getB(int, double)} method of a simple continued fraction.
     */
    @Test
    public void testGetB() {
        SqrtTwoContinuedFraction sqrtTwoCF = new SqrtTwoContinuedFraction();
        double result = sqrtTwoCF.getB(1, 2.0);
        double expected = 2.0;
        Assert.assertEquals(expected, result, 1e-6);
    }

    /**
     * Test for the {@link ContinuedFraction#evaluate(double, double, int)} method with a simple continued fraction.
     */
    @Test
    public void testEvaluateWithCustomEpsilonAndMaxIterations() {
        SqrtTwoContinuedFraction sqrtTwoCF = new SqrtTwoContinuedFraction();
        double result = sqrtTwoCF.evaluate(2.0, 1e-8, 15);
        double expected = 1.4142135623730955;
        Assert.assertEquals(expected, result, 1e-9);
    }

}