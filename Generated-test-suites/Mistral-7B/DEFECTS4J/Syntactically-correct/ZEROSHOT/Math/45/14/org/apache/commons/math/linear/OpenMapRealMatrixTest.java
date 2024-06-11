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
    public void testCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDimension() {
        new OpenMapRealMatrix(-1, 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix c = a.add(b);
        assertNotSame(a, c);
        assertNotSame(b, c);
        assertEquals(a.getRowDimension(), c.getRowDimension());
        assertEquals(a.getColumnDimension(), c.getColumnDimension());
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < a.getColumnDimension(); j++) {
                double aij = a.getEntry(i, j);
                double bij = b.getEntry(i, j);
                double cij = c.getEntry(i, j);
                if (aij >= 0 && bij >= 0) {
                    assertEquals(aij + bij, cij, 1e-13);
                } else if (aij < 0 && bij < 0) {
                    assertEquals(aij + bij, cij, 1e-13);
                } else {
                    assertEquals(0.0, cij, 1e-13);
                }
            }
        }
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix c = a.subtract(b);
        assertNotSame(a, c);
        assertNotSame(b, c);
        assertEquals(a.getRowDimension(), c.getRowDimension());
        assertEquals(a.getColumnDimension(), c.getColumnDimension());
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < a.getColumnDimension(); j++) {
                double aij = a.getEntry(i, j);
                double bij = b.getEntry(i, j);
                double cij = c.getEntry(i, j);
                if (aij >= 0 && bij >= 0) {
                    assertEquals(aij - bij, cij, 1e-13);
                } else if (aij < 0 && bij < 0) {
                    assertEquals(aij - bij, cij, 1e-13);
                } else {
                    assertEquals(0.0, cij, 1e-13);
                }
            }
        }
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix b = new OpenMapRealMatrix(3, 3);
        RealMatrix c = a.multiply(b);
        assertNotSame(a, c);
        assertNotSame(b, c);
        assertEquals(a.getRowDimension(), c.getRowDimension());
        assertEquals(b.getColumnDimension(), c.getColumnDimension());
        MatrixUtils.assertEquals(new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}}), c);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-13);
        matrix.setEntry(2, 2, 0.5);
        assertEquals(0.5, matrix.getEntry(2, 2), 1e-13);
        matrix.setEntry(-1, 1, Double.NaN);
        assertNull(matrix.getEntry(-1, 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-13);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(3, 3, 2.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-13);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(3, 3, 2.0);
    }

    @Test
    public void testSize() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(9, matrix.size());
    }

    @Test
    public void testToString() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);
        matrix.setEntry(2, 2, 3.0);
        assertEquals("OpenMapRealMatrix(3, 3)\n" +
                "  [ 1.0,  0.0,  0.0;\n" +
                "   0.0,  2.0,  0.0;\n" +
                "   0.0,  0.0,  3.0]", matrix.toString());
    }

    @Test
    public void testGetColumn() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        RealVector column = matrix.getColumnVector(0);
        assertEquals(3.0, column.getEntry(2), 1e-13);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetColumnOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getColumnVector(3);
    }

    @Test
    public void testGetRow() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        RealVector row = matrix.getRowVector(0);
        assertEquals(3.0, row.getEntry(2), 1e-13);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetRowOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getRowVector(3);
    }

    @Test
    public void testTranspose() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(0, 2, 3.0);
        matrix.setEntry(1, 0, 4.0);
        matrix.setEntry(1, 1, 5.0);
        matrix.setEntry(1, 2, 6.0);
        matrix.setEntry(2, 0, 7.0);
        matrix.setEntry(2, 1, 8.0);
        matrix.setEntry(2, 2, 9.0);
        OpenMapRealMatrix transpose = matrix.transpose();
        assertNotSame(matrix, transpose);
        assertEquals(3, transpose.getRowDimension());
        assertEquals(3, transpose.getColumnDimension());
        for (int i = 0; i < transpose.getRowDimension(); i++) {
            for (int j = 0; j < transpose.getColumnDimension(); j++) {
                double aij = matrix.getEntry(i, j);
                double tij = transpose.getEntry(j, i);
                if (aij >= 0) {
                    assertEquals(aij, tij, 1e-13);
                } else {
                    assertEquals(-aij, tij, 1e-13);
                }
            }
        }
    }
}