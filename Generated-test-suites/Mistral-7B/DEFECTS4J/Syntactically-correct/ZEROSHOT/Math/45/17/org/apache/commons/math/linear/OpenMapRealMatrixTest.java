import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;

public class OpenMapRealMatrixTest {

    @org.junit.Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertEquals(0, matrix.getEntry(0, 0));
    }

    @org.junit.Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        Assert.assertNotSame(original, copy);
        Assert.assertEquals(original.getRowDimension(), copy.getRowDimension());
        Assert.assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        for (int i = 0; i < original.getRowDimension(); i++) {
            for (int j = 0; j < original.getColumnDimension(); j++) {
                double valueOriginal = original.getEntry(i, j);
                double valueCopy = copy.getEntry(i, j);
                Assert.assertEquals(valueOriginal, valueCopy, 0.0);
            }
        }
    }

    @org.junit.Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.5);
        matrix2.setEntry(0, 1, 2.5);
        matrix2.setEntry(1, 0, 3.5);
        matrix2.setEntry(1, 1, 4.5);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        Assert.assertEquals(2, result.getRowDimension());
        Assert.assertEquals(3, result.getColumnDimension());

        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                double expected = matrix1.getEntry(i, j) + matrix2.getEntry(i, j);
                double actual = result.getEntry(i, j);
                Assert.assertEquals(expected, actual, 0.0);
            }
        }
    }

    @org.junit.Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        matrix2.setEntry(0, 0, 1.5);
        matrix2.setEntry(0, 1, 2.5);
        matrix2.setEntry(1, 0, 3.5);
        matrix2.setEntry(1, 1, 4.5);

        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        Assert.assertEquals(2, result.getRowDimension());
        Assert.assertEquals(3, result.getColumnDimension());

        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                double expected = matrix1.getEntry(i, j) - matrix2.getEntry(i, j);
                double actual = result.getEntry(i, j);
                Assert.assertEquals(expected, actual, 0.0);
            }
        }
    }

    @org.junit.Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);

        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(0, 2, 3.0);
        matrix1.setEntry(1, 0, 4.0);
        matrix1.setEntry(1, 1, 5.0);
        matrix1.setEntry(1, 2, 6.0);

        matrix2.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 1, 2.0);
        matrix2.setEntry(1, 0, 3.0);
        matrix2.setEntry(1, 1, 4.0);

        RealMatrix result = matrix1.multiply(matrix2);

        Assert.assertEquals(2, result.getRowDimension());
        Assert.assertEquals(2, result.getColumnDimension());

        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                double expected = 0.0;
                for (int k = 0; k < matrix1.getColumnDimension(); k++) {
                    expected += matrix1.getEntry(i, k) * matrix2.getEntry(k, j);
                }
                double actual = result.getEntry(i, j);
                Assert.assertEquals(expected, actual, 1e-12);
            }
        }
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetEntryWithNegativeValues() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, -1.0);
    }

    @org.junit.Test
    public void testGetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        try {
            matrix.getEntry(3, 3);
            Assert.fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        try {
            matrix.setEntry(3, 3, 0.0);
            Assert.fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSetEntryWithLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        try {
            matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
            Assert.fail();
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }
}