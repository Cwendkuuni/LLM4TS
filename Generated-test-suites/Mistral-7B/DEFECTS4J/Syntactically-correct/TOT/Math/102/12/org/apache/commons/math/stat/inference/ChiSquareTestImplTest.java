package org.apache.commons.math.stat.inference;

import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.MathException;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareSingleDataSet() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = {2, 3, 1, 4, 5, 6};
        long[] expected = {3, 4, 2, 3, 1, 4};
        double alpha = 0.05;

        Assert.assertEquals(3.944270665355178, test.chiSquare(expected, observed), 1e-6);
        Assert.assertEquals(0.02671844682392626, test.chiSquareTest(expected, observed), 1e-6);
        Assert.assertTrue(test.chiSquareTest(expected, observed, alpha));

        // Edge case, expected and observed arrays with same values
        Assert.assertEquals(0.0d, test.chiSquare(expected, observed), 1e-6);
        Assert.assertEquals(1.0d, test.chiSquareTest(expected, observed), 1e-6);
        Assert.assertFalse(test.chiSquareTest(expected, observed, alpha));

        // Error case, different lengths
        Assert.assertThrows(IllegalArgumentException.class, () -> test.chiSquare(new long[5], observed));
        Assert.assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, new long[5]));
    }

    @Test
    public void testChiSquareTwoDataSets() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {2, 3, 1};
        long[] observed2 = {3, 2, 2};

        Assert.assertEquals(1.0d, test.chiSquareDataSetsComparison(observed1, observed2), 1e-6);
        Assert.assertEquals(1.0d, test.chiSquareTestDataSetsComparison(observed1, observed2), 1e-6);
        Assert.assertTrue(test.chiSquareTestDataSetsComparison(observed1, observed2, 0.1));

        // Edge case, empty datasets
        Assert.assertThrows(IllegalArgumentException.class, () -> test.chiSquareDataSetsComparison(new long[0], observed2));
        Assert.assertThrows(IllegalArgumentException.class, () -> test.chiSquareDataSetsComparison(observed1, new long[0]));

        // Error case, different lengths
        Assert.assertThrows(IllegalArgumentException.class, () -> test.chiSquareDataSetsComparison(new long[2], observed2));
        Assert.assertThrows(IllegalArgumentException.class, () -> test.chiSquareDataSetsComparison(observed1, new long[2]));
    }

    @Test
    public void testChiSquareWithDistribution() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        long[] observed = {2, 3, 1, 4, 5, 6};
        long[] expected = {3, 4, 2, 3, 1, 4};
        double alpha = 0.05;

        Assert.assertEquals(3.944270665355178, test.chiSquare(expected, observed), 1e-6);
        Assert.assertEquals(0.02671844682392626, test.chiSquareTest(expected, observed), 1e-6);
        Assert.assertTrue(test.chiSquareTest(expected, observed, alpha));

        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(3.0);
        test.setDistribution(distribution);
        Assert.assertEquals(3.944270665355178, test.chiSquare(expected, observed), 1e-6);
        Assert.assertEquals(0.02671844682392626, test.chiSquareTest(expected, observed), 1e-6);
        Assert.assertTrue(test.chiSquareTest(expected, observed, alpha));

        // Error case, invalid degrees of freedom
        ChiSquaredDistribution distributionNegativeFreedom = new ChiSquaredDistributionImpl(-3.0);
        test.setDistribution(distributionNegativeFreedom);
        Assert.assertThrows(IllegalArgumentException.class, () -> test.chiSquare(expected, observed));
        Assert.assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(expected, observed));
    }
}