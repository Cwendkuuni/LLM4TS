import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertNull(pearson.getCorrelationMatrix());
        assertEquals(0, pearson.getnObs());
    }

    @Test
    public void testEmptyDoubleArrayConstructor() {
        double[][] data = {};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        assertNull(pearson.getCorrelationMatrix());
        assertEquals(0, pearson.getnObs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangularArrayInvalidDimensionConstructor() {
        double[][] data = {{1, 2}, {3, 4, 5}};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testRectangularArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8660254, 0.8191525},
                {0.8660254, 1.0, 0.8660254},
                {0.8191525, 0.8660254, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearson.getnObs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRealMatrixInvalidDimensionConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 5}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8660254, 0.8191525},
                {0.8660254, 1.0, 0.8660254},
                {0.8191525, 0.8660254, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearson.getnObs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceNullCovarianceMatrixConstructor() {
        new PearsonsCorrelation(null);
    }

    @Test
    public void testCovarianceCovarianceMatrixConstructor() throws MathException {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 1.0},
                {2.0, 3.0, 2.0},
                {1.0, 2.0, 1.0}});
        PearsonsCorrelation pearson = new PearsonsCorrelation(covarianceMatrix, covarianceMatrix.getRowDimension());
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8888889, 0.8611148},
                {0.8888889, 1.0, 0.8888889},
                {0.8611148, 0.8888889, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearson.getnObs());
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearson.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8660254, 0.8191525},
                {0.8660254, 1.0, 0.8660254},
                {0.8191525, 0.8660254, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearson.getCorrelationStandardErrors();
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.7227939, 0.4774409, 0.4161468},
                {0.4774409, 0.7227939, 0.4774409},
                {0.4161468, 0.4774409, 0.7227939}});
        assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearson.getCorrelationPValues();
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{{0.10, 0.90, 0.95},
                {0.90, 0.10, 0.90},
                {0.95, 0.90, 0.10}});
        assertEquals(expectedCorrelationPValues, correlationPValues);
    }
}