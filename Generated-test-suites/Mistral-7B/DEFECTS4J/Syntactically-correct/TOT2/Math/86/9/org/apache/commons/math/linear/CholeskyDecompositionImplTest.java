package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.special.Epsilon;
import org.junit.Test;

/**
 * Test CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test CholeskyDecompositionImpl constructor with default threshold values.
     */
    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.7071067811865476, 0.0}, {0.4226180688022161, 0.4226180688022161, 1.0}});
        RealMatrix expectedLT = expectedL.transpose();
        assertEquals("L decomposition not correct", expectedL, l);
        assertEquals("LT decomposition not correct", expectedLT, lt);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with custom threshold values.
     */
    @Test
    public void testCustomConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0e-3, 1.0e-3, 0.0}, {1.0e-3, 2.0e-3, 1.0e-3}, {0.0, 1.0e-3, 3.0e-3}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-14);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.9999999999999998, 1.0, 0.0}, {0.9999999999999996, 0.9999999999999996, 1.0}});
        RealMatrix expectedLT = expectedL.transpose();
        assertEquals("L decomposition not correct", expectedL, l);
        assertEquals("LT decomposition not correct", expectedLT, lt);
    }

    /**
     * Test CholeskyDecompositionImpl with non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl with not symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
        double[][] data = matrix.getData();
        data[1][1] += Epsilon.MACHINE_EPSILON;
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(data));
    }

    /**
     * Test CholeskyDecompositionImpl with not positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
    }

    /**
     * Test getL method.
     */
    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
        RealMatrix l = cholesky.getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.7071067811865476, 0.0}, {0.4226180688022161, 0.4226180688022161, 1.0}});
        assertEquals("L decomposition not correct", expectedL, l);
    }

    /**
     * Test getLT method.
     */
    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.7071067811865476, 0.4226180688022161}, {0.0, 0.7071067811865476, 0.4226180688022161}, {0.0, 0.0, 1.0}});
        assertEquals("LT decomposition not correct", expectedLT, lt);
    }

    /**
     * Test getDeterminant method.
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
        double determinant = cholesky.getDeterminant();
        double expectedDeterminant = 1.0 / Math.sqrt(2.0) * Math.sqrt(3.0);
        assertEquals("Determinant not correct", expectedDeterminant, determinant, 1e-12);
    }

    /**
     * Test getSolver method.
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-15);
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        RealVector solution = cholesky.getSolver().solve(b);
        RealVector expectedSolution = new ArrayRealVector(new double[]{0.3826834323650895, 0.3826834323650895, 0.0});
        assertEquals("Solution not correct", expectedSolution, solution);
    }
}