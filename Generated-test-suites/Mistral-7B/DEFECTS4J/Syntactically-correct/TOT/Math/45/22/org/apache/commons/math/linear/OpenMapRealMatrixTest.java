package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RealArray2D;
import org.apache.commons.math3.util.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;

/**
 * Test for OpenMapRealMatrix class
 */
public class OpenMapRealMatrixTest {

    /**
     * Test construction with correct dimensions
     */
    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test construction with zero dimensions
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithZeroDimensions() {
        new OpenMapRealMatrix(0, 3);
    }

    /**
     * Test construction with negative dimensions
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithNegativeDimensions() {
        new OpenMapRealMatrix(-3, 3);
    }

    /**
     * Test addition with two matrices of the same dimensions
     */
    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);

        RealMatrix expected = new RealArray2D(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                expected.setEntry(i, j, 0.0);
                matrix1.setEntry(i, j, (i + j) % 2.0);
                matrix2.setEntry(i, j, (i + j + 1) % 2.0);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expectedValue = expected.getEntry(i, j);
                double actualValue = result.getEntry(i, j);
                assertEquals(expectedValue, Precision.round(actualValue, 14));
            }
        }
    }

    /**
     * Test addition with matrices of different dimensions
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithDifferentDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.add(matrix2);
    }

    /**
     * Test subtraction with two matrices of the same dimensions
     */
    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);

        RealMatrix expected = new RealArray2D(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                expected.setEntry(i, j, 0.0);
                matrix1.setEntry(i, j, (i + j) % 2.0);
                matrix2.setEntry(i, j, (i + j + 1) % 2.0);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expectedValue = expected.getEntry(i, j);
                double actualValue = result.getEntry(i, j);
                assertEquals(expectedValue, Precision.round(actualValue, 14));
            }
        }
    }

    /**
     * Test subtraction with matrices of different dimensions
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractionWithDifferentDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.subtract(matrix2);
    }

    /**
     * Test multiplication with a vector
     */
    @Test
    public void testMultiplicationVector() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        double[] vector = {1.0, 2.0, 3.0};
        RealMatrix result = matrix.multiply(RealMatrixUtils.createColumnMatrix(vector));
        RealMatrix expected = new RealArray2D(3, 1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 1; j++) {
                expected.setEntry(i, j, 0.0);
                matrix.setEntry(i, 0, (i + 1) % 2.0);
            }
        }

        for (int i = 0; i < 3; i++) {
            double expectedValue = expected.getEntry(i, 0);
            double actualValue = result.getEntry(i, 0);
            assertEquals(expectedValue, Precision.round(actualValue, 14));
        }
    }

    /**
     * Test multiplication with a matrix
     */
    @Test
    public void testMultiplicationMatrix() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        RealMatrix result = matrix1.multiply(matrix2);
        RealMatrix expected = new RealArray2D(3, 3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                expected.setEntry(i, j, 0.0);
                for (int k = 0; k < 3; k++) {
                    expected.addToEntry(i, j, matrix1.getEntry(i, k) * matrix2.getEntry(k, j));
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double expectedValue = expected.getEntry(i, j);
                double actualValue = result.getEntry(i, j);
                assertEquals(expectedValue, Precision.round(actualValue, 14));
            }
        }
    }

    /**
     * Test multiplication with a matrix of different dimensions
     */
    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplicationWithDifferentDimensions() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
        matrix1.multiply(matrix2);
    }

    /**
     * Test setting an entry out of bounds
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(-1, 0, 0.0);
    }

    /**
     * Test setting an entry with a non-finite value
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingEntryNonFiniteValue() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test getting an entry out of bounds
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGettingEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.getEntry(-1, 0);
    }
}