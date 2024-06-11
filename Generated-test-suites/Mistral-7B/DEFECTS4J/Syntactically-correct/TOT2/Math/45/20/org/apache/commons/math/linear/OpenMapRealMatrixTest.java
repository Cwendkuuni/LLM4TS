package org.apache.commons.math.linear.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MatrixDimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;

import org.apache.commons.math.linear.AbstractRealMatrix;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.SparseRealMatrix;
import org.apache.commons.math.linear.util.MatrixUtils;

/**
 * Test cases for the {@link OpenMapRealMatrix} class.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
        assertNotNull(matrix.entries);
        assertTrue(matrix.entries.isEmpty());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertEquals(original, copy);
    }

    /**
     * Test {@code copy()} method.
     */
    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        OpenMapRealMatrix copy = original.copy();
        assertEquals(original, copy);
    }

    /**
     * Test {@code createMatrix()} method.
     */
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix();
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
        assertTrue(newMatrix.isEmpty());
    }

    /**
     * Test {@code getColumnDimension()} method.
     */
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(2, matrix.getColumnDimension());
    }

    /**
     * Test {@code add()} method with compatible matrices.
     */
    @Test
    public void testAddCompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        first.setEntry(0, 0, 1.0);
        first.setEntry(1, 1, 2.0);

        OpenMapRealMatrix second = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        second.setEntry(0, 0, 3.0);
        second.setEntry(1, 1, 4.0);

        OpenMapRealMatrix sum = first.add(second);

        assertEquals(2, sum.getRowDimension());
        assertEquals(2, sum.getColumnDimension());
        assertEquals(1.0 + 3.0, sum.getEntry(0, 0), 1e-14);
        assertEquals(2.0 + 4.0, sum.getEntry(1, 1), 1e-14);
    }

    /**
     * Test {@code add()} method with incompatible matrices.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddIncompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        first.setEntry(0, 0, 1.0);
        first.setEntry(1, 1, 2.0);

        OpenMapRealMatrix second = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        second.setEntry(0, 0, 3.0);
        second.setEntry(1, 1, 4.0);
        second.setEntry(2, 2, 5.0);

        first.add(second);
    }

    /**
     * Test {@code subtract()} method with compatible matrices.
     */
    @Test
    public void testSubtractCompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        first.setEntry(0, 0, 1.0);
        first.setEntry(1, 1, 2.0);

        OpenMapRealMatrix second = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        second.setEntry(0, 0, 3.0);
        second.setEntry(1, 1, 4.0);

        OpenMapRealMatrix difference = first.subtract(second);

        assertEquals(2, difference.getRowDimension());
        assertEquals(2, difference.getColumnDimension());
        assertEquals(-2.0, difference.getEntry(0, 0), 1e-14);
        assertEquals(-2.0, difference.getEntry(1, 1), 1e-14);
    }

    /**
     * Test {@code subtract()} method with incompatible matrices.
     */
    @Test
    public void testSubtractIncompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        first.setEntry(0, 0, 1.0);
        first.setEntry(1, 1, 2.0);

        RealMatrix second = new Matrix4x4(); // A different matrix
        first.subtract(second);
    }

    /**
     * Test {@code multiply()} method with compatible matrices.
     */
    @Test
    public void testMultiplyCompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        first.setEntry(0, 0, 1.0);
        first.setEntry(0, 1, 2.0);
        first.setEntry(1, 0, 3.0);
        first.setEntry(1, 1, 4.0);

        OpenMapRealMatrix second = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        second.setEntry(0, 0, 5.0);
        second.setEntry(0, 1, 6.0);
        second.setEntry(1, 0, 7.0);
        second.setEntry(1, 1, 8.0);

        RealMatrix result = first.multiply(second);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 15.0);
        expected.setEntry(0, 1, 22.0);
        expected.setEntry(1, 0, 21.0);
        expected.setEntry(1, 1, 28.0);

        assertEquals(expected, result);
    }

    /**
     * Test {@code multiply()} method with incompatible matrices.
     */
    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyIncompatibleMatrices() {
        OpenMapRealMatrix first = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        first.setEntry(0, 0, 1.0);
        first.setEntry(0, 1, 2.0);
        first.setEntry(1, 0, 3.0);
        first.setEntry(1, 1, 4.0);

        OpenMapRealMatrix second = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        second.setEntry(0, 0, 5.0);
        second.setEntry(0, 1, 6.0);
        second.setEntry(0, 2, 7.0);
        second.setEntry(1, 0, 8.0);
        second.setEntry(1, 1, 9.0);
        second.setEntry(1, 2, 10.0);
        second.setEntry(2, 0, 11.0);
        second.setEntry(2, 1, 12.0);
        second.setEntry(2, 2, 13.0);

        first.multiply(second);
    }

    /**
     * Test {@code getEntry()} method.
     */
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, 2.0);

        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test {@code setEntry()} method.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));

        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 1e-14);

        matrix.setEntry(1, 1, 2.0);
        assertEquals(2.0, matrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test {@code addToEntry()} method.
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 1.0);

        matrix.addToEntry(0, 0, 2.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test {@code multiplyEntry()} method.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, 1.0);

        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test exception handling when setting an entry with a value that is too large.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryTooLargeValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2));
        matrix.setEntry(0, 0, Double.MAX_VALUE + 1.0);
    }
}