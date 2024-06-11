import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    /**
     * Test constructor with valid dimensions.
     */
    @Test
    public void testConstructorValidDimensions() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        Assert.assertEquals(original, copy);
    }

    /**
     * Test add method with two empty matrices.
     */
    @Test
    public void testAddEmptyMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        Assert.assertEquals(sum.getEntry(0, 0), 0.0);
    }

    /**
     * Test add method with two different matrices.
     */
    @Test
    public void testAddDifferentMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][]{{6.0, 8.0}, {10.0, 12.0}});
        Assert.assertEquals(expected, sum);
    }

    /**
     * Test add method with the same matrix.
     */
    @Test
    public void testAddSameMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix sum = matrix.add(matrix);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][]{{2.0, 4.0}, {6.0, 8.0}});
        Assert.assertEquals(expected, sum);
    }

    /**
     * Test subtract method with two empty matrices.
     */
    @Test
    public void testSubtractEmptyMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(1, 1);
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        Assert.assertEquals(difference.getEntry(0, 0), 0.0);
    }

    /**
     * Test subtract method with two different matrices.
     */
    @Test
    public void testSubtractDifferentMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}));
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][]{{-4.0, -4.0}, {-4.0, -4.0}});
        Assert.assertEquals(expected, difference);
    }

    /**
     * Test subtract method with the same matrix.
     */
    @Test(expected = AssertionError.class)
    public void testSubtractSameMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix difference = matrix.subtract(matrix);
        Assert.fail("Expected AssertionError but got " + difference);
    }

    /**
     * Test multiply method with a scalar.
     */
    @Test
    public void testMultiplyScalar() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix result = matrix.multiply(2.0);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][]{{2.0, 4.0}, {6.0, 8.0}});
        Assert.assertEquals(expected, result);
    }

    /**
     * Test multiply method with a different matrix.
     */
    @Test
    public void testMultiplyDifferentMatrices() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}});
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][]{{16.0, 22.0, 22.0, 32.0}, {23.0, 34.0, 44.0, 56.0}, {36.0, 50.0, 70.0, 92.0}});
        Assert.assertEquals(expected, result);
    }

    /**
     * Test getEntry method with valid indices.
     */
    @Test
    public void testGetEntryValidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(1.0, value, 0.001);
    }

    /**
     * Test getEntry method with invalid row index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.getEntry(4, 0);
    }

    /**
     * Test getEntry method with invalid column index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.getEntry(0, 4);
    }

    /**
     * Test setEntry method with valid indices.
     */
    @Test
    public void testSetEntryValidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.setEntry(0, 0, 5.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(5.0, value, 0.001);
    }

    /**
     * Test setEntry method with invalid row index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.setEntry(4, 0, 5.0);
    }

    /**
     * Test setEntry method with invalid column index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.setEntry(0, 5, 5.0);
    }

    /**
     * Test addToEntry method with valid indices and non-zero value.
     */
    @Test
    public void testAddToEntryValidIndicesNonZeroValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(0, 0, 5.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(6.0, value, 0.001);
    }

    /**
     * Test addToEntry method with valid indices and zero value.
     */
    @Test
    public void testAddToEntryValidIndicesZeroValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(0, 0, 0.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(1.0, value, 0.001);
    }

    /**
     * Test addToEntry method with invalid row index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddToEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(4, 0, 5.0);
    }

    /**
     * Test addToEntry method with invalid column index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddToEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.addToEntry(0, 5, 5.0);
    }

    /**
     * Test multiplyEntry method with valid indices.
     */
    @Test
    public void testMultiplyEntryValidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.multiplyEntry(0, 0, 5.0);
        double value = matrix.getEntry(0, 0);
        Assert.assertEquals(5.0, value, 0.001);
    }

    /**
     * Test multiplyEntry method with invalid row index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyEntryInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.multiplyEntry(4, 0, 5.0);
    }

    /**
     * Test multiplyEntry method with invalid column index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyEntryInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.multiplyEntry(0, 5, 5.0);
    }

    /**
     * Test computeKey method with valid indices.
     */
    @Test
    public void testComputeKeyValidIndices() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        int key = matrix.computeKey(0, 0);
        Assert.assertEquals(0, key % columns);
        Assert.assertEquals(0, key / columns);
    }

    /**
     * Test computeKey method with invalid row index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeKeyInvalidRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.computeKey(4, 0);
    }

    /**
     * Test computeKey method with invalid column index.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testComputeKeyInvalidColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        matrix.computeKey(0, 5);
    }
}