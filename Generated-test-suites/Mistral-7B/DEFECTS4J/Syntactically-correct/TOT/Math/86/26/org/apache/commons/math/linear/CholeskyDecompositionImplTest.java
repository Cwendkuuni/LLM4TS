package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CholeskyDecompositionImpl.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the constructor with a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        double[][] matrixData = new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        assertEquals(l.getRowDimension(), lt.getRowDimension());
        assertEquals(l.getColumnDimension(), lt.getColumnDimension());

        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[] {1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertEquals(b, l.operate(x));

        RealMatrix expected = new Array2DRowRealMatrix(
                new double[][] {{1.0 / Math.sqrt(1.0), 0.0, 0.0},
                                {0.5, Math.sqrt(5.0)/Math.sqrt(5.0), 0.0},
                                {0.0, 0.5, Math.sqrt(10.0)/Math.sqrt(10.0)}});
        assertArrayEquals(expected.getData(), l.getData(), 0.0);
    }

    /**
     * Test the constructor with a non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        double[][] matrixData = new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 0.0, 6.0},
            {3.0, 6.0, 10.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetric() {
        double[][] matrixData = new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 1.0, 6.0},
            {3.0, 6.0, 10.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-10);
    }

    /**
     * Test the constructor with a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquare() {
        double[][] matrixData = new double[][] {
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the determinant method.
     */
    @Test
    public void testDeterminant() {
        double[][] matrixData = new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        assertEquals(1.0 * 5.0 * 10.0, choleskyDecomposition.getDeterminant(), 1.0e-14);
    }

}