package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.junit.Test;

import org.apache.commons.math.linear.OpenMapRealMatrix;

/**
 * Test class for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test constructor with valid dimensions.
     */
    @Test
    public void testValidConstruction() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        assertEquals(rowDimension, matrix.getRowDimension());
        assertEquals(columnDimension, matrix.getColumnDimension());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(originalMatrix);
        assertEquals(originalMatrix, copy);
    }

    /**
     * Test adding matrices with the same dimensions.
     */
    @Test
    public void testAddMatricesSameDimensions() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix A = new OpenMapRealMatrix(rowDimension, columnDimension);
        final OpenMapRealMatrix B = new OpenMapRealMatrix(rowDimension, columnDimension);
        final OpenMapRealMatrix C = A.add(B);
        assertEquals(A, C);
    }

    /**
     * Test adding matrices with different dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddMatricesDifferentDimensions() {
        final int rowA = 3;
        final int columnA = 2;
        final int rowB = 2;
        final int columnB = 3;
        final OpenMapRealMatrix A = new OpenMapRealMatrix(rowA, columnA);
        final OpenMapRealMatrix B = new OpenMapRealMatrix(rowB, columnB);
        A.add(B);
    }

    /**
     * Test subtracting matrices with the same dimensions.
     */
    @Test
    public void testSubtractMatricesSameDimensions() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix A = new OpenMapRealMatrix(rowDimension, columnDimension);
        final OpenMapRealMatrix B = new OpenMapRealMatrix(rowDimension, columnDimension);
        final OpenMapRealMatrix C = A.subtract(B);
        assertEquals(A, C);
    }

    /**
     * Test subtracting matrices with different dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractMatricesDifferentDimensions() {
        final int rowA = 3;
        final int columnA = 2;
        final int rowB = 2;
        final int columnB = 3;
        final OpenMapRealMatrix A = new OpenMapRealMatrix(rowA, columnA);
        final OpenMapRealMatrix B = new OpenMapRealMatrix(rowB, columnB);
        A.subtract(B);
    }

    /**
     * Test multiplying matrices with compatible dimensions.
     */
    @Test
    public void testMultiplyMatricesCompatibleDimensions() {
        final int rowDimensionA = 3;
        final int columnDimensionA = 2;
        final int rowDimensionB = 2;
        final int columnDimensionB = 3;
        final OpenMapRealMatrix A = new OpenMapRealMatrix(rowDimensionA, columnDimensionA);
        final OpenMapRealMatrix B = new OpenMapRealMatrix(rowDimensionB, columnDimensionB);
        final OpenMapRealMatrix C = A.multiply(B);
        assertEquals(rowDimensionA, C.getRowDimension());
        assertEquals(columnDimensionB, C.getColumnDimension());
    }

    /**
     * Test multiplying matrices with incompatible dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyMatricesIncompatibleDimensions() {
        final int rowDimensionA = 3;
        final int columnDimensionA = 2;
        final int rowDimensionB = 3;
        final int columnDimensionB = 2;
        final OpenMapRealMatrix A = new OpenMapRealMatrix(rowDimensionA, columnDimensionA);
        final OpenMapRealMatrix B = new OpenMapRealMatrix(rowDimensionB, columnDimensionB);
        A.multiply(B);
    }

    /**
     * Test getting an entry from a matrix.
     */
    @Test
    public void testGetEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    /**
     * Test setting an entry in a matrix.
     */
    @Test
    public void testSetEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    /**
     * Test adding an entry to a matrix.
     */
    @Test
    public void testAddToEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.addToEntry(0, 0, 1.0);
        assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    /**
     * Test multiplying an entry in a matrix.
     */
    @Test
    public void testMultiplyEntry() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
    }

    /**
     * Test adding too large values to a matrix.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddTooLargeValues() {
        final int rowDimension = 3;
        final int columnDimension = 2;
        final OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(0, 0, Long.MAX_VALUE);
        matrix.addToEntry(0, 0, Long.MAX_VALUE);
    }
}