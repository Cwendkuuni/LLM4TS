package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;

import java.util.Arrays;

public class ChiSquareTestImplTest {

    @org.junit.Before
    public void setUp() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.setDistribution(new ChiSquaredDistributionImpl(1.0));
    }

    @org.junit.Test
    public void testChiSquare() throws IllegalArgumentException {
        long[] observed = {2, 3};
        double[] expected = {1.5, 2.5};
        double chiSquare = chiSquareTest.chiSquare(expected, observed);
        assertEquals(4.60517013861, chiSquare, 0.0001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidObserved() throws IllegalArgumentException {
        long[] observed = {2, 3};
        double[] expected = {1.5, 2.5};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquare(expected, observed);
        chiSquareTest.setDistribution(null);
        chiSquareTest.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidExpected() throws IllegalArgumentException {
        long[] observed = {2, 3};
        double[] expected = {1.5, -2.5};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquare(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidLengths() throws IllegalArgumentException {
        long[] observed = {2};
        double[] expected = {1.5, 2.5, 3.5};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquare(expected, observed);
    }

    @org.junit.Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        long[] observed = {2, 3};
        double[] expected = {1.5, 2.5};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.0455271691858, pValue, 0.0001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidObserved() throws IllegalArgumentException, MathException {
        long[] observed = {2, 3};
        double[] expected = {1.5, 2.5};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareTest(expected, observed);
        chiSquareTest.setDistribution(null);
        chiSquareTest.chiSquareTest(expected, observed);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidExpected() throws IllegalArgumentException, MathException {
        long[] observed = {2, 3};
        double[] expected = {1.5, 2.5, 3.5};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareTest(expected, observed);
    }

    @org.junit.Test
    public void testChiSquareTestInvalidAlpha() throws IllegalArgumentException, MathException {
        long[] observed = {2, 3};
        double[] expected = {1.5, 2.5};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 1.1);
        assertFalse(result);
    }

    @org.junit.Test
    public void testChiSquareTestValidAlpha() throws IllegalArgumentException, MathException {
        long[] observed = {2, 3};
        double[] expected = {1.5, 2.5};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        assertTrue(result);
    }

    @org.junit.Test
    public void testChiSquareComparison() throws IllegalArgumentException {
        long[] observed1 = {2, 3};
        long[] observed2 = {1, 4};
        double chiSquare = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(6.25, chiSquare, 0.001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareComparisonInvalidObserved1() throws IllegalArgumentException {
        long[] observed1 = {2, 3};
        long[] observed2 = {1, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        chiSquareTest.setDistribution(null);
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareComparisonInvalidObserved2() throws IllegalArgumentException {
        long[] observed1 = {2, 3};
        long[] observed2 = {1, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        chiSquareTest.setDistribution(new ChiSquaredDistributionImpl(1.0));
        chiSquareTest.chiSquareDataSetsComparison(observed1, null);
    }

    @org.junit.Test
    public void testChiSquareTestComparison() throws IllegalArgumentException, MathException {
        long[] observed1 = {2, 3};
        long[] observed2 = {1, 4};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.0128119102737, pValue, 0.0001);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestComparisonInvalidObserved1() throws IllegalArgumentException, MathException {
        long[] observed1 = {2, 3};
        long[] observed2 = {1, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        chiSquareTest.setDistribution(null);
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestComparisonInvalidObserved2() throws IllegalArgumentException, MathException {
        long[] observed1 = {2, 3};
        long[] observed2 = {1, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        chiSquareTest.setDistribution(new ChiSquaredDistributionImpl(1.0));
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, null);
    }

    @org.junit.Test
    public void testChiSquareTestComparisonInvalidAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {2, 3};
        long[] observed2 = {1, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        chiSquareTest.setDistribution(new ChiSquaredDistributionImpl(1.0));
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 1.1);
        assertFalse(result);
    }

    @org.junit.Test
    public void testChiSquareTestComparisonValidAlpha() throws IllegalArgumentException, MathException {
        long[] observed1 = {2, 3};
        long[] observed2 = {1, 4};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        chiSquareTest.setDistribution(new ChiSquaredDistributionImpl(1.0));
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertTrue(result);
    }

    // Testing checkArray method

    @org.junit.Test
    public void testCheckArray() {
        long[][] counts = new long[][]{{1, 2}, {3, 4}};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.checkArray(counts);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCheckArrayNullInput() {
        long[][] counts = null;
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.checkArray(counts);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCheckArrayEmptyInput() {
        long[][] counts = {};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.checkArray(counts);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCheckArrayInvalidInputShape() {
        long[][] counts = {{1, 2}, {3}};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.checkArray(counts);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCheckArrayInvalidInputValues() {
        long[][] counts = {{-1, 2}, {3, 4}};
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        chiSquareTest.checkArray(counts);
    }
}