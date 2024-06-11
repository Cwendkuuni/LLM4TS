package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test that a call to {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a positive semidefinite matrix returns a valid decomposition and root matrix.
     */
    @Test
    public void testPositiveSemidefinite() {
        double[][] testMatrix = {
            {1.0, 1.0, 1.0},
            {1.0, 2.0, 2.0},
            {1.0, 2.0, 3.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testMatrix);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            double[][] expectedRoot = {
                {FastMath.sqrt(1.0), 0.0, 0.0},
                {0.5, FastMath.sqrt(2.0), 0.0},
                {0.5, 0.5, FastMath.sqrt(3.0)}
            };
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    assertEquals(expectedRoot[i][j], root.getEntry(i, j), Precision.EPSILON);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test that a call to {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a non-positive definite matrix throws an exception.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        double[][] testMatrix = {
            {1.0, 1.0, 1.0},
            {1.0, -1.0, 2.0},
            {1.0, 2.0, -1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testMatrix);
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test that a call to {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a singular matrix throws an exception.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingular() {
        double[][] testMatrix = {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 0.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testMatrix);
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test that a call to {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with a matrix in row echelon form returns a valid decomposition and root matrix.
     */
    @Test
    public void testRowEchelonForm() {
        double[][] testMatrix = {
            {1.0, 1.0, 1.0, 0.0},
            {1.0, 0.0, 1.0, 0.0},
            {0.0, 1.0, 1.0, 1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testMatrix);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            double[][] expectedRoot = {
                {FastMath.sqrt(1.0), 0.0, 0.0, 0.0},
                {0.0, FastMath.sqrt(1.0), 0.0, 0.0},
                {0.0, 0.0, FastMath.sqrt(1.0), 1.0}
            };
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    assertEquals(expectedRoot[i][j], root.getEntry(i, j), Precision.EPSILON);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}