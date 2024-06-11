import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeDimension() {
        new OpenMapRealMatrix(-1, -1);
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testOpenMapRealMatrixEquals() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        assertTrue(matrix1.equals(matrix2));

        matrix2.setEntry(0, 0, 1.0);
        assertFalse(matrix1.equals(matrix2));
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(0.0, result.getEntry(0, 0), 1e-14);
        assertEquals(0.0, result.getEntry(1, 1), 1e-14);
        assertEquals(0.0, result.getEntry(2, 2), 1e-14);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        result = matrix1.add(matrix2);
        assertEquals(3.0, result.getEntry(0, 0), 1e-14);
        assertEquals(4.0, result.getEntry(1, 1), 1e-14);
        assertEquals(6.0, result.getEntry(2, 2), 1e-14);

        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(3, 3);
        matrix3.setEntry(1, 1, 1.0);
        Result = matrix1.add(matrix3);
        assertEquals(4.0, result.getEntry(0, 0), 1e-14);
        assertEquals(5.0, result.getEntry(1, 1), 1e-14);
        assertEquals(6.0, result.getEntry(2, 2), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixAdditionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(0.0, result.getEntry(0, 0), 1e-14);
        assertEquals(0.0, result.getEntry(1, 1), 1e-14);
        assertEquals(0.0, result.getEntry(2, 2), 1e-14);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(1, 1, 2.0);
        matrix2.setEntry(2, 2, 3.0);

        result = matrix1.subtract(matrix2);
        assertEquals(0.0, result.getEntry(0, 0), 1e-14);
        assertEquals(0.0, result.getEntry(1, 1), 1e-14);
        assertEquals(0.0, result.getEntry(2, 2), 1e-14);

        matrix2.setEntry(1, 1, -1.0);

        result = matrix1.subtract(matrix2);
        assertEquals(2.0, result.getEntry(0, 0), 1e-14);
        assertEquals(1.0, result.getEntry(1, 1), 1e-14);
        assertEquals(3.0, result.getEntry(2, 2), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixSubtractionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        RealMatrix result = matrix1.multiply(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double sum = 0.0;
                for (int k = 0; k < 3; k++) {
                    sum += matrix1.getEntry(i, k) * matrix2.getEntry(k, j);
                }
                assertEquals(sum, result.getEntry(i, j), 1e-14);
            }
        }

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix1.setEntry(2, 2, 3.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 2.0);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(2, 2, 4.0);

        result = matrix1.multiply(matrix2);
        assertEquals(1.0, result.getEntry(0, 0), 1e-14);
        assertEquals(6.0, result.getEntry(0, 1), 1e-14);
        assertEquals(7.0, result.getEntry(1, 0), 1e-14);
        assertEquals(13.0, result.getEntry(1, 1), 1e-14);
        assertEquals(11.0, result.getEntry(2, 0), 1e-14);
        assertEquals(12.0, result.getEntry(2, 1), 1e-14);
        assertEquals(12.0, result.getEntry(2, 2), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixMultiplicationWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrixMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test
    public void testOpenMapRealMatrixIsEmpty() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertTrue(matrix.isEmpty());

        matrix.setEntry(0, 0, 1.0);
        assertFalse(matrix.isEmpty());
    }

    @Test
    public void testOpenMapRealMatrixToString() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        assertEquals("OpenMapRealMatrix([[0.0, 0.0, 0.0],\n" +
                     "[0.0, 2.0, 0.0],\n" +
                     "[0.0, 0.0, 3.0]],\n" +
                     "rowSize=3, columnSize=3, isSymmetric=false)",
                     matrix.toString());
    }
}