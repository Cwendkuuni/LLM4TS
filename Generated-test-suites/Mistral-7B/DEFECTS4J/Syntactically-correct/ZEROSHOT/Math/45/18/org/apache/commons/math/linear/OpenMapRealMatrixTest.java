import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertNull(matrix.getEntry(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDimension() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        assertEquals(original, expected);
        assertEquals(copy, expected);
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assertEquals(3.0, result.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assertEquals(-1.0, result.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix1.setEntry(0, 0, 1.0);
        matrix2.setEntry(0, 0, 2.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(2.0, result.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testAddEntryGreaterThanMaxDouble() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        try {
            matrix.setEntry(0, 0, Double.MAX_VALUE + 1.0);
            fail("Number is too large exception expected");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        assertFalse(matrix1.equals(null));
        assertFalse(matrix1.equals(new Object()));
        assertEquals(matrix1, matrix1);
        assertFalse(matrix1.equals(matrix2));
    }

    @Test
    public void testHashCode() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        assertEquals(matrix1.hashCode(), matrix1.hashCode());
        assertNotEquals(matrix1.hashCode(), matrix2.hashCode());
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testMultiplyEntryByZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 0.0);
        assertEquals(0.0, matrix.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testAddToEntryZero() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrixB = new OpenMapRealMatrix(3, 3);
        matrixB.setEntry(0, 0, 1.0);
        matrix.addToEntry(0, 0, 0.0);
        OpenMapRealMatrix result = matrix.add(matrixB);
        assertNull(result.getEntry(0, 0));
    }

    @Test
    public void testMultiplyByZero() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        matrix2.setEntry(0, 0, 0.0);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assertEquals(0.0, result.getEntry(0, 0), 1e-12);
    }

    @Test
    public void testMultiplyByIdentity() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix identity = MatrixUtils.createIdentityMatrix(3);
        OpenMapRealMatrix result = matrix1.multiply(identity);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateIdentityMatrix() {
        OpenMapRealMatrix identity = MatrixUtils.createIdentityMatrix(3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(3, 3);
        result.setEntry(0, 0, 1.0);
        result.setEntry(1, 1, 1.0);
        result.setEntry(2, 2, 1.0);
        assertEquals(result, identity);
    }

    @Test
    public void testTranspose() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(2, 2, 1.0);
        OpenMapRealMatrix transpose = matrix.transpose();
        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 1.0);
        expected.setEntry(2, 2, 1.0);
        assertEquals(expected, transpose);
    }

    @Test
    public void testTransposeSelf() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(2, 2, 1.0);
        OpenMapRealMatrix transpose = matrix.transpose();
        assertEquals(matrix, transpose);
    }

    @Test
    public void testSubtractIdentity() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix identity = MatrixUtils.createIdentityMatrix(3);
        matrix.subtract(identity);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i == j) {
                    expected.setEntry(i, j, -1.0);
                } else {
                    expected.setEntry(i, j, 0.0);
                }
            }
        }
        assertEquals(expected, matrix);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSubMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        BlockRealMatrix subMatrix = new BlockRealMatrix(new double[3][3]);
        matrix.setSubMatrix(0, 0, subMatrix);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSubMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getSubMatrix(0, 0);
    }

    @Test
    public void testToString() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(2, 2, 1.0);
        String toString = matrix.toString();
        String expected = "[1.0, 0.0, 0.0]\n[0.0, 1.0, 0.0]\n[0.0, 0.0, 1.0]";
        assertEquals(expected, toString);
    }
}