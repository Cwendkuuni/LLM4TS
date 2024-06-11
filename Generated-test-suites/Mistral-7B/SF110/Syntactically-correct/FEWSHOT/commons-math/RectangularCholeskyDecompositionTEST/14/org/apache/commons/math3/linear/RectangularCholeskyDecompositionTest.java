package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{}));
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(2, rank);
        assertNotNull(root);
        assertEquals(2, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, root.getData());
    }

    @Test
    public void testDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 4.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(2, rank);
        assertNotNull(root);
        assertEquals(2, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.0}, {0.0, 2.0}}, root.getData());
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-14);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNegativeDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-14);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testMatrixWithSwaps() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 1.0}, {2.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(2, rank);
        assertNotNull(root);
        assertEquals(2, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.5}, {0.0, 1.0}}, root.getData());
    }

    @Test
    public void testMatrixWithNegativeElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {-3.0, 4.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-14);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testMatrixWithRowEchelonForm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, {0.0, 0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}, root.getData());
    }

    @Test
    public void testLargeMatrix() {
        int size = 10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[size * size][size]);
        for (int i = 0; i < size * size; i++) {
            matrix.setEntry(i / size, i % size, FastMath.random());
        }
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(size, rank);
        assertNotNull(root);
        assertEquals(size, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
    }

    @Test
    public void testRoundingError() {
        double small = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0 + Precision.EPSILON}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // ignore
        }
        assertTrue(decomposition != null);
    }
}