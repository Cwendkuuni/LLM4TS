package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

/**
 * Test class for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test the creation of an OpenMapRealMatrix with the correct dimensions.
     */
    @Test
    public void testCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }

    /**
     * Test adding two matrices with the same dimensions.
     */
    @Test
    public void testAddition() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {0.0, 1.0},
            {2.0, 3.0},
            {4.0, 5.0}
        }));
        OpenMapRealMatrix sum = matrix1.add(matrix2);
        OpenMapRealMatrix expectedSum = new OpenMapRealMatrix(new double[][]{
            {1.0, 3.0},
            {5.0, 7.0},
            {9.0, 11.0}
        });
        assertEquals(expectedSum, sum);
    }

    /**
     * Test adding a matrix with different dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.add(matrix2);
    }

    /**
     * Test subtracting a matrix from another matrix.
     */
    @Test
    public void testSubtraction() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {0.0, 1.0},
            {2.0, 3.0},
            {4.0, 5.0}
        }));
        OpenMapRealMatrix difference = matrix1.subtract(matrix2);
        OpenMapRealMatrix expectedDifference = new OpenMapRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, 1.0},
            {1.0, 1.0}
        });
        assertEquals(expectedDifference, difference);
    }

    /**
     * Test subtracting a different sized matrix from another matrix.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.subtract(matrix2);
    }

    /**
     * Test multiplying a matrix by a scalar.
     */
    @Test
    public void testScalarMultiplication() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        OpenMapRealMatrix scaledMatrix = matrix.multiply(2.0);
        OpenMapRealMatrix expectedScaledMatrix = new OpenMapRealMatrix(new double[][]{
            {2.0, 4.0},
            {6.0, 8.0},
            {10.0, 12.0}
        });
        assertEquals(expectedScaledMatrix, scaledMatrix);
    }

    /**
     * Test multiplying a matrix by another matrix.
     */
    @Test
    public void testMatrixMultiplication() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {7.0, 8.0},
            {9.0, 10.0}
        }));
        OpenMapRealMatrix product = matrix1.multiply(matrix2);
        OpenMapRealMatrix expectedProduct = new OpenMapRealMatrix(new double[][]{
            {19.0, 22.0},
            {43.0, 52.0},
            {67.0, 82.0}
        });
        assertEquals(expectedProduct, product);
    }

    /**
     * Test multiplying a matrix by another matrix with different dimensions.
     */
    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMatrixMultiplicationDimensionMismatch() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 2);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
        matrix1.multiply(matrix2);
    }

    /**
     * Test getting a non-existent entry.
     */
    @Test
    public void testGetNonExistentEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        assertNull(matrix.getEntry(3, 3));
    }

    /**
     * Test setting an entry.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        matrix.setEntry(0, 0, 10.0);
        assertEquals(10.0, matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test setting an entry with a value that is too large.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryNumberTooLarge() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        matrix.setEntry(0, 0, Double.MAX_VALUE);
    }

    /**
     * Test getting an entry with a negative row or column index.
     */
    @Test
    public void testGetEntryNegativeIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        assertNull(matrix.getEntry(-1, 0));
        assertNull(matrix.getEntry(0, -1));
    }

    /**
     * Test adding an entry with a negative key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEntryNegativeKey() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        }));
        matrix.addToEntry(-1, 0, 1.0);
    }

}