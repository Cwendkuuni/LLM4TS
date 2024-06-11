import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = originalMatrix.copy();
        Assert.assertNotSame(originalMatrix, copiedMatrix);
        Assert.assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        Assert.assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        for (int i = 0; i < originalMatrix.getRowDimension(); i++) {
            for (int j = 0; j < originalMatrix.getColumnDimension(); j++) {
                double originalValue = originalMatrix.getEntry(i, j);
                double copiedValue = copiedMatrix.getEntry(i, j);
                Assert.assertEquals(originalValue, copiedValue, 0.0);
            }
        }
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                double value1 = matrix1.getEntry(i, j);
                double value2 = matrix2.getEntry(i, j);
                double sumValue = sum.getEntry(i, j);
                Assert.assertEquals(value1 + value2, sumValue, 0.0);
            }
        }
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                double value1 = matrix1.getEntry(i, j);
                double value2 = matrix2.getEntry(i, j);
                double differenceValue = difference.getEntry(i, j);
                Assert.assertEquals(value1 - value2, differenceValue, 0.0);
            }
        }
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 1);
        matrix2.setEntry(0, 0, 4.0);
        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        OpenMapRealMatrix expectedProduct = new OpenMapRealMatrix(2, 1);
        expectedProduct.setEntry(0, 0, 4.0 * 1.0);
        expectedProduct.setEntry(1, 0, 4.0 * 2.0);
        Assert.assertEquals(expectedProduct, product);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(1.0, value, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(3, 3);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(1.0, value, 0.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(1.0, value, 0.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(2.0, value, 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testEquals() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix3 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix4 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 0, 1.0);
        matrix3.setEntry(0, 0, 2.0);
        Assert.assertFalse(matrix1.equals(matrix2));
        Assert.assertFalse(matrix1.equals(matrix3));
        Assert.assertFalse(matrix1.equals(matrix4));
        OpenMapRealMatrix matrix5 = new OpenMapRealMatrix(matrix1);
        Assert.assertEquals(matrix1, matrix5);
    }
}