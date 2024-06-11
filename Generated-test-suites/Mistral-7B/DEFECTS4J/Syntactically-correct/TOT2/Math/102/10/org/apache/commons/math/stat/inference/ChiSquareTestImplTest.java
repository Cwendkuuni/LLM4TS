package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl} class
 */
public class ChiSquareTestImplTest {

    /**
     * TestchiSquare method with valid input
     */
    @Test
    public void testChiSquare() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        long[] expected = {2, 3, 3, 2, 1, 2, 2, 1, 2};
        double chiSquareStat = chiSquareTest.chiSquare(expected, observed);
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertTrue(chiSquareStat > 0);
        assertTrue(pValue > 0.05);
    }

    /**
     * TestchiSquare method with invalid input - expected and observed arrays with different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput1() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3, 3, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * TestchiSquare method with invalid input - expected and observed arrays with negative values
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput2() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        long[] expected = {-2, 3, 3, 2};
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * TestchiSquareTest method with valid input
     */
    @Test
    public void testChiSquareTest() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        long[] expected = {2, 3, 3, 2, 1, 2, 2, 1, 2};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        double significanceLevel = 0.05;
        assertTrue(pValue < significanceLevel);
    }

    /**
     * TestchiSquareTest method with invalid input - expected and observed arrays with different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput1() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3, 3, 2, 1};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * TestchiSquareTest method with invalid input - expected and observed arrays with negative values
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput2() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        long[] expected = {-2, 3, 3, 2, 1};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * TestchiSquareTest method with invalid input - significance level less than 0 or greater than 0.5
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput3() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3, 3, 2, 1};
        double significanceLevel = 0.0;
        chiSquareTest.chiSquareTest(expected, observed, significanceLevel);
    }

    /**
     * TestchiSquareTest method with valid input - significance level greater than 0.5
     */
    @Test
    public void testChiSquareTestInvalidInput4() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed = {1, 2, 3};
        long[] expected = {2, 3, 3, 2, 1};
        double significanceLevel = 0.6;
        boolean result = chiSquareTest.chiSquareTest(expected, observed, significanceLevel);
        assertFalse(result);
    }

    /**
     * TestchiSquareDataSetsComparison method with valid input
     */
    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        long[] observed2 = {2, 3, 3, 2, 1, 2, 2, 1, 2};
        double chiSquareStat = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(chiSquareStat > 0);
        assertTrue(pValue > 0.05);
    }

    /**
     * TestchiSquareDataSetsComparison method with invalid input - arrays with different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidInput1() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3, 2, 1, 2};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    /**
     * TestchiSquareDataSetsComparison method with invalid input - arrays with negative values
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidInput2() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {-2, 3, 3, 2, 1};
        chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
    }

    /**
     * TestchiSquareTestDataSetsComparison method with valid input
     */
    @Test
    public void testChiSquareTestDataSetsComparison() throws MathException, IllegalArgumentException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        long[] observed2 = {2, 3, 3, 2, 1, 2, 2, 1, 2};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        double significanceLevel = 0.05;
        assertTrue(pValue < significanceLevel);
    }

    /**
     * TestchiSquareTestDataSetsComparison method with invalid input - arrays with different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInput1() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3, 2, 1, 2};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * TestchiSquareTestDataSetsComparison method with invalid input - arrays with negative values
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInput2() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {-2, 3, 3, 2, 1};
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * TestchiSquareTestDataSetsComparison method with invalid input - significance level less than 0 or greater than 0.5
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonInvalidInput3() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3, 2, 1};
        double significanceLevel = 0.0;
        chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
    }

    /**
     * TestchiSquareTestDataSetsComparison method with valid input - significance level greater than 0.5
     */
    @Test
    public void testChiSquareTestDataSetsComparisonInvalidInput4() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3, 2, 1};
        double significanceLevel = 0.6;
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        assertFalse(result);
    }
}