package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test the instantiation of a multivariate normal distribution.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testMultivariateNormalDistribution() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertNotNull(dist);

        // When
        final double[] sample = dist.sample();

        // Then
        assertNotNull(sample);
        assertEquals(sample.length, 2);
        // Check that the sample lies within the expected bounds
        for (int i = 0; i < sample.length; i++) {
            assertEquals(Precision.closeTo(means[i], 0.001), sample[i], 0.001);
        }
    }

    /**
     * Test the instantiation of a multivariate normal distribution with a non-positive definite covariance matrix.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistributionWithNonPositiveDefiniteCovarianceMatrix() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = {
                {1.0, -0.5},
                {-0.5, 2.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test the instantiation of a multivariate normal distribution with a singular covariance matrix.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistributionWithSingularCovarianceMatrix() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = {
                {1.0, 0.0},
                {0.0, 0.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test the instantiation of a multivariate normal distribution with an incorrect number of dimensions.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionWithIncorrectNumberOfDimensions() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0, 2.0};
        final double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.0},
                {0.0, 0.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test the instantiation of a multivariate normal distribution with an incorrect number of dimensions in the covariance matrix.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionWithIncorrectNumberOfDimensionsInCovarianceMatrix() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.0},
                {0.5, 2.0}
        };
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test the density calculation.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testDensity() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final double[] sample = new double[]{2.0, 3.0};

        // When
        final double density = dist.density(sample);

        // Then
        assertEquals(Double.NEGATIVE_INFINITY, density, 0.0);

        // Given
        final double[] sample2 = new double[]{0.0, 1.0};

        // When
        final double density2 = dist.density(sample2);

        // Then
        final double expectedDensity = FastMath.exp(-3.0 / 2.0) * FastMath.sqrt(FastMath.PI / 2.0) / FastMath.sqrt(2.0) * FastMath.exp(-0.5 * 0.5) * FastMath.exp(-0.5 * 1.0 * 1.0) / FastMath.sqrt(2.0) / 2.0;
        assertEquals(expectedDensity, density2, 1e-6);
    }

    /**
     * Test the sample method.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testSample() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.0}
        };
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        final double[] sample = dist.sample();

        // Then
        assertNotNull(sample);
        assertEquals(sample.length, 2);
        // Check that the sample lies within the expected bounds
        for (int i = 0; i < sample.length; i++) {
            assertEquals(Precision.closeTo(means[i], 0.001), sample[i], 0.001);
        }

        // When
        final double[] sample2 = dist.sample();

        // Then
        assertNotNull(sample2);
        assertEquals(sample2.length, 2);
        // Check that the sample lies within the expected bounds
        for (int i = 0; i < sample2.length; i++) {
            assertEquals(Precision.closeTo(means[i], 0.001), sample2[i], 0.001);
        }

        // When
        final Random rnd = new Random();
        final MultivariateNormalDistribution dist2 = new MultivariateNormalDistribution(rnd, means, covariances);
        final double[] sample3 = dist2.sample();

        // Then
        assertNotNull(sample3);
        assertEquals(sample3.length, 2);
        // Check that the sample lies within the expected bounds
        for (int i = 0; i < sample3.length; i++) {
            assertEquals(Precision.closeTo(means[i], 0.001), sample3[i], 0.001);
        }
    }

    /**
     * Test the getMeans method.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testGetMeans() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0};
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, new double[2][2]);

        // When
        final double[] result = dist.getMeans();

        // Then
        assertNotNull(result);
        assertEquals(result.length, 2);
        for (int i = 0; i < result.length; i++) {
            assertEquals(means[i], result[i], 1e-6);
        }
    }

    /**
     * Test the getCovariances method.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    public void testGetCovariances() throws Exception {
        // Given
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[2][2];
        final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        // When
        final RealMatrix result = dist.getCovariances();

        // Then
        assertNotNull(result);
        assertEquals(result.getRowDimension(), 2);
        assertEquals(result.getColumnDimension(), 2);
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                assertEquals(covariances[i][j], result.getEntry(i, j), 1e-6);
            }
        }
    }
}