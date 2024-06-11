package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test CholeskyDecompositionImpl construction with correct input
     */
    @Test
    public void testCorrectInput() {
        double[][] testMatrix = {{2.0, 1.0, 1.0, 1.0},
                                {1.0, 2.0, 1.0, 1.0},
                                {1.0, 1.0, 2.0, 1.0},
                                {1.0, 1.0, 1.0, 2.0}};
        try {
            RealMatrix matrix = new Array2DRowRealMatrix(testMatrix);
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            RealMatrix L = cholesky.getL();
            RealMatrix LT = cholesky.getLT();
            RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.41421356, 0.0, 0.0, 0.0},
                                                                              {0.0, 1.41421356, 0.0, 0.0},
                                                                              {0.0, 0.0, 1.41421356, 0.0},
                                                                              {0.0, 0.0, 0.0, 1.41421356}});
            RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.41421356, 1.41421356, 1.41421356, 1.41421356},
                                                                                {0.0, 0.90630929, 0.90630929, 0.90630929},
                                                                                {0.0, 0.90630929, 0.90630929, 0.90630929},
                                                                                {0.0, 0.90630929, 0.90630929, 0.90630929}});
            assertArrayEquals(expectedL.getData(), L.getData(), 0.0);
            assertArrayEquals(expectedLT.getData(), LT.getData(), 0.0);
        } catch (NotPositiveDefiniteMatrixException e) {
            // Should not reach here since the input matrix is positive definite
        } catch (NotSymmetricMatrixException e) {
            // Should not reach here since the input matrix is symmetric
        }
    }

    /**
     * Test CholeskyDecompositionImpl construction with incorrect input - non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testIncorrectInputNonSquare() {
        double[][] testMatrix = {{2.0, 1.0},
                                {1.0, 2.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testMatrix);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl construction with incorrect input - not symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testIncorrectInputNotSymmetric() {
        double[][] testMatrix = {{2.0, 1.0},
                                {1.0, 2.0},
                                {1.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testMatrix);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl construction with incorrect input - not positive definite matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testIncorrectInputNotPositiveDefinite() {
        double[][] testMatrix = {{2.0, 1.0},
                                {1.0, -1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(testMatrix);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test getL() method
     */
    @Test
    public void testGetL() {
        double[][] testMatrix = {{2.0, 1.0, 1.0, 1.0},
                                {1.0, 2.0, 1.0, 1.0},
                                {1.0, 1.0, 2.0, 1.0},
                                {1.0, 1.0, 1.0, 2.0}};
        RealMatrix matrix;
        try {
            matrix = new Array2DRowRealMatrix(testMatrix);
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            RealMatrix L = cholesky.getL();
            RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.41421356, 0.0, 0.0, 0.0},
                                                                              {0.0, 1.41421356, 0.0, 0.0},
                                                                              {0.0, 0.0, 1.41421356, 0.0},
                                                                              {0.0, 0.0, 0.0, 1.41421356}});
            assertArrayEquals(expectedL.getData(), L.getData(), 0.0);
        } catch (NotPositiveDefiniteMatrixException e) {
            // Should not reach here since the input matrix is positive definite
        } catch (NotSymmetricMatrixException e) {
            // Should not reach here since the input matrix is symmetric
        }
    }

    /**
     * Test getLT() method
     */
    @Test
    public void testGetLT() {
        double[][] testMatrix = {{2.0, 1.0, 1.0, 1.0},
                                {1.0, 2.0, 1.0, 1.0},
                                {1.0, 1.0, 2.0, 1.0},
                                {1.0, 1.0, 1.0, 2.0}};
        RealMatrix matrix;
        try {
            matrix = new Array2DRowRealMatrix(testMatrix);
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            RealMatrix LT = cholesky.getLT();
            RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.41421356, 1.41421356, 1.41421356, 1.41421356},
                                                                                {0.90630929, 0.90630929, 0.90630929, 0.90630929},
                                                                                {0.90630929, 0.90630929, 0.90630929, 0.90630929},
                                                                                {0.90630929, 0.90630929, 0.90630929, 0.90630929}});
            assertArrayEquals(expectedLT.getData(), LT.getData(), 0.0);
        } catch (NotPositiveDefiniteMatrixException e) {
            // Should not reach here since the input matrix is positive definite
        } catch (NotSymmetricMatrixException e) {
            // Should not reach here since the input matrix is symmetric
        }
    }

    /**
     * Test getDeterminant() method
     */
    @Test
    public void testGetDeterminant() {
        double[][] testMatrix = {{2.0, 1.0, 1.0, 1.0},
                                {1.0, 2.0, 1.0, 1.0},
                                {1.0, 1.0, 2.0, 1.0},
                                {1.0, 1.0, 1.0, 2.0}};
        RealMatrix matrix;
        try {
            matrix = new Array2DRowRealMatrix(testMatrix);
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            double determinant = cholesky.getDeterminant();
            assertEquals(8.0, determinant, 0.0);
        } catch (NotPositiveDefiniteMatrixException e) {
            // Should not reach here since the input matrix is positive definite
        } catch (NotSymmetricMatrixException e) {
            // Should not reach here since the input matrix is symmetric
        }
    }

    /**
     * Test getSolver() method
     */
    @Test
    public void testGetSolver() {
        double[][] testMatrix = {{2.0, 1.0, 1.0, 1.0},
                                {1.0, 2.0, 1.0, 1.0},
                                {1.0, 1.0, 2.0, 1.0},
                                {1.0, 1.0, 1.0, 2.0}};
        RealMatrix matrix;
        try {
            matrix = new Array2DRowRealMatrix(testMatrix);
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = cholesky.getSolver();
            RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0, 4.0});
            RealVector x = solver.solve(b);
            RealVector expectedX = new RealVectorImpl(new double[]{0.43229554, -0.13823128, -0.11009683, 0.23432295});
            assertArrayEquals(expectedX.getData(), x.getData(), 0.0);
        } catch (NotPositiveDefiniteMatrixException e) {
            // Should not reach here since the input matrix is positive definite
        } catch (NotSymmetricMatrixException e) {
            // Should not reach here since the input matrix is symmetric
        }
    }

    /**
     * Test Solver class
     */
    @Test
    public void testSolver() {
        double[][] testMatrix = {{2.0, 1.0, 1.0, 1.0},
                                {1.0, 2.0, 1.0, 1.0},
                                {1.0, 1.0, 2.0, 1.0},
                                {1.0, 1.0, 1.0, 2.0}};
        double[][] b = {{1.0, 1.0, 1.0, 1.0},
                         {-1.0, -1.0, -1.0, -1.0},
                         {2.0, 2.0, 2.0, 2.0},
                         {3.0, 3.0, 3.0, 3.0}};
        double[][] expectedX = {{0.43229554, -0.13823128, -0.11009683, 0.23432295},
                               {-1.38231282, 0.43229554, -0.11009683, -0.11009683},
                               {1.11009683, -0.43229554, 0.43229554, 0.43229554},
                               {0.23432295, 0.11009683, 0.11009683, 0.43229554}};
        RealMatrix matrix;
        try {
            matrix = new Array2DRowRealMatrix(testMatrix);
            CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
            Solver solver = cholesky.getSolver();
            RealVector x;
            for (int i = 0; i < 4; i++) {
                x = solver.solve(new RealVectorImpl(b[i]));
                assertArrayEquals(expectedX[i], x.getData(), 0.0);
            }
        } catch (NotPositiveDefiniteMatrixException e) {
            // Should not reach here since the input matrix is positive definite
        } catch (NotSymmetricMatrixException e) {
            // Should not reach here since the input matrix is symmetric
        }
    }

}