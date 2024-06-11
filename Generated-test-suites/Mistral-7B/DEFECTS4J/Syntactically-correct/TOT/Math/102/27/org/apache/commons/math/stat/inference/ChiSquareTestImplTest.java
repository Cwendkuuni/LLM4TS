package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareWithValidInput() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed = {2, 4, 3, 5};
        double[] expected = {3.0, 5.0, 3.0, 5.0};
        double testStatistic = chiSquareTest.chiSquare(expected, observed);
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(1.0, pValue, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputLength() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        double[] observed = {1.0, 2.0};
        long[] expected = {2, 3};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputNonPositiveExpected() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed = {2, 4, 3, 5};
        double[] expected = {-1.0, 5.0, 3.0, 5.0};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputNegativeObserved() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed = {2, 4, -3, 5};
        double[] expected = {3.0, 5.0, 3.0, 5.0};
        chiSquareTest.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareWithLongArray() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[][] observed = {{2, 4}, {3, 5}};
        double testStatistic = chiSquareTest.chiSquare(observed);
        double pValue = chiSquareTest.chiSquareTest(observed);
        assertEquals(1.0, pValue, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputNonRectangular() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[][] observed = {{2, 4}, {3}, {5}};
        chiSquareTest.chiSquare(observed);
    }

    @Test
    public void testChiSquareTestWithValidInputAndAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed = {2, 4, 3, 5};
        double[] expected = {3.0, 5.0, 3.0, 5.0};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInput() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed1 = {2, 4};
        long[] observed2 = {3, 5};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.0, pValue, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithInvalidInputLengths() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed1 = {2};
        long[] observed2 = {3, 5};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInputAndAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed1 = {2, 4};
        long[] observed2 = {3, 5};
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(result);
    }
}