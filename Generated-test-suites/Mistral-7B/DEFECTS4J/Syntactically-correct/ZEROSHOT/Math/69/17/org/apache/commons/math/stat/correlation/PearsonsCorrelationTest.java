package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.junit.Test;

/**
 * JUnit tests for the {@link PearsonsCorrelation} class
 */
public class PearsonsCorrelationTest {

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation()}. Test an
     * empty correlation matrix
     */
    @Test
    public void testEmptyPearsonsCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertEquals(0, pc.getCorrelationMatrix().getRowDimension());
        assertEquals(0, pc.getCorrelationMatrix().getColumnDimension());
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(double[][])}. Test
     * a correlation matrix from a 2x2 double array
     */
    @Test
    public void testPearsonsCorrelationDoubleArray() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-14);
        assertEquals(0.64, correlationMatrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix)}. Test
     * a correlation matrix from a 2x2 RealMatrix
     */
    @Test
    public void testPearsonsCorrelationRealMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-14);
        assertEquals(0.64, correlationMatrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationMatrix()}. Test getting a
     * correlation matrix from empty {@link PearsonsCorrelation} instance
     */
    @Test(expected = IllegalStateException.class)
    public void testGetCorrelationMatrixEmptyInstance() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.getCorrelationMatrix();
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}. Test
     * getting a correlation standard errors matrix from an empty {@link PearsonsCorrelation}
     * instance
     */
    @Test(expected = IllegalStateException.class)
    public void testGetCorrelationStandardErrorsEmptyInstance() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.getCorrelationStandardErrors();
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}. Test getting a
     * correlation p-values matrix from an empty {@link PearsonsCorrelation} instance
     */
    @Test(expected = IllegalStateException.class)
    public void testGetCorrelationPValuesEmptyInstance() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.getCorrelationPValues();
    }

    /**
     * Test method for {@link PearsonsCorrelation#computeCorrelationMatrix(RealMatrix)}. Test
     * computing correlation matrix from a 3x3 RealMatrix
     */
    @Test
    public void testComputeCorrelationMatrixRealMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(matrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-14);
        assertEquals(-0.6, correlationMatrix.getEntry(0, 2), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-14);
        assertEquals(0.5, correlationMatrix.getEntry(1, 1), 1e-14);
        assertEquals(-0.3, correlationMatrix.getEntry(1, 2), 1e-14);
        assertEquals(-0.6, correlationMatrix.getEntry(2, 0), 1e-14);
        assertEquals(-0.3, correlationMatrix.getEntry(2, 1), 1e-14);
        assertEquals(0.5, correlationMatrix.getEntry(2, 2), 1e-14);
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}. Test
     * correlation between two arrays with length 2
     */
    @Test
    public void testCorrelationTwoArraysLengthTwo() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {2.0, 3.0};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 1e-14);
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}. Test
     * correlation between two arrays with different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationDifferentLengthArrays() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {2.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}. Test
     * correlation between two arrays with insufficient data (less than 2)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1.0};
        double[] yArray = {1.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Test method for {@link PearsonsCorrelation#covarianceToCorrelation(RealMatrix)}. Test
     * converting a covariance matrix to a correlation matrix
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-14);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-14);
        assertEquals(0.64, correlationMatrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}. Test
     * getting correlation standard errors from an empty {@link PearsonsCorrelation} instance
     */
    @Test(expected = IllegalStateException.class)
    public void testGetCorrelationStandardErrorsEmptyInstance() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.getCorrelationStandardErrors();
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}. Test
     * getting correlation standard errors from {@link PearsonsCorrelation} instance with
     * correlation matrix size 2x2
     */
    @Test
    public void testGetCorrelationStandardErrorsCorrelationMatrixSizeTwo() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        assertEquals(2, correlationStandardErrors.getRowDimension());
        assertEquals(2, correlationStandardErrors.getColumnDimension());
        assertEquals(0.32, correlationStandardErrors.getEntry(0, 0), 1e-14);
        assertEquals(0.48, correlationStandardErrors.getEntry(0, 1), 1e-14);
        assertEquals(0.48, correlationStandardErrors.getEntry(1, 0), 1e-14);
        assertEquals(0.32, correlationStandardErrors.getEntry(1, 1), 1e-14);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}. Test getting
     * correlation p-values from an empty {@link PearsonsCorrelation} instance
     */
    @Test(expected = IllegalStateException.class)
    public void testGetCorrelationPValuesEmptyInstance() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.getCorrelationPValues();
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}. Test getting
     * correlation p-values from {@link PearsonsCorrelation} instance with correlation
     * matrix size 2x2
     */
    @Test
    public void testGetCorrelationPValuesCorrelationMatrixSizeTwo() throws MathException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        assertEquals(2, correlationPValues.getRowDimension());
        assertEquals(2, correlationPValues.getColumnDimension());
        assertEquals(0.2045, correlationPValues.getEntry(0, 0), 1e-14);
        assertEquals(0.2045, correlationPValues.getEntry(1, 0), 1e-14);
        assertEquals(0.409, correlationPValues.getEntry(0, 1), 1e-14);
        assertEquals(0.409, correlationPValues.getEntry(1, 1), 1e-14);
    }
}