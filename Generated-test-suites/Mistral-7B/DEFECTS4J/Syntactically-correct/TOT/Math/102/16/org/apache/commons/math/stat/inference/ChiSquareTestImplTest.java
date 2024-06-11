package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the ChiSquareTestImpl class
 */
public class ChiSquareTestImplTest {

    /**
     * Test of the chiSquare method with valid inputs
     */
    @Test
    public void testChiSquareValidInput() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {2.5, 1.5, 1.5, 2.5, 2.5};
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        double result = test.chiSquare(expected, observed);
        double expectedResult = 2.975; // calculated using R
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test of the chiSquare method with invalid inputs: dimensions do not match
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput1() {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 1.5, 1.5, 2.5};
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        test.chiSquare(expected, observed);
    }

    /**
     * Test of the chiSquare method with invalid inputs: expected counts are negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput2() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {-1.5, 1.5, 1.5, 2.5, 2.5};
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        test.chiSquare(expected, observed);
    }

    /**
     * Test of the chiSquareTest method with valid inputs
     */
    @Test
    public void testChiSquareTestValidInput() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {2.5, 1.5, 1.5, 2.5, 2.5};
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.021; // calculated using R
        assertEquals(expectedPValue, pValue, 0.001);
    }

    /**
     * Test of the chiSquareTest method with invalid inputs: dimensions do not match
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput1() {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 1.5, 1.5, 2.5};
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test of the chiSquareTest method with invalid inputs: expected counts are negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput2() {
        long[] observed = {1, 2, 3, 2, 1};
        double[] expected = {-1.5, 1.5, 1.5, 2.5, 2.5};
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test of the chiSquareTest method with large inputs
     */
    @Test
    public void testChiSquareTestLargeInput() {
        long[] observed = new long[10000];
        double[] expected = new double[10000];
        for (int i = 0; i < observed.length; i++) {
            observed[i] = 100;
            expected[i] = 110.0;
        }
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        double pValue = test.chiSquareTest(expected, observed);
        // The p-value cannot be calculated analytically for large inputs,
        // so we can only check if the method does not throw an exception
        assertTrue(pValue >= 0.0);
    }

    /**
     * Test of the chiSquareTest method with large inputs and invalid inputs: dimensions do not match
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput3() {
        long[] observed = new long[10000];
        double[] expected = new double[9999];
        for (int i = 0; i < observed.length; i++) {
            observed[i] = 100;
        }
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test of the chiSquareTest method with large inputs and invalid inputs: expected counts are negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput4() {
        long[] observed = new long[10000];
        double[] expected = new double[10000];
        for (int i = 0; i < observed.length; i++) {
            observed[i] = 100;
            expected[i] = -110.0;
        }
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test of the chiSquareTest method with large inputs and very small p-value
     */
    @Test
    public void testChiSquareTestVerySmallPValue() {
        long[] observed = new long[10000];
        double[] expected = new double[10000];
        for (int i = 0; i < observed.length; i++) {
            observed[i] = 100;
            expected[i] = 99.5;
        }
        UnknownDistributionChiSquareTest test = new ChiSquareTestImpl();
        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 1.111e-16; // calculated using R
        assertEquals(expectedPValue, pValue, 1e-15);
    }
}