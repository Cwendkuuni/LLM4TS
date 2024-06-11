package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImporter;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.SingularValueException;
import org.junit.Test;

/**
 * Test cases for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test Cholesky decomposition of a positive definite matrix.
     */
    @Test
    public void testPositiveDefiniteMatrix() {
        final double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };

        final RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData, false);
        final CholeskyDecompositionImpl decomposed = new CholeskyDecompositionImpl(inputMatrix);

        final RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                               {2.0, 1.5, 0.0},
                                                                               {3.0, 2.0, 2.3162277660168377}});
        final RealMatrix expectedLT = MatrixUtils.transpose(expectedL);

        assertEquals("L matrix not correct", expectedL, decomposed.getL());
        assertEquals("LT matrix not correct", expectedLT, decomposed.getLT());
        assertEquals("Determinant not correct", 15.0, decomposed.getDeterminant(), 1e-14);

        final RealVector b = new Array2DRowRealVector(new double[][]{{1.0}, {1.0}, {1.0}});
        final RealVector x = decomposed.getSolver().solve(b);
        RealVector expectedX = new Array2DRowRealVector(new double[][]{{3.0}, {2.0}, {1.0}});
        assertEquals("The solution is not correct", expectedX, x);
    }

    /**
     * Test Cholesky decomposition of a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        final double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0}
        };

        final RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(inputMatrix);
    }

    /**
     * Test Cholesky decomposition of a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() {
        final double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0},
            {3.0, 6.0, 10.0}
        };

        final RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(inputMatrix, 1e-14, 1e-10);
    }

    /**
     * Test Cholesky decomposition of a non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        final double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, -1.0, 6.0},
            {3.0, 6.0, -10.0}
        };

        final RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(inputMatrix, 1e-14, 1e-10);
    }

    /**
     * Test Cholesky decomposition of a singular matrix.
     */
    @Test(expected = SingularValueException.class)
    public void testSingularMatrix() {
        final double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 2.0, 3.0},
            {3.0, 3.0, 0.0}
        };

        final RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(inputMatrix, 1e-14, 1e-10);
    }

}