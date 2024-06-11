import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.getNObs());
    }

    @Test
    public void testTwoDArrayConstructor() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new RealArray2D[]{
                { 1, 0.8, 0.73205080756888 },
                { 0.8, 1, 0.8 },
                { 0.73205080756888, 0.8, 1 }
        });
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.getNObs());
    }

    @Test
    public void testRealMatrixConstructor() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new RealArray2D[]{
                { 1, 0.8, 0.73205080756888 },
                { 0.8, 1, 0.8 },
                { 0.73205080756888, 0.8, 1 }
        });
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.getNObs());
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new RealArray2D[]{
                { 1, 0.8, 0.73205080756888 },
                { 0.8, 1, 0.8 },
                { 0.73205080756888, 0.8, 1 }
        });
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.getNObs());
    }

    @Test
    public void testStandardErrors() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new RealArray2D[]{
                { 0.56652747120432, 0.47294316226364, 0.44316304578282 },
                { 0.47294316226364, 0.56652747120432, 0.44316304578282 },
                { 0.44316304578282, 0.44316304578282, 0.56652747120432 }
        });
        assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test
    public void testPValues() throws MathException {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new RealArray2D[]{
                { 0.99942536103031, 0.99931352253134, 0.99911314502656 },
                { 0.99931352253134, 0.99942536103031, 0.99911314502656 },
                { 0.99911314502656, 0.99911314502656, 0.99942536103031 }
        });
        assertEquals(expectedCorrelationPValues, correlationPValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArraySize() {
        double[][] data = { 1 };
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataMatrixSize() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{ { 1 } });
        new PearsonsCorrelation(matrix);
    }
}