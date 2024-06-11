package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.TransposeStrategy;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    /**
     * Test Cholesky decomposition of a valid symmetric positive definite matrix.
     */
    @Test
    public void testValidDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 2.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealVector det = cholesky.getDeterminant();
        Solver solver = cholesky.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = null;
        try {
            x = solver.solve(b);
        } catch (IllegalArgumentException e) {
            // ignore, test will fail if matrix is not square
        }
        assertNotNull(x);
        assertArrayEquals(new double[]{2.5, 1.0, 0.5}, x.toArray(), 0.0);
        assertArrayEquals(new double[][]{
                {1.41421356, -0.70710678, 0.0},
                {-0.70710678, 1.41421356, 0.70710678},
                {0.0, 0.70710678, 1.41421356}
        }, l.getData(), 0.0);
        assertArrayEquals(new double[][]{
                {1.41421356, 0.70710678, 0.0},
                {0.70710678, 1.41421356, 0.70710678},
                {0.0, 0.70710678, 1.41421356}
        }, lt.getData(), 0.0);
        assertEquals(Math.sqrt(det.getEntry(0) * det.getEntry(0) * det.getEntry(1) * det.getEntry(1) * det.getEntry(2) * det.getEntry(2)), det.getEntry(0), 1e-12);
    }

    /**
     * Test Cholesky decomposition of a valid symmetric positive definite matrix with zero diagonal.
     */
    @Test
    public void testZeroDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 1.0, 0.0},
                {1.0, 0.0, 1.0},
                {0.0, 1.0, 0.0}
        });
        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected IllegalArgumentException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a non-symmetric matrix.
     */
    @Test
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 1.0}
        });
        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a non-square matrix.
     */
    @Test
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0}
        });
        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a singular matrix.
     */
    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 1.0}
        });
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        if (svd.getRank() < matrix.getRowDimension()) {
            try {
                new CholeskyDecompositionImpl(matrix);
                fail("Expected NotPositiveDefiniteMatrixException or NotSymmetricMatrixException");
            } catch (NotPositiveDefiniteMatrixException | NotSymmetricMatrixException e) {
                // expected
            }
        }
    }

    /**
     * Test Cholesky decomposition of a diagonal matrix with positive diagonal elements.
     */
    @Test
    public void testDiagonalMatrixPositiveDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 2.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealVector det = cholesky.getDeterminant();
        Solver solver = cholesky.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = null;
        try {
            x = solver.solve(b);
        } catch (IllegalArgumentException e) {
            // ignore, test will fail if matrix is not square
        }
        assertNotNull(x);
        assertArrayEquals(new double[]{2.5, 0.0, 0.5}, x.toArray(), 0.0);
        assertArrayEquals(new double[][]{
                {Math.sqrt(2.0), 0.0, 0.0},
                {0.0, Math.sqrt(1.0), 0.0},
                {0.0, 0.0, Math.sqrt(2.0)}
        }, l.getData(), 0.0);
        assertArrayEquals(new double[][]{
                {Math.sqrt(2.0), 0.0, 0.0},
                {0.0, Math.sqrt(1.0), 0.0},
                {0.0, 0.0, Math.sqrt(2.0)}
        }, lt.getData(), 0.0);
        assertEquals(Math.sqrt(det.getEntry(0) * det.getEntry(1) * det.getEntry(2)), det.getEntry(0), 1e-12);
    }

    /**
     * Test Cholesky decomposition of a diagonal matrix with zero diagonal elements.
     */
    @Test
    public void testDiagonalMatrixZeroDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });
        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    /**
     * Test Cholesky decomposition of a diagonal matrix with negative diagonal elements.
     */
    @Test
    public void testDiagonalMatrixNegativeDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 0.0, 0.0},
                {0.0, -1.0, 0.0},
                {0.0, 0.0, -1.0}
        });
        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

}