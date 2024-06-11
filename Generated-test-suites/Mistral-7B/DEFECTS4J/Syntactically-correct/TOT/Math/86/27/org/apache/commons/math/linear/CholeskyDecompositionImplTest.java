package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.MatrixUtils.Entry;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.InvalidMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Verify that Cholesky decomposition of a symmetric positive definite matrix
     * is correct.
     */
    @Test
    public void testCholeskyDecomposition() {
        final double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.5},
            {0.5, 1.5, 3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();

        // check that L is lower triangular
        for (int i = 0; i < l.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0.0, l.getEntry(i, j).getValue(), 1e-15);
            }
        }

        // check that LT is symmetric
        RealMatrix ltTranspose = lt.transpose();
        for (int i = 0; i < lt.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0.0, lt.getEntry(i, j).getValue(), 1e-15);
                assertEquals(0.0, ltTranspose.getEntry(j, i).getValue(), 1e-15);
            }
        }

        // check that LT * L = I
        RealMatrix productLL = lt.multiply(l);
        RealMatrix identity = MatrixUtils.createIdentityMatrix(3);
        for (int i = 0; i < productLL.getRowDimension(); i++) {
            for (int j = 0; j < productLL.getColumnDimension(); j++) {
                if (i == j) {
                    assertEquals(1.0, productLL.getEntry(i, j).getValue(), 1e-15);
                } else {
                    assertEquals(0.0, productLL.getEntry(i, j).getValue(), 1e-15);
                }
            }
        }

        // check that L * LT = I
        RealMatrix productLLT = l.multiply(lt);
        for (int i = 0; i < productLLT.getRowDimension(); i++) {
            for (int j = 0; j < productLLT.getColumnDimension(); j++) {
                if (i == j) {
                    assertEquals(1.0, productLLT.getEntry(i, j).getValue(), 1e-15);
                } else {
                    assertEquals(0.0, productLLT.getEntry(i, j).getValue(), 1e-15);
                }
            }
        }

        // check that L is the inverse of LT
        RealMatrix productLTInv = l.inverse().multiply(lt);
        for (int i = 0; i < productLTInv.getRowDimension(); i++) {
            for (int j = 0; j < productLTInv.getColumnDimension(); j++) {
                assertEquals(1.0, productLTInv.getEntry(i, j).getValue(), 1e-15);
            }
        }

        // check that determinant is correct
        assertEquals(2.0 * Math.sqrt(2.0), cholesky.getDeterminant(), 1e-15);
    }

    /**
     * Verify that Cholesky decomposition of a non-square matrix throws an
     * exception.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testCholeskyNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        matrix = MatrixUtils.appendColumns(matrix, new Array2DRowRealMatrix(new Entry[][]{
            {1.0, 0.0},
            {0.0, 1.0},
            {0.0, 0.0}}));
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Verify that Cholesky decomposition of a non-symmetric matrix throws an
     * exception.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyNonSymmetricMatrix() {
        final double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.5},
            {0.5, 1.5, 3.0},
            {0.1, 0.1, 0.1}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Verify that Cholesky decomposition of a non-positive definite matrix throws
     * an exception.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyNonPositiveDefiniteMatrix() {
        final double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, -1.0, 1.5},
            {0.5, 1.5, -3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Verify that Cholesky decomposition of a diagonal matrix is equivalent to
     * LU decomposition.
     */
    @Test
    public void testCholeskyDiagonal() {
        final double[][] matrixData = {
            {1.0, 0.0, 0.0},
            {0.0, 2.0, 0.0},
            {0.0, 0.0, 3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix lu = LUDecomposition.decompose(matrix);

        // check that L is lower triangular and LT is upper triangular
        for (int i = 0; i < l.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0.0, l.getEntry(i, j).getValue(), 1e-15);
            }
        }
        for (int i = 0; i < lu.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0.0, lu.getEntry(i, j).getValue(), 1e-15);
            }
        }

        // check that L * U = I
        RealMatrix productLU = l.multiply(lu);
        RealMatrix identity = MatrixUtils.createIdentityMatrix(3);
        for (int i = 0; i < productLU.getRowDimension(); i++) {
            for (int j = 0; j < productLU.getColumnDimension(); j++) {
                if (i == j) {
                    assertEquals(1.0, productLU.getEntry(i, j).getValue(), 1e-15);
                } else {
                    assertEquals(0.0, productLU.getEntry(i, j).getValue(), 1e-15);
                }
            }
        }

        // check that LT * L = I
        RealMatrix productLTLL = lt.transpose().multiply(l);
        for (int i = 0; i < productLTLL.getRowDimension(); i++) {
            for (int j = 0; j < productLTLL.getColumnDimension(); j++) {
                if (i == j) {
                    assertEquals(1.0, productLTLL.getEntry(i, j).getValue(), 1e-15);
                } else {
                    assertEquals(0.0, productLTLL.getEntry(i, j).getValue(), 1e-15);
                }
            }
        }

        // check that determinant is correct
        assertEquals(1.0 * 2.0 * 3.0, cholesky.getDeterminant(), 1e-15);
        assertEquals(1.0 * 2.0 * 3.0, LUDecomposition.getDeterminant(matrix), 1e-15);
    }

    /**
     * Verify that Cholesky decomposition solves the equation A * X = B.
     */
    @Test
    public void testCholeskySolve() {
        final double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.5},
            {0.5, 1.5, 3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        double[] b = {1.0, 2.0, 3.0};
        RealVector solution = cholesky.getSolver().solve(new Array2DRowRealMatrix(b));
        RealVector expected = new RealVectorImpl(new double[]{-0.5, 1.0, -1.5});

        // check that solution is correct
        assertArrayEquals(expected.getData(), solution.getData(), 1e-15);

        // check that solution is unique
        RealVector solution2 = cholesky.getSolver().solve(new Array2DRowRealMatrix(b).add(new Array2DRowRealMatrix(new double[]{-0.1, 0.0, 0.1})));
        assertEquals(solution.getClass(), solution2.getClass());
        assertArrayEquals(solution.getData(), solution2.getData(), 1e-15);
    }

    /**
     * Verify that Cholesky decomposition solves the equation A * X = B with a
     * vector b having more columns than the number of rows in the matrix.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testCholeskySolveVectorWithMoreColumns() {
        final double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.5},
            {0.5, 1.5, 3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        double[] b = {1.0, 2.0, 3.0, 4.0};
        cholesky.getSolver().solve(new Array2DRowRealMatrix(b));
    }

}