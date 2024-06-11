import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isSparse());
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).identity()));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());

        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                double expected = matrix1.getEntry(i, j) + matrix2.getEntry(i, j);
                assertEquals(expected, result.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionDimensionsMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2);
        result = matrix1.add(result);
        matrix1.add(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2).identity();
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3).identity();
        RealMatrix result = matrix1.multiply(matrix2);
        OpenMapRealMatrix openResult = new OpenMapRealMatrix(result);

        assertEquals(matrix1.getRowDimension(), openResult.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), openResult.getColumnDimension());

        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix2.getColumnDimension(); j++) {
                double expected = 0.0;
                for (int k = 0; k < matrix2.getRowDimension(); k++) {
                    expected += matrix1.getEntry(i, k) * matrix2.getEntry(k, j);
                }
                assertEquals(expected, openResult.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test
    public void testMultiplicationWithIdentity() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2).identity();
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2).identity();
        RealMatrix result = matrix1.multiply(matrix2);
        OpenMapRealMatrix openResult = new OpenMapRealMatrix(result);

        assertEquals(matrix1.getRowDimension(), openResult.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), openResult.getColumnDimension());

        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                assertEquals(1.0, openResult.getEntry(i, j), 1e-10);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationDimensionsMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2);
        result = matrix1.multiply(result);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 3.0);
        matrix.setEntry(1, 1, 5.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-10);
        assertEquals(5.0, matrix.getEntry(1, 1), 1e-10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(3, 3, 5.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, 3.0);
        matrix.addToEntry(1, 1, 5.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-10);
        assertEquals(5.0, matrix.getEntry(1, 1), 1e-10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(3, 3, 5.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, 3.0);
        matrix.multiplyEntry(1, 1, 5.0);
        assertEquals(3.0 * 3.0, matrix.getEntry(0, 0), 1e-10);
        assertEquals(5.0 * 5.0, matrix.getEntry(1, 1), 1e-10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(3, 3, 5.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }
}