package org.apache.commons.math.linear.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.exception.NotSymmetricMatrixException;
import org.junit.Test;

/**
 * Tests for the CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the Cholesky decomposition of a positive definite symmetric matrix
     * with positive diagonal elements.
     */
    @Test
    public void testPositiveDefiniteSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {3.0, 1.0, 0.0},
            {1.0, 5.0, 1.0},
            {0.0, 1.0, 7.0}
        });
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertEquals(matrix, l.multiply(lt));
        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[] {1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertEquals(new RealVectorImpl(new double[] {1.5, 0.25, 0.0}), x);
    }

    /**
     * Test the Cholesky decomposition of a positive definite symmetric matrix
     * with negative diagonal elements.
     */
    @Test
    public void testPositiveDefiniteSymmetricNegativeDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {-3.0, 1.0, 0.0},
            {1.0, 5.0, 1.0},
            {0.0, 1.0, 7.0}
        });
        assertThrows(NotPositiveDefiniteMatrixException.class, () -> new CholeskyDecompositionImpl(matrix));
    }

    /**
     * Test the Cholesky decomposition of a positive semi-definite symmetric matrix
     * with zero diagonal elements.
     */
    @Test
    public void testPositiveSemiDefiniteSymmetricZeroDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {3.0, 1.0, 0.0},
            {1.0, 0.0, 1.0},
            {0.0, 1.0, 0.0}
        });
        assertFalse(matrix.isPositiveDefinite());
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertNull(l);
        assertNull(lt);
    }

    /**
     * Test the Cholesky decomposition of a non-symmetric matrix.
     */
    @Test
    public void testNonSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {3.0, 1.0},
            {1.0, 5.0}
        });
        assertThrows(NotSymmetricMatrixException.class, () -> new CholeskyDecompositionImpl(matrix));
    }

    /**
     * Test the Cholesky decomposition of a non-square matrix.
     */
    @Test
    public void testNonSquare() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {3.0, 1.0},
            {1.0, 5.0},
            {0.0, 1.0}
        });
        assertThrows(NonSquareMatrixException.class, () -> new CholeskyDecompositionImpl(matrix));
    }

}