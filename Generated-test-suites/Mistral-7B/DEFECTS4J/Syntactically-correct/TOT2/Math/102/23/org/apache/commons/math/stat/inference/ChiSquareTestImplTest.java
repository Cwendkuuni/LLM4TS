import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquare() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {2, 3, 1, 4, 5};
        double chiSquareStat = test.chiSquare(expected, observed);
        double pValue = test.chiSquareTest(expected, observed);
        Assert.assertEquals(5.0, chiSquareStat, 0.01); // typical use case
        Assert.assertEquals(0.01, pValue, 0.001); // typical use case

        // edge cases
        long[] emptyObserved = {};
        long[] emptyExpected = {};
        try {
            test.chiSquare(emptyExpected, emptyObserved);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long[] negativeExpected = {-1, 2, 1};
        try {
            test.chiSquare(negativeExpected, observed);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long[] negativeObserved = {1, -2, 1};
        try {
            test.chiSquare(expected, negativeObserved);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long[] notSameLength = {1, 2, 3, 5};
        try {
            test.chiSquare(notSameLength, observed);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long[] zeroObserved = {0, 0, 0};
        long[] zeroExpected = {0, 0, 0};
        double chiSquareZero = test.chiSquare(zeroExpected, zeroObserved);
        double pValueZero = test.chiSquareTest(zeroExpected, zeroObserved);
        Assert.assertEquals(0.0, chiSquareZero, 0.01); // edge case
        Assert.assertEquals(1.0, pValueZero, 0.001); // edge case
    }

    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        long[] observed = {1, 2, 3, 4, 5};
        long[] expected = {2, 3, 1, 4, 5};
        double alpha = 0.05;
        boolean rejectNull = test.chiSquareTest(expected, observed, alpha);
        Assert.assertTrue(rejectNull); // typical use case

        // edge cases
        long[] emptyObserved = {};
        long[] emptyExpected = {};
        try {
            test.chiSquareTest(emptyExpected, emptyObserved, 0.01);
            Assert.fail("MathException expected");
        } catch (MathException e) {
            // expected
        }

        long[] negativeExpected = {-1, 2, 1};
        try {
            test.chiSquareTest(negativeExpected, observed, 0.01);
            Assert.fail("MathException expected");
        } catch (MathException e) {
            // expected
        }

        long[] negativeObserved = {1, -2, 1};
        try {
            test.chiSquareTest(expected, negativeObserved, 0.01);
            Assert.fail("MathException expected");
        } catch (MathException e) {
            // expected
        }

        long[] notSameLength = {1, 2, 3, 5};
        try {
            test.chiSquareTest(notSameLength, observed, 0.01);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long[] zeroObserved = {0, 0, 0};
        long[] zeroExpected = {0, 0, 0};
        boolean rejectNullZero = test.chiSquareTest(zeroExpected, zeroObserved, 0.01);
        Assert.assertFalse(rejectNullZero); // edge case
    }

    @Test
    public void testChiSquareComparison() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5};
        long[] observed2 = {2, 3, 1, 4, 5};
        double chiSquareComparison = test.chiSquareDataSetsComparison(observed1, observed2);
        double pValueComparison = test.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertEquals(1.0, chiSquareComparison, 0.01); // typical use case
        Assert.assertEquals(0.0, pValueComparison, 0.001); // typical use case

        // edge cases
        long[] emptyObserved1 = {};
        long[] emptyObserved2 = {};
        try {
            test.chiSquareDataSetsComparison(emptyObserved1, emptyObserved2);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long[] negativeObserved1 = {-1, 2, 1};
        long[] negativeObserved2 = {1, 2, 1};
        try {
            test.chiSquareDataSetsComparison(negativeObserved1, negativeObserved2);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        long[] notSameLength1 = {1, 2, 3};
        long[] observed2SameLength = {2, 3, 1, 4, 5};
        try {
            test.chiSquareDataSetsComparison(notSameLength1, observed2SameLength);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}