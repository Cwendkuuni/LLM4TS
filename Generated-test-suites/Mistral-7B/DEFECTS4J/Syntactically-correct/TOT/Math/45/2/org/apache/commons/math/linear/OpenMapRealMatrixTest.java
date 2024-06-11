import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {
    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isSparse());
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrixUtils.identity(2)));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(RealMatrixUtils.identity(2)));
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        RealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrixUtils.identity(2))).add(new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(RealMatrixUtils.identity(2))));
        assertEquals(expected, sum);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrixUtils.identity(2)));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(RealMatrixUtils.identity(2)));
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        RealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(RealMatrixUtils.identity(2))).subtract(new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(RealMatrixUtils.identity(2))));
        assertEquals(expected, difference);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2).multiply(new OpenMapRealMatrix(2, 3).identity()));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).multiply(new OpenMapRealMatrix(3, 2).identity()));
        RealMatrix product = matrix1.multiply(matrix2);
        RealMatrix expected = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).multiply(new OpenMapRealMatrix(2, 3).identity())).multiply(new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2).multiply(new OpenMapRealMatrix(3, 3).identity())));
        assertEquals(expected, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
    }

    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplicationWithDifferentSizes() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    @Test
    public void testSettingValues() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testGettingValues() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(0.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(0.0, matrix.getEntry(1, 0), 1e-12);
        assertEquals(1.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testSettingValuesOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(3, 3, 1.0);
        assertEquals(0.0, matrix.getEntry(3, 3), 1e-12);
    }

    @Test
    public void testAddingValuesOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(3, 3, 1.0);
        assertEquals(0.0, matrix.getEntry(3, 3), 1e-12);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddingValuesOutOfBoundsWithNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(-1, -1, 1.0);
    }

    @Test
    public void testMultiplyingValuesOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(-1, -1, 1.0);
        assertEquals(0.0, matrix.getEntry(-1, -1), 1e-12);
    }

    @Test
    public void testGettingValuesOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        assertEquals(0.0, matrix.getEntry(2, 2), 1e-12);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGettingValuesWithNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        matrix.getEntry(-1, -1);
    }

    @Test
    public void testSettingDiagonalValues() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-12);
        assertEquals(0.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(0.0, matrix.getEntry(1, 0), 1e-12);
    }

    @Test
    public void testAddingDiagonalValues() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-12);
        assertEquals(0.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(0.0, matrix.getEntry(1, 0), 1e-12);
    }

    @Test
    public void testMultiplyingDiagonalValues() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, 1.0);
        matrix.multiplyEntry(1, 1, 2.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-12);
        assertEquals(0.0, matrix.getEntry(0, 1), 1e-12);
        assertEquals(0.0, matrix.getEntry(1, 0), 1e-12);
    }

    @Test
    public void testSettingValuesWithZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 0.0);
        matrix.setEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(0.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testSettingValuesWithNaN() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, Double.NaN);
        matrix.setEntry(1, 1, Double.NaN);
        assertEquals(Double.NaN, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.NaN, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testSettingValuesWithInfiniteNumber() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
        matrix.setEntry(1, 1, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.POSITIVE_INFINITY, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testSettingValuesWithNegativeInfiniteNumber() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, Double.NEGATIVE_INFINITY);
        matrix.setEntry(1, 1, Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.NEGATIVE_INFINITY, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testAddingValuesWithZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, 0.0);
        matrix.addToEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(0.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testAddingValuesWithNaN() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, Double.NaN);
        matrix.addToEntry(1, 1, Double.NaN);
        assertEquals(Double.NaN, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.NaN, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testAddingValuesWithInfiniteNumber() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, Double.POSITIVE_INFINITY);
        matrix.addToEntry(1, 1, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.POSITIVE_INFINITY, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testAddingValuesWithNegativeInfiniteNumber() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.addToEntry(0, 0, Double.NEGATIVE_INFINITY);
        matrix.addToEntry(1, 1, Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.NEGATIVE_INFINITY, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testMultiplyingValuesWithZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, 0.0);
        matrix.multiplyEntry(1, 1, 0.0);
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-12);
        assertEquals(0.0, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testMultiplyingValuesWithNaN() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, Double.NaN);
        matrix.multiplyEntry(1, 1, Double.NaN);
        assertEquals(Double.NaN, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.NaN, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testMultiplyingValuesWithInfiniteNumber() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, Double.POSITIVE_INFINITY);
        matrix.multiplyEntry(1, 1, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.POSITIVE_INFINITY, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testMultiplyingValuesWithNegativeInfiniteNumber() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.multiplyEntry(0, 0, Double.NEGATIVE_INFINITY);
        matrix.multiplyEntry(1, 1, Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, matrix.getEntry(0, 0), 1e-12);
        assertEquals(Double.NEGATIVE_INFINITY, matrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testSettingValuesWithNumberTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        try {
            matrix.setEntry(0, 0, Long.MAX_VALUE);
        } catch (NumberIsTooLargeException e) {
            assertTrue(true);
        }
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-12);
    }
}