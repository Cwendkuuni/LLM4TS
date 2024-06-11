package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstruction() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        assertNotNull(testImpl.getDistribution());
    }

    @Test
    public void testChiSquareTestImplConstructionWithChiSquaredDistribution() {
        ChiSquaredDistribution chiSquaredDist = new ChiSquaredDistributionImpl(1);
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl(chiSquaredDist);
        assertEquals(chiSquaredDist, testImpl.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidNullArrayData() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = null;
        long[] observed = {1, 2};
        testImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidSizeMismatchedArrays() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1, 2};
        long[] observed = {1};
        testImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidNegativeExpectedValues() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {-1, 2};
        long[] observed = {1, 2};
        testImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidNegativeObservedValues() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1, 2};
        long[] observed = {-1, 2};
        testImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethod() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2};
        double chiSquare = testImpl.chiSquare(expected, observed);
        double expectedChiSquare = 0.5 * Math.pow((1.0 - 1.0 / 3.0) * 2.0, 2.0)
                + 0.5 * Math.pow((1.0 - 1.0 / 3.0) * 2.0, 2.0);
        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithPValue() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2};
        double pValue = testImpl.chiSquareTest(expected, observed);
        double degreesOfFreedom = expected.length - 1.0;
        ChiSquaredDistribution chiSquaredDist = new ChiSquaredDistributionImpl(degreesOfFreedom);
        double expectedPValue = 1.0 - chiSquaredDist.cumulativeProbability(0.5 * Math.pow(3.0, degreesOfFreedom / 2.0) * Math.exp(-0.5 * degreesOfFreedom * 0.5));
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithSignificanceLevel() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2};
        double alpha = 0.05;
        boolean rejectNullHypothesis = testImpl.chiSquareTest(expected, observed, alpha);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestMethodWithNonRejectNullHypothesis() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2};
        double alpha = 0.1;
        boolean rejectNullHypothesis = testImpl.chiSquareTest(expected, observed, alpha);
        assertFalse(rejectNullHypothesis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidNegativeAlpha() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2};
        double alpha = -0.1;
        testImpl.chiSquareTest(expected, observed, alpha);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithAlphaGreaterThan05() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {1, 2};
        double alpha = 0.6;
        testImpl.chiSquareTest(expected, observed, alpha);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidNonRectangular2WayTable() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {1, 2}, {1}};
        testImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidNegativeInput2WayTable() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{-1, 2}, {1, 2}};
        testImpl.chiSquareTest(counts);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidDegreesOfFreedom() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {1, 2}};
        testImpl.chiSquareTestDataSetsComparison(counts, -1.0);
    }

    @Test
    public void testChiSquareTestMethodWithValidDegreesOfFreedom() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {1, 2}};
        double pValue = testImpl.chiSquareTestDataSetsComparison(counts);
        ChiSquaredDistribution chiSquaredDist = new ChiSquaredDistributionImpl(2.0);
        double expectedPValue = 1.0 - chiSquaredDist.cumulativeProbability(0.5 * Math.pow(3.0, 2.0 / 2.0) * Math.exp(-0.5 * 2.0 * 0.5));
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithSignificanceLevelAndRejectNullHypothesis() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {1, 2}};
        double alpha = 0.05;
        boolean rejectNullHypothesis = testImpl.chiSquareTestDataSetsComparison(counts, alpha);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestMethodWithSignificanceLevelAndNonRejectNullHypothesis() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {1, 2}};
        double alpha = 0.1;
        boolean rejectNullHypothesis = testImpl.chiSquareTestDataSetsComparison(counts, alpha);
        assertFalse(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethodWithValidInputDataSets() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 1};
        double chiSquare = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        double expectedChiSquare = 3.0;
        assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithPValue() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 1};
        double degreesOfFreedom = (observed1.length - 1) * (observed2.length - 1);
        ChiSquaredDistribution chiSquaredDist = new ChiSquaredDistributionImpl(degreesOfFreedom);
        double expectedPValue = 1.0 - chiSquaredDist.cumulativeProbability(0.5 * Math.pow(3.0, degreesOfFreedom / 2.0) * Math.exp(-0.5 * degreesOfFreedom * 0.5));
        double pValue = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidSignificanceLevelAndRejectNullHypothesis() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 1};
        double alpha = 0.05;
        boolean rejectNullHypothesis = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(rejectNullHypothesis);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidSignificanceLevelAndNonRejectNullHypothesis() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {2, 1};
        double alpha = 0.1;
        boolean rejectNullHypothesis = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(rejectNullHypothesis);
    }

}