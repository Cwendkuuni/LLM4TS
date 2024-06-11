package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Tests the evaluation of the square root of 2 continued fraction.
     */
    @Test
    public void testSquareRootOfTwo() {
        ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        double result = sqrt2.evaluate(2.0);
        double expected = 1.41421356237309552535061825769;
        assertEquals(expected, result, 1e-10);
    }

    /**
     * Tests the evaluation of the e continued fraction.
     */
    @Test
    public void testE() {
        ContinuedFraction e = new EContinuedFraction();
        double result = e.evaluate(2.71828);
        double expected = 2.7182818284590451082032733533;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the logarithm base 2 continued fraction.
     */
    @Test
    public void testLogBaseTwo() {
        ContinuedFraction log2 = new LogBase2ContinuedFraction();
        double result = log2.evaluate(2.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the logarithm base e continued fraction.
     */
    @Test
    public void testLogBaseE() {
        ContinuedFraction logE = new LogBaseEContinuedFraction();
        double result = logE.evaluate(2.71828);
        double expected = 1.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the sine continued fraction.
     */
    @Test
    public void testSine() {
        ContinuedFraction sine = new SineContinuedFraction();
        double result = sine.evaluate(0.0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-15);

        result = sine.evaluate(FastMath.PI / 2.0);
        expected = 1.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the cosine continued fraction.
     */
    @Test
    public void testCosine() {
        ContinuedFraction cosine = new CosineContinuedFraction();
        double result = cosine.evaluate(0.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-15);

        result = cosine.evaluate(FastMath.PI);
        expected = -1.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the tangent continued fraction.
     */
    @Test
    public void testTangent() {
        ContinuedFraction tangent = new TangentContinuedFraction();
        double result = tangent.evaluate(FastMath.PI / 4.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the cotangent continued fraction.
     */
    @Test
    public void testCotangent() {
        ContinuedFraction cotangent = new CotangentContinuedFraction();
        double result = cotangent.evaluate(FastMath.PI / 4.0);
        double expected = 1.0 / FastMath.SQRT2;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the inverse tangent continued fraction.
     */
    @Test
    public void testArctan() {
        ContinuedFraction arctan = new ArctanContinuedFraction();
        double result = arctan.evaluate(0.0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-15);

        result = arctan.evaluate(1.0);
        expected = FastMath.PI / 4.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the inverse cotangent continued fraction.
     */
    @Test
    public void testArccot() {
        ContinuedFraction arccot = new ArccotContinuedFraction();
        double result = arccot.evaluate(1.0 / FastMath.SQRT2);
        double expected = FastMath.PI / 4.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the inverse sine continued fraction.
     */
    @Test
    public void testArcsin() {
        ContinuedFraction arcsin = new ArcsinContinuedFraction();
        double result = arcsin.evaluate(0.0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-15);

        result = arcsin.evaluate(1.0);
        expected = FastMath.PI / 2.0;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the inverse cosine continued fraction.
     */
    @Test
    public void testArccos() {
        ContinuedFraction arccos = new ArccosContinuedFraction();
        double result = arccos.evaluate(1.0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-15);

        result = arccos.evaluate(-1.0);
        expected = FastMath.PI;
        assertEquals(expected, result, 1e-15);
    }

    /**
     * Tests the evaluation of the continued fraction for x = -1.
     */
    @Test(expected = ConvergenceException.class)
    public void testNegativeX() {
        ContinuedFraction test = new TestContinuedFraction();
        test.evaluate(-1.0);
    }

    /**
     * Tests the evaluation of the continued fraction when epsilon is too small.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEpsilonTooSmall() {
        ContinuedFraction test = new TestContinuedFraction();
        test.evaluate(0.0, 1e-20);
    }

    /**
     * Tests the evaluation of the continued fraction when epsilon is too large.
     */
    @Test(expected = ConvergenceException.class)
    public void testEpsilonTooLarge() {
        ContinuedFraction test = new TestContinuedFraction();
        test.evaluate(0.0, 1.0);
    }

    /**
     * Tests the evaluation of the continued fraction when maxIterations is too small.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsTooSmall() {
        ContinuedFraction test = new TestContinuedFraction();
        test.evaluate(0.0, 10);
    }

    /**
     * Tests the evaluation of the continued fraction when maxIterations is too large.
     */
    @Test(expected = ConvergenceException.class)
    public void testMaxIterationsTooLarge() {
        ContinuedFraction test = new TestContinuedFraction();
        test.evaluate(0.0, Integer.MAX_VALUE + 1);
    }
}