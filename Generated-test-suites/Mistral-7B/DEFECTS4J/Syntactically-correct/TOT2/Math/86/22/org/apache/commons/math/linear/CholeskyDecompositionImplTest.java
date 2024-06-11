package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.junit.Test;

/**
 * Test for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the default constructor
     */
    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        });

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0, 0.0},
                {1.4142135625, 1.0, 0.0},
                {1.73205080757, 1.4142135625, 1.0}
        });
        RealMatrix actualL = choleskyDecomposition.getL();
        MatrixUtils.assertEquals(expectedL, actualL);

        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][] {
                {1.0, 1.4142135625, 1.73205080757},
                {0.0, 1.0, 1.4142135625},
                {0.0, 0.0, 1.0}
        });
        RealMatrix actualLT = choleskyDecomposition.getLT();
        MatrixUtils.assertEquals(expectedLT, actualLT);

        assertTrue(choleskyDecomposition.getDeterminant() == 11.0);
        Solver solver = choleskyDecomposition.getSolver();
        double[] b = new double[] {1.0, 2.0, 3.0};
        RealVector solution = solver.solve(new ArrayRealVector(b));
        assertArrayEquals(new double[] {-0.322552879537368, 0.655111739054812, 1.0}, solution.toArray(), 0.0);
    }

    /**
     * Test the constructor with a matrix that is not symmetric
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0},
                {3.0, 6.0, 9.0}
        });

        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a matrix that is not positive definite
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.0, 0.0},
                {0.0, -1.0, 0.0},
                {0.0, 0.0, -1.0}
        });

        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 2.0},
                {3.0, 4.0}
        });

        new CholeskyDecompositionImpl(matrix);
    }

}