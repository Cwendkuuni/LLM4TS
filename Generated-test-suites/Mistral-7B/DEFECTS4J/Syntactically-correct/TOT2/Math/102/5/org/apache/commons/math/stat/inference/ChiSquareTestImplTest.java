package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl(); // default constructor
        ChiSquareTestImpl testImpl1 = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        assertEquals(1.0, testImpl.getDistribution().getDegreesOfFreedom(), 0.0);
        assertEquals(1.0, testImpl1.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testChiSquareMethodWithBasicInput() throws IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 3.0, 2.0};
        long[] observed = {3, 4, 2, 3}; // expected and observed arrays must have same length
        double chiSquareStatistic = testImpl.chiSquare(expected, observed);
        assertEquals(7.417633235, chiSquareStatistic, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidInput() throws IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {2.0};
        long[] observed = {3, 4, 2}; // expected and observed arrays must have same length
        testImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithBasicInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 3.0, 2.0};
        long[] observed = {3, 4, 2, 3}; // expected and observed arrays must have same length
        double pValue = testImpl.chiSquareTest(expected, observed);
        assertEquals(0.061719625, pValue, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {2.0};
        long[] observed = {3, 4, 2}; // expected and observed arrays must have same length
        testImpl.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] expected = {2.0, 3.0, 3.0, 2.0};
        long[] observed = {3, 4, 2, 3}; // expected and observed arrays must have same length
        boolean result = testImpl.chiSquareTest(expected, observed, 0.05);
        assertFalse(result);
    }

    @Test
    public void testChiSquareMethodWith2DArrayInput() throws IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{2, 3, 1}, {3, 3, 2}, {1, 2, 3}};
        double chiSquareStatistic = testImpl.chiSquare(counts);
        assertEquals(7.0, chiSquareStatistic, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalid2DArrayInput() throws IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{2}, {3, 2}, {1, 2, 3}};
        testImpl.chiSquare(counts);
    }

    @Test
    public void testChiSquareTestMethodWith2DArrayInputAndAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{2, 3, 1}, {3, 3, 2}, {1, 2, 3}};
        double pValue = testImpl.chiSquareTest(counts, 0.05);
        assertTrue(pValue > 0.05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWith2DArrayInputAndInvalidAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[][] counts = {{2, 3, 1}, {3, 3, 2}, {1, 2, 3}};
        testImpl.chiSquareTest(counts, -0.1);
    }

    @Test
    public void testChiSquareDataSetsComparisonMethodWithBasicInput() throws IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {3, 4};
        long[] observed2 = {5, 2};
        double chiSquareStatistic = testImpl.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(4.0, chiSquareStatistic, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonMethodWithInvalidInput() throws IllegalArgumentException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {3};
        long[] observed2 = {5, 2};
        testImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithBasicInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {3, 4};
        long[] observed2 = {5, 2};
        double pValue = testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.123648666, pValue, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidInput() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {3};
        long[] observed2 = {5, 2};
        testImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithAlpha() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        long[] observed1 = {3, 4};
        long[] observed2 = {5, 2};
        boolean result = testImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(result);
    }
}