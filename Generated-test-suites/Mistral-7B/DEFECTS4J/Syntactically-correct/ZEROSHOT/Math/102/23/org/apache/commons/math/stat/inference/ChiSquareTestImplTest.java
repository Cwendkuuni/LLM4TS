package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructorNoArgs() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        assertNotNull(chiSquareTestImpl.getDistribution());
        assertEquals(1.0, chiSquareTestImpl.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testChiSquareTestImplConstructorWithChiSquaredDistribution() {
        double degreesOfFreedom = 2.0;
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(degreesOfFreedom);
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(chiSquaredDistribution);
        assertNotNull(chiSquareTestImpl);
        assertEquals(degreesOfFreedom, chiSquareTestImpl.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplConstructorWithNullChiSquaredDistribution() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareConstructorWithInvalidNumberOfDegreesOfFreedom() {
        double degreesOfFreedom = -1.0;
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(degreesOfFreedom));
    }

    @Test
    public void testChiSquareTestImplGetDistribution() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        ChiSquaredDistribution chiSquaredDistribution = chiSquareTestImpl.getDistribution();
        assertNotNull(chiSquaredDistribution);
        assertEquals(chiSquareTestImpl.getDistribution().getDegreesOfFreedom(), chiSquaredDistribution.getDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testSetDistribution() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        ChiSquaredDistribution chiSquaredDistribution = new ChiSquaredDistributionImpl(5.0);
        chiSquareTestImpl.setDistribution(chiSquaredDistribution);
        ChiSquaredDistribution chiSquaredDistributionResult = chiSquareTestImpl.getDistribution();
        assertEquals(chiSquaredDistribution.getDegreesOfFreedom(), chiSquaredDistributionResult.getDegreesOfFreedom(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidObservedOrExpectedArraySize() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[0];
        double[] expected = new double[1];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidObservedOrExpectedArrayLengthMismatch() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[1];
        double[] expected = new double[2];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithNegativeExpectedValue() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[1];
        double[] expected = new double[]{-1.0, 2.0};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithNegativeObservedValue() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[]{-1};
        double[] expected = new double[]{1.0, 2.0};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestImplWithValidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        double expectedResult = 0.06666666666666667;
        assertEquals(expectedResult, result, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidAlphaValue() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[1][1];
        chiSquareTestImpl.chiSquareTest(counts, -0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplWithInvalidAlphaValueTooHigh() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[1][1];
        chiSquareTestImpl.chiSquareTest(counts, 1.5);
    }

    @Test
    public void testChiSquareTestImplWithValidInputAndAlpha() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[2][2];
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTest(counts, alpha);
        boolean expectedResult = false;
        assertEquals(expectedResult, result);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestImplWithInvalidInputForChiSquareTest() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[1][1];
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithValidInputAndAlpha() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{2, 3, 4, 5};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        boolean expectedResult = false;
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidInputSize() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[1];
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidInputLengthMismatch() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[1];
        long[] observed2 = new long[2];
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithValidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{2, 3, 4, 5};
        double result = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 3.841459665553693;
        assertEquals(expectedResult, result, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonWithInvalidInputForChiSquareTest() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{2, 3, 4, 5};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testCheckArray() {
        long[][] counts = new long[2][2];
        counts[0][0] = 1;
        counts[0][1] = 2;
        counts[1][0] = 3;
        counts[1][1] = 4;
        chiSquareTestImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidInputArray() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[2][1];
        chiSquareTestImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidInputArrayEmpty() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[0][0];
        chiSquareTestImpl.checkArray(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayWithInvalidInputArrayNegative() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{-1, {1}};
        chiSquareTestImpl.checkArray(counts);
    }
}