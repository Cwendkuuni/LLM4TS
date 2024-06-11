import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        OpenMapRealMatrix copy = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix.getRowDimension(), copy.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copy.getColumnDimension());
        assertArrayEquals(originalMatrix.toString().split("\n"), copy.toString().split("\n"));
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        OpenMapRealMatrix sum = firstMatrix.add(secondMatrix);
        assertEquals(firstMatrix.getRowDimension(), sum.getRowDimension());
        assertEquals(firstMatrix.getColumnDimension(), sum.getColumnDimension());
        assertArrayEquals(firstMatrix.toString().split("\n"), sum.toString().split("\n"));

        secondMatrix.setEntry(0, 0, 2.0);
        sum = firstMatrix.add(secondMatrix);
        double value = sum.getEntry(0, 0);
        assertEquals(2.0, value, 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(3, 3));
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(3, 4));
        firstMatrix.add(secondMatrix);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        OpenMapRealMatrix difference = firstMatrix.subtract(secondMatrix);
        assertEquals(firstMatrix.getRowDimension(), difference.getRowDimension());
        assertEquals(firstMatrix.getColumnDimension(), difference.getColumnDimension());
        assertArrayEquals(firstMatrix.toString().split("\n"), difference.toString().split("\n"));

        secondMatrix.setEntry(0, 0, 2.0);
        difference = firstMatrix.subtract(secondMatrix);
        double value = difference.getEntry(0, 0);
        assertEquals(-2.0, value, 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(3, 3));
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(3, 4));
        firstMatrix.subtract(secondMatrix);
    }

    @Test
    public void testMultiplicationByScalar() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        matrix.multiplyEntry(0, 0, 2.0);
        double value = matrix.getEntry(0, 0);
        assertEquals(2.0, value, 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplicationByTooLargeScalar() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        matrix.multiplyEntry(0, 0, Double.MAX_VALUE);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        RealMatrix product = firstMatrix.multiply(secondMatrix);
        assertEquals(firstMatrix.getRowDimension(), product.getRowDimension());
        assertEquals(secondMatrix.getColumnDimension(), product.getColumnDimension());

        firstMatrix.setEntry(0, 0, 2.0);
        secondMatrix.setEntry(0, 0, 3.0);
        secondMatrix.setEntry(0, 1, 4.0);
        double[] expectedProduct = {2.0 * 3.0, 2.0 * 4.0};
        assertArrayEquals(expectedProduct, product.getEntryValues(), 0.001);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplicationDimensionMismatch() {
        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(2, 2));
        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap(3, 2));
        firstMatrix.multiply(secondMatrix);
    }

    @Test
    public void testToString() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap());
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        String expectedString = "1.0\n0.0 2.0";
        String actualString = matrix.toString();
        assertEquals(expectedString, actualString);
    }
}