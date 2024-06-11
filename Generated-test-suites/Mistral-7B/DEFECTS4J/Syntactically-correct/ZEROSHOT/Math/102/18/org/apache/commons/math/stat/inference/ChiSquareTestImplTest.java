package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstruction() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        assertNotNull(testImpl);
    }

    @Test
    public void testChiSquareTestImplWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, testImpl.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidObservedOrExpected() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1.0, 1.0};
        long[] observed = new long[]{1, 2};
        testImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethodWithValidData() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.0, 3.0};
        long[] observed = new long[]{3, 5};
        double chiSquare = testImpl.chiSquare(expected, observed);
        double expectedChiSquare = 2.5564; // calculated using an excel spreadsheet
        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithValidData() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.0, 3.0};
        long[] observed = new long[]{3, 5};
        double pValue = testImpl.chiSquareTest(expected, observed);
        double expectedPValue = 0.2865; // calculated using an excel spreadsheet
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithSmallAlpha() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.0, 3.0};
        long[] observed = new long[]{3, 5};
        boolean canRejectNullHypothesis = testImpl.chiSquareTest(expected, observed, 0.02);
        assertFalse(canRejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestMethodWithLargeAlpha() throws MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{2.0, 3.0};
        long[] observed = new long[]{3, 5};
        boolean canRejectNullHypothesis = testImpl.chiSquareTest(expected, observed, 0.5);
        assertTrue(canRejectNullHypothesis);
    }

    @Test
    public void testChiSquareMethodWithTwoWayTable() throws IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double chiSquare = testImpl.chiSquare(counts);
        double expectedChiSquare = 7.1125; // calculated using an excel spreadsheet
        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithTwoWayTableAndSmallAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        boolean canRejectNullHypothesis = testImpl.chiSquareTest(counts, 0.02);
        assertFalse(canRejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestMethodWithTwoWayTableAndLargeAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        boolean canRejectNullHypothesis = testImpl.chiSquareTest(counts, 0.5);
        assertTrue(canRejectNullHypothesis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidData() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{3};
        long[] observed2 = new long[]{};
        testImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithValidData() throws IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 3, 4};
        double chiSquare = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        double expectedChiSquare = 2.7061; // calculated using an excel spreadsheet
        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidData() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 3, 4};
        double pValue = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.9112; // calculated using an excel spreadsheet
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithSmallAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 3, 4};
        boolean canRejectNullHypothesis = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.02);
        assertFalse(canRejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithLargeAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 3, 4};
        boolean canRejectNullHypothesis = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.5);
        assertTrue(canRejectNullHypothesis);
    }

    @Test
    public void testCheckArray() throws IllegalArgumentException {
        long[][] counts = new long[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        testImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithEmptyTable() throws IllegalArgumentException {
        long[][] counts = new long[][]{};
        testImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithTableLessThanTwoRows() throws IllegalArgumentException {
        long[][] counts = new long[][]{{1, 2}};
        testImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithTableLessThanTwoColumns() throws IllegalArgumentException {
        long[][] counts = new long[][]{1, 2, 3};
        testImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithNegativeEntry() throws IllegalArgumentException {
        long[][] counts = new long[][]{{1, 2, -1}, {2, 3, 4}, {3, 4, 5}};
        testImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithNonRectangularTable() throws IllegalArgumentException {
        long[][] counts = new long[][]{{1, 2}, {2, 3}, {3, 4, 5}};
        testImpl.checkArray(counts);
    }

    private ChiSquareTestImpl testImpl;
}