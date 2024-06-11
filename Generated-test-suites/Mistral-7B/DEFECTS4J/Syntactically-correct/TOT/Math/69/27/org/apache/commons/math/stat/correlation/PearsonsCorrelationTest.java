package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test correlation method with two arrays.
     */
    @Test
    public void testCorrelationWithArrays() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {2.0, 3.0, 4.0, 5.0};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        double correlation = pearsonCorrelation.correlation(xArray, yArray);
        assertEquals("Correlation value is incorrect.", 0.8, correlation, 0.01);
    }

    /**
     * Test correlation method with two matrices.
     */
    @Test
    public void testCorrelationWithMatrices() {
        double[][] dataX = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[][] dataY = {{2.0, 3.0, 4.0}, {5.0, 6.0, 7.0}, {8.0, 9.0, 10.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        RealMatrix matrixX = new BlockRealMatrix(dataX);
        RealMatrix matrixY = new BlockRealMatrix(dataY);
        RealMatrix correlationMatrix = pearsonCorrelation.computeCorrelationMatrix(matrixX);
        RealVector correlationVectorX = correlationMatrix.getColumnVector(0);
        RealVector correlationVectorY = correlationMatrix.getColumnVector(1);
        double correlation = correlationVectorX.getEntry(0) * correlationVectorY.getEntry(0);
        assertEquals("Correlation value is incorrect.", 0.8, correlation, 0.01);
    }

    /**
     * Test correlation method with a covariance.
     */
    @Test
    public void testCorrelationWithCovariance() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealVector correlationVectorX = correlationMatrix.getColumnVector(0);
        RealVector correlationVectorY = correlationMatrix.getColumnVector(1);
        double correlation = correlationVectorX.getEntry(0) * correlationVectorY.getEntry(0);
        assertEquals("Correlation value is incorrect.", 0.8, correlation, 0.01);
    }

    /**
     * Test correlation method with insufficient data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1.0};
        double[] yArray = {};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        pearsonCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlation method with different lengths.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithDifferentLengths() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {2.0, 3.0, 4.0};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        pearsonCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test correlationStandardErrors method.
     */
    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        RealMatrix matrixX = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsonCorrelation.computeCorrelationMatrix(matrixX);
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        double correlation = correlationMatrix.getEntry(0, 0) * correlationMatrix.getEntry(1, 0);
        double standardErrorX = correlationStandardErrors.getEntry(0, 0);
        double standardErrorY = correlationStandardErrors.getEntry(0, 1);
        double expectedStandardError = Math.sqrt((1 - correlation * correlation) / (data.length - 2));
        assertEquals("Standard error is incorrect.", expectedStandardError, standardErrorX, 0.01);
        assertEquals("Standard error is incorrect.", expectedStandardError, standardErrorY, 0.01);
    }

    /**
     * Test correlationPValues method.
     */
    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        RealMatrix matrixX = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsonCorrelation.computeCorrelationMatrix(matrixX);
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        double correlation = correlationMatrix.getEntry(0, 0) * correlationMatrix.getEntry(1, 0);
        double pValue = correlationPValues.getEntry(0, 0);
        double expectedPValue = 1.0 - new SimpleRegression().tTest(correlation, data.length - 2);
        assertEquals("p-value is incorrect.", expectedPValue, pValue, 0.01);
    }

    /**
     * Test covarianceToCorrelation method with a covariance matrix.
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrix = {{1.0, 0.5}, {0.5, 1.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonCorrelation.covarianceToCorrelation(new BlockRealMatrix(covarianceMatrix));
        assertEquals("Correlation value is incorrect.", 0.8, correlationMatrix.getEntry(0, 0), 0.01);
        assertEquals("Correlation value is incorrect.", 0.8, correlationMatrix.getEntry(1, 0), 0.01);
        assertEquals("Correlation value is incorrect.", 0.8, correlationMatrix.getEntry(0, 1), 0.01);
        assertEquals("Correlation value is incorrect.", 0.8, correlationMatrix.getEntry(1, 1), 0.01);
    }

    /**
     * Test covarianceToCorrelation method with an empty covariance matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceToCorrelationWithEmptyMatrix() {
        double[][] covarianceMatrix = {};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        pearsonCorrelation.covarianceToCorrelation(new BlockRealMatrix(covarianceMatrix));
    }
}