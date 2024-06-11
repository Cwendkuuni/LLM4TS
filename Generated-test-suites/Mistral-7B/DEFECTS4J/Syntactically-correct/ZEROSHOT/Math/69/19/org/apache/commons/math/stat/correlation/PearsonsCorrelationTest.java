package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.getnObs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangularArrayDataWithLessThanTwoColumnsOrRows() {
        double[][] data = new double[1][];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testRectangularArrayData() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}, {6.0, 7.0, 8.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8191518532991574, 0.6950231258876243},
                {0.8191518532991574, 1.0, 0.8191518532991574},
                {0.6950231258876243, 0.8191518532991574, 1.0}
        });
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.getnObs());
    }

    @Test
    public void testRealMatrixData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {3.0, 4.0, 5.0},
                {6.0, 7.0, 8.0}
        });
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8191518532991574, 0.6950231258876243},
                {0.8191518532991574, 1.0, 0.8191518532991574},
                {0.6950231258876243, 0.8191518532991574, 1.0}
        });
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.getnObs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRealMatrixDataWithLessThanTwoColumnsOrRows() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0}
        });
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCovariance() {
        double[][] covarianceData = new double[][]{
                {1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, 1.0}
        };
        Covariance covariance = new Covariance(covarianceData);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8, 0.8},
                {0.8, 1.0, 0.8},
                {0.8, 0.8, 1.0}
        });
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.getnObs());
    }

    @Test
    public void testCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, 1.0}
        });
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8, 0.8},
                {0.8, 1.0, 0.8},
                {0.8, 0.8, 1.0}
        });
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.getnObs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceMatrixWithLessThanTwoColumnsOrRows() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0}
        });
        new PearsonsCorrelation(covarianceMatrix, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullData() {
        new PearsonsCorrelation(null);
    }

    @Test
    public void testComplexData() {
        Complex[][] complexData = new Complex[][]{{Complex.ONE, Complex.ONE}, {Complex.ONE, Complex.TWO}};
        Complex[][] expectedCorrelationComplexData = new Complex[][]{{Complex.ONE,  Complex.ONE}, {Complex.ONE, Complex.ONE}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonCorrelation.computeCorrelationMatrix(new Array2DRowComplexMatrix(complexData));
        assertArrayEquals(expectedCorrelationComplexData, correlationMatrix.getData());
    }
}