package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquare(double[], long[])}.
     */
    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 2, 1, 3};

        assertEquals(6.60251529686, test.chiSquare(expected, observed), 1e-6);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTest(double[], long[])}.
     */
    @Test
    public void testChiSquareTest() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 2, 1, 3};

        double pValue = test.chiSquareTest(expected, observed);
        assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTest(double[], long[], double)}.
     */
    @Test
    public void testChiSquareTestSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] expected = {1.0, 2.0, 1.0, 2.0};
        long[] observed = {2, 2, 1, 3};

        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquare(long[][])}.
     */
    @Test
    public void testChiSquareTwoWayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[][] counts = {{1, 2, 0, 3}, {2, 1, 1, 1}, {3, 1, 2, 1}, {4, 3, 1, 2}};

        assertEquals(7.77945294445, test.chiSquare(counts), 1e-6);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTest(long[][])}.
     */
    @Test
    public void testChiSquareTestTwoWayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[][] counts = {{1, 2, 0, 3}, {2, 1, 1, 1}, {3, 1, 2, 1}, {4, 3, 1, 2}};

        double pValue = test.chiSquareTest(counts);
        assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTest(long[][], double)}.
     */
    @Test
    public void testChiSquareTestSignificanceLevelTwoWayTable() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[][] counts = {{1, 2, 0, 3}, {2, 1, 1, 1}, {3, 1, 2, 1}, {4, 3, 1, 2}};

        boolean result = test.chiSquareTest(counts, 0.05);
        assertTrue(result);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareDataSetsComparison(long[], long[])}.
     */
    @Test
    public void testChiSquareDataSetComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed1 = {2, 2, 1, 1, 1, 1};
        long[] observed2 = {1, 3, 1, 1, 2, 2};

        assertEquals(2.77681261111, test.chiSquareDataSetsComparison(observed1, observed2), 1e-6);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])}.
     */
    @Test
    public void testChiSquareTestDataSetComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed1 = {2, 2, 1, 1, 1, 1};
        long[] observed2 = {1, 3, 1, 1, 2, 2};

        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[], double)}.
     */
    @Test
    public void testChiSquareTestSignificanceLevelDataSetComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed1 = {2, 2, 1, 1, 1, 1};
        long[] observed2 = {1, 3, 1, 1, 2, 2};

        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    /**
     * Test method for {@link ChiSquareTestImpl#checkArray(long[][])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[][] counts = new long[1][];
        counts[0] = new long[] {1, 2, 0, 3};
        test.checkArray(counts);
    }
}