package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;

/**
 * Test class for the OpenMapRealMatrix class.
 */
public class OpenMapRealMatrixTest {

    /**
     * Test construction with correct dimensions.
     */
    @Test
    public void testConstruction() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test construction with invalid dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithInvalidDimensions() {
        new OpenMapRealMatrix(-1, 3);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        assertNotSame(original, copy);
        for (int i = 0; i < original.getRowDimension(); i++) {
            for (int j = 0; j < original.getColumnDimension(); j++) {
                assertEquals(original.getEntry(i, j), copy.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test getColumnDimension method.
     */
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(3, matrix.getColumnDimension());
    }

    /**
     * Test getRowDimension method.
     */
    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        assertEquals(2, matrix.getRowDimension());
    }

    /**
     * Test add method with compatible matrices.
     */
    @Test
    public void testAddCompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        OpenMapRealMatrix sum = a.add(b);
        assertEquals(a, sum);
    }

    /**
     * Test add method with incompatible matrices.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddIncompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(3, 3)));
        a.add(b);
    }

    /**
     * Test multiply matrix by scalar.
     */
    @Test
    public void testMultiplyByScalar() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        OpenMapRealMatrix b = new OpenMapRealMatrix(a.multiply(2.5));
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < a.getColumnDimension(); j++) {
                assertEquals(a.getEntry(i, j) * 2.5, b.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test multiply method with compatible matrices.
     */
    @Test
    public void testMultiplyCompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2).add(new OpenMapRealMatrix(3, 2)));
        OpenMapRealMatrix c = a.multiply(b);
        assertEquals(a.getRowDimension(), c.getRowDimension());
        assertEquals(b.getColumnDimension(), c.getColumnDimension());
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < b.getColumnDimension(); j++) {
                double sum = 0.0;
                for (int k = 0; k < b.getRowDimension(); k++) {
                    sum += a.getEntry(i, k) * b.getEntry(k, j);
                }
                assertEquals(sum, c.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test multiply method with incompatible matrices.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyIncompatible() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(3, 3)));
        a.multiply(b);
    }

    /**
     * Test setting an entry.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 42);
        assertEquals(42, matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test setting a non-existing entry.
     */
    @Test
    public void testSetNonExistingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(3, 3, 42);
        assertEquals(0.0, matrix.getEntry(3, 3), 1e-14);
    }

    /**
     * Test adding an entry.
     */
    @Test
    public void testAddEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(0, 0, 42);
        assertEquals(42, matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test adding a non-existing entry.
     */
    @Test
    public void testAddNonExistingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(3, 3, 42);
        assertEquals(0.0, matrix.getEntry(3, 3), 1e-14);
    }

    /**
     * Test multiplying an entry.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(0, 0, 2);
        assertEquals(2 * matrix.getEntry(0, 0), matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test multiplying a non-existing entry.
     */
    @Test
    public void testMultiplyNonExistingEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(3, 3, 2);
        assertEquals(0.0, matrix.getEntry(3, 3), 1e-14);
    }

    /**
     * Test getting an entry out of bounds.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.getEntry(3, 3);
    }

    /**
     * Test setting an entry out of bounds.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(3, 3, 42);
    }

    /**
     * Test adding an entry out of bounds.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.addToEntry(3, 3, 42);
    }

    /**
     * Test multiplying an entry out of bounds.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.multiplyEntry(3, 3, 2);
    }

    /**
     * Test adding two matrices with different sizes.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(3, 3)));
        a.add(b);
    }

    /**
     * Test multiplying a matrix with a vector.
     */
    @Test
    public void testMultiplyMatrixVector() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        RealVector x = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector y = a.operate(x);
        RealVector expected = new ArrayRealVector(new double[]{5.0, 11.0});
        assertEquals(expected, y);
    }

    /**
     * Test multiplying a matrix with a vector of different size.
     */
    @Test(expected = MatrixDimensionMismatchException.class)
    public void testMultiplyMatrixVectorDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        RealVector x = new ArrayRealVector(new double[]{1.0, 2.0, 3.0, 4.0});
        a.operate(x);
    }

    /**
     * Test multiplying a matrix with a null vector.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyMatrixVectorNull() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        a.operate(null);
    }

    /**
     * Test multiplying a matrix with a diagonal matrix.
     */
    @Test
    public void testMultiplyMatrixDiagonal() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        RealMatrix b = new BlockRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)));
        b.setEntry(0, 0, 2);
        b.setEntry(1, 1, 3);
        OpenMapRealMatrix c = a.multiply(b);
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < b.getColumnDimension(); j++) {
                double sum = 0.0;
                for (int k = 0; k < b.getRowDimension(); k++) {
                    sum += a.getEntry(i, k) * b.getEntry(k, j);
                }
                assertEquals(sum, c.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test multiplying a matrix with a singular matrix.
     */
    @Test
    public void testMultiplyMatrixSingular() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)));
        RealMatrix b = new BlockRealMatrix(new OpenMapRealMatrix(2, 2).add(new OpenMapRealMatrix(2, 2)).add(new OpenMapRealMatrix(2, 2).multiply(2)));
        OpenMapRealMatrix c = a.multiply(b);
        assertEquals(a.getRowDimension(), c.getRowDimension());
        assertEquals(b.getColumnDimension(), c.getColumnDimension());
        for (int i = 0; i < a.getRowDimension(); i++) {
            for (int j = 0; j < b.getColumnDimension(); j++) {
                double sum = 0.0;
                for (int k = 0; k < b.getRowDimension(); k++) {
                    sum += a.getEntry(i, k) * b.getEntry(k, j);
                }
                assertEquals(sum, c.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test adding a matrix with a large number.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddLargeNumber() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)).multiply(2).multiply(2));
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).add(new OpenMapRealMatrix(2, 3)).multiply(2).multiply(2));
        a.add(b.add(new OpenMapRealMatrix(2, 3).multiply(Long.MAX_VALUE)));
    }

    /**
     * Test constructing a matrix with a negative number of rows or columns.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDimensions() {
        new OpenMapRealMatrix(-1, 3);
        new OpenMapRealMatrix(2, -1);
    }

    /**
     * Test constructing a matrix with a zero number of rows or columns.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroDimensions() {
        new OpenMapRealMatrix(0, 3);
        new OpenMapRealMatrix(2, 0);
    }

}