package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSpectralDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Tests constructor with correct parameters.
     */
    @Test
    public void testCorrectConstruction() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        Assert.assertArrayEquals(means, dist.getMeans(), 0.001);
        final RealMatrix covMat = dist.getCovariances();
        final RealArray2DSpectralDecomposition eigDecomposition = covMat.decompose();
        Assert.assertEquals(2.0, eigDecomposition.getDeterminant(), 0.001);
    }

    /**
     * Tests constructor with incorrect parameters length.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIncorrectMeansLength() {
        final double[] means = {1.0, 2.0, 3.0};
        final double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Tests constructor with incorrect parameters rows and columns.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIncorrectCovariancesShape() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {1.0, 0.5, 0.0},
            {0.5, 1.0, 1.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Tests negative eigenvalues in covariance matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeEigenvalues() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {1.0, -0.5},
            {-0.5, 1.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Tests method {@link MultivariateNormalDistribution#density(double[])}.
     */
    @Test
    public void testDensity() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final double[] testValues = {1.5, 2.5};
        final double expectedDensity = 0.11874507826911912;
        Assert.assertEquals(expectedDensity, dist.density(testValues), 1e-6);
    }

    /**
     * Tests method {@link MultivariateNormalDistribution#sample()}.
     */
    @Test
    public void testSample() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {
            {1.0, 0.5},
            {0.5, 1.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final double[] sample = dist.sample();
        final double[] expectedSampleMeans = {1.0, 2.0};
        Assert.assertArrayEquals(expectedSampleMeans, sample, 0.001);
    }
}