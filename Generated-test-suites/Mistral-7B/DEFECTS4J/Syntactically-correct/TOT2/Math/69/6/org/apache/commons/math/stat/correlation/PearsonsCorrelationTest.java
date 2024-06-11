import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        try {
            new PearsonsCorrelation(matrix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyArray() {
        double[][] data = new double[0][];
        try {
            new PearsonsCorrelation(data);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.0, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.0, correlationMatrix.getEntry(1, 0), 0.001);
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();

        // Test diagonal elements
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(1.0, correlationMatrix.getEntry(2, 2), 0.001);

        // Test off-diagonal elements
        assertEquals(0.7558, correlationMatrix.getEntry(0, 1), 0.001); // Approx correlation between first and second variable
        assertEquals(0.7558, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.8224, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.8224, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.8118, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.8118, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.6961, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6961, correlationMatrix.getEntry(2, 1), 0.001);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();

        // Test diagonal elements
        assertEquals(0.3914, correlationStandardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.3914, correlationStandardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.3914, correlationStandardErrors.getEntry(2, 2), 0.001);

        // Test off-diagonal elements
        assertEquals(0.1652, correlationStandardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.1652, correlationStandardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.1628, correlationStandardErrors.getEntry(0, 2), 0.001);
        assertEquals(0.1628, correlationStandardErrors.getEntry(2, 0), 0.001);
        assertEquals(0.1616, correlationStandardErrors.getEntry(1, 2), 0.001);
        assertEquals(0.1616, correlationStandardErrors.getEntry(2, 1), 0.001);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        RealMatrix correlationPValues = correlation.getCorrelationPValues();

        // Test diagonal elements
        assertEquals(0.5, correlationPValues.getEntry(0, 0), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(1, 1), 0.001);
        assertEquals(0.5, correlationPValues.getEntry(2, 2), 0.001);

        // Test off-diagonal elements
        assertEquals(0.5961, correlationPValues.getEntry(0, 1), 0.001);
        assertEquals(0.5961, correlationPValues.getEntry(1, 0), 0.001);
        assertEquals(0.5551, correlationPValues.getEntry(0, 2), 0.001);
        assertEquals(0.5551, correlationPValues.getEntry(2, 0), 0.001);
        assertEquals(0.5373, correlationPValues.getEntry(1, 2), 0.001);
        assertEquals(0.5373, correlationPValues.getEntry(2, 1), 0.001);
    }
}