package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {

    /** Matrix dimensions. */
    private static final int ROWS = 3;
    private static final int COLUMNS = 4;

    /** Test matrix. */
    private OpenMapRealMatrix matrix;

    /** Test values. */
    private double[][] testValues = new double[][] {
            {0.0, 1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0, 7.0},
            {8.0, 9.0, 10.0, 11.0}
    };

    /**
     * Set up test matrix.
     */
    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(ROWS, COLUMNS);
        setValues(matrix, testValues);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 4);
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(4, matrix.getColumnDimension());
        Assert.assertTrue(matrix.isEmpty());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(testValues);
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);
        Assert.assertNotSame(original, copy);
        Assert.assertEquals(original, copy);
    }

    /**
     * Test add method.
     */
    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0, 3.0, 4.0},
                {5.0, 6.0, 7.0, 8.0},
                {9.0, 10.0, 11.0, 12.0}
        });
        OpenMapRealMatrix result = matrix.add(m);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(testValues);
        expected.add(m);
        Assert.assertEquals(expected, result);
        Assert.assertNotSame(matrix, result);
    }

    /**
     * Test subtract method.
     */
    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0, 3.0, 4.0},
                {5.0, 6.0, 7.0, 8.0},
                {9.0, 10.0, 11.0, 12.0}
        });
        OpenMapRealMatrix result = matrix.subtract(m);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(testValues);
        expected.subtract(m);
        Assert.assertEquals(expected, result);
        Assert.assertNotSame(matrix, result);
    }

    /**
     * Test multiply method.
     */
    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0},
                {3.0, 4.0}
        });
        OpenMapRealMatrix result = matrix.multiply(m);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new double[][] {
                {12.0, 15.0, 18.0, 21.0},
                {27.0, 32.0, 37.0, 42.0},
                {42.0, 49.0, 56.0, 63.0}
        });
        Assert.assertEquals(expected, result);
        Assert.assertNotSame(matrix, result);
    }

    /**
     * Test getEntry method.
     */
    @Test
    public void testGetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0, 3.0, 4.0},
                {5.0, 6.0, 7.0, 8.0},
                {9.0, 10.0, 11.0, 12.0}
        });
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                double expected = testValues[i][j];
                double value = matrix.getEntry(i, j);
                Assert.assertEquals(expected, value, 1e-14);
                if (i == 1 && j == 2) {
                    m.setEntry(i, j, 0.0);
                }
                value = m.getEntry(i, j);
                Assert.assertEquals(expected, value, 1e-14);
            }
        }
    }

    /**
     * Test setEntry method.
     */
    @Test
    public void testSetEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0, 3.0, 4.0},
                {5.0, 6.0, 7.0, 8.0},
                {9.0, 10.0, 11.0, 12.0}
        });
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                double value = Math.random() * 10.0;
                matrix.setEntry(i, j, value);
                Assert.assertEquals(value, matrix.getEntry(i, j), 1e-14);
                m.setEntry(i, j, value);
                Assert.assertEquals(value, m.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test addToEntry method.
     */
    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0, 3.0, 4.0},
                {5.0, 6.0, 7.0, 8.0},
                {9.0, 10.0, 11.0, 12.0}
        });
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                double value = Math.random() * 10.0;
                matrix.addToEntry(i, j, value);
                Assert.assertEquals(value + matrix.getEntry(i, j), matrix.getEntry(i, j), 1e-14);
                m.addToEntry(i, j, value);
                Assert.assertEquals(value + m.getEntry(i, j), m.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test multiplyEntry method.
     */
    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0, 3.0, 4.0},
                {5.0, 6.0, 7.0, 8.0},
                {9.0, 10.0, 11.0, 12.0}
        });
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                double value = Math.random() * 10.0;
                matrix.multiplyEntry(i, j, value);
                Assert.assertEquals(value * matrix.getEntry(i, j), matrix.getEntry(i, j), 1e-14);
                m.multiplyEntry(i, j, value);
                Assert.assertEquals(value * m.getEntry(i, j), m.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test copy method.
     */
    @Test
    public void testCopy() {
        OpenMapRealMatrix result = matrix.copy();
        Assert.assertEquals(matrix, result);
        Assert.assertNotSame(matrix, result);
    }

    /**
     * Test createMatrix method.
     */
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix result = matrix.createMatrix(3, 4);
        Assert.assertEquals(3, result.getRowDimension());
        Assert.assertEquals(4, result.getColumnDimension());
        Assert.assertTrue(result.isEmpty());
    }

    /**
     * Test dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testAddWithIncompatibleDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0},
                {3.0, 4.0}
        });
        matrix.add(m);
    }

    /**
     * Test multiply with incompatible dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithIncompatibleDimensions() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(new double[][] {
                {1.0, 2.0},
                {3.0, 4.0}
        });
        matrix.multiply(m);
    }

    /**
     * Test setEntry with negative value.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSetEntryWithNegativeValue() {
        matrix.setEntry(0, 0, -1.0);
    }

    /**
     * Test addToEntry with large value.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddToEntryWithLargeValue() {
        matrix.addToEntry(0, 0, 1e100);
    }

    /**
     * Test multiplyEntry with large value.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplyEntryWithLargeValue() {
        matrix.multiplyEntry(0, 0, 1e100);
    }

    /**
     * Test multiplyEntry with negative value.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testMultiplyEntryWithNegativeValue() {
        matrix.multiplyEntry(0, 0, -1.0);
    }

    /**
     * Set values in matrix.
     *
     * @param matrix matrix to set values in
     * @param values values to set
     */
    private void setValues(OpenMapRealMatrix matrix, double[][] values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                matrix.setEntry(i, j, values[i][j]);
            }
        }
    }
}