package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MultivariateNormalDistribution}
 */
public class MultivariateNormalDistributionTest {

    /**
     * Tests the constructor with valid input
     */
    @Test
    public void testValidConstruction() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        Assert.assertArrayEquals(means, dist.getMeans(), 0.001);

        RealMatrix covMat = dist.getCovariances().copy();
        Assert.assertArrayEquals(covariances[0], covMat.getRowVector(0).toArray(), 0.001);
        Assert.assertArrayEquals(covariances[1], covMat.getRowVector(1).toArray(), 0.001);
    }

    /**
     * Tests the constructor with invalid input (dimension mismatch)
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidConstruction1() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Tests the constructor with invalid input (singular covariance matrix)
     */
    @Test(expected = SingularMatrixException.class)
    public void testInvalidConstruction2() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 0.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Tests the constructor with negative eigenvalues in the covariance matrix
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidConstruction3() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Tests the density method for valid input
     */
    @Test
    public void testDensityValidInput() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        final double[] testValues = {1.5, 2.5};
        double density = dist.density(testValues);

        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(dist.getCovariances().getDeterminant(), -0.5) * getExponentTerm(dist, testValues);
        Assert.assertEquals(expectedDensity, density, 0.001);
    }

    /**
     * Tests the density method for edge cases
     */
    @Test
    public void testDensityEdgeCases() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        final double[] testValues = {-5.0, -5.0};
        double density = dist.density(testValues);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(dist.getCovariances().getDeterminant(), -0.5) * getExponentTerm(dist, testValues);
        Assert.assertEquals(expectedDensity, density, 0.001);

        double[] testValues2 = {5.0, 5.0};
        density = dist.density(testValues2);
        expectedDensity = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(dist.getCovariances().getDeterminant(), -0.5) * getExponentTerm(dist, testValues2);
        Assert.assertEquals(expectedDensity, density, 0.001);
    }

    /**
     * Tests the density method for error cases
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityErrorCases() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        final double[] testValues = {1.5, 2.5, 3.0};
        dist.density(testValues);
    }

    /**
     * Tests the sample method for valid input
     */
    @Test
    public void testSampleValidInput() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] sample = dist.sample();

        for (int i = 0; i < sample.length; i++) {
            Assert.assertNotSame(means[i], sample[i], 0.001);
        }

        double[] testMeans = dist.getMeans();
        for (int i = 0; i < sample.length; i++) {
            double[] centered = new double[]{sample[i] - testMeans[i]};
            double[] preMultiplied = dist.getCovariances().inverse().operate(centered);
            double sum = 0;
            for (int j = 0; j < preMultiplied.length; j++) {
                sum += preMultiplied[j] * centered[j];
            }
            double exponentTerm = FastMath.exp(-0.5 * sum);
            Assert.assertEquals(exponentTerm, 1.0, 0.001);
        }
    }

    /**
     * Tests the sample method for edge cases
     */
    @Test
    public void testSampleEdgeCases() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] sample = dist.sample();

        for (int i = 0; i < sample.length; i++) {
            Assert.assertNotSame(means[i], sample[i], 0.001);
        }

        double[] testMeans = dist.getMeans();
        double[] sampleEdgeCase = new double[sample.length];
        for (int i = 0; i < sampleEdgeCase.length; i++) {
            sampleEdgeCase[i] = Double.NEGATIVE_INFINITY;
        }
        try {
            dist.sample();
            Assert.fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        for (int i = 0; i < sampleEdgeCase.length; i++) {
            sampleEdgeCase[i] = Double.POSITIVE_INFINITY;
            double[] centered = new double[]{sampleEdgeCase[i] - testMeans[i]};
            double[] preMultiplied = dist.getCovariances().inverse().operate(centered);
            double sum = 0;
            for (int j = 0; j < preMultiplied.length; j++) {
                sum += preMultiplied[j] * centered[j];
            }
            double exponentTerm = FastMath.exp(-0.5 * sum);
            Assert.assertEquals(exponentTerm, 0.0, 0.001);
        }
    }

    private double getExponentTerm(MultivariateNormalDistribution dist, double[] vals) {
        double[] centered = new double[vals.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = vals[i] - dist.getMeans()[i];
        }
        double[] preMultiplied = dist.getCovariances().inverse().operate(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.exp(-0.5 * sum);
    }
}