package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionWithNegativeValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 2.0, -1.0 } });
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testDecompositionWithSmallValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.1 }, { 0.1, 1.0 } });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1.0E-16);
        assertNotNull(decomposition.getRootMatrix());
        assertNotNull(decomposition.getRank());
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testDecompositionWithIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0 }, { 0.0, 1.0 } });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition.getRootMatrix());
        assertNotNull(decomposition.getRank());
        assertEquals(2, decomposition.getRank());

        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (i == j) {
                    assertEquals(1.0, root.getEntry(i, j), 1.0E-16);
                } else {
                    assertEquals(0.0, root.getEntry(i, j), 1.0E-16);
                }
            }
        }
    }

    @Test
    public void testDecompositionWithSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.5 }, { 0.5, 1.0 } });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition.getRootMatrix());
        assertNotNull(decomposition.getRank());
        assertEquals(2, decomposition.getRank());

        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(2, root.getRowDimension());
        assertEquals(2, root.getColumnDimension());

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (i == j) {
                    assertEquals(1.0, root.getEntry(i, j), 1.0E-16);
                } else {
                    assertEquals(0.5, root.getEntry(i, j), 1.0E-16);
                }
                assertEquals(root.getEntry(i, j), root.getEntry(j, i), 1.0E-16);
            }
        }
    }

    @Test
    public void testDecompositionWithLowRankMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.5 } });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition.getRootMatrix());
        assertNotNull(decomposition.getRank());
        assertEquals(3, decomposition.getRank());

        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());

        int[] indices = new int[3];
        for (int i = 0; i < 3; ++i) {
            indices[i] = i;
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < decomposition.getRank(); ++j) {
                assertEquals(0.0, root.getEntry(i, j), 1.0E-16);
            }
        }
        for (int i = 0; i < 3; ++i) {
            int k = indices[i];
            for (int j = 0; j < decomposition.getRank(); ++j) {
                int l = indices[j];
                if (i == j) {
                    double expected = Math.sqrt(matrix.getEntry(k, k));
                    assertEquals(expected, root.getEntry(i, j), 1.0E-16);
                } else {
                    double expected = (matrix.getEntry(k, l) / Math.sqrt(matrix.getEntry(k, k)));
                    assertEquals(expected, root.getEntry(i, j), 1.0E-16);
                }
            }
        }
        for (int k = 0; k < 3; ++k) {
            for (int i = 0; i < decomposition.getRank(); ++i) {
                int l = indices[i];
                if (k != i && k != l) {
                    assertEquals(0.0, root.getEntry(i, k), 1.0E-16);
                }
            }
        }
    }

    @Test
    public void testDecompositionWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0 }, { 0.0, 0.0 } });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testDecompositionWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0 }, { 0.0, 2.0 } });
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}