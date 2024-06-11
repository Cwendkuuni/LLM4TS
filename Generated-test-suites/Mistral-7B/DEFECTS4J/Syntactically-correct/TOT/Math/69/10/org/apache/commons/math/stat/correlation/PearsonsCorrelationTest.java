package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for PearsonsCorrelation class
 */
public class PearsonsCorrelationTest {

    /**
     * Test correlation matrix computation from a double array
     */
    @Test
    public void testCorrelationMatrixFromDoubleArray() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new RealArray2D[]{
                new Array2DRowRealMatrix(new double[][]{1.0, 0.8327, 0.8258, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8327, 1.0, 0.8258, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8258, 0.8327, 1.0, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8327, 0.8327, 0.8327, 1.0}, false)
        });
        RealMatrix result = correlation.getCorrelationMatrix();
        Assert.assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Test correlation matrix computation from a RealMatrix
     */
    @Test
    public void testCorrelationMatrixFromRealMatrix() {
        RealMatrix data = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new RealArray2D[]{
                new Array2DRowRealMatrix(new double[][]{1.0, 0.8327, 0.8258, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8327, 1.0, 0.8258, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8258, 0.8327, 1.0, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8327, 0.8327, 0.8327, 1.0}, false)
        });
        RealMatrix result = correlation.getCorrelationMatrix();
        Assert.assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Test correlation matrix computation from a Covariance
     */
    @Test
    public void testCorrelationMatrixFromCovariance() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());
        RealMatrix expected = new Array2DRowRealMatrix(new RealArray2D[]{
                new Array2DRowRealMatrix(new double[][]{1.0, 0.8327, 0.8258, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8327, 1.0, 0.8258, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8258, 0.8327, 1.0, 0.8327}, false),
                new Array2DRowRealMatrix(new double[][]{0.8327, 0.8327, 0.8327, 1.0}, false)
        });
        RealMatrix result = correlation.getCorrelationMatrix();
        Assert.assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Test correlation between two arrays
     */
    @Test
    public void testCorrelationBetweenArrays() {
        double[] xArray = new double[]{1.0, 2.0, 3.0};
        double[] yArray = new double[]{2.0, 4.0, 6.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double expected = 0.8;
        double result = correlation.correlation(xArray, yArray);
        assertEquals(expected, result, 0.001);
    }

    /**
     * Test correlation between two arrays with different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationBetweenArraysWithDifferentLengths() {
        double[] xArray = new double[]{1.0, 2.0, 3.0};
        double[] yArray = new double[]{2.0, 4.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    /**
     * Test correlation between two arrays with insufficient data
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationBetweenArraysWithInsufficientData() {
        double[] xArray = new double[]{1.0};
        double[] yArray = new double[]{2.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    /**
     * Test correlation matrix standard errors
     */
    @Test
    public void testCorrelationStandardErrors() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix standardErrors = correlation.getCorrelationStandardErrors();
        RealMatrix expected = new Array2DRowRealMatrix(new RealArray2D[]{
                new Array2DRowRealMatrix(new double[][]{0.4216, 0.6729, 0.6526, 0.6729}, false),
                new Array2DRowRealMatrix(new double[][]{0.6729, 0.4216, 0.6526, 0.6729}, false),
                new Array2DRowRealMatrix(new double[][]{0.6526, 0.6729, 0.4216, 0.6729}, false),
                new Array2DRowRealMatrix(new double[][]{0.6729, 0.6729, 0.6729, 0.4216}, false)
        });
        RealMatrix result = correlation.getCorrelationStandardErrors();
        Assert.assertNotNull(result);
        assertEquals(expected, result);
    }

    /**
     * Test correlation matrix p-values
     */
    @Test
    public void testCorrelationPValues() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix pValues = correlation.getCorrelationPValues();
        RealMatrix expected = new Array2DRowRealMatrix(new RealArray2D[]{
                new Array2DRowRealMatrix(new double[][]{0.0005, 0.0013, 0.0013, 0.0005}, false),
                new Array2DRowRealMatrix(new double[][]{0.0013, 0.0005, 0.0013, 0.0005}, false),
                new Array2DRowRealMatrix(new double[][]{0.0013, 0.0005, 0.0005, 0.0005}, false),
                new Array2DRowRealMatrix(new double[][]{0.0005, 0.0005, 0.0005, 0.0005}, false)
        });
        RealMatrix result = correlation.getCorrelationPValues();
        Assert.assertNotNull(result);
        assertEquals(expected, result);
    }
}