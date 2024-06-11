package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.MatrixUtils;
import org.apache.commons.math3.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import org.junit.Test;

/**
 * Tests for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a positive definite matrix.
     */
    @Test
    public void testDecompositionPositiveDefinite() {
        final double[][] a = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 7.0 }, { 3.0, 7.0, 13.0 } };
        final RealMatrix matrix = new Array2DRowRealMatrix(a);
        final double small = 1e-5;
        final RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
        final RealMatrix root = decomp.getRootMatrix();

        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());

        final double[][] expected = { { 1.0, 0.0, 0.0 }, { 0.5, 2.1622776601683796, 0.0 }, { 0.0, 1.1612903225986923, 2.734621132400556 } };
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assertEquals(expected[i][j], root.getEntry(i, j), 1e-6);
            }
        }
    }

    /**
     * Test the decomposition of a positive semidefinite matrix with rank < code>2</code>.
     */
    @Test
    public void testDecompositionPositiveSemidefiniteRank2() {
        final double[][] a = { { 1.0, 2.0, 3.0, 0.0 }, { 2.0, 5.0, 7.0, 0.0 }, { 3.0, 7.0, 13.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 } };
        final RealMatrix matrix = new Array2DRowRealMatrix(a);
        final double small = 1e-5;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test the decomposition of a positive semidefinite matrix with rank < code>1</code>.
     */
    @Test
    public void testDecompositionPositiveSemidefiniteRank1() {
        final double[][] a = { { 1.0, 2.0, 0.0 }, { 2.0, 5.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        final RealMatrix matrix = new Array2DRowRealMatrix(a);
        final double small = 1e-5;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test the decomposition of a non-symmetric matrix.
     */
    @Test
    public void testDecompositionNonSymmetric() {
        final double[][] a = { { 1.0, 2.0 }, { 3.0, 4.0 } };
        final RealMatrix matrix = new Array2DRowRealMatrix(a);
        final double small = 1e-5;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test the decomposition of a singular matrix.
     */
    @Test
    public void testDecompositionSingular() {
        final double[][] a = { { 1.0, 2.0, 0.0 }, { 2.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0 } };
        final RealMatrix matrix = new Array2DRowRealMatrix(a);
        final double small = 1e-5;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
    }

    /**
     * Test the getRank method.
     */
    @Test
    public void testGetRank() {
        final double[][] a = { { 1.0, 2.0, 3.0 }, { 2.0, 5.0, 7.0 }, { 3.0, 7.0, 13.0 } };
        final RealMatrix matrix = new Array2DRowRealMatrix(a);
        final double small = 1e-5;
        final RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);

        assertEquals(3, decomp.getRank());
    }

    /**
     * Test the getRank method with a singular matrix.
     */
    @Test
    public void testGetRankSingular() {
        final double[][] a = { { 1.0, 2.0, 0.0 }, { 2.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0 } };
        final RealMatrix matrix = new Array2DRowRealMatrix(a);
        final double small = 1e-5;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
        assertNull(new RectangularCholeskyDecomposition(matrix, small).getRank());
    }

    /**
     * Test the getRank method with a matrix of rank < code>0</code>.
     */
    @Test
    public void testGetRankZero() {
        final double[][] a = {};
        final RealMatrix matrix = new Array2DRowRealMatrix(a);
        final double small = 1e-5;

        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, small));
        assertNull(new RectangularCholeskyDecomposition(matrix, small).getRank());
    }
}