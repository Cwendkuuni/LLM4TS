import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isEmpty());
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(1, 1).identity()));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(1, 1).identity()));
        OpenMapRealMatrix result = matrix1.add(matrix2);

        RealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(1, 1).identity()).subtract(new OpenMapRealMatrix(1, 1).identity()));
        assertEquals(expected, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2).identity();
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3).eye();
        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        RealMatrix expected = new OpenMapRealMatrix(2, 3).multiply(matrix1);
        assertEquals(expected, result);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
    }

    @Test
    public void testEntryAccess() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
    }

    @Test
    public void testEntryModification() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 3.0);
        assertEquals(4.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test
    public void testExceptionWhenEntryValueTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        try {
            matrix.setEntry(0, 0, Double.MAX_VALUE + 1.0);
            fail();
        } catch (NumberIsTooLargeException expected) {
        }
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix copy = matrix.copy();
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        assertEquals(expected, copy);
    }
}