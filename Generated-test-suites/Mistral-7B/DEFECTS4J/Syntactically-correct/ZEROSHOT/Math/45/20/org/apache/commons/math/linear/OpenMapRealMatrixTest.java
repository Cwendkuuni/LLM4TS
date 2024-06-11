import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    private static final double EPSILON = 1e-14;

    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructionWithNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertEquals(original.entries, copy.entries);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 1, 4.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(result.getRowDimension(), 3);
        assertEquals(result.getColumnDimension(), 3);
        assertEquals(result.getEntry(0, 0), 4.0, EPSILON);
        assertEquals(result.getEntry(0, 1), 6.0, EPSILSON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithIncompatibleMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(0, 1, 4.0);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(result.getRowDimension(), 3);
        assertEquals(result.getColumnDimension(), 3);
        assertEquals(result.getEntry(0, 0), -2.0, EPSILON);
        assertEquals(result.getEntry(0, 1), -2.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionWithIncompatibleMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(0, 1, 5.0);
        matrix2.setEntry(0, 2, 6.0);

        RealMatrix result = matrix1.multiply(matrix2);
        assertEquals(result.getRowDimension(), 3);
        assertEquals(result.getColumnDimension(), 3);
        assertEquals(result.getEntry(0, 0), 16.0, EPSILON);
        assertEquals(result.getEntry(0, 1), 20.0, EPSILON);
        assertEquals(result.getEntry(0, 2), 24.0, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationWithIncompatibleMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), EPSILON);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), EPSILON);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), EPSILON);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), EPSILON);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test
    public void testEquals() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix3.setEntry(0, 0, 3.0);
        matrix3.setEntry(0, 1, 2.0);

        assertTrue(matrix1.equals(matrix2));
        assertFalse(matrix1.equals(matrix3));
    }

    @Test
    public void testHashCode() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix3.setEntry(0, 0, 3.0);
        matrix3.setEntry(0, 1, 2.0);

        int hashCode1 = matrix1.hashCode();
        int hashCode2 = matrix2.hashCode();
        int hashCode3 = matrix3.hashCode();

        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode2, hashCode3);
    }

    @Test
    public void testToString() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 1, 3.0);

        String expected = "OpenMapRealMatrix(3, 3)\n" +
                "  [, ]\n" +
                "  [1.0, 2.0]\n" +
                "  [, 3.0]\n";

        assertEquals(expected, matrix.toString());
    }
}