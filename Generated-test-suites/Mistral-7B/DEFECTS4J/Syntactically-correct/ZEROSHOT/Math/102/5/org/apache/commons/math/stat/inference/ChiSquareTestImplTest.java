package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import java.util.Arrays;

public class ChiSquareTestImplTest {

    @org.junit.Before
    public void setUp() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.setDistribution(new ChiSquaredDistributionImpl(1.0));
    }

    @org.junit.Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        double result = test.chiSquare(expected, observed);
        double expectedResult = 3.125;
        assertEquals(expectedResult, result, 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidArgs() throws IllegalArgumentException {
        long[] observed = {1, 2, 3};
        double[] expected = {2.5, 2.5, 2.5};
        test.chiSquare(expected, observed);
        fail("Unexpected exception not thrown");
    }

    @org.junit.Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        double alpha = 0.05;
        double result = test.chiSquareTest(expected, observed, alpha);
        double expectedResult = 0.02582693; // calculated using R
        assertEquals(expectedResult, result, 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidArgs() throws IllegalArgumentException, MathException {
        long[] observed = {1, 2, 3, 4};
        double[] expected = {2.5, 2.5, 2.5, 2.5};
        double alpha = 1.1;
        test.chiSquareTest(expected, observed, alpha);
        fail("Unexpected exception not thrown");
    }

    @org.junit.Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3};
        double alpha = 0.05;
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        boolean expectedResult = false;
        assertEquals(expectedResult, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidArgs() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3};
        double alpha = 1.1;
        test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        fail("Unexpected exception not thrown");
    }

    @org.junit.Test
    public void testChiSquareTestDataSetsComparisonWithDifferentCounts() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3, 4};
        double alpha = 0.05;
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        boolean expectedResult = true;
        assertEquals(expectedResult, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonWithUnequalLengths() throws IllegalArgumentException, MathException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3};
        double alpha = 0.05;
        test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        fail("Unexpected exception not thrown");
    }
}