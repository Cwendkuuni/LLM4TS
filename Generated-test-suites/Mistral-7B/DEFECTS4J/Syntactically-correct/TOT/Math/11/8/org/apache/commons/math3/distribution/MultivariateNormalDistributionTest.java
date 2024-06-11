package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSparseMatrix;
import org.apache.commons.math3.linear.RealArray2DVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {
    private static final int N = 5;
    private static final int M = 10000;
    private static final double EPS = 1e-10;

    private double[] means;
    private double[][] covariances;
    private MultivariateNormalDistribution dist;

    @Before
    public void setUp() {
        means = new double[]{0.0, 1.0, 2.0, 3.0, 4.0};
        covariances = new double[][]{{1.0, 0.5, 0.1, 0.0, 0.0},
            {0.5, 2.0, 1.5, 0.5, 0.5},
            {0.1, 1.5, 3.0, 1.5, 1.5},
            {0.0, 0.5, 1.5, 4.0, 3.0},
            {0.0, 0.5, 1.5, 3.0, 5.0}};

        dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test that the mean vector matches the constructor argument.
     */
    @Test
    public void testMeans() {
        Assert.assertArrayEquals(means, dist.getMeans(), EPS);
    }

    /**
     * Test that the covariance matrix matches the constructor argument.
     */
    @Test
    public void testCovarianceMatrix() {
        final RealMatrix mat = dist.getCovariances();
        final double[][] covarianceMatrix = mat.getData();
        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances.length; j++) {
                Assert.assertEquals(covariances[i][j], covarianceMatrix[i][j], EPS);
            }
        }
    }

    /**
     * Test that the inverse covariance matrix can be computed and is the inverse of
     * the constructor argument.
     */
    @Test
    public void testCovarianceMatrixInverse() {
        final RealMatrix inverse = dist.getCovarianceMatrixInverse();
        final double[][] inverseMat = inverse.getData();
        final RealMatrix covarianceMatrix = dist.getCovariances().copy();

        final RealMatrix expected = new EigenDecomposition(covarianceMatrix.copy())
                .getSolver().getInverse();

        for (int i = 0; i < inverseMat.length; i++) {
            for (int j = 0; j < inverseMat[i].length; j++) {
                Assert.assertEquals(expected.getEntry(i, j), inverseMat[i][j], EPS);
            }
        }
    }

    /**
     * Test that the determinant of the covariance matrix is non-negative.
     */
    @Test
    public void testCovarianceMatrixDeterminant() {
        Assert.assertFalse("Determinant should be positive", dist.getCovarianceMatrixDeterminant() < 0);
    }

    /**
     * Test that the distribution has the expected mean.
     */
    @Test
    public void testExpectedMean() {
        final double[] means = dist.getMeans();
        final RealVector expected = new Array2DVector(new double[]{0.0, 1.0, 2.0, 3.0, 4.0}, 1);
        final RealVector actual = dist.sample(M, new Well19937c());

        final RealVector diff = actual.subtract(expected);
        final double norm = diff.normInf();
        Assert.assertTrue("Mean should be close to the expected value", norm < EPS);
    }

    /**
     * Test that the distribution has the expected covariance matrix.
     */
    @Test
    public void testExpectedCovarianceMatrix() {
        final double[] means = dist.getMeans();
        final int dim = dist.getDimension();
        final double[][] covariances = new double[dim][dim];

        for (int i = 0; i < M; i++) {
            final double[] sample = dist.sample();
            for (int j = 0; j < dim; j++) {
                covariances[j][j] += FastMath.pow(sample[j] - means[j], 2);
            }
            for (int j = 0; j < dim; j++) {
                covariances[j][dim + j] += (sample[j] - means[j]) * (sample[dim + j] - means[dim + j]);
            }
        }

        final RealMatrix covarianceMatrix = new Array2DRowRealMatrix(covariances);
        final double[][] expected = dist.getCovariances().getData();

        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances.length; j++) {
                Assert.assertEquals(expected[i][j], covariances[i][j], EPS);
            }
        }
    }

    /**
     * Test that the distribution has the expected correlation matrix.
     */
    @Test
    public void testExpectedCorrelationMatrix() {
        final double[] means = dist.getMeans();
        final int dim = dist.getDimension();
        final double[][] covariances = new double[dim][dim];

        for (int i = 0; i < M; i++) {
            final double[] sample = dist.sample();
            for (int i2 = 0; i2 < dim; i2++) {
                covariances[i2][i2] += FastMath.pow(sample[i2] - means[i2], 2);
            }
            for (int j = 0; j < dim; j++) {
                covariances[j][i2] += (sample[j] - means[j]) * (sample[i2] - means[i2]);
            }
        }

        final RealMatrix covarianceMatrix = new Array2DRowRealMatrix(covariances);
        final RealMatrix correlationMatrix = covarianceMatrix.copy();
        final RealMatrix identity = new Array2DRowRealMatrix(new double[dim][dim], 0.0);
        identity.setIdentity();
        identity = identity.subtract(covarianceMatrix.multiply(identity).multiply(covarianceMatrix));

        final PearsonsCorrelation pearson = new PearsonsCorrelation();
        final RealMatrix expected = pearson.correlationMatrix(dist.sample(M, new Well19937c()).toArray(new double[0][M]));

        final RealMatrix diff = new RealArray2DSparseMatrix(expected).subtract(correlationMatrix);
        final double norm = diff.normInf();
        Assert.assertTrue("Correlation matrix should be close to the expected value", norm < EPS);
    }

    /**
     * Test that an IllegalArgumentException is thrown if the means and covariances
     * arrays have the wrong length.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIllegalArgumentException() {
        new MultivariateNormalDistribution(new Well19937c(), new double[0], covariances);
    }

    /**
     * Test that a SingularMatrixException is thrown if the covariance matrix is
     * not positive definite.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        final double[][] singCovariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, singCovariances);
    }
}