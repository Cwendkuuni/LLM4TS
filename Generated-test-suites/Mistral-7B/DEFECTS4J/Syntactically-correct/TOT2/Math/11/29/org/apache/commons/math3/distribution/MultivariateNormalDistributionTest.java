package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MultivariateNormalDistribution} class.
 */
public class MultivariateNormalDistributionTest {

    private static final int TEST_DIM = 2;
    private static final double[][] TEST_COVARIANCES = {
            {1.0, 0.5},
            {0.5, 1.0}
    };
    private static final double[] TEST_MEANS = {1.0, 1.0};
    private MultivariateNormalDistribution dist;
    private RealMatrix covMat;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        dist = new MultivariateNormalDistribution(TEST_MEANS, TEST_COVARIANCES) {
            @Override
            public RealMatrix getCovariances() {
                return covMat.copy();
            }
        };
        covMat = new EigenDecomposition(new Array2DRowRealMatrix(TEST_COVARIANCES))
                .getSolver().getInverse();
    }

    @Test
    public void testGetDimension() {
        Assert.assertEquals(TEST_DIM, dist.getDimension());
    }

    @Test
    public void testGetMeans() {
        Assert.assertArrayEquals(TEST_MEANS, dist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        RealMatrix cov = dist.getCovariances();
        RealMatrix expected = new Array2DRowRealMatrix(TEST_COVARIANCES);
        Assert.assertArrayEquals(expected.getData(), cov.getData());
    }

    @Test
    public void testSampleSingleDimension() {
        double mean = dist.getMeans()[0];
        double stdDev = Math.sqrt(dist.getCovariances().getData()[0][0]);

        double sample = dist.sample()[0];
        Assert.assertNotEquals(mean, sample);
        Assert.assertTrue(Precision.abs(sample - mean) < stdDev);
    }

    @Test
    public void testSampleMultipleDimensions() {
        double[] means = dist.getMeans();
        double[] stdDeviations = dist.getStandardDeviations();

        double[] sample = dist.sample();
        for (int i = 0; i < TEST_DIM; i++) {
            Assert.assertNotEquals(means[i], sample[i]);
            Assert.assertTrue(Precision.abs(sample[i] - means[i]) < stdDeviations[i]);
        }
    }

    @Test
    public void testDensity() {
        double[] testValue = new double[TEST_DIM];

        double density = dist.density(testValue);
        Assert.assertNotNull(density);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithWrongSize() {
        double testValue[] = new double[TEST_DIM + 1];
        dist.density(testValue);
    }

    @Test
    public void testGetExponentTerm() {
        double[] testValue = new double[TEST_DIM];
        double exponentTerm = dist.getExponentTerm(testValue);
        Assert.assertNotNull(exponentTerm);
    }

    @Test
    public void testConstructorWithInvalidCovarianceMatrix() {
        double[][] covariances = TEST_COVARIANCES.clone();
        covariances[0][0] = -1.0;
        try {
            new MultivariateNormalDistribution(rng, TEST_MEANS, covariances);
            Assert.fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}