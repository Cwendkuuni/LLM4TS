package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 0.0},
                {2.0, 4.0, 2.0},
                {0.0, 2.0, 3.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(3, rank);
            assertEquals(3, rootMatrix.getRowDimension());
            assertEquals(rank, rootMatrix.getColumnDimension());

            for (int i = 0; i < rank; i++) {
                for (int j = 0; j < rank; j++) {
                    assertEquals(Precision.SAFE_EPSILON, rootMatrix.getEntry(i, j), 0.0, 0.0);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Decomposition exception not expected.");
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {-1.0, 2.0},
                {2.0, -3.0}
        });

        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testRankThree() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(3, rank);
            assertEquals(3, rootMatrix.getRowDimension());
            assertEquals(rank, rootMatrix.getColumnDimension());

            for (int i = 0; i < rank; i++) {
                for (int j = 0; j < rank; j++) {
                    assertEquals(0.0, rootMatrix.getEntry(i, j), 0.0, 0.0);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Decomposition should have succeeded.");
        }
    }

    @Test
    public void testRankTwo() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {2.0, 3.0},
                {3.0, 5.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(2, rank);
            assertEquals(2, rootMatrix.getRowDimension());
            assertEquals(rank, rootMatrix.getColumnDimension());

            for (int i = 0; i < rank; i++) {
                for (int j = 0; j < rank; j++) {
                    assertEquals(0.0, rootMatrix.getEntry(i, j), 0.0, 0.0);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Decomposition should have succeeded.");
        }
    }

    @Test
    public void testRankOne() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[]{1.0});

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(1, rank);
            assertEquals(1, rootMatrix.getRowDimension());
            assertEquals(rank, rootMatrix.getColumnDimension());

            assertEquals(0.0, rootMatrix.getEntry(0, 0), 0.0, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Decomposition should have succeeded.");
        }
    }

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{});

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(0, rank);
            assertEquals(0, rootMatrix.getRowDimension());
            assertEquals(rank, rootMatrix.getColumnDimension());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Decomposition should have succeeded.");
        }
    }
}