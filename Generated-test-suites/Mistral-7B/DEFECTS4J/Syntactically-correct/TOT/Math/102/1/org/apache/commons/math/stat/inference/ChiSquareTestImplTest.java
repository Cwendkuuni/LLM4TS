package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

/**
 * Test suite for ChiSquareTestImpl class
 */
public class ChiSquareTestImplTest {

    /**
     * Test of chiSquare method, of class ChiSquareTestImpl.
     */
    @Test
    public void testChiSquare() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.0, 3.0, 1.0, 2.0, 2.0};
        double expResult = 2.916666666666667;
        double result = instance.chiSquare(expected, observed);
        assertEquals(expResult, result, 0.001);
    }

    /**
     * Test of chiSquare method with incorrect input sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidSizes() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        double[] expected = {2.0, 3.0, 1.0, 2.0};
        instance.chiSquare(expected, observed);
    }

    /**
     * Test of chiSquare method with negative expected values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareNegativeExpected() {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {-2.0, 3.0, 1.0, 2.0, 2.0};
        instance.chiSquare(expected, observed);
    }

    /**
     * Test of chiSquareTest method, of class ChiSquareTestImpl.
     */
    @Test
    public void testChiSquareTest() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.0, 3.0, 1.0, 2.0, 2.0};
        double expResult = 0.01;
        double result = instance.chiSquareTest(expected, observed);
        assertEquals(expResult, result, 0.001);
    }

    /**
     * Test of chiSquareTest method with incorrect input sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidSizes() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed = {1, 2, 3};
        double[] expected = {2.0, 3.0, 1.0, 2.0, 2.0};
        instance.chiSquareTest(expected, observed);
    }

    /**
     * Test of chiSquareTest method with incorrect distribution.
     */
    @Test(expected = MathException.class)
    public void testChiSquareTestIncorrectDistribution() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5};
        double[] expected = {2.0, 3.0, 1.0, 2.0, 2.0};
        instance.chiSquareTest(expected, observed, new ChiSquaredDistributionImpl(2.0));
    }

    /**
     * Test of chiSquareTestDataSetsComparison method, of class ChiSquareTestImpl.
     */
    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 1};
        double expResult = 3.8414596663243305;
        double result = instance.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(expResult, result, 0.001);
    }

    /**
     * Test of chiSquareTestDataSetsComparison method with incorrect input sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidSizes() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl instance = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2};
        instance.chiSquareTestDataSetsComparison(observed1, observed2);
    }
}