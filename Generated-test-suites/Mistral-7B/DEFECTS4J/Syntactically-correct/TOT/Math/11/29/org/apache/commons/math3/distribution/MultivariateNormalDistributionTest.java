package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    private static final double EPS = 1e-12;
    private MultivariateNormalDistribution dist;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        rng = new Well19937c();
        dist = new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] means = dist.getMeans();
        assertEquals(means.length, 2);
        assertEquals(means[0], 0.0, EPS);
        assertEquals(means[1], 0.0, EPS);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix covariances = dist.getCovariances();
        assertEquals(covariances.getRowDimension(), 2);
        assertEquals(covariances.getColumnDimension(), 2);
        assertEquals(covariances.getEntry(0, 0), 1.0, EPS);
        assertEquals(covariances.getEntry(0, 1), 0.5, EPS);
        assertEquals(covariances.getEntry(1, 0), 0.5, EPS);
        assertEquals(covariances.getEntry(1, 1), 1.0, EPS);
    }

    @Test
    public void testSample() {
        double[] sample = dist.sample();
        assertEquals(sample.length, 2);
        // Check that sample is within the bounds
        for (int i = 0; i < sample.length; i++) {
            assertTrue(sample[i] >= -5 * EPS && sample[i] <= 5 * EPS);
        }
    }

    @Test
    public void testDensity() {
        double[] sample = dist.sample();
        double density = dist.density(sample);
        double logDensity = FastMath.log(density);
        double meanLogDensity = -0.5 * (FastMath.log(2 * FastMath.PI) * 2 + FastMath.log(dist.getCovariances().determinant()) + getExponentTerm(sample));
        assertEquals(logDensity, meanLogDensity, EPS);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithWrongSize() {
        double[] sample = new double[3];
        dist.density(sample);
    }

    @Test
    public void testGetStandardDeviations() {
        double[] stdDeviations = dist.getStandardDeviations();
        assertEquals(stdDeviations.length, 2);
        assertEquals(stdDeviations[0], FastMath.sqrt(covariances.getEntry(0, 0)), EPS);
        assertEquals(stdDeviations[1], FastMath.sqrt(covariances.getEntry(1, 1)), EPS);
    }

    @Test
    public void testGetSampleMatrix() {
        final int numberSamples = 10;
        final int dim = dist.getDimension();

        double[][] sampleMatrix = new double[numberSamples][dim];
        for (int i = 0; i < numberSamples; i++) {
            sampleMatrix[i] = dist.sample();
        }

        RealMatrix matrix = new Array2DRowRealMatrix(sampleMatrix);
        RealMatrix expectedMatrix = dist.samplingMatrix.copy();
        assertEquals(matrix, expectedMatrix);
    }

    private static double getExponentTerm(double[] values) {
        final double[] centered = new double[values.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = values[i] - dist.means[i];
        }
        final double[] preMultiplied = dist.covarianceMatrixInverse.operate(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return -0.5 * sum;
    }
}