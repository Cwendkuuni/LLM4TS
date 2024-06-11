package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the positive definite matrix with default thresholds
     */
    @Test
    public void testPositiveDefinite() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 2.0},
                                                                           {2.0, 3.0, 2.0},
                                                                           {2.0, 2.0, 3.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix L = decomposition.getL();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                             {1.0, 1.4142135625, 0.0},
                                                                             {1.0, 0.8164965809, 1.7320508076}});
        Assert.assertArrayEquals(expectedL.getData(), L.getData(), 0.0);
    }

    /**
     * Test the non-positive definite matrix with default thresholds
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                           {0.0, 0.0, 0.0},
                                                                           {0.0, 0.0, -1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the non-square matrix with default thresholds
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquare() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the non-symmetric matrix with default thresholds
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetric() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the positive definite matrix with custom thresholds
     */
    @Test
    public void testPositiveDefiniteCustomThresholds() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 2.0},
                                                                           {2.0, 3.0, 2.0},
                                                                           {2.0, 2.0, 3.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-10, 1e-15);
        RealMatrix L = decomposition.getL();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                             {1.0, 1.4142135625, 0.0},
                                                                             {1.0, 0.8164965809, 1.7320508076}});
        Assert.assertArrayEquals(expectedL.getData(), L.getData(), 1e-15);
    }

    /**
     * Test the non-positive definite matrix with custom thresholds
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteCustomThresholds() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                           {0.0, 0.0, 0.0},
                                                                           {0.0, 0.0, -1.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-20, 1e-20);
    }

    /**
     * Test the non-square matrix with custom thresholds
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareCustomThresholds() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-20, 1e-20);
    }

    /**
     * Test the non-symmetric matrix with custom thresholds
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricCustomThresholds() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-20, 1e-20);
    }

    /**
     * Test the LU decomposition solver
     */
    @Test
    public void testSolver() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, -3.0},
                                                                           {1.0, 4.0, 2.0},
                                                                           {-3.0, 2.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition.getSolver().solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{2.0, -1.0, 1.0});
        Assert.assertArrayEquals(expectedX.getData(), x.getData(), 0.0);
    }

    /**
     * Test the LU decomposition solver with non-square matrices
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolverNonSquare() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition.getSolver().solve(b);
    }

    /**
     * Test the LU decomposition solver with singular matrices
     */
    @Test(expected = InvalidMatrixException.class)
    public void testSolverSingular() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                           {0.0, 0.0, 0.0},
                                                                           {0.0, 0.0, 0.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition.getSolver().solve(b);
    }

}