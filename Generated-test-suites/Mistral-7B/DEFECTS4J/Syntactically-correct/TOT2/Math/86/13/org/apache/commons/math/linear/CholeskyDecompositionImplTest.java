package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix expectedLT = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix lt = decomposition.getLT();
        assertEquals("L is not equal to LT transpose", expectedLT, lt.transpose());
        assertEquals("Determinant should be 1", 1.0, decomposition.getDeterminant(), 1e-12);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4, 5}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1, 0.1}, {0.1, 1}});
        matrix.setEntry(1, 1, matrix.getEntry(1, 1) + 0.1);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        assertNotSame("L is equal to LT transpose", expectedLT, lt.transpose());
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix expectedLT = MatrixUtils.createRealIdentityMatrix(3);
        matrix.setEntry(1, 1, matrix.getEntry(1, 1) - 1.0);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testSolveVector() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        RealVector b = new RealVectorImpl(new double[]{1, 2, 3}, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector x = decomposition.getSolver().solve(b);
        assertArrayEquals("Solution does not match expected", new double[]{1, 1, 1}, x.toArray(), 1e-13);
    }

    @Test
    public void testSolveMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix b = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix x = decomposition.getSolver().solve(b);
        assertEquals("Solutions do not match expected", matrix, x);
    }

    @Test
    public void testSolveSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        try {
            RealMatrix x = decomposition.getSolver().solve(MatrixUtils.createRealIdentityMatrix(2));
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSolveLargeMatrix() {
        int size = 100;
        RealMatrix matrix = MatrixUtils.createRealRandomMatrix(size, size);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix x = decomposition.getSolver().solve(MatrixUtils.createRealIdentityMatrix(size));
        double tolerance = 1e-6;
        for (int i = 0; i < size; i++) {
            assertEquals("Solution does not match expected", Precision.round(1.0 / matrix.getEntry(i, i), tolerance), x.getEntry(i, 0), tolerance);
        }
    }

}