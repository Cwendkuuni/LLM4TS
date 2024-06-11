package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import java.util.Arrays;

public class ChiSquareTestImplTest {

    @org.junit.Before
    public void setUp() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.setDistribution(new ChiSquaredDistributionImpl(1.0));
    }

    @org.junit.Test
    public void testChiSquare() throws MathIllegalArgumentException {
        long[] observed = new long[]{2, 3};
        double[] expected = new double[]{3.5, 2.5};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(2.916666666666667, chiSquare, 1e-5);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareInvalidObserved() {
        double[] expected = new double[]{1, 1};
        long[] observed = new long[]{1, 2};
        test.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareInvalidExpected() {
        double[] expected = new double[]{1, -1};
        long[] observed = new long[]{1, 1};
        test.chiSquare(expected, observed);
    }

    @org.junit.Test
    public void testChiSquareTest() throws MathIllegalArgumentException, MathRuntimeException {
        long[] observed = new long[]{2, 3};
        double[] expected = new double[]{3.5, 2.5};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.49999999999999996, pValue, 1e-5);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestInvalidObserved() {
        double[] expected = new double[]{1, 1};
        long[] observed = new long[]{1, 2};
        test.chiSquareTest(expected, observed);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestInvalidExpected() {
        double[] expected = new double[]{1, -1};
        long[] observed = new long[]{1, 1};
        test.chiSquareTest(expected, observed);
    }

    @org.junit.Test(expected = MathRuntimeException.class)
    public void testChiSquareTestInvalidDF() {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(0.5));
        double[] expected = new double[]{1, 1};
        long[] observed = new long[]{1, 1};
        test.chiSquareTest(expected, observed);
    }

    @org.junit.Test
    public void testChiSquareTestAlpha() throws MathIllegalArgumentException, MathRuntimeException {
        long[] observed = new long[]{2, 3};
        double[] expected = new double[]{3.5, 2.5};
        double alpha = 0.05;
        boolean rejectH0 = test.chiSquareTest(expected, observed, alpha);
        assertFalse(rejectH0);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestInvalidAlpha() {
        long[] observed = new long[]{2, 3};
        double[] expected = new double[]{3.5, 2.5};
        double alpha = -0.1;
        test.chiSquareTest(expected, observed, alpha);
    }

    @org.junit.Test
    public void testChiSquareDataSetsComparison() throws MathIllegalArgumentException {
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 1, 3};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(1.5, chiSquare, 1e-5);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidLength() {
        long[] observed1 = new long[]{1};
        long[] observed2 = new long[]{1, 1};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidObserved1() {
        long[] observed1 = new long[]{1, -1};
        long[] observed2 = new long[]{1, 1};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidObserved2() {
        long[] observed1 = new long[]{1, 1};
        long[] observed2 = new long[]{1, -1};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test
    public void testChiSquareTestDataSetsComparison() throws MathIllegalArgumentException, MathRuntimeException {
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 1, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.5, pValue, 1e-5);
    }

    @org.junit.Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidAlpha() {
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 1, 3};
        double alpha = -0.1;
        test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
    }

    @org.junit.Test
    public void testChiSquareTestDataSetsComparisonRejectH0() throws MathIllegalArgumentException, MathRuntimeException {
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 1, 3};
        double alpha = 0.05;
        boolean rejectH0 = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(rejectH0);
    }
}