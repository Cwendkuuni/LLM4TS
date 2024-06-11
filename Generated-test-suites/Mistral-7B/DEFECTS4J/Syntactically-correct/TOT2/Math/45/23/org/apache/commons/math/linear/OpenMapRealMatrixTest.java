import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.entries.isEmpty());
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copyMatrix = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix, copyMatrix);
    }

    @Test
    public void testAddMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(1, 2, 4.0);
        matrix2.setEntry(2, 2, 5.0);

        OpenMapRealMatrix sumMatrix = matrix1.add(matrix2);

        assertEquals(matrix1.getRowDimension(), sumMatrix.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), sumMatrix.getColumnDimension());
        Assert.assertArrayEquals(new double[]{1.0, 3.0, 3.0, 2.0, 5.0, 5.0}, sumMatrix.getEntryArray(), 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtractMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(1, 1, 3.0);
        matrix2.setEntry(1, 2, 4.0);
        matrix2.setEntry(2, 2, 5.0);

        OpenMapRealMatrix differenceMatrix = matrix1.subtract(matrix2);

        assertEquals(matrix1.getRowDimension(), differenceMatrix.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), differenceMatrix.getColumnDimension());
        Assert.assertArrayEquals(new double[]{-2.0, 1.0, -2.0, -1.0, -1.0, -1.0}, differenceMatrix.getEntryArray(), 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractMatrixDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplyMethod() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(0, 1, 5.0);
        matrix2.setEntry(0, 2, 6.0);

        OpenMapRealMatrix productMatrix = matrix1.multiply(matrix2);

        assertEquals(matrix1.getRowDimension(), productMatrix.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), productMatrix.getColumnDimension());
        Assert.assertArrayEquals(new double[]{4.0, 10.0, 15.0, 13.0, 22.0, 33.0}, productMatrix.getEntryArray(), 0.001);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyMatrixDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        assertEquals(2.0, matrix.getEntry(1, 1), 0.001);
        assertEquals(3.0, matrix.getEntry(2, 2), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        matrix.setEntry(2, 0, 4.0);
        assertEquals(4.0, matrix.getEntry(2, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testMultiplyEntryMethod() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);

        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test
    public void testNumberIsTooLargeException() {
        try {
            OpenMapRealMatrix matrix = new OpenMapRealMatrix(Integer.MAX_VALUE, Integer.MAX_VALUE);
        } catch (NumberIsTooLargeException e) {
            // Expected exception
        }
    }
}