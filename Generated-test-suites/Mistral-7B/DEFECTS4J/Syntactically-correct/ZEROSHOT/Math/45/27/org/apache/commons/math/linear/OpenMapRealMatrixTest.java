import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructors() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        Assert.assertEquals(2, matrix1.getRowDimension());
        Assert.assertEquals(3, matrix1.getColumnDimension());
        Assert.assertEquals(2, matrix2.getRowDimension());
        Assert.assertEquals(3, matrix2.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = original.copy();
        Assert.assertArrayEquals(original.getData(), copy.getData(), 0.0);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix2.setEntry(0, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        OpenMapRealMatrix sum = matrix1.add(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 3);
        expected.setEntry(0, 0, 4.0);
        expected.setEntry(0, 1, 3.0);
        expected.setEntry(1, 1, 4.0);

        Assert.assertArrayEquals(expected.getData(), sum.getData(), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAddDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.add(matrix2);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 1.0);
        matrix2.setEntry(1, 1, 2.0);

        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 3);
        expected.setEntry(0, 0, 0.0);
        expected.setEntry(0, 1, 1.0);
        expected.setEntry(1, 1, 1.0);

        Assert.assertArrayEquals(expected.getData(), difference.getData(), 0.0);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 1, 3.0);
        matrix2.setEntry(0, 0, 4.0);
        matrix2.setEntry(0, 1, 5.0);
        matrix2.setEntry(1, 1, 6.0);

        RealMatrix product = matrix1.multiply(matrix2);
        BlockRealMatrix expected = new BlockRealMatrix(2, 2);
        expected.setEntry(0, 0, 4.0 * 1.0 + 5.0 * 2.0);
        expected.setEntry(0, 1, 4.0 * 2.0 + 5.0 * 3.0);
        expected.setEntry(1, 0, 3.0 * 4.0);
        expected.setEntry(1, 1, 3.0 * 5.0 + 6.0 * 3.0);

        Assert.assertEquals(expected, product);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplyDimensionMismatchException() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 4);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testGetEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testSetEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testAddToEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testMultiplyEntryIndexOutOfBoundsException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }
}