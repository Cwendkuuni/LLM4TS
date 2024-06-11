package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testZeroColumnsMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[2][0]);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testZeroRowsMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][2]);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0, 5.0}
        });
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        });
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testCorrelationMatrixFromDataArrays() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {2.0, 3.0, 4.0, 5.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation = correlation.correlation(xArray, yArray);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.75},
            {0.75, 1.0}
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testCorrelationMatrixFromDataMatrix() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation = correlation.correlation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.75},
            {0.75, 1.0}
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testCorrelationMatrixFromCovariance() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.75},
            {0.75, 1.0}
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testCorrelationMatrixFromCovarianceMatrix() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        RealMatrix covarianceMatrix = StatUtils.covariance(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 4);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.75},
            {0.75, 1.0}
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNonNumericData() {
        double[] xArray = {1.0, 2.0, "three", 4.0};
        double[] yArray = {2.0, "four", 3.0, 5.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation = correlation.correlation(xArray, yArray);
    }

    @Test(expected = MathRuntimeException.class)
    public void testNullData() {
        double[] xArray = null;
        double[] yArray = null;
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation = correlation.correlation(xArray, yArray);
    }

    @Test(expected = MathRuntimeException.class)
    public void testComplexData() {
        double[] xArray = {1.0, new Complex(1.0, 1.0), 2.0};
        double[] yArray = {2.0, new Complex(1.0, 2.0), 3.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation = correlation.correlation(xArray, yArray);
    }
}