package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Test;

/**
 * Test class for ChiSquareTestImpl
 */
public class ChiSquareTestImplTest {

    /**
     * Test ChiSquareTestImpl constructor.
     */
    @Test
    public void testConstructor() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        assertNotNull(chiSquareTest);
    }

    /**
     * Test ChiSquareTestImpl constructor with custom distribution.
     */
    @Test
    public void testConstructorWithCustomDistribution() {
        ChiSquareDistribution dist = new ChiSquaredDistributionImpl(2);
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl(dist);
        assertNotNull(chiSquareTest);
        assertEquals(dist, chiSquareTest.getDistribution());
    }

    /**
     * Test chiSquare method with valid input.
     */
    @Test
    public void testChiSquareWithValidInput() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double result = chiSquareTest.chiSquare(expected, observed);
        assertEquals(7.0 / 3.0, result, 0.0001);
    }

    /**
     * Test chiSquare method with incorrect input sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareWithInvalidInputSizes() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2, 3};
        chiSquareTest.chiSquare(expected, observed);
    }

    /**
     * Test chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestWithValidInput() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double pValue = chiSquareTest.chiSquareTest(expected, observed);
        assertEquals(0.1186, pValue, 0.001);
    }

    /**
     * Test chiSquareTest method with incorrect input sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestWithInvalidInputSizes() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0};
        long[] observed = {2, 3};
        chiSquareTest.chiSquareTest(expected, observed);
    }

    /**
     * Test chiSquareTest method with significance level.
     */
    @Test
    public void testChiSquareTestWithSignificanceLevel() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        boolean result = chiSquareTest.chiSquareTest(expected, observed, 0.05);
        assertFalse(result);
    }

    /**
     * Test chiSquare method with 2-way table.
     */
    @Test
    public void testChiSquareWith2WayTable() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double result = chiSquareTest.chiSquare(counts);
        assertEquals(11.0 / 2.0, result, 0.0001);
    }

    /**
     * Test chiSquareTest method with 2-way table and p-value.
     */
    @Test
    public void testChiSquareTestWith2WayTable() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        double pValue = chiSquareTest.chiSquareTest(counts);
        assertEquals(0.0556, pValue, 0.001);
    }

    /**
     * Test chiSquareTest method with 2-way table and significance level.
     */
    @Test
    public void testChiSquareTestWith2WayTableAndSignificanceLevel() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[][] counts = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        boolean result = chiSquareTest.chiSquareTest(counts, 0.05);
        assertFalse(result);
    }

    /**
     * Test chiSquareDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareDataSetsComparisonWithValidInput() {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 4};
        double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(3.0, result, 0.001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input and p-value.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInput() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 4};
        double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.0275, pValue, 0.001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with valid input and significance level.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithValidInputAndSignificanceLevel() throws MathException {
        ChiSquareTestImpl chiSquareTest = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 4};
        boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        assertFalse(result);
    }
}