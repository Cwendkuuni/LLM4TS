package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test for constructor with mean vector and covariance matrix.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testConstructWithMeanAndCovarianceMatrix() throws Exception {
        final double[] means = {0.0, 0.0, 0.0};
        final double[][] covariances = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.0},
            {0.5, 1.0, 2.0}
        };

        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        Assert.assertArrayEquals(means, dist.getMeans(), 0.0);

        final RealMatrix covMatrix = dist.getCovariances();
        final Array2DRowRealMatrix covMatrixExpected = new Array2DRowRealMatrix(covariances);
        Assert.assertArrayEquals(covMatrixExpected.getData(), covMatrix.getData(), 0.0);
    }

    /**
     * Test for constructor with mean vector, random number generator and covariance matrix.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testConstructWithMeanRandomNumberGeneratorAndCovarianceMatrix()
            throws Exception {
        final double[] means = {0.0, 0.0, 0.0};
        final double[][] covariances = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.0},
            {0.5, 1.0, 2.0}
        };

        final RandomGenerator rng = new Well19937c();

        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);

        Assert.assertArrayEquals(means, dist.getMeans(), 0.0);

        final RealMatrix covMatrix = dist.getCovariances();
        final Array2DRowRealMatrix covMatrixExpected = new Array2DRowRealMatrix(covariances);
        Assert.assertArrayEquals(covMatrixExpected.getData(), covMatrix.getData(), 0.0);
    }

    /**
     * Test for density calculation.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testDensity() throws Exception {
        final double[] means = {0.0, 0.0, 0.0};
        final double[][] covariances = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.0},
            {0.5, 1.0, 2.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        final double[] testValues = {0.5, 1.5, 2.5};
        final double expectedDensity = FastMath.pow(2 * FastMath.PI, -3.0 / 2) *
                FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) *
                dist.getExponentTerm(testValues);

        final double density = dist.density(testValues);

        Assert.assertEquals(expectedDensity, density, 1e-6);
    }

    /**
     * Test for standard deviations calculation.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testStandardDeviations() throws Exception {
        final double[] means = {0.0, 0.0, 0.0};
        final double[][] covariances = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.0},
            {0.5, 1.0, 2.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        final double[] expectedStandardDeviations = {1.0, FastMath.sqrt(2.0), FastMath.sqrt(2.0)};
        final double[] standardDeviations = dist.getStandardDeviations();

        Assert.assertArrayEquals(expectedStandardDeviations, standardDeviations, 0.0);
    }

    /**
     * Test for sample calculation.
     *
     * @throws Exception if test fails.
     */
    @Test
    public void testSample() throws Exception {
        final double[] means = {0.0, 0.0, 0.0};
        final double[][] covariances = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.0},
            {0.5, 1.0, 2.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);

        final double[] sample = dist.sample();

        for (int i = 0; i < sample.length; i++) {
            Assert.assertNotSame(means[i], sample[i], 0.0);
        }
    }

    /**
     * Test for exception handling when constructing with invalid inputs.
     *
     * @throws Exception if test fails.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidInputDimensionMismatch() throws Exception {
        final double[] means = {0.0, 0.0, 0.0};
        final double[][] covariances = {
            {1.0, 0.5},
            {0.5, 2.0, 1.0},
            {0.5, 1.0, 2.0}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test for exception handling when constructing with non-positive definite covariance matrix.
     *
     * @throws Exception if test fails.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidInputNonPositiveDefiniteMatrix() throws Exception {
        final double[] means = {0.0, 0.0, 0.0};
        final double[][] covariances = {
            {1.0, 0.5, 0.5},
            {0.5, -2.0, 1.0},
            {0.5, 1.0, 2.0}
        };
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test for exception handling when constructing with singular covariance matrix.
     *
     * @throws Exception if test fails.
     */
    @Test(expected = SingularMatrixException.class)
    public void testInvalidInputSingularMatrix() throws Exception {
        final double[] means = {0.0, 0.0, 0.0};
        final double[][] covariances = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 0.0},
            {0.5, 0.0, 2.0}
        };
        new MultivariateNormalDistribution(means, covariances);
    }
}