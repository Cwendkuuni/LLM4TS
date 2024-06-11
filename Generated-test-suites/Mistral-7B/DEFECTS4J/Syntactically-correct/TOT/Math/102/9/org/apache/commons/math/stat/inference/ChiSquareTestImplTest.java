package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link ChiSquareTestImpl} class.
 */
public class ChiSquareTestImplTest {

    /**
     * Tests the constructor without arguments.
     */
    @Test
    public void testEmptyConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        Assert.assertNotNull(test.getDistribution());
        Assert.assertEquals(1.0, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the constructor with a given ChiSquaredDistribution.
     */
    @Test
    public void testWithDistributionConstructor() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(3.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        Assert.assertSame(distribution, test.getDistribution());
        Assert.assertEquals(3.0, test.getDistribution().getDegreesOfFreedom(), 0.0);
    }

    /**
     * Tests the chiSquare method with valid input.
     */
    @Test
    public void testChiSquareValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 1.0};
        long[] observed = {2.0, 1.0, 3.0};
        double actual = test.chiSquare(expected, observed);
        double expectedValue = 2.0702312352423; // approximated using Wolfram Alpha
        Assert.assertEquals(expectedValue, actual, 0.0001);
    }

    /**
     * Tests the chiSquareTest method with valid input.
     */
    @Test
    public void testChiSquareTestValidInput() throws MathIllegalArgumentException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 1.0};
        long[] observed = {2.0, 1.0, 3.0};
        double pValue = test.chiSquareTest(expected, observed);
        double expectedValue = 0.952581525821; // approximated using Wolfram Alpha
        Assert.assertEquals(expectedValue, pValue, 0.0001);
    }

    /**
     * Tests the chiSquareTest method with invalid input.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testChiSquareTestInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        double[] expected = new double[1];
        long[] observed = {1, 2, 3};
        test.chiSquareTest(expected, observed);
    }

    /**
     * Tests the chiSquareTestDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareTestDataSetsComparisonValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 1, 3, 2, 1};
        long[] observed2 = {3, 2, 2, 2, 1};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedValue = 0.784246864491; // approximated using Wolfram Alpha
        Assert.assertEquals(expectedValue, pValue, 0.0001);
    }

    /**
     * Tests the chiSquareDataSetsComparison method with valid input.
     */
    @Test
    public void testChiSquareDataSetsComparisonValidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 1, 3, 2, 1};
        long[] observed2 = {3, 2, 2, 2, 1};
        double actual = test.chiSquareDataSetsComparison(observed1, observed2);
        double expectedValue = 0.784246864491; // approximated using Wolfram Alpha
        Assert.assertEquals(expectedValue, actual, 0.0001);
    }

    /**
     * Tests the chiSquareDataSetsComparison method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 1};
        long[] observed2 = {3};
        test.chiSquareDataSetsComparison(observed1, observed2);
    }
}