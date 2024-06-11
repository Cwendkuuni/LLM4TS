package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test rectangular Cholesky decomposition of a positive definite matrix.
     */
    @Test
    public void testDecompositionOfPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 2.0, 3.0},
            {2.0, 3.0, 4.0}
        });
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposer.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (i == j) {
                        assertEquals(Precision.safeEQ(Math.sqrt(matrix.getEntry(i, i)), root.getEntry(i, i)), true);
                    } else {
                        assertEquals(Precision.safeEQ(0.0, root.getEntry(i, j)), true);
                    }
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test rectangular Cholesky decomposition of a positive semidefinite matrix.
     */
    @Test
    public void testDecompositionOfPositiveSemidefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 0.5, 3.0},
            {2.0, 3.0, 4.0}
        });
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.5);
            RealMatrix root = decomposer.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (i == j) {
                        assertTrue(Precision.safeGT(matrix.getEntry(i, i), 0.0));
                        assertTrue(Precision.safeGT(root.getEntry(i, i), 0.0));
                        assertEquals(Precision.safeEQ(Math.sqrt(matrix.getEntry(i, i)), root.getEntry(i, i)), true);
                    } else {
                        assertEquals(Precision.SAFE_EQUALS, root.getEntry(i, j), 0.0);
                    }
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

    /**
     * Test rectangular Cholesky decomposition of a singular matrix.
     */
    @Test
    public void testDecompositionOfSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 0.0, 3.0},
            {2.0, 3.0, 0.0}
        });
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test rectangular Cholesky decomposition with negative diagonal elements.
     */
    @Test
    public void testDecompositionWithNegativeDiagonalElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {-1.0, 2.0, 2.0},
            {2.0, -1.0, 3.0},
            {2.0, 3.0, -1.0}
        });
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

}