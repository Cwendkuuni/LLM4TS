import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        Assert.assertEquals(0, matrix.getEntry(0, 0));
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copiedMatrix = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        Assert.assertNotSame(originalMatrix.entries, copiedMatrix.entries);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix sum = matrix1.add(matrix2);
        assertEquals(2, sum.getRowDimension());
        assertEquals(2, sum.getColumnDimension());
        Assert.assertEquals(4.0, sum.getEntry(0, 0), 0.001);
        Assert.assertEquals(6.0, sum.getEntry(0, 1), 0.001);
        Assert.assertEquals(3.0, sum.getEntry(1, 0), 0.001);
        Assert.assertEquals(4.0, sum.getEntry(1, 1), 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        assertEquals(2, difference.getRowDimension());
        assertEquals(2, difference.getColumnDimension());
        Assert.assertEquals(-2.0, difference.getEntry(0, 0), 0.001);
        Assert.assertEquals(-1.0, difference.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.0, difference.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.0, difference.getEntry(1, 1), 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);
        matrix1.setEntry(2, 0, 5.0);
        matrix1.setEntry(2, 1, 6.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix2.setEntry(0, 0, 7.0);
        matrix2.setEntry(0, 1, 8.0);
        matrix2.setEntry(0, 2, 9.0);
        matrix2.setEntry(1, 0, 10.0);
        matrix2.setEntry(1, 1, 11.0);
        matrix2.setEntry(1, 2, 12.0);

        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        assertEquals(3, product.getRowDimension());
        assertEquals(3, product.getColumnDimension());
        Assert.assertEquals(55.0, product.getEntry(0, 0), 0.001);
        Assert.assertEquals(70.0, product.getEntry(0, 1), 0.001);
        Assert.assertEquals(85.0, product.getEntry(0, 2), 0.001);
        Assert.assertEquals(67.0, product.getEntry(1, 0), 0.001);
        Assert.assertEquals(88.0, product.getEntry(1, 1), 0.001);
        Assert.assertEquals(110.0, product.getEntry(1, 2), 0.001);
        Assert.assertEquals(54.0, product.getEntry(2, 0), 0.001);
        Assert.assertEquals(78.0, product.getEntry(2, 1), 0.001);
        Assert.assertEquals(102.0, product.getEntry(2, 2), 0.001);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 1);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(3.0, matrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.setEntry(0, 0, 5.0);
        Assert.assertEquals(5.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.addToEntry(0, 0, 3.0);
        Assert.assertEquals(4.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(3, 3, 2.0);
    }
}