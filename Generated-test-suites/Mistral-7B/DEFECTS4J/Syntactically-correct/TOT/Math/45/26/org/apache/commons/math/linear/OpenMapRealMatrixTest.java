package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;
    private OpenMapRealMatrix identityMatrix;

    /**
     * Set up test variables.
     */
    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
        identityMatrix = new OpenMapRealMatrix(3, 3);
        identityMatrix.setIdentity();
    }

    /**
     * Test constructor with valid dimensions.
     */
    @Test
    public void testConstructorWithValidDimensions() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
    }

    /**
     * Test constructor with incorrect dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithIncorrectDimensions() {
        new OpenMapRealMatrix(2, 4);
    }

    /**
     * Test setEntry method with valid index and value.
     */
    @Test
    public void testSetEntryWithValidIndexAndValue() {
        matrix.setEntry(0, 0, 3.0);
        assertEquals(3.0, matrix.getEntry(0, 0), 1e-10);
    }

    /**
     * Test setEntry method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidIndex() {
        matrix.setEntry(3, 3, 3.0);
    }

    /**
     * Test setEntry method with invalid value.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetEntryWithInvalidValue() {
        matrix.setEntry(0, 0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test add method with compatible matrices.
     */
    @Test
    public void testAddWithCompatibleMatrices() {
        final double eps = 1e-12;
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2).identity()));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix c = a.add(b);
        assertEquals(a.getRowDimension(), c.getRowDimension());
        assertEquals(a.getColumnDimension(), c.getColumnDimension());
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < a.getColumnDimension(); j++) {
                double aij = a.getEntry(i, j);
                double bij = b.getEntry(i, j);
                double cij = c.getEntry(i, j);
                assertEquals(Precision.round(aij + bij, eps), cij, eps);
            }
        }
    }

    /**
     * Test add method with incompatible matrices.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddWithIncompatibleMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        OpenMapRealMatrix c = a.add(b);
    }

    /**
     * Test subtract method with compatible matrices.
     */
    @Test
    public void testSubtractWithCompatibleMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix c = a.subtract(b);
        assertEquals(a.getRowDimension(), c.getRowDimension());
        assertEquals(a.getColumnDimension(), c.getColumnDimension());
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < a.getColumnDimension(); j++) {
                double aij = a.getEntry(i, j);
                double bij = b.getEntry(i, j);
                double cij = c.getEntry(i, j);
                assertEquals(Precision.round(aij - bij, 1e-12), cij, 1e-12);
            }
        }
    }

    /**
     * Test subtract method with incompatible matrices.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testSubtractWithIncompatibleMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        OpenMapRealMatrix c = a.subtract(b);
    }

    /**
     * Test multiply method with compatible matrices.
     */
    @Test
    public void testMultiplyWithCompatibleMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(identityMatrix);
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix c = a.multiply(b);
        assertEquals(a.getRowDimension(), c.getRowDimension());
        assertEquals(b.getColumnDimension(), c.getColumnDimension());
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < b.getColumnDimension(); j++) {
                double aij = a.getEntry(i, 0);
                double bjk = b.getEntry(0, j);
                double cijjk = c.getEntry(i, j);
                assertEquals(aij * bjk, cijjk, 1e-12);
            }
        }
    }

    /**
     * Test multiply method with incompatible matrices.
     */
    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyWithIncompatibleMatrices() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(identityMatrix);
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity());
        OpenMapRealMatrix c = a.multiply(b);
    }

    /**
     * Test getEntry method with valid index.
     */
    @Test
    public void testGetEntryWithValidIndex() {
        matrix.setEntry(1, 1, 1.0);
        assertEquals(1.0, matrix.getEntry(1, 1), 1e-12);
    }

    /**
     * Test getEntry method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidIndex() {
        matrix.getEntry(3, 3);
    }

    /**
     * Test getRowDimension method.
     */
    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }

    /**
     * Test getColumnDimension method.
     */
    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }
}