
package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        // Check that the root matrix is correct
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.6, 0.8090169943749475, 0.2797053162641506}, {0.2797053162641506, 0.8090169943749475, 0.6}});
        assertMatrixEquals(expectedRoot, root, 1e-9);
        // Test with a small eigenvalue
        RealMatrix matrixSmallEigenvalue = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 0.0001}});
        try {
            new RectangularCholeskyDecomposition(matrixSmallEigenvalue, 0.0001);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        // Test with empty matrix
        RealMatrix emptyMatrix = MatrixUtils.createRealMatrix(new double[][]{{}, {}});
        try {
            new RectangularCholeskyDecomposition(emptyMatrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        // Test with a singular matrix
        RealMatrix singularMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(singularMatrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        // Test with a diagonal matrix
        RealMatrix diagonalMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decompositionDiagonal = new RectangularCholeskyDecomposition(diagonalMatrix);
        RealMatrix rootDiagonal = decompositionDiagonal.getRootMatrix();
        int rankDiagonal = decompositionDiagonal.getRank();
        assertEquals(2, rankDiagonal);
        // Check that the root matrix is correct
        RealMatrix expectedRootDiagonal = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertMatrixEquals(expectedRootDiagonal, rootDiagonal);
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        // Non-positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, -1.0, 0.0}, {-1.0, 1.0, 0.0}, {0.0, 0.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
        // Test with a non-square matrix
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RectangularCholeskyDecomposition decompositionNS = null;
        try {
            decompositionNS = new RectangularCholeskyDecomposition(nonSquareMatrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Ignore
        }
        assertNull(decompositionNS);
    }

    @Test
    public void testGetRootMatrixAndRank() {
        // Positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        // Check that the root matrix is correct
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.6, 0.8090169943749475, 0.2797053162641506}, {0.2797053162641506, 0.8090169943749475, 0.6}});
        assertMatrixEquals(expectedRoot, root, 1e-9);
        // Test with a non-square matrix
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RectangularCholeskyDecomposition decompositionNS = null;
        try {
            decompositionNS = new RectangularCholeskyDecomposition(nonSquareMatrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Ignore
        }
        assertNull(decompositionNS);
    }

    private void assertMatrixEquals(RealMatrix expected, RealMatrix actual, double precision) {
        for (int i = 0; i < expected.getRowDimension(); i++) {
            for (int j = 0; j < expected.getColumnDimension(); j++) {
                double difference = Precision.doubleValue(expected.getEntry(i, j)) - Precision.doubleValue(actual.getEntry(i, j));
                assertTrue("Matrix elements not equal: difference = " + difference, Precision.abs(difference) <= precision);
            }
        }
    }
}
