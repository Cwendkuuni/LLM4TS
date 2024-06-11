package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test cases for {@link MultivariateNormalDistribution}.
 */
public class MultivariateNormalDistributionTest {

    /**
     * Test of constructor MultivariateNormalDistribution.
     *
     * @throws Exception any exception
     */
    @Test
    public void testConstructor() throws Exception {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertArrayEquals(means, dist.getMeans());
        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(means.length, covarianceMatrix.getRowDimension());
        assertEquals(means.length, covarianceMatrix.getColumnDimension());
        assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        assertEquals(covariances[0].length, covarianceMatrix.getRowDimension());
        assertArrayEquals(covariances, covarianceMatrix.getData());
    }

    /**
     * Test of getMeans method, of class MultivariateNormalDistribution.
     */
    @Test
    public void testGetMeans() {
        final MultivariateNormalDistribution instance = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 1.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        final double[] expResult = new double[]{0.0, 1.0};
        final double[] result = instance.getMeans();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getCovariances method, of class MultivariateNormalDistribution.
     */
    @Test
    public void testGetCovariances() {
        final MultivariateNormalDistribution instance = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 1.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        final double[][] expResult = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        final RealMatrix result = instance.getCovariances();
        assertEquals(result.getRowDimension(), expResult.length);
        assertEquals(result.getColumnDimension(), expResult[0].length);
        assertArrayEquals(expResult, result.getData());
    }

    /**
     * Test of density method, of class MultivariateNormalDistribution.
     */
    @Test
    public void testDensity() {
        final MultivariateNormalDistribution instance = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 1.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        double[] values = new double[]{1.5, 2.0};
        double expected = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(instance.covarianceMatrixDeterminant, -0.5) * instance.getExponentTerm(values);
        double result = instance.density(values);
        assertEquals(expected, result, 1e-5);
    }

    /**
     * Test of sample method, of class MultivariateNormalDistribution.
     */
    @Test
    public void testSample() {
        final MultivariateNormalDistribution instance = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 1.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        double[] expResult = new double[]{0.0, 1.0};
        double[] result = instance.sample();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getStandardDeviations method, of class MultivariateNormalDistribution.
     */
    @Test
    public void testGetStandardDeviations() {
        final MultivariateNormalDistribution instance = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 1.0},
                new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        double[] expResult = new double[]{FastMath.SQRT(1.0), FastMath.SQRT(0.5)};
        double[] result = instance.getStandardDeviations();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of exception when covariance matrix is not positive definite.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test of exception when covariance matrix is singular.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    /**
     * Test of exception when dimensions do not match.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        final double[] means = new double[]{0.0, 1.0};
        final double[][] covariances = new double[][]{{1.0}, {1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }
}