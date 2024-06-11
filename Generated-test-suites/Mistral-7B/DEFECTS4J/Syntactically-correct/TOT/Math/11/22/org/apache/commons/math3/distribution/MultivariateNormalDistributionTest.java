package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {
    private static final int DIM = 3;
    private static final double EPSILON = 1e-9;
    private static final double[] MEANS = {1.0, 2.0, 3.0};
    private static final double[][] COVARIANCES = {
            {1.0, 0.5, 0.25},
            {0.5, 2.0, 1.0},
            {0.25, 1.0, 3.0}
    };
    private static final double[] STD_DEV = {1.0, 1.4142135625, 1.7320508076};
    private MultivariateNormalDistribution dist;
    private RandomGenerator rng;
    private double[] means;
    private RealMatrix covarianceMatrix;
    private RealMatrix covarianceMatrixInverse;

    @Before
    public void setUp() {
        rng = new Well19937c();
        try {
            dist = new MultivariateNormalDistribution(rng, MEANS, COVARIANCES);
            means = dist.getMeans();
            covarianceMatrix = dist.getCovariances();
            covarianceMatrixInverse = dist.getCovarianceMatrixInverse();
        } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception thrown while initializing distribution: " + e.getMessage());
        }
    }

    /**
     * Test constructor with correct input.
     */
    @Test
    public void testConstructor() {
        Assert.assertArrayEquals(MEANS, dist.getMeans());
        RealMatrix covMatrix = dist.getCovariances();
        Assert.assertEquals(DIM, covMatrix.getRowDimension());
        Assert.assertEquals(DIM, covMatrix.getColumnDimension());
        RealVector eigenVectors = covMatrix.getEigenvectors();
        RealMatrix eigenVectorsTranspose = eigenVectors.getTranspose();
        RealMatrix eigenValues = covMatrix.getEigenvalues();
        RealMatrix inverse = covarianceMatrixInverse;
        RealMatrix product = eigenVectorsTranspose.multiply(covarianceMatrixInverse);
        Assert.assertEquals(covarianceMatrix, product.multiply(eigenVectors));
        Assert.assertEquals(inverse, eigenVectorsTranspose.multiply(covarianceMatrix));
    }

    /**
     * Test mean vector.
     */
    @Test
    public void testMeans() {
        Assert.assertArrayEquals(MEANS, dist.getMeans());
    }

    /**
     * Test covariance matrix.
     */
    @Test
    public void testCovarianceMatrix() {
        RealMatrix covMatrix = dist.getCovariances();
        Assert.assertEquals(DIM, covMatrix.getRowDimension());
        Assert.assertEquals(DIM, covMatrix.getColumnDimension());
        RealVector eigenVectors = covMatrix.getEigenvectors();
        RealMatrix eigenVectorsTranspose = eigenVectors.getTranspose();
        RealMatrix eigenValues = covMatrix.getEigenvalues();
        RealMatrix inverse = covarianceMatrixInverse;
        RealMatrix product = eigenVectorsTranspose.multiply(covarianceMatrixInverse);
        Assert.assertEquals(covarianceMatrix, product.multiply(eigenVectors));
        Assert.assertEquals(inverse, eigenVectorsTranspose.multiply(covarianceMatrix));
        for (int i = 0; i < DIM; i++) {
            Assert.assertTrue(FastMath.abs(eigenValues.getEntry(i, 0) - 1.0) < EPSILON);
        }
    }

    /**
     * Test inverse covariance matrix.
     */
    @Test
    public void testCovarianceMatrixInverse() {
        RealMatrix inverse = dist.getCovarianceMatrixInverse();
        RealMatrix product = inverse.multiply(covarianceMatrix);
        RealMatrix identity = new Array2DRowRealMatrix(DIM, DIM);
        for (int i = 0; i < DIM; i++) {
            identity.setEntry(i, i, 1.0);
        }
        Assert.assertEquals(identity, product);
    }

    /**
     * Test density calculation.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[DIM];
        double maxDensity = -Double.MAX_VALUE;
        double minDensity = Double.MAX_VALUE;
        for (int i = 0; i < 10000; i++) {
            testValues = dist.sample();
            double density = dist.density(testValues);
            if (density > maxDensity) {
                maxDensity = density;
            }
            if (density < minDensity) {
                minDensity = density;
            }
        }
        Assert.assertTrue(maxDensity > 0);
        Assert.assertTrue(minDensity > 0);
        for (int i = 0; i < 10000; i++) {
            testValues = dist.sample();
            double density = dist.density(testValues);
            Assert.assertTrue(density > 0);
        }
    }

    /**
     * Test sampling.
     */
    @Test
    public void testSample() {
        RealMatrix sampleMatrix = new Array2DRowRealMatrix(1000, DIM);
        for (int i = 0; i < 1000; i++) {
            double[] sample = dist.sample();
            sampleMatrix.setRow(i, sample);
        }
        RealArray2DSample sampleView = sampleMatrix.columnVectorValue(0);
        RealVector meanSample = new ArrayRealVector(DIM);
        meanSample.setEntry(0, 0, 1.0);
        meanSample.setEntry(1, 0, 2.0);
        meanSample.setEntry(2, 0, 3.0);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationCoefficient = correlation.correlation(Mean.VECTOR_MEAN, sampleView);
        Assert.assertTrue(FastMath.abs(correlationCoefficient - 1.0) < EPSILON);
        for (int i = 0; i < DIM; i++) {
            Assert.assertEquals(MEANS[i], sampleView.getEntry(0, i), EPSILON);
        }
    }
}