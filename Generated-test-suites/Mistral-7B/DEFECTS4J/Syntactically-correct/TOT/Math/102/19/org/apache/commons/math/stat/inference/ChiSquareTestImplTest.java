package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test cases for ChiSquareTestImpl class
 */
public class ChiSquareTestImplTest {

    private ChiSquareTestImpl test;

    @Test
    public void testEmptyArrays() {
        test = new ChiSquareTestImpl();
        try {
            test.chiSquare(new double[0], new long[0]);
            test.chiSquareTest(new double[0], new long[0]);
            test.chiSquareTest(new double[0], new long[0], 0.0);
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testNullArrays() {
        test = new ChiSquareTestImpl();
        try {
            test.chiSquare(null, null);
            test.chiSquareTest(null, null);
            test.chiSquareTest(null, null, 0.0);
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testInvalidLength() {
        test = new ChiSquareTestImpl();
        long[] observed = new long[2];
        double[] expected = new double[3];
        try {
            test.chiSquare(expected, observed);
            test.chiSquareTest(expected, observed);
            test.chiSquareTest(expected, observed, 0.0);
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testInvalidInputs() {
        test = new ChiSquareTestImpl();
        long[] observed = new long[2];
        double[] expected = new double[2];
        try {
            test.chiSquare(expected, observed);
            test.chiSquareTest(expected, observed);
            test.chiSquareTest(expected, observed, 1.1);
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testPositiveExpectedValues() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed = new long[2];
        double[] expected = new double[2];
        observed[0] = 1;
        observed[1] = 2;
        expected[0] = 1.5;
        expected[1] = 2.5;
        double chiSquareStat = test.chiSquare(expected, observed);
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(chiSquareStat, 1.0, 0.0001);
        assertEquals(pValue, 0.5, 0.0001);
    }

    @Test
    public void testZeroExpectedValues() throws MathException, IllegalArgumentException {
        test = new ChiSquareTestImpl();
        long[] observed = new long[2];
        double[] expected = new double[2];
        observed[0] = 1;
        observed[1] = 1;
        expected[0] = 0;
        expected[1] = 0;
        try {
            test.chiSquare(expected, observed);
            test.chiSquareTest(expected, observed);
        } catch (NumberIsTooSmallException ex) {
            // expected for zero expected values
        }
    }

    @Test
    public void testNegativeExpectedValues() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed = new long[2];
        double[] expected = new double[2];
        observed[0] = 1;
        observed[1] = 2;
        expected[0] = -1.5;
        expected[1] = -2.5;
        try {
            test.chiSquare(expected, observed);
            test.chiSquareTest(expected, observed);
            test.chiSquareTest(expected, observed, 0.0);
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testLargeArrays() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed = new long[100];
        double[] expected = new double[100];
        for (int i = 0; i < observed.length; i++) {
            observed[i] = 1;
            expected[i] = (i + 1.0) / 2.0;
        }
        double chiSquareStat = test.chiSquare(expected, observed);
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(chiSquareStat, 1.0, 0.0001);
        assertEquals(pValue, 0.5, 0.0001);
    }

    @Test
    public void testDegreesOfFreedom() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed = new long[3];
        double[] expected = new double[3];
        observed[0] = 1;
        observed[1] = 2;
        observed[2] = 1;
        expected[0] = 1.5;
        expected[1] = 2.5;
        expected[2] = 1.5;
        test.setDistribution(new ChiSquaredDistributionImpl(2.0));
        double chiSquareStat = test.chiSquare(expected, observed);
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(chiSquareStat, 4.0, 0.0001);
        assertEquals(pValue, 0.25, 0.0001);

        test.setDistribution(new ChiSquaredDistributionImpl(1.0));
        chiSquareStat = test.chiSquare(expected, observed);
        pValue = test.chiSquareTest(expected, observed);
        assertEquals(chiSquareStat, 6.0, 0.0001);
        assertEquals(pValue, 0.1, 0.0001);

        test.setDistribution(new ChiSquaredDistributionImpl(3.0));
        chiSquareStat = test.chiSquare(expected, observed);
        pValue = test.chiSquareTest(expected, observed);
        assertEquals(chiSquareStat, 3.0, 0.0001);
        assertEquals(pValue, 0.6, 0.0001);
    }

    @Test
    public void testAlpha() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed = new long[3];
        double[] expected = new double[3];
        observed[0] = 1;
        observed[1] = 2;
        observed[2] = 1;
        expected[0] = 1.5;
        expected[1] = 2.5;
        expected[2] = 1.5;
        double alpha = 0.1;
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test
    public void testTwoWayTableComparison() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{2, 3, 4, 5};
        double[] expected = new double[]{2.5, 2.5, 2.5, 2.5};
        double chiSquareStat = test.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(chiSquareStat, 2.778, 0.0001);
        assertEquals(pValue, 0.1, 0.0001);
    }

    @Test
    public void testZeroCounts() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{0, 2, 3, 4};
        long[] observed2 = new long[]{2, 0, 3, 4};
        double[] expected = new double[]{2.5, 2.5, 2.5, 2.5};
        try {
            test.chiSquareDataSetsComparison(observed1, observed2);
            test.chiSquareTestDataSetsComparison(observed1, observed2);
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testDifferentLengths() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{1, 2, 3};
        double[] expected = new double[]{2.5, 2.5, 2.5, 2.5};
        try {
            test.chiSquareDataSetsComparison(observed1, observed2);
            test.chiSquareTestDataSetsComparison(observed1, observed2);
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    @Test
    public void testSameCounts() throws MathException {
        test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{1, 2, 3, 4};
        double[] expected = new double[]{2.5, 2.5, 2.5, 2.5};
        double chiSquareStat = test.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(chiSquareStat, 0.0, 0.0001);
        assertEquals(pValue, 1.0, 0.0001);
    }

}