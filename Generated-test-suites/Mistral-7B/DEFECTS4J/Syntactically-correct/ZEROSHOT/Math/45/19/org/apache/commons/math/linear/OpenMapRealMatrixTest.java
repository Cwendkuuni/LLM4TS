import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(3.0, result.getEntry(0, 0), 0.01);
        assertEquals(3.0, result.getEntry(1, 1), 0.01);
        assertEquals(1.0, result.getEntry(0, 1), 0.01);
        assertEquals(2.0, result.getEntry(1, 0), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(1, 1, 3.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(2, result.getRowDimension());
        assertEquals(2, result.getColumnDimension());
        assertEquals(-1.0, result.getEntry(0, 0), 0.01);
        assertEquals(-1.0, result.getEntry(1, 1), 0.01);
        assertEquals(1.0, result.getEntry(0, 1), 0.01);
        assertEquals(0.0, result.getEntry(1, 0), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(1, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 1, 4.0);
        matrix2.setEntry(0, 2, 5.0);

        RealMatrix result = matrix1.multiply(matrix2);
        BlockRealMatrix expectedResult = new BlockRealMatrix(2, 3);
        expectedResult.setEntry(0, 0, 3.0 * 1.0);
        expectedResult.setEntry(0, 1, 3.0 * 2.0);
        expectedResult.setEntry(0, 2, 3.0 * 5.0);
        expectedResult.setEntry(1, 0, 2.0 * 3.0);
        expectedResult.setEntry(1, 1, 2.0 * 4.0);
        expectedResult.setEntry(1, 2, 2.0 * 5.0);

        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.01);
        assertNull(matrix.getEntry(0, 1));
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.01);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.01);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.01);
    }

    @Test
    public void testComputeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        int key = matrix.computeKey(1, 1);
        int row = key / 3;
        int column = key % 3;
        assertEquals(1, row);
        assertEquals(1, column);
    }
}