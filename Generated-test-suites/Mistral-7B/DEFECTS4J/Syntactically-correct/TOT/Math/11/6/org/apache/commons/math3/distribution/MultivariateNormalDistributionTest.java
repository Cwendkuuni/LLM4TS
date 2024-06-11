package org.apache.commons.math3.distribution;

import org.apache.commons.math3.LinearAlgebraTests;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test construction with given mean and covariance matrix.
     */
    @Test
    public void testConstruction() {
        // Test with given mean and covariance matrix
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertEquals(means, dist.getMeans());
        RealMatrix covMatrix = dist.getCovariances();
        LinearAlgebraTests.assertEquals(covariances, covMatrix.getData(), 0.001);
    }

    /**
     * Test construction with random mean and covariance matrix.
     */
    @Test
    public void testConstructionRandom() {
        // Test with random mean and covariance matrix
        RandomGenerator rng = new Well19937c();
        final int dim = 2;
        final double[] means = new double[dim];
        final double[][] covariances = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            means[i] = rng.nextGaussian();
            for (int j = 0; j < dim; j++) {
                covariances[i][j] = rng.nextGaussian();
            }
        }
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(dim, dist.getDimension());
        assertArrayEquals(means, dist.getMeans(), 0.001);
        RealMatrix covMatrix = dist.getCovariances();
        LinearAlgebraTests.assertEquals(covariances, covMatrix.getData(), 0.001);
    }

    /**
     * Test density with valid input.
     */
    @Test
    public void testDensityValidInput() {
        // Test density with valid input
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        final double[] values = {1.0, 1.5};
        double density = dist.density(values);
        // Calculate the density using the formula
        double exponentTerm = dist.getExponentTerm(values);
        double term1 = FastMath.pow(2 * FastMath.PI, -dist.getDimension() / 2);
        double term2 = FastMath.pow(dist.getCovariances().determinant(), -0.5);
        double densityCalculated = term1 * term2 * exponentTerm;
        assertEquals(density, densityCalculated, 0.001);
    }

    /**
     * Test density with incorrect number of dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityIncorrectNumberOfDimensions() {
        // Test density with incorrect number of dimensions
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        final double[] values = {1.0};
        dist.density(values);
    }

    /**
     * Test sample with valid input.
     */
    @Test
    public void testSampleValidInput() {
        // Test sample with valid input
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        assertEquals(dist.getDimension(), sample.length);
    }

    /**
     * Test sample with random input.
     */
    @Test
    public void testSampleRandomInput() {
        // Test sample with random input
        RandomGenerator rng = new Well19937c();
        final int dim = 2;
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, new double[dim], new double[dim][dim]);
        double[] sample = dist.sample();
        for (int i = 0; i < sample.length; i++) {
            assertTrue(sample[i] > -1000);
            assertTrue(sample[i] < 1000);
        }
    }

    /**
     * Test getting standard deviations.
     */
    @Test
    public void testGetStandardDeviations() {
        // Test getting standard deviations
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] standardDeviations = dist.getStandardDeviations();
        assertEquals(dist.getDimension(), standardDeviations.length);
        for (int i = 0; i < standardDeviations.length; i++) {
            assertTrue(standardDeviations[i] > 0);
        }
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructionInvalidInput() {
        // Test constructor with invalid input
        final double[] means = {0.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test constructor with non-positive definite covariance matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructionNonPositiveDefinite() {
        // Test constructor with non-positive definite covariance matrix
        final double[] means = {0.0};
        final double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    /**
     * Test constructor with singular covariance matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testConstructionSingular() {
        // Test constructor with singular covariance matrix
        final double[] means = {0.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 0.0}};
        new MultivariateNormalDistribution(means, covariances);
    }
}