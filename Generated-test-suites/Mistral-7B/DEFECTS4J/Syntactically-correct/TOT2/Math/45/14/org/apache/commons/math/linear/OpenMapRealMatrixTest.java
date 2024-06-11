import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX).add(RealMatrix.IDENTITY_MATRIX));
        assertEquals(expected, sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix sum = matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(RealMatrix.ZERO);
        assertEquals(expected, difference);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        RealMatrix product = matrix1.multiply(matrix2);
        RealMatrix expected = RealMatrixUtils.multiply(RealMatrix.IDENTITY_MATRIX, RealMatrix.IDENTITY_MATRIX);
        assertEquals(expected, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2).add(RealMatrix.IDENTITY_MATRIX));
        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(1.0, matrix.getEntry(1, 1), 1e-14);
        assertEquals(0.0, matrix.getEntry(1, 0), 1e-14);
        assertEquals(0.0, matrix.getEntry(0, 1), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        matrix.setEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        matrix.setEntry(3, 3, 2.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        matrix.addToEntry(3, 3, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrix.IDENTITY_MATRIX));
        matrix.multiplyEntry(3, 3, 2.0);
    }
}