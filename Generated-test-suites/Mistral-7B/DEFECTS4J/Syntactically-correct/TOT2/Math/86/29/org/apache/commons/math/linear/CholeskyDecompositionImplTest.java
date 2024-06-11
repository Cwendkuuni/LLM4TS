package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.E;

import org.junit.Test;

public class CholeskyDecompositionImplTest {

    /**
     * Test that the Cholesky decomposition of a square matrix is correctly computed.
     */
    @Test
    public void testBasicDecomposition() {
        double[][] testData = {{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        assertNotNull(l);
        assertArrayEquals(testData, l.transpose().multiply(l));
    }

    /**
     * Test that the Cholesky decomposition of a symmetric positive definite matrix with null diagonal
     * elements fails.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNullDiagonal() {
        double[][] testData = {{1.0, 2.0, 2.0}, {2.0, 0.0, 3.0}, {2.0, 3.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testData);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test that the Cholesky decomposition of a symmetric positive definite matrix with non-zero but
     * small off-diagonal elements fails.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() {
        double[][] testData = {{1.0, 1.0e-15, 2.0}, {1.0e-15, 1.0, 3.0}, {2.0, 3.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testData);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test that the Cholesky decomposition of a non-square matrix fails.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        double[][] testData = {{1.0}, {1.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testData);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test that the Cholesky decomposition returns the correct determinant.
     */
    @Test
    public void testDeterminant() {
        double[][] testData = {{2.0, 1.0}, {1.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        assertEquals(E.POWER_OF_E(1.5), decomposition.getDeterminant(), 1e-14);
    }

    /**
     * Test that the solver correctly solves the equation Ax = b.
     */
    @Test
    public void testSolver() {
        double[][] testData = {{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new Array2DRowRealVector(new double[]{-1.0, 2.0});
        RealVector x = decomposition.getSolver().solve(b);
        assertEquals(-0.5, x.getEntry(0), 1e-14);
        assertEquals(0.5, x.getEntry(1), 1e-14);
    }

    /**
     * Test that the solver correctly solves the equation Ax = b for a singular matrix.
     */
    @Test(expected = SingularValueDecomposition.SingularMatrixException.class)
    public void testSingularSolver() {
        double[][] testData = {{1.0, 2.0}, {2.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new Array2DRowRealVector(new double[]{-1.0, 2.0});
        decomposition.getSolver().solve(b);
    }

}