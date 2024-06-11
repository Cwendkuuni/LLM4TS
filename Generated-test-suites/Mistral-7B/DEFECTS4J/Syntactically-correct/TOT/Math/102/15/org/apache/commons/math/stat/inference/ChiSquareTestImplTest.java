package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.stat.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.distribution.ChiSquaredDistributionImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /**
     * Test for constructor with default distribution.
     */
    @Test
    public void testDefaultConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        Assert.assertNotNull(test.getDistribution());
        Assert.assertEquals(1.0, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for constructor with custom distribution.
     */
    @Test
    public void testCustomConstructor() {
        ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(3.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        Assert.assertSame(dist, test.getDistribution());
        Assert.assertEquals(3.0, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquare(double[], long[])} method with valid input.
     */
    @Test
    public void testChiSquareValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 3.0};
        double result = test.chiSquare(expected, observed);
        Assert.assertNotNull(result);
        Assert.assertEquals(7.0 / 2.0, result, 0.001);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquare(double[], long[])} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {2.0, 3.0};
        test.chiSquare(expected, observed);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTest(double[], long[])} method with valid input.
     */
    @Test
    public void testChiSquareTestValidInput() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 3.0};
        double pValue = test.chiSquareTest(expected, observed);
        Assert.assertNotNull(pValue);
        Assert.assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTest(double[], long[])} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {};
        long[] observed = {2.0, 3.0};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTest(double[], long[], double)} method with valid input.
     */
    @Test
    public void testChiSquareTestSignificanceLevelValidInput() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 3.0};
        double significanceLevel = 0.05;
        boolean rejectNullHypothesis = test.chiSquareTest(expected, observed, significanceLevel);
        Assert.assertFalse(rejectNullHypothesis);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTest(double[], long[], double)} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestInvalidInputSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2.0, 3.0, 3.0};
        double significanceLevel = 1.5;
        test.chiSquareTest(expected, observed, significanceLevel);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquare(long[][])} method with valid input.
     */
    @Test
    public void testChiSquareContingencyTableValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double result = test.chiSquare(counts);
        Assert.assertNotNull(result);
        Assert.assertEquals(3.0, result, 0.001);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTest(long[][])} method with valid input.
     */
    @Test
    public void testChiSquareTestContingencyTableValidInput() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double pValue = test.chiSquareTest(counts);
        Assert.assertNotNull(pValue);
        Assert.assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTest(long[][])} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestContingencyTableInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1}, {2}};
        test.chiSquareTest(counts);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTest(long[][])} method with valid input and significance level.
     */
    @Test
    public void testChiSquareTestContingencyTableSignificanceLevel() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double significanceLevel = 0.05;
        boolean rejectNullHypothesis = test.chiSquareTest(counts, significanceLevel);
        Assert.assertFalse(rejectNullHypothesis);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTest(long[][])} method with invalid input and significance level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestContingencyTableInvalidInputSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double significanceLevel = 1.5;
        test.chiSquareTest(counts, significanceLevel);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareDataSetsComparison(long[], long[])} method with valid input.
     */
    @Test
    public void testChiSquareDataSetComparisonValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double result = test.chiSquareDataSetsComparison(observed1, observed2);
        Assert.assertNotNull(result);
        Assert.assertEquals(4.0 / 2.0, result, 0.001);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])} method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetComparisonValidInput() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        Assert.assertNotNull(pValue);
        Assert.assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetComparisonInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2};
        long[] observed2 = {3};
        test.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])} method with valid input and significance level.
     */
    @Test
    public void testChiSquareTestDataSetComparisonSignificanceLevel() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double significanceLevel = 0.05;
        boolean rejectNullHypothesis = test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
        Assert.assertFalse(rejectNullHypothesis);
    }

    /**
     * Test for {@link ChiSquareTestImpl#chiSquareTestDataSetsComparison(long[], long[])} method with invalid input and significance level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetComparisonInvalidInputSignificanceLevel() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double significanceLevel = 1.5;
        test.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
    }
}