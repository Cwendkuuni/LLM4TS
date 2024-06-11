package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.MathException;
import org.apache.commons.math3.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test with a valid positive semidefinite matrix.
     */
    @Test
    public void testValidDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    double expected = MathUtils.sqrt(matrix.getEntry(i, i));
                    assertEquals(expected, Precision.round(root.getEntry(i, j), 10), 1e-12);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new TestFailureException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test with a matrix that is not positive semidefinite.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testInvalidDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {-1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        });
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test with a small diagonal threshold.
     */
    @Test
    public void testSmallDiagonalThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 0.1}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    double expected = MathUtils.sqrt(matrix.getEntry(i, i));
                    assertEquals(expected, Precision.round(root.getEntry(i, j), 10), 1e-12);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            throw new TestFailureException("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test with a matrix where some diagonal elements are NaN.
     */
    @Test(expected = MathException.class)
    public void testNaNDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {Double.NaN, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        });
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test with a matrix where some off-diagonal elements are NaN.
     */
    @Test(expected = MathException.class)
    public void testNaNOffDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.5, Double.NaN},
            {0.5, 1.0, 0.5},
            {Double.NaN, 0.5, 1.0}
        });
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

}