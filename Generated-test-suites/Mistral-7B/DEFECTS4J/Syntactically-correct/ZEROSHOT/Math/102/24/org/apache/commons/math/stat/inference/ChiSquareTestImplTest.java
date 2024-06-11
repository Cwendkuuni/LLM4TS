package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    private ChiSquareTestImpl chiSquareTestImpl;

    @Before
    public void setUp() {
        chiSquareTestImpl = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
    }

    /**
     * Test chiSquare method with long[][] input.
     */
    @Test
    public void testChiSquareLongArray() {
        long[][] counts = new long[][]{{1, 2}, {2, 3}, {3, 1}};
        double result = chiSquareTestImpl.chiSquare(counts);
        double expected = 2.0; // degrees of freedom = 2
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test chiSquare method with long[] input.
     */
    @Test
    public void testChiSquareLongArraySingleDistribution() {
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0};
        long[] observed = new long[]{2.0, 3.0, 4.0, 1.0};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        double expectedResult = 3.0; // degrees of freedom = 3 - 1
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test chiSquareTest method with long[] input.
     */
    @Test
    public void testChiSquareTestLongArraySingleDistribution() throws MathException {
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0};
        long[] observed = new long[]{2.0, 3.0, 4.0, 1.0};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        double alpha = 0.05;
        assertTrue(pValue < alpha);
    }

    /**
     * Test chiSquareTest method with long[][] input.
     */
    @Test
    public void testChiSquareTestLongArrayTwoDistributions() throws MathException, IllegalArgumentException {
        long[][] counts1 = new long[][]{{1, 2}, {2, 3}, {3, 1}};
        long[][] counts2 = new long[][]{{2, 3}, {3, 2}, {1, 2}};
        double pValue = chiSquareTestImpl.chiSquareTest(counts1, counts2);
        double alpha = 0.05;
        assertTrue(pValue < alpha);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with long[] input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonLongArraySingleDistribution()
            throws MathException {
        double[] expected = new double[]{1.0, 2.0, 3.0, 4.0};
        long[] observed1 = new long[]{2.0, 3.0, 4.0, 1.0};
        long[] observed2 = new long[]{3.0, 2.0, 1.0, 4.0};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        double alpha = 0.05;
        assertTrue(pValue < alpha);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with long[][] input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonLongArrayTwoDistributions()
            throws MathException, IllegalArgumentException {
        long[][] counts1 = new long[][]{{1, 2}, {2, 3}, {3, 1}};
        long[][] counts2 = new long[][]{{2, 3}, {3, 2}, {1, 2}};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(counts1, counts2);
        double alpha = 0.05;
        assertTrue(pValue < alpha);
    }
}