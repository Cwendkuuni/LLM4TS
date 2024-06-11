import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for OpenMapRealMatrix.
 */
public class OpenMapRealMatrixTest {
    /**
     * Test the creation of an empty matrix.
     */
    @Test
    public void testEmptyMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isSparse());
        assertTrue(matrix.isAccessible());
        assertNull(matrix.getEntry(0, 0));
    }

    /**
     * Test the creation of a matrix with a single entry.
     */
    @Test
    public void testSingleEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 2.0);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isSparse());
        assertTrue(matrix.isAccessible());
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-12);
        assertNull(matrix.getEntry(0, 0));
        assertNull(matrix.getEntry(1, 0));
        assertNull(matrix.getEntry(1, 2));
        assertNull(matrix.getEntry(2, 1));
    }

    /**
     * Test the creation of a matrix by copying another one.
     */
    @Test
    public void testCopyMatrix() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 2.0);
            setEntry(1, 2, 3.0);
            setEntry(2, 1, 4.0);
        }});
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertEquals(original.isSparse(), copy.isSparse());
        assertEquals(original.isAccessible(), copy.isAccessible());
        assertEquals(original.getEntry(1, 1), copy.getEntry(1, 1), 1e-12);
        assertEquals(original.getEntry(1, 2), copy.getEntry(1, 2), 1e-12);
        assertEquals(original.getEntry(2, 1), copy.getEntry(2, 1), 1e-12);
    }

    /**
     * Test the addition of two matrices.
     */
    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 2.0);
            setEntry(1, 2, 3.0);
            setEntry(2, 1, 4.0);
        }});
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 1.0);
            setEntry(1, 2, 2.0);
            setEntry(2, 1, 3.0);
        }});
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(matrix1, result);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        assertEquals(matrix1.isSparse(), result.isSparse());
        assertEquals(matrix1.isAccessible(), result.isAccessible());
        assertEquals(matrix1.getEntry(1, 1), result.getEntry(1, 1), 1e-12);
        assertEquals(matrix1.getEntry(1, 2), result.getEntry(1, 2), 1e-12);
        assertEquals(matrix1.getEntry(2, 1), result.getEntry(2, 1), 1e-12);
    }

    /**
     * Test the subtraction of two matrices.
     */
    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 2.0);
            setEntry(1, 2, 3.0);
            setEntry(2, 1, 4.0);
        }});
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 1.0);
            setEntry(1, 2, 2.0);
            setEntry(2, 1, 3.0);
        }});
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(matrix1, result);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        assertEquals(matrix1.isSparse(), result.isSparse());
        assertEquals(matrix1.isAccessible(), result.isAccessible());
        assertEquals(matrix1.getEntry(1, 1), result.getEntry(1, 1), 1e-12);
        assertEquals(matrix1.getEntry(1, 2), result.getEntry(1, 2), 1e-12);
        assertEquals(matrix1.getEntry(2, 1), result.getEntry(2, 1), 1e-12);
    }

    /**
     * Test the multiplication of a matrix by a scalar.
     */
    @Test
    public void testMultiplicationByScalar() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 1.0);
            setEntry(1, 2, 2.0);
            setEntry(2, 1, 3.0);
        }});
        OpenMapRealMatrix result = matrix.multiply(2.0);
        assertEquals(matrix, result);
        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(matrix.getColumnDimension(), result.getColumnDimension());
        assertEquals(matrix.isSparse(), result.isSparse());
        assertEquals(matrix.isAccessible(), result.isAccessible());
        assertEquals(2.0 * matrix.getEntry(1, 1), result.getEntry(1, 1), 1e-12);
        assertEquals(2.0 * matrix.getEntry(1, 2), result.getEntry(1, 2), 1e-12);
        assertEquals(2.0 * matrix.getEntry(2, 1), result.getEntry(2, 1), 1e-12);
    }

    /**
     * Test the multiplication of two matrices.
     */
    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 1.0);
            setEntry(1, 2, 2.0);
            setEntry(2, 1, 3.0);
        }});
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 1.0);
            setEntry(1, 2, 2.0);
            setEntry(2, 1, 3.0);
            setEntry(2, 2, 4.0);
        }});
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(matrix1, result);
        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());
        assertEquals(matrix1.isSparse(), result.isSparse());
        assertEquals(matrix1.isAccessible(), result.isAccessible());
        assertEquals(matrix1.getEntry(1, 1), result.getEntry(1, 1), 1e-12);
        assertEquals(matrix1.getEntry(1, 2), result.getEntry(1, 2), 1e-12);
        assertEquals(matrix1.getEntry(2, 1), result.getEntry(2, 1), 1e-12);
        assertEquals(matrix1.getEntry(2, 2), result.getEntry(2, 2), 1e-12);
    }

    /**
     * Test the setting of an entry that already exists.
     */
    @Test
    public void testSetEntryExisting() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 1.0);
            setEntry(1, 2, 2.0);
            setEntry(2, 1, 3.0);
        }};
        matrix.setEntry(1, 1, 3.0);
        assertEquals(3.0, matrix.getEntry(1, 1), 1e-12);
    }

    /**
     * Test the setting of an entry that does not exist.
     */
    @Test
    public void testSetEntryNonExisting() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 1.0);
            setEntry(1, 2, 2.0);
            setEntry(2, 1, 3.0);
        }};
        matrix.setEntry(2, 2, 4.0);
        assertFalse(matrix.contains(computeKey(2, 2)));
        assertEquals(4.0, matrix.getEntry(2, 2), 1e-12);
    }

    /**
     * Test the removal of an entry.
     */
    @Test
    public void testRemoveEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3) {{
            setEntry(1, 1, 1.0);
            setEntry(1, 2, 2.0);
            setEntry(1, 3, 3.0);
            setEntry(2, 1, 3.0);
            setEntry(2, 2, 4.0);
            setEntry(2, 3, 5.0);
        }};
        matrix.removeEntry(computeKey(1, 3));
        assertFalse(matrix.contains(computeKey(1, 3)));
        assertEquals(5.0, matrix.getEntry(2, 3), 1e-12);
    }

    /**
     * Test the exception when adding too large matrices.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAdditionTooLarge() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(1000, 1000) {{
            for (int i = 0; i < 1000 * 1000; i++) {
                setEntry(i / 1000, i % 1000, i % 1000 + 1.0);
            }
        }});
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(1000, 1000) {{
            for (int i = 0; i < 1000 * 1000; i++) {
                setEntry(i / 1000, i % 1000, i % 1000 + 1000.0);
            }
        }});
        matrix1.add(matrix2);
    }

    /**
     * Test the exception when multiplying too large matrices.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplicationTooLarge() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(1000, 1000) {{
            for (int i = 0; i < 1000 * 1000; i++) {
                setEntry(i / 1000, i % 1000, i % 1000 + 1.0);
            }
        }});
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(1000, 1000) {{
            for (int i = 0; i < 1000 * 1000; i++) {
                setEntry(i / 1000, i % 1000, i % 1000 + 1000.0);
            }
        }});
        matrix1.multiply(matrix2);
    }

    /**
     * Test the computation of the key for a matrix entry.
     */
    private int computeKey(int row, int column) {
        return row * columns + column;
    }
}