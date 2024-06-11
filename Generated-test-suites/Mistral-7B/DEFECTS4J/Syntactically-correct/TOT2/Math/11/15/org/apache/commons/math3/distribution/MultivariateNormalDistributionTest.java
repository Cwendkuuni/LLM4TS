package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link MultivariateNormalDistribution} class.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        // Test with valid input
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(means.length, distribution.getDimension());
        assertEquals(means, distribution.getMeans());
        RealMatrix covarianceMatrix = distribution.getCovariances();
        assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        assertEquals(covariances[0].length, covarianceMatrix.getRowDimension());
        assertEquals(covariances, covarianceMatrix.getData());

        // Test density
        double[] testValues = {1.5, 3.5};
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -means.length / 2) * FastMath.pow(distribution.getCovariances().determinant(), -0.5) * MultivariateNormalDistribution.getExponentTerm(testValues, distribution.getMeans(), distribution.getCovariances(), distribution.getCovariancesInverse(), distribution.getCovariancesDeterminant());
        double actualDensity = distribution.density(testValues);
        assertEquals(expectedDensity, actualDensity, 1e-12);

        // Test sample
        double[] sampleValue = distribution.sample();
        assertEquals(means.length, sampleValue.length);
        for (int i = 0; i < means.length; i++) {
            assertEquals(means[i], sampleValue[i], 1e-12);
        }

        // Test with invalid input
        double[] invalidMeans = {};
        double[][] invalidCovariances = {{1.0}, {1.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), invalidMeans, invalidCovariances);
            fail("DimensionMismatchException or SingularMatrixException should have been thrown.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        } catch (SingularMatrixException e) {
            // Expected exception
        }

        // Test eigenvalues are positive
        double[][] covariancesWithNegativeEigenvalue = {{1.0, -0.5}, {0.5, 2.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariancesWithNegativeEigenvalue);
            fail("NonPositiveDefiniteMatrixException should have been thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }

        // Test incorrect number of columns in covariances
        double[][] covariancesWithMismatchedColumns = {{1.0, 0.5}, {1.0, 2.1}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariancesWithMismatchedColumns);
            fail("DimensionMismatchException should have been thrown.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }

        // Test incorrect number of rows in covariances
        double[][] covariancesWithMismatchedRows = {{1.0, 0.5}, {1.0, 2.0, 3.0}};
        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariancesWithMismatchedRows);
            fail("DimensionMismatchException should have been thrown.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }

        // Test incorrect number of dimensions for sample
        double[] sampleValueWithInvalidLength = distribution.sample();
        fail("DimensionMismatchException should have been thrown.");
    }
}