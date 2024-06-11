package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testPositiveDefiniteMatrixDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive definite matrix to decompose successfully.");
        }
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(rank, rootMatrix.getColumnDimension());
        for (int i = 0; i < rank; i++) {
            for (int j = 0; j < rank; j++) {
                // Check that the triangular matrix is correct
                if (i <= j) {
                    double expected = 0.0;
                    for (int k = 0; k < i; k++) {
                        expected += Math.pow(rootMatrix.getEntry(i, k), 2);
                    }
                    double actual = rootMatrix.getEntry(i, j);
                    assertEquals(expected, actual, Precision.SAFE_EPSILON);
                } else {
                    // The off-diagonal elements should be zero
                    assertEquals(0.0, rootMatrix.getEntry(i, j), Precision.SAFE_EPSILON);
                }
            }
        }
    }

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertNotNull(e.getMessage());
            assertEquals("The given matrix is singular, and cannot be decomposed using Cholesky Decomposition.", e.getMessage());
            assertFalse(decomposition instanceof RectangularCholeskyDecomposition);
            return;
        }
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
        assertEquals(3, rank);
        for (int i = 0; i < rank; i++) {
            for (int j = 0; j < rank; j++) {
                assertEquals(0.0, rootMatrix.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    double expected = 1.0;
                    assertEquals(expected, rootMatrix.getEntry(i, j), Precision.SAFE_EPSILON);
                } else {
                    assertEquals(0.0, rootMatrix.getEntry(i, j), Precision.SAFE_EPSILON);
                }
            }
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException to be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testSmallEigenvalueException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0 + 1e-10}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-8);
            fail("Expected NonPositiveDefiniteMatrixException to be thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

}