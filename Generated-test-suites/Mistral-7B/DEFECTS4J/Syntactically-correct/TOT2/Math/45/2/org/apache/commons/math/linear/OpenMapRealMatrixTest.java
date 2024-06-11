import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        assertNotNull(copiedMatrix.entries);
        assertEquals(originalMatrix.entries, copiedMatrix.entries);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        RealMatrix sum = m1.add(m2);
        assertEquals(m1.getRowDimension(), sum.getRowDimension());
        assertEquals(m1.getColumnDimension(), sum.getColumnDimension());
        OpenMapRealMatrix expectedSum = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < m1.getRowDimension(); i++) {
            for (int j = 0; j < m1.getColumnDimension(); j++) {
                double value1 = m1.getEntry(i, j);
                double value2 = m2.getEntry(i, j);
                expectedSum.setEntry(i, j, value1 + value2);
            }
        }
        assertEquals(expectedSum, sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithDifferentDimensions() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 2);
        m1.add(m2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        RealMatrix difference = m1.subtract(m2);
        assertEquals(m1.getRowDimension(), difference.getRowDimension());
        assertEquals(m1.getColumnDimension(), difference.getColumnDimension());
        OpenMapRealMatrix expectedDifference = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < m1.getRowDimension(); i++) {
            for (int j = 0; j < m1.getColumnDimension(); j++) {
                double value1 = m1.getEntry(i, j);
                double value2 = m2.getEntry(i, j);
                expectedDifference.setEntry(i, j, value1 - value2);
            }
        }
        assertEquals(expectedDifference, difference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionWithDifferentDimensions() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 2);
        m1.subtract(m2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        RealMatrix product = m1.multiply(m2);
        assertEquals(m1.getRowDimension(), product.getRowDimension());
        assertEquals(m2.getColumnDimension(), product.getColumnDimension());
        OpenMapRealMatrix expectedProduct = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < m1.getRowDimension(); i++) {
            for (int j = 0; j < m2.getColumnDimension(); j++) {
                double sum = 0.0;
                for (int k = 0; k < m1.getColumnDimension(); k++) {
                    sum += m1.getEntry(i, k) * m2.getEntry(k, j);
                }
                expectedProduct.setEntry(i, j, sum);
            }
        }
        assertEquals(expectedProduct, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationWithDifferentDimensions() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(2, 2);
        m1.multiply(m2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
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
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAdditionWithTooLargeValue() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, Double.MAX_VALUE);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix sum = m1.add(m2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSubtractionWithTooLargeValue() {
        OpenMapRealMatrix m1 = new OpenMapRealMatrix(3, 3);
        m1.setEntry(0, 0, Double.MAX_VALUE);
        OpenMapRealMatrix m2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix difference = m1.subtract(m2);
    }
}