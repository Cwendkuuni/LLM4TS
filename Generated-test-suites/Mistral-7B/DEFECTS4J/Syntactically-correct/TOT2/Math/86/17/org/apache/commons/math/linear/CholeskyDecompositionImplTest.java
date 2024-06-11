package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Tests constructor with default thresholds: {@link #DEFAULT_RELATIVE_SYMMETRY_THRESHOLD} and {@link #DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD}.
     */
    @Test
    public void testDefaultThresholds() {
        // define test matrix
        double[][] testData = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.5},
            {0.5, 1.5, 2.5}
        };
        RealMatrix testMatrix = new Array2DRowRealMatrix(testData, false);

        // test CholeskyDecompositionImpl construction
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(testMatrix);

        // test getL()
        RealMatrix l = cholesky.getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                         {0.70710678118654758, 0.70710678118654758, 0.0},
                                                                         {-0.70710678118654758, -0.70710678118654758, 0.81649658092772602}}, false);
        assert ArrayEquals(expectedL.getData(), l.getData(), 0.0);

        // test getLT()
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.70710678118654758, -0.70710678118654758},
                                                                          {0.5, 0.70710678118654758, -0.52599212855592416},
                                                                          {0.5, 0.52599212855592416, 0.81649658092772602}}, false);
        assert ArrayEquals(expectedLT.getData(), lt.getData(), 0.0);

        // test getDeterminant()
        double det = cholesky.getDeterminant();
        assertEquals(2.5066282746310005, det, 1e-11);

        // test getSolver()
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0, 1.0}, false);
        RealVector x = cholesky.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{1.5, 1.5, 1.0}, false);
        assertArrayEquals(expectedX.getData(), x.getData(), 0.0);

    }

    /**
     * Tests constructor with custom thresholds.
     */
    @Test
    public void testCustomThresholds() {
        // define test matrix
        double[][] testData = {
            {1.0, 1e-10, 1e-10},
            {1e-10, 2.0, 1e-10},
            {1e-10, 1e-10, 2.0}
        };
        RealMatrix testMatrix = new Array2DRowRealMatrix(testData, false);

        // test CholeskyDecompositionImpl construction with custom thresholds
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(testMatrix, 1e-12, 1e-12);

        // test getL()
        RealMatrix l = cholesky.getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                         {1.0, 1.41421374105926468, 0.0},
                                                                         {1.0, 0.0, 0.81649658092772602}}, false);
        assert ArrayEquals(expectedL.getData(), l.getData(), 0.0);

        // test getLT()
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0},
                                                                          {1.41421374105926468, 1.41421374105926468, 0.0},
                                                                          {0.0, 0.0, 0.81649658092772602}}, false);
        assert ArrayEquals(expectedLT.getData(), lt.getData(), 0.0);

        // test getDeterminant()
        double det = cholesky.getDeterminant();
        assertEquals(1.41421374105926468, det, 1e-12);

        // test getSolver()
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0, 1.0}, false);
        RealVector x = cholesky.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{0.70710678118654758, 0.70710678118654758, 0.81649658092772602}, false);
        assertArrayEquals(expectedX.getData(), x.getData(), 0.0);
    }

    /**
     * Tests non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        double[][] testData = {
            {1.0, 0.5, 0.5},
            {1.0, 2.0, 1.5},
            {1.0, 1.5, 2.5},
            {1.0, 1.0, 1.0}
        };
        RealMatrix testMatrix = new Array2DRowRealMatrix(testData, false);
        new CholeskyDecompositionImpl(testMatrix);
    }

    /**
     * Tests non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        double[][] testData = {
            {1.0, 0.5, 0.5},
            {0.5, 2.0, 1.5},
            {0.5, 1.5, 2.5},
            {0.5, 1.5, 2.5},
        };
        RealMatrix testMatrix = new Array2DRowRealMatrix(testData, false);
        new CholeskyDecompositionImpl(testMatrix);
    }

    /**
     * Tests not positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        double[][] testData = {
            {1.0, 1.0, 1.0},
            {1.0, -1.0, 0.0},
            {1.0, 0.0, -1.0}
        };
        RealMatrix testMatrix = new Array2DRowRealMatrix(testData, false);
        new CholeskyDecompositionImpl(testMatrix);
    }

    /**
     * Helper method to compare two arrays.
     */
    private static void assertArrayEquals(double[] expected, double[] actual, double epsilon) {
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i], epsilon);
        }
    }

}