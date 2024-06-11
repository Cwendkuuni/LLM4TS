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
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix instanceof AbstractRealMatrix);
        assertTrue(matrix instanceof SparseRealMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroDimensions() {
        new OpenMapRealMatrix(0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSquareMatrix() {
        new OpenMapRealMatrix(2, 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, -1.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(5.0, result.getEntry(0, 0), 1e-14);
        assertEquals(0.0, result.getEntry(1, 1), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 4);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, -1.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(1.0, result.getEntry(0, 0), 1e-14);
        assertEquals(0.0, result.getEntry(1, 1), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 4);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 2.0);
        matrix1.setEntry(0, 1, 3.0);
        matrix1.setEntry(0, 2, 1.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 2, 3.0);

        RealMatrix result = matrix1.multiply(matrix2);
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(7.0, result.getEntry(0, 0), 1e-14);
        assertEquals(6.0, result.getEntry(0, 1), 1e-14);
        assertEquals(3.0, result.getEntry(0, 2), 1e-14);
        assertEquals(6.0, result.getEntry(1, 0), 1e-14);
        assertEquals(7.0, result.getEntry(1, 1), 1e-14);
        assertEquals(9.0, result.getEntry(1, 2), 1e-14);
        assertEquals(3.0, result.getEntry(2, 0), 1e-14);
        assertEquals(3.0, result.getEntry(2, 1), 1e-14);
        assertEquals(3.0, result.getEntry(2, 2), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 4);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);

        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(3, 0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);

        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 0, 2.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);

        matrix.addToEntry(0, 0, 3.0);
        assertEquals(5.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 0, 3.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 2.0);

        matrix.multiplyEntry(0, 0, 3.0);
        assertEquals(6.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 0, 3.0);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        int key = matrix.computeKey(0, 0);

        assertEquals(0, key / 3);
        assertEquals(0, key % 3);
    }
}