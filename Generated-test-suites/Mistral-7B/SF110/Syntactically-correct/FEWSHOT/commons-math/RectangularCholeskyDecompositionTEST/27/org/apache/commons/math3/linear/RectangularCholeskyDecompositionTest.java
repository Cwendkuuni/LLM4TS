package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(3, rank);
            assertEquals(3, rootMatrix.getRowDimension());
            assertEquals(rank, rootMatrix.getColumnDimension());
            for (int i = 0; i < rank; i++) {
                for (int j = 0; j < rank; j++) {
                    assertEquals(Precision.isClose(1.0, rootMatrix.getEntry(i, j), 0.001), true);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not throw NonPositiveDefiniteMatrixException for positive definite matrix.");
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 0.0, 3.0}, {3.0, 3.0, 0.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should throw NonPositiveDefiniteMatrixException for singular matrix.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 3.0}, {3.0, 3.0, -4.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should throw NonPositiveDefiniteMatrixException for non-positive definite matrix.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixExceptionMessage() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 3.0}, {3.0, 3.0, -4.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should throw NonPositiveDefiniteMatrixException for non-positive definite matrix.");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-4.0, e.getValue(), 0.0);
            assertEquals(2, e.getRow());
            assertEquals(0.0, e.getSmall());
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            int rank = decomposition.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Should not throw NonPositiveDefiniteMatrixException for positive definite matrix.");
        }
    }
}