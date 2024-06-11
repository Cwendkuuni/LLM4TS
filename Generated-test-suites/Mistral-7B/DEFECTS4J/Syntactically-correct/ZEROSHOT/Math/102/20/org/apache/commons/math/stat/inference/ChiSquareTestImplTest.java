package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

/**
 * Test class for {@link ChiSquareTestImpl}
 */
public class ChiSquareTestImplTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        assert null != test.getDistribution();
    }

    /**
     * Test constructor with provided distribution
     */
    @Test
    public void testConstructorWithDistribution() {
        final ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(1.0);
        final ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertEquals(dist, test.getDistribution());
    }

    /**
     * Test chiSquare method
     */
    @Test
    public void testChiSquare() throws IllegalArgumentException {
        final double[] expected = {1.0, 2.0};
        final long[] observed = {2.0, 3.0};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertEquals(1.3312783475, test.chiSquare(expected, observed), 0.001);
    }

    /**
     * Test chiSquareTest method
     */
    @Test
    public void testChiSquareTest() throws IllegalArgumentException, MathException {
        final double[] expected = {1.0, 2.0};
        final long[] observed = {2.0, 3.0};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertEquals(0.9534975012, test.chiSquareTest(expected, observed), 0.001);
    }

    /**
     * Test chiSquareTest method with given significance level
     */
    @Test
    public void testChiSquareTestWithSignificanceLevel() throws IllegalArgumentException, MathException {
        final double[] expected = {1.0, 2.0};
        final long[] observed = {2.0, 3.0};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertTrue(test.chiSquareTest(expected, observed, 0.05));
    }

    /**
     * Test chiSquare method for 2x2 contingency table
     */
    @Test
    public void testChiSquareFor2x2() throws IllegalArgumentException {
        long[][] counts = {{2, 3}, {4, 5}};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertEquals(6.6348539256, test.chiSquare(counts), 0.001);
    }

    /**
     * Test chiSquareTest method for 2x2 contingency table
     */
    @Test
    public void testChiSquareTestFor2x2() throws IllegalArgumentException, MathException {
        long[][] counts = {{2, 3}, {4, 5}};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertEquals(0.0455285326, test.chiSquareTest(counts), 0.001);
    }

    /**
     * Test chiSquareTest method for 2x2 contingency table with given significance level
     */
    @Test
    public void testChiSquareTestFor2x2WithSignificanceLevel() throws IllegalArgumentException, MathException {
        long[][] counts = {{2, 3}, {4, 5}};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertFalse(test.chiSquareTest(counts, 0.05));
    }

    /**
     * Test chiSquareDataSetsComparison method
     */
    @Test
    public void testChiSquareDataSetsComparison() throws IllegalArgumentException {
        final long[] observed1 = {2, 3};
        final long[] observed2 = {3, 2};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertEquals(0.2417239374, test.chiSquareDataSetsComparison(observed1, observed2), 0.001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method
     */
    @Test
    public void testChiSquareTestDataSetsComparison() throws IllegalArgumentException, MathException {
        final long[] observed1 = {2, 3};
        final long[] observed2 = {3, 2};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertEquals(0.8128761278, test.chiSquareTestDataSetsComparison(observed1, observed2), 0.001);
    }

    /**
     * Test chiSquareTestDataSetsComparison method with given significance level
     */
    @Test
    public void testChiSquareTestDataSetsComparisonWithSignificanceLevel() throws IllegalArgumentException, MathException {
        final long[] observed1 = {2, 3};
        final long[] observed2 = {3, 2};
        final ChiSquareTestImpl test = new ChiSquareTestImpl();

        assertTrue(test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05));
    }

    /**
     * Test getDistribution method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDistribution() {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.getDistribution();
    }

    /**
     * Test setDistribution method
     */
    @Test
    public void testSetDistribution() {
        final ChiSquareTestImpl test = new ChiSquareTestImpl();
        final ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(2.0);
        test.setDistribution(dist);
        assertEquals(dist, test.getDistribution());
    }
}