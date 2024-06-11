package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with simple continued fraction.
     */
    @Test
    public void testEvaluateSqrt2() {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        double result = sqrt2.evaluate(2.0);
        assertEquals(Math.sqrt(2), result, 1e-7);
    }

    /**
     * Test evaluate method with non-converging continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNonConverging() {
        ContinuedFraction nonConverging = new NonConvergingContinuedFraction();
        nonConverging.evaluate(0.0);
    }

    /**
     * Test evaluate method with infinite continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfinite() {
        ContinuedFraction infinite = new InfiniteContinuedFraction();
        infinite.evaluate(0.0);
    }

    /**
     * Test evaluate method with nan continued fraction.
     */
    @Test(expected = ArithmeticException.class)
    public void testEvaluateNaN() {
        ContinuedFraction nan = new NaNContinuedFraction();
        nan.evaluate(0.0);
    }

    /**
     * Test evaluate method with max iterations reached.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterations() {
        ContinuedFraction maxIterations = new MaxIterationsContinuedFraction();
        maxIterations.evaluate(0.0, 1);
    }

    /**
     * Test evaluate method with small epsilon.
     */
    @Test
    public void testEvaluateSmallEpsilon() {
        ContinuedFraction smallEpsilon = new SmallEpsilonContinuedFraction();
        double result = smallEpsilon.evaluate(2.0, 1e-12);
        assertEquals(Math.sqrt(2), result, 1e-13);
    }

}

class Sqrt2ContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return 1.0 / (2.0 * x + 2.0 * n + 1.0);
        } else {
            return 2.0 * x;
        }
    }

    @Override
    protected double getB(int n, double x) {
        return 2.0 * (n + 1.0);
    }
}

class NonConvergingContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return 1.0 / (x + n);
    }

    @Override
    protected double getB(int n, double x) {
        return x + n;
    }
}

class InfiniteContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return 2.0 * x;
    }

    @Override
    protected double getB(int n, double x) {
        return 2.0 * x;
    }
}

class NaNContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return Double.NaN;
    }

    @Override
    protected double getB(int n, double x) {
        return 2.0;
    }
}

class MaxIterationsContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return 1.0 / (x + n);
    }

    @Override
    protected double getB(int n, double x) {
        return 2.0 * (n + 1.0);
    }
}

class SmallEpsilonContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return Math.pow(x, n);
    }

    @Override
    protected double getB(int n, double x) {
        return Math.pow(x, n) + 1.0;
    }
}