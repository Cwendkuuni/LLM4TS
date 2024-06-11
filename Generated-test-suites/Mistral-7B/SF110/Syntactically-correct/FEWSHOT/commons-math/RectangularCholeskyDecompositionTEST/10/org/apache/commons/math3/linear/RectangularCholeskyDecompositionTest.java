package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[0][0]));
            fail("Exception expected.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {0.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(1.0, Precision.doubleEquals(root.getEntry(0, 0), 1.0, 1e-16));
        assertNull(root.getRowDimension() - root.getColumnDimension());
        assertEquals(1, decomposition.getRank());
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Exception expected.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 4.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(1.0, Precision.doubleEquals(root.getEntry(0, 0), 1.0, 1e-16));
        assertEquals(0.0, Precision.doubleEquals(root.getEntry(1, 1), 4.0, 1e-16));
        assertNull(root.getRowDimension() - root.getColumnDimension());
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testSymmetricPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 2.0}, {2.0, 2.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(1.0, Precision.doubleEquals(root.getEntry(0, 0), 1.4142135625, 1e-16));
        assertEquals(1.0, Precision.doubleEquals(root.getEntry(1, 1), 1.4142135625, 1e-16));
        assertEquals(1.0, Precision.doubleEquals(root.getEntry(2, 2), 1.7320508076, 1e-16));
        assertNull(root.getRowDimension() - root.getColumnDimension());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testNonsymmetricPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(1.0, Precision.doubleEquals(root.getEntry(0, 0), 1.4142135625, 1e-16));
        assertEquals(0.0, Precision.doubleEquals(root.getEntry(1, 0), 0.0, 1e-16));
        assertEquals(1.0, Precision.doubleEquals(root.getEntry(1, 1), 1.7320508076, 1e-16));
        assertNull(root.getRowDimension() - root.getColumnDimension());
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testNonPositiveDefiniteMatrixSmallDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, -1.0, 2.0}, {2.0, 2.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-16);
            fail("Exception expected.");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().contains("1.0E-16"));
            assertTrue(e.getMessage().contains("rank: 2"));
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixNegativeDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, -1.0, 2.0}, {2.0, 2.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, -1.0);
            fail("Exception expected.");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().contains("negative diagonal element"));
            assertTrue(e.getMessage().contains("rank: 2"));
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixNegativeSubDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, -1.0, -1.0}, {2.0, 2.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-16);
            fail("Exception expected.");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().contains("1.0E-16"));
            assertTrue(e.getMessage().contains("rank: 2"));
        }
    }
}