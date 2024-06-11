package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for Pearson's correlation coefficient computation.
 */
public class PearsonsCorrelationTest {

    /**
     * Check correlation matrix computation from a rectangular array.
     */
    @Test
    public void testCorrelationMatrixFromData() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);

        // check dimensions
        assertEquals("Unexpected correlation matrix dimensions", 3, pc.correlationMatrix.getRowDimension());
        assertEquals("Unexpected correlation matrix dimensions", 3, pc.correlationMatrix.getColumnDimension());

        // check correlation coefficients
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expected = 1.0;
                if (i != j) {
                    double corr = Statistics.pearson(data[i], data[j]);
                    assertNotSame("Unexpected correlation coefficient", Double.NaN, corr);
                    assertEquals("Unexpected correlation coefficient for variables " + i + " and " + j, expected, pc.correlationMatrix.getEntry(i, j), 1e-8);
                    expected = corr;
                }
            }
        }

        // check diagonal
        for (int i = 0; i < 3; i++) {
            assertEquals("Unexpected diagonal coefficient for variable " + i, 1.0, pc.correlationMatrix.getEntry(i, i), 1e-8);
        }
    }

    /**
     * Check correlation matrix computation from a RealMatrix.
     */
    @Test
    public void testCorrelationMatrixFromMatrix() {
        RealMatrix data = new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(data);

        // check dimensions
        assertEquals("Unexpected correlation matrix dimensions", 3, pc.correlationMatrix.getRowDimension());
        assertEquals("Unexpected correlation matrix dimensions", 3, pc.correlationMatrix.getColumnDimension());

        // check correlation coefficients
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expected = 1.0;
                if (i != j) {
                    double corr = Statistics.pearson(data.getColumnVector(i), data.getColumnVector(j));
                    assertNotSame("Unexpected correlation coefficient", Double.NaN, corr);
                    assertEquals("Unexpected correlation coefficient for variables " + i + " and " + j, expected, pc.correlationMatrix.getEntry(i, j), 1e-8);
                    expected = corr;
                }
            }
        }

        // check diagonal
        for (int i = 0; i < 3; i++) {
            assertEquals("Unexpected diagonal coefficient for variable " + i, 1.0, pc.correlationMatrix.getEntry(i, i), 1e-8);
        }
    }

    /**
     * Check correlation computation from two arrays.
     */
    @Test
    public void testCorrelationFromArrays() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double corr = new PearsonsCorrelation().correlation(x, y);

        // check correlation coefficient
        assertEquals("Unexpected correlation coefficient", 0.8, corr, 1e-8);

        // check standard error
        RealMatrix cm = new PearsonsCorrelation(x, y).correlationMatrix;
        RealMatrix se = cm.copy();
        se.multiply(new TDistributionImpl(2).inverseCumulativeProbability(0.95)).multiply(se);
        double seExpected = Math.sqrt(1.0 - corr * corr) / Math.sqrt(x.length - 1);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double se = se.getEntry(i, j);
                assertEquals("Unexpected standard error", seExpected, se, 1e-8);
            }
        }
    }

    /**
     * Test correlation computation from a covariance matrix.
     */
    @Test
    public void testCorrelationFromCovarianceMatrix() throws MathException {
        double[][] covarianceData = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        RealMatrix covariance = new BlockRealMatrix(covarianceData);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance, covariance.getRowDimension());

        // check correlation coefficients
        double corr = pc.correlationMatrix.getEntry(0, 1);
        assertEquals("Unexpected correlation coefficient for variables 0 and 1", 0.8, corr, 1e-8);
        corr = pc.correlationMatrix.getEntry(1, 2);
        assertEquals("Unexpected correlation coefficient for variables 1 and 2", 0.8, corr, 1e-8);
    }

    /**
     * Test correlation computation from a covariance matrix with non-square shape.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationFromNonSquareCovarianceMatrix() throws MathException {
        double[][] covarianceData = {{1.0, 0.5}, {0.5, 1.0}, {0.5, 1.0}};
        RealMatrix covariance = new BlockRealMatrix(covarianceData);
        new PearsonsCorrelation(covariance, covariance.getRowDimension());
    }

    /**
     * Test correlation computation from a covariance matrix with negative determinant.
     */
    @Test(expected = MathException.class)
    public void testCorrelationFromCovarianceMatrixWithNegativeDeterminant() throws MathException {
        double[][] covarianceData = {{1.0, -0.5}, {-0.5, 1.0}};
        RealMatrix covariance = new BlockRealMatrix(covarianceData);
        new PearsonsCorrelation(covariance, covariance.getRowDimension());
    }

}