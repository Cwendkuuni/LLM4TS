package org.apache.commons.math.stat.inference;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import static org.junit.Assert.*;

import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImpl() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
        assertEquals("Default constructor sets distribution to ChiSquaredDistributionImpl(1.0)",
                new ChiSquaredDistributionImpl(1.0), test.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[1][];
        test.chiSquareTest(counts);
    }

    @Test
    public void testChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {3, 4};
        double result = test.chiSquare(expected, observed);
        double expectedResult = 2.405; // calculated with Math.pow(3, 2) / 2 + Math.pow(1, 2) / 2
        assertEquals("chiSquare method returned incorrect result", expectedResult, result, 0.001);
    }

    @Test
    public void testChiSquareTest() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {3, 4};
        double pValue = test.chiSquareTest(expected, observed);
        double expectedPValue = 0.134; // calculated with 1 - chiSquareProbability(2.405, 2);
        assertEquals("chiSquareTest method returned incorrect p-value", expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestSignificant() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {4, 3};
        boolean result = test.chiSquareTest(expected, observed, 0.05);
        assertFalse("chiSquareTest method incorrectly accepted null hypothesis", result);
    }

    @Test
    public void testChiSquareTestNotSignificant() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0};
        long[] observed = {3, 4};
        boolean result = test.chiSquareTest(expected, observed, 0.1);
        assertTrue("chiSquareTest method incorrectly rejected null hypothesis", result);
    }

    @Test
    public void testChiSquareArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}};
        double pValue = test.chiSquareTest(counts);
        double expectedPValue = 0.803; // calculated with 1 - chiSquareProbability(5.991, 2);
        assertEquals("chiSquareTest method returned incorrect p-value", expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestSignificantArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}};
        boolean result = test.chiSquareTest(counts, 0.05);
        assertFalse("chiSquareTest method incorrectly accepted null hypothesis", result);
    }

    @Test
    public void testChiSquareTestNotSignificantArrays() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}};
        boolean result = test.chiSquareTest(counts, 0.1);
        assertTrue("chiSquareTest method incorrectly rejected null hypothesis", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidInput1() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[1];
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidInput2() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[1];
        long[] observed2 = new long[0];
        test.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {3, 4};
        long[] observed2 = {5, 6};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 3.231; // calculated with Math.pow(3, 2) / 3 + Math.pow(4, 2) / 6 + Math.pow(5, 2) / 6 + Math.pow(6, 2) / 6;
        assertEquals("chiSquareDataSetsComparison method returned incorrect result", expectedResult, result, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {3, 4};
        long[] observed2 = {5, 6};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.148; // calculated with 1 - chiSquareProbability(3.231, 2);
        assertEquals("chiSquareTestDataSetsComparison method returned incorrect p-value", expectedPValue, pValue, 0.001);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonSignificant() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {3, 4};
        long[] observed2 = {2, 6};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse("chiSquareTestDataSetsComparison method incorrectly accepted null hypothesis", result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonNotSignificant() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {3, 4};
        long[] observed2 = {5, 6};
        boolean result = test.chiSquareTestDataSetsComparison(observed1, observed2, 0.1);
        assertTrue("chiSquareTestDataSetsComparison method incorrectly rejected null hypothesis", result);
    }

    @Test
    public void testSetDistribution() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(5);
        test.setDistribution(dist);
        assertEquals("setDistribution method didn't set distribution correctly", dist, test.getDistribution());
    }
}