package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test file for the PearsonsCorrelation class.
 */
public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(0, pearsonCorrelation.getNObs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullData2DArray() {
        double[][] data = null;
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData2DArray() {
        double[][] data = new double[0][0];
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testData2DArrayTooFewColumns() {
        double[][] data = new double[][]{{1.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testData2DArrayTooFewRows() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.0}};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testData2DArrayCorrect() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, pearsonCorrelation.getNObs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        new PearsonsCorrelation(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRealMatrix() {
        RealMatrix matrix = null;
        new PearsonsCorrelation(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooFewColumnsRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0},
                {2.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooFewRowsRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {1.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testRealMatrixCorrect() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, pearsonCorrelation.getNObs());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expected = i == j ? 1.0 : 0.5;
                Assert.assertEquals(expected, correlationMatrix.getEntry(i, j), 0.001);
            }
        }
    }

    @Test
    public void testCovariance() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, pearsonCorrelation.getNObs());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expected = i == j ? 1.0 : 0.5;
                Assert.assertEquals(expected, correlationMatrix.getEntry(i, j), 0.001);
            }
        }

        RealMatrix correlationStandardErrorsMatrix = pearsonCorrelation.getCorrelationStandardErrors();
        Assert.assertEquals(3, correlationStandardErrorsMatrix.getRowDimension());
        Assert.assertEquals(3, correlationStandardErrorsMatrix.getColumnDimension());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expected = Math.sqrt((1.0 - 0.5 * 0.5) / (3.0 - 2.0));
                Assert.assertEquals(expected, correlationStandardErrorsMatrix.getEntry(i, j), 0.001);
            }
        }

        RealMatrix correlationPValuesMatrix = null;
        try {
            correlationPValuesMatrix = pearsonCorrelation.getCorrelationPValues();
        } catch (MathException e) {
            // Expected
        }
        Assert.assertNull(correlationPValuesMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};

        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        Assert.assertEquals(0.5, correlation, 0.001);

        correlation = new PearsonsCorrelation().correlation(yArray, xArray);
        Assert.assertEquals(0.5, correlation, 0.001);

        double[] xArray1 = {1.0, 2.0};
        double[] yArray1 = {4.0, 5.0};

        Assert.assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation().correlation(xArray1, yArray1));

        double[] xArray2 = {1.0};
        double[] yArray2 = {4.0, 5.0};

        Assert.assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation().correlation(xArray2, yArray2));
    }
}