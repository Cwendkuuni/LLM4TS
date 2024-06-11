package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSparseMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        Assert.assertNull(pc.getCorrelationMatrix());
        Assert.assertEquals(0, pc.nObs);
    }

    @Test
    public void testEmptyData2DArrays() {
        double[][] emptyData = {};
        PearsonsCorrelation pc = new PearsonsCorrelation(emptyData);
        Assert.assertNull(pc.getCorrelationMatrix());
        Assert.assertEquals(0, pc.nObs);
    }

    @Test
    public void testEmptyDataRealMatrix() {
        RealMatrix emptyData = new Array2DRowRealMatrix(new double[0][0]);
        PearsonsCorrelation pc = new PearsonsCorrelation(emptyData);
        Assert.assertNull(pc.getCorrelationMatrix());
        Assert.assertEquals(0, pc.nObs);
    }

    @Test
    public void testDifferentDimensionsData2DArrays() {
        double[][] data1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        double[][] data2 = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
        try {
            new PearsonsCorrelation(data1, data2);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testZeroData2DArrays() {
        double[][] zeroData = {{0, 0}, {0, 0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(zeroData);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        Assert.assertEquals(expected, correlationMatrix);
        Assert.assertEquals(2, pc.nObs);
    }

    @Test
    public void testIdentityDataRealMatrix() {
        RealMatrix identityData = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        PearsonsCorrelation pc = new PearsonsCorrelation(identityData);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        Assert.assertEquals(expected, correlationMatrix);
        Assert.assertEquals(2, pc.nObs);
    }

    @Test
    public void testNonIdentityDataRealMatrix() {
        RealMatrix data = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.8109255166066971, 0.809686555057601},
                {0.809686555057601, 0.8109255166066971}});
        Assert.assertEquals(expected, correlationMatrix, 0.0001);
        Assert.assertEquals(3, pc.nObs);
    }

    @Test
    public void testNonIdentityData2DArrays() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.8109255166066971, 0.809686555057601},
                {0.809686555057601, 0.8109255166066971}});
        Assert.assertEquals(expected, correlationMatrix, 0.0001);
        Assert.assertEquals(3, pc.nObs);
    }

    @Test
    public void testCovarianceMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.8109255166066971, 0.809686555057601},
                {0.809686555057601, 0.8109255166066971}});
        Assert.assertEquals(expected, correlationMatrix, 0.0001);
        Assert.assertEquals(3, pc.nObs);
    }

    @Test
    public void testSparseCovarianceMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Covariance covariance = new Covariance(new RealArray2DSparseMatrix(data));
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.8109255166066971, 0.809686555057601},
                {0.809686555057601, 0.8109255166066971}});
        Assert.assertEquals(expected, correlationMatrix, 0.0001);
        Assert.assertEquals(3, pc.nObs);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        RealVector column1 = correlationMatrix.getColumnVector(0);
        RealVector column2 = correlationMatrix.getColumnVector(1);
        double stdErr11 = correlationStandardErrors.getEntry(0, 0);
        double stdErr12 = correlationStandardErrors.getEntry(0, 1);
        double stdErr21 = correlationStandardErrors.getEntry(1, 0);
        double stdErr22 = correlationStandardErrors.getEntry(1, 1);
        Assert.assertEquals(stdErr11, 0.1209278322997422, 0.0001);
        Assert.assertEquals(stdErr12, 0.1209278322997422, 0.0001);
        Assert.assertEquals(stdErr21, 0.1209278322997422, 0.0001);
        Assert.assertEquals(stdErr22, 0.1209278322997422, 0.0001);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        double pValue11 = correlationPValues.getEntry(0, 0);
        double pValue12 = correlationPValues.getEntry(0, 1);
        double pValue21 = correlationPValues.getEntry(1, 0);
        double pValue22 = correlationPValues.getEntry(1, 1);
        Assert.assertEquals(pValue11, 0.3535533905932738, 0.0001);
        Assert.assertEquals(pValue12, 0.3535533905932738, 0.0001);
        Assert.assertEquals(pValue21, 0.3535533905932738, 0.0001);
        Assert.assertEquals(pValue22, 0.3535533905932738, 0.0001);
    }
}