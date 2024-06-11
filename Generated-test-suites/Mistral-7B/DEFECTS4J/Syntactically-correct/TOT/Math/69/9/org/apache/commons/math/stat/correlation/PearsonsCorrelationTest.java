package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link PearsonsCorrelation}.
 */
public class PearsonsCorrelationTest {

    /**
     * Test correlation between two arrays.
     */
    @Test
    public void testCorrelationArray() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {1.1, 2.1, 3.1, 4.1, 5.1};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(xArray, yArray);
        double expectedCorrelation = regression.getR();
        Assert.assertEquals(expectedCorrelation, correlation, 1e-9);
    }

    /**
     * Test correlation between two columns of a matrix.
     */
    @Test
    public void testCorrelationMatrixColumns() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0, 4.0, 5.0},
                {1.1, 2.1, 3.1, 4.1, 5.1}
        });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        double correlation = correlationMatrix.getEntry(0, 1);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(matrix.getColumnVector(0), matrix.getColumnVector(1));
        double expectedCorrelation = regression.getR();
        Assert.assertEquals(expectedCorrelation, correlation, 1e-9);
    }

    /**
     * Test correlation between two columns with identical values in a matrix.
     */
    @Test
    public void testCorrelationMatrixIdenticalColumns() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0}
        });
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        double correlation = correlationMatrix.getEntry(0, 0);
        Assert.assertEquals(1.0, correlation, 1e-9);
    }

    /**
     * Test correlation between an empty matrix.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationEmptyMatrix() {
        RealMatrix emptyMatrix = new Array2DRowRealMatrix(0, 0);
        new PearsonsCorrelation(emptyMatrix);
    }

    /**
     * Test correlation between a matrix with less than two columns and two rows.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationInsufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}
        });
        new PearsonsCorrelation(matrix);
    }

    /**
     * Test correlation standard errors.
     */
    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {1.1, 2.1, 3.1, 4.1, 5.1};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        double xMean = SimpleRegression.mean(xArray);
        double yMean = SimpleRegression.mean(yArray);
        double xStdDev = Math.sqrt(SimpleRegression.variance(xArray));
        double yStdDev = Math.sqrt(SimpleRegression.variance(yArray));
        for (int i = 0; i < correlationMatrix.getColumnDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                if (i == j) {
                    Assert.assertEquals(1.0, correlationStandardErrors.getEntry(i, j), 1e-9);
                } else {
                    double expectedSE = Math.sqrt(
                            ((1.0 - correlation * correlation) / (pearsonsCorrelation.nObs - 2.0))
                    );
                    Assert.assertEquals(expectedSE, correlationStandardErrors.getEntry(i, j), 1e-9);
                }
            }
        }
    }

    /**
     * Test correlation p-values.
     */
    @Test
    public void testCorrelationPValues() throws MathException {
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {1.1, 2.1, 3.1, 4.1, 5.1};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        for (int i = 0; i < correlationMatrix.getColumnDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                if (i == j) {
                    Assert.assertEquals(0.0, correlationPValues.getEntry(i, j), 1e-9);
                } else {
                    double n = pearsonsCorrelation.nObs;
                    double r = correlation;
                    double t = Math.abs(r) * Math.sqrt((n - 2.0) / (1.0 - r * r));
                    double expectedPValue = 2 * (1.0 - new TDistribution(n - 2.0).cumulativeProbability(t));
                    Assert.assertEquals(expectedPValue, correlationPValues.getEntry(i, j), 1e-9);
                }
            }
        }
    }
}