package org.apache.commons.math.linear;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertTrue(matrix.isSparse());
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).identity()));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).identity()));
        OpenMapRealMatrix result = matrix1.add(matrix2);

        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());

        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                double expected = matrix1.getEntry(i, j) + matrix2.getEntry(i, j);
                assertEquals(expected, result.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2);
        result.add(matrix1);
        result.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2).identity()));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).subtract(new OpenMapRealMatrix(2, 2).identity()));
        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix1.getColumnDimension(), result.getColumnDimension());

        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix1.getColumnDimension(); j++) {
                double expected = matrix1.getEntry(i, j) - matrix2.getEntry(i, j);
                assertEquals(expected, result.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2);
        result.subtract(matrix1);
        result.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).multiply(new OpenMapRealMatrix(3, 3).identity()));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).multiply(new OpenMapRealMatrix(3, 3).identity()));
        OpenMapRealMatrix result = matrix1.multiply(matrix2);

        assertEquals(matrix1.getRowDimension(), result.getRowDimension());
        assertEquals(matrix2.getColumnDimension(), result.getColumnDimension());

        for (int i = 0; i < matrix1.getRowDimension(); i++) {
            for (int j = 0; j < matrix2.getColumnDimension(); j++) {
                double expected = 0.0;
                for (int k = 0; k < matrix1.getColumnDimension(); k++) {
                    expected += matrix1.getEntry(i, k) * matrix2.getEntry(k, j);
                }
                assertEquals(expected, result.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
    }

    @Test(expected = IllegalArgumentException. class)
    public void testMultiplicationIncompatibleDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        OpenMapRealMatrix result = new OpenMapRealMatrix(2, 2);
        result.multiply(matrix1);
        result.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).identity()));
        double expected = 1.0;
        assertEquals(expected, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        assertEquals(expected, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
        assertNull(matrix.getEntry(2, 3));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryNegativeRowIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).identity()));
        matrix.getEntry(-1, 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryNegativeColumnIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).identity()));
        matrix.getEntry(0, -1);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        double value = 5.0;
        matrix.setEntry(0, 0, value);
        assertEquals(value, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        matrix.setEntry(1, 1, 2.5);
        assertEquals(2.5, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryInvalidDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(4, 4, 3.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        double value = 3.0;
        matrix.addToEntry(0, 0, value);
        assertEquals(value + 1.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        matrix.addToEntry(0, 0, value);
        assertEquals(2.0 * value + 1.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToEntryInvalidDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.addToEntry(4, 4, 3.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        double value = 2.0;
        matrix.multiplyEntry(0, 0, value);
        assertEquals(value * 1.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
        matrix.multiplyEntry(0, 0, value);
        assertEquals(value * value * 1.0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyEntryInvalidDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.multiplyEntry(4, 4, 3.0);
    }
}