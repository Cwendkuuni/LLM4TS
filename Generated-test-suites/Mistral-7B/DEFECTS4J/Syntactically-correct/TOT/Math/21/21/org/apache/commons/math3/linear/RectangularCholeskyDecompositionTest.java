package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test that the Cholesky decomposition of a positive definite matrix
     * returns a valid root matrix.
     */
    @Test
    public void testPositiveDefinite() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
            assertEquals("Root matrix not equal to the original matrix.", expected, root);
            assertTrue("Root matrix should be symmetric.", root.isSymmetric());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e);
        }
    }

    /**
     * Test that the Cholesky decomposition of a positive semidefinite matrix
     * with small diagonal elements returns a valid root matrix.
     */
    @Test
    public void testPositiveSemidefinite() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.5}, {0.5, 0.1}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomposition.getRootMatrix();
            RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.5}});
            assertEquals("Root matrix not equal to the expected matrix.", expected, root, 1e-9);
            assertTrue("Root matrix should be symmetric.", root.isSymmetric());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e);
        }
    }

    /**
     * Test that the Cholesky decomposition of a non positive definite matrix
     * throws an exception.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, -0.5}, {-0.5, 1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test that the Cholesky decomposition of a non symmetric matrix throws an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonSymmetric() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {1.0, 2.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test that the rank of the matrix is correct.
     */
    @Test
    public void testRank() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 0.5}, {0.5, 0.1}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            assertEquals("Unexpected rank.", 2, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e);
        }
    }

    /**
     * Test that the Cholesky decomposition of a matrix with zero diagonal
     * elements throws an exception.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testZeroDiagonal() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0.0, 0.5}, {0.5, 0.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }
}