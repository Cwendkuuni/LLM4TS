package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private RealMatrix testMatrix;
    private CholeskyDecompositionImpl choleskyDecomposition;

    @Before
    public void setUp() {
        double[][] data = {
                {1, 0.5, 1.5},
                {0.5, 1, 0.5},
                {1.5, 0.5, 2}
        };
        testMatrix = new Array2DRowRealMatrix(data, false);
        choleskyDecomposition = new CholeskyDecompositionImpl(testMatrix);
    }

    @Test
    public void testConstructorWithDefaultThresholds() {
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(testMatrix);
        // Test the getters here
    }

    @Test
    public void testConstructorWithCustomThresholds() {
        double relativeSymmetryThreshold = 1.0e-5;
        double absolutePositivityThreshold = 1.0e-15;
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(testMatrix, relativeSymmetryThreshold, absolutePositivityThreshold);
        // Test the getters here
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] nonSquareData = {
                {1, 0.5},
                {1.5, 2}
        };
        RealMatrix nonSquareMatrix = new Array2DRowRealMatrix(nonSquareData, false);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(nonSquareMatrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] notSymmetricData = {
                {1, 2},
                {1, 1}
        };
        RealMatrix notSymmetricMatrix = new Array2DRowRealMatrix(notSymmetricData, false);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(notSymmetricMatrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] notPositiveDefiniteData = {
                {-1, 0.5},
                {0.5, -1}
        };
        RealMatrix notPositiveDefiniteMatrix = new Array2DRowRealMatrix(notPositiveDefiniteData, false);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(notPositiveDefiniteMatrix);
    }

    @Test
    public void testGetL() {
        // Test the getL() method here
    }

    @Test
    public void testGetLT() {
        // Test the getLT() method here
    }

    @Test
    public void testGetDeterminant() {
        // Test the getDeterminant() method here
    }

    @Test
    public void testGetSolver() {
        // Test the getSolver() method here
        RealVector b = new RealVectorImpl(new double[]{1, 2, 3});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expected = new RealVectorImpl(new double[]{0.0628564575129846, -0.2422785281822193, 0.7816331217127061});
        assertEquals(expected, x);
    }

    @Test
    public void testLTEqualsIdentity() {
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(testMatrix.getRowDimension());
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix multiplied = lt.multiply(identity);
        RealMatrix expected = MatrixUtils.createRealIdentityMatrix(testMatrix.getRowDimension());
        assertEquals(expected, multiplied);
    }

    @Test
    public void testLTEqualsTransposeOfL() {
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix multiplied = l.transpose().multiply(l);
        RealMatrix expected = MatrixUtils.createRealIdentityMatrix(testMatrix.getRowDimension());
        assertEquals(expected, multiplied);
    }

    @Test
    public void testSolverWith3x1Vector() {
        RealVector b = new RealVectorImpl(new double[]{1, 1, 1});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expected = new RealVectorImpl(new double[]{0.4624234323604298, -0.1382305728784797, 0.670820327586083});
        assertEquals(expected, x);
    }

    @Test
    public void testSolverWith3x3Matrix() {
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        });
        RealMatrix x = choleskyDecomposition.getSolver().solve(b);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
                {1.3720275504752313, -0.2838510267147454, -0.5447220534294863},
                {-0.2838510267147454, 1.4747957346903697, -0.3762111832731217},
                {-0.5447220534294863, -0.3762111832731217, 1.821632634771593}
        });
        assertEquals(expected, x);
    }

    @Test
    public void testSolveIdentity() {
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(testMatrix.getRowDimension());
        RealMatrix x = choleskyDecomposition.getSolver().solve(identity);
        RealMatrix expected = identity;
        assertEquals(expected, x);
    }
}