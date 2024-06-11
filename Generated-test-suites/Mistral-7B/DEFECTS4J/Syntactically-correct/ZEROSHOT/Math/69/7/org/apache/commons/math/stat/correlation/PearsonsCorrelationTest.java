import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataNull() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataEmpty() {
        double[][] emptyData = {};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(emptyData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataOneVariable() {
        double[][] oneVariableData = {{1.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(oneVariableData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataNotSquare() {
        double[][] notSquareData = {{1.0}, {2.0, 3.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(notSquareData);
    }

    @Test
    public void testDataTwoVariables() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.8, 0.8, 0.8, 0.8},
                {0.8, 1.0, 0.8, 0.8, 0.8},
                {0.8, 0.8, 1.0, 0.8, 0.8},
                {0.8, 0.8, 0.8, 1.0, 0.8},
                {0.8, 0.8, 0.8, 0.8, 1.0}
        });
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertTrue(correlationMatrix.equals(expectedCorrelationMatrix));
        assertEquals(5, pearsonsCorrelation.nObs);
    }

    @Test
    public void testDataTwoVariablesEqualColumns() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0}
        });
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertTrue(correlationMatrix.equals(expectedCorrelationMatrix));
        assertEquals(5, pearsonsCorrelation.nObs);
    }

    @Test
    public void testDataTwoVariablesEqualRows() {
        double[][] data = {{1.0, 2.0}, {1.0, 2.0}, {1.0, 2.0}, {1.0, 2.0}, {1.0, 2.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        });
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertTrue(correlationMatrix.equals(expectedCorrelationMatrix));
        assertEquals(5, pearsonsCorrelation.nObs);
    }

    @Test
    public void testDataTwoVariablesDifferentColumns() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.433013},
                {0.433013, 1.0}
        });
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertTrue(correlationMatrix.equals(expectedCorrelationMatrix));
        assertEquals(5, pearsonsCorrelation.nObs);
    }

    @Test
    public void testDataTwoVariablesDifferentRows() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0}, {11.0, 12.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.431536},
                {0.431536, 1.0}
        });
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertTrue(correlationMatrix.equals(expectedCorrelationMatrix));
        assertEquals(6, pearsonsCorrelation.nObs);
    }

    @Test
    public void testDataThreeVariables() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.666667, -0.333333},
                {0.666667, 1.0, 0.333333},
                {-0.333333, 0.333333, 1.0}
        });
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertTrue(correlationMatrix.equals(expectedCorrelationMatrix));
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCovarianceNull() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(null);
    }

    @Test
    public void testCovariance() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.433013},
                {0.433013, 1.0}
        });
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertTrue(correlationMatrix.equals(expectedCorrelationMatrix));
        assertEquals(5, pearsonsCorrelation.nObs);
    }

    @Test
    public void testCovarianceMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 5);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.433013},
                {0.433013, 1.0}
        });
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertTrue(correlationMatrix.equals(expectedCorrelationMatrix));
        assertEquals(5, pearsonsCorrelation.nObs);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationStandardErrors = new BlockRealMatrix(new double[][]{
                {0.447214, 0.447214},
                {0.447214, 0.447214}
        });
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertTrue(correlationStandardErrors.equals(expectedCorrelationStandardErrors));
        assertEquals(5, pearsonsCorrelation.nObs);
        assertEquals(2, correlationStandardErrors.getRowDimension());
        assertEquals(2, correlationStandardErrors.getColumnDimension());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationPValues = new BlockRealMatrix(new double[][]{
                {0.3658, 0.3658},
                {0.3658, 0.3658}
        });
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertTrue(correlationPValues.equals(expectedCorrelationPValues));
        assertEquals(5, pearsonsCorrelation.nObs);
        assertEquals(2, correlationPValues.getRowDimension());
        assertEquals(2, correlationPValues.getColumnDimension());
    }
}