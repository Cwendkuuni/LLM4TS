import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalid2DArrayData() {
        double[][] data = new double[1][1];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testValid2DArrayData() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        BlockRealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.81915254, 0.81915254},
                {0.81915254, 1.0, 0.81915254},
                {0.81915254, 0.81915254, 1.0}
        });

        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testValidRealMatrixData() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        BlockRealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.81915254, 0.81915254},
                {0.81915254, 1.0, 0.81915254},
                {0.81915254, 0.81915254, 1.0}
        });

        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCovarianceData() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN()).getCorrelationMatrix();
        BlockRealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.81915254, 0.81915254},
                {0.81915254, 1.0, 0.81915254},
                {0.81915254, 0.81915254, 1.0}
        });

        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testRealMatrixDataWithNaN() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, Double.NaN},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        try {
            new PearsonsCorrelation(matrix);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testValidCovarianceDataWithNaN() {
        double[][] data = {{1.0, 2.0, Double.NaN}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Covariance covariance = new Covariance(data);
        try {
            new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();

        BlockRealMatrix expectedCorrelationStandardErrors = new BlockRealMatrix(new double[][]{
                {0.47351158, 0.54029195, 0.54029195},
                {0.54029195, 0.47351158, 0.54029195},
                {0.54029195, 0.54029195, 0.47351158}
        });

        assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();

        BlockRealMatrix expectedCorrelationPValues = new BlockRealMatrix(new double[][]{
                {1.0, 0.01334918, 0.01334918},
                {0.01334918, 1.0, 0.01334918},
                {0.01334918, 0.01334918, 1.0}
        });

        assertEquals(expectedCorrelationPValues, correlationPValues);
    }
}