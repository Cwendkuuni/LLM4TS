import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
        assertNull(matrix.getEntry(1, 2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixCreationWithNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix second = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = first.add(second);
        assertEquals(first, result);
        first.setEntry(0, 0, 1.0);
        first.setEntry(1, 1, 2.0);
        second.setEntry(1, 1, 3.0);
        result = first.add(second);
        assertEquals(3.0, result.getEntry(0, 0), 0.001);
        assertEquals(5.0, result.getEntry(1, 1), 0.001);
        assertEquals(0.0, result.getEntry(0, 1), 0.001);
        assertEquals(0.0, result.getEntry(1, 0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixAdditionWithIncompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(2, 2);
        RealMatrix second = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix result = first.add(second);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix second = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = first.subtract(second);
        assertEquals(first, result);
        first.setEntry(0, 0, 1.0);
        first.setEntry(1, 1, 2.0);
        second.setEntry(1, 1, 3.0);
        result = first.subtract(second);
        assertEquals(0.0, result.getEntry(0, 0), 0.001);
        assertEquals(-1.0, result.getEntry(1, 1), 0.001);
        assertEquals(0.0, result.getEntry(0, 1), 0.001);
        assertEquals(0.0, result.getEntry(1, 0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixSubtractionWithIncompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(2, 2);
        RealMatrix second = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix result = first.subtract(second);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix second = new OpenMapRealMatrix(3, 2);
        RealMatrix result = first.multiply(second);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double sum = 0.0;
                for (int k = 0; k < 3; k++) {
                    sum += first.getEntry(i, k) * second.getEntry(k, j);
                }
                assertEquals(sum, result.getEntry(i, j), 0.001);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixMultiplicationWithIncompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(2, 3);
        RealMatrix second = new BlockRealMatrix(new double[][]{{1.0,
                2.0, 3.0}, {4.0, 5.0, 6.0}});
        OpenMapRealMatrix result = (OpenMapRealMatrix) first.multiply(second);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(3, 0, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(3, 0, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(3, 0, 2.0);
    }
}