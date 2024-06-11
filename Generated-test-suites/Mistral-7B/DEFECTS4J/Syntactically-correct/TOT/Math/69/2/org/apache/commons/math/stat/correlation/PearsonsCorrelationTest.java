package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation()}.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        Assert.assertNull(pearon.correlationMatrix);
        Assert.assertEquals(0, pearson.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(double[][])}.
     */
    @Test
    public void testDoubleArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 3.0}, {1.0, 2.0, 3.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.8571428571}, {1.0, 1.0, 0.8571428571}, {0.8571428571, 0.8571428571, 1.0}});
        RealMatrix actual = pearson.correlationMatrix;
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(3, pearson.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix)}.
     */
    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        RealMatrix actual = pearson.correlationMatrix;
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(2, pearson.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationMatrix()}.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 3.0}, {1.0, 2.0, 3.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.8571428571}, {1.0, 1.0, 0.8571428571}, {0.8571428571, 0.8571428571, 1.0}});
        RealMatrix actual = pearson.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}.
     */
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 3.0}, {1.0, 2.0, 3.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.4592956921, 0.4592956921, 0.349723329}, {0.4592956921, 0.4592956921, 0.349723329}, {0.349723329, 0.349723329, 0.4592956921}});
        RealMatrix actual = pearson.getCorrelationStandardErrors();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 3.0}, {1.0, 2.0, 3.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.0, 0.0, 0.876032911}, {0.0, 0.0, 0.876032911}, {0.876032911, 0.876032911, 0.0}});
        RealMatrix actual = pearson.getCorrelationPValues();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PearsonsCorrelation#computeCorrelationMatrix(RealMatrix)}.
     */
    @Test
    public void testComputeCorrelationMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        RealMatrix actual = PearsonsCorrelation.computeCorrelationMatrix(matrix);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PearsonsCorrelation#computeCorrelationMatrix(double[][])}.
     */
    @Test
    public void testComputeCorrelationMatrix_doubleArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 3.0}, {1.0, 2.0, 3.0}};
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.8571428571}, {1.0, 1.0, 0.8571428571}, {0.8571428571, 0.8571428571, 1.0}});
        RealMatrix actual = PearsonsCorrelation.computeCorrelationMatrix(data);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}.
     */
    @Test
    public void testCorrelation_doubleArray() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 3.0};
        double expected = 0.8571428571;
        double actual = PearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(expected, actual, 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#covarianceToCorrelation(RealMatrix)}.
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 4.0}});
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.6}, {0.6, 1.0}});
        RealMatrix actual = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test method for {@link PearsonsCorrelation#checkSufficientData(RealMatrix)}.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientData_emptyMatrix() {
        RealMatrix emptyMatrix = new Array2DRowRealMatrix(new double[0][0]);
        PearsonsCorrelation.checkSufficientData(emptyMatrix);
    }

    /**
     * Test method for {@link PearsonsCorrelation#checkSufficientData(RealMatrix)}.
     */
    @Test
    public void testCheckSufficientData_validMatrix() {
        RealMatrix validMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation.checkSufficientData(validMatrix);
    }
}