package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the Cholesky decomposition constructor with a valid matrix
     * @throws Exception
     */
    @Test
    public void testCholeskyDecompositionWithValidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 2.0}
        });
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomp.getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][] {
            {Math.sqrt(2.0), 0.0},
            {0.5, Math.sqrt(2.0)}
        });
        Assert.assertEquals(expectedL, l);
        RealMatrix lt = decomp.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][] {
            {Math.sqrt(2.0), 0.5},
            {0.5, Math.sqrt(2.0)}
        });
        Assert.assertEquals(expectedLT, lt);
        RealMatrix inverse = decomp.getSolver().getInverse();
        RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][] {
            {0.5, -0.5},
            {-0.5, 0.5}
        });
        Assert.assertEquals(expectedInverse, inverse);
    }

    /**
     * Test the Cholesky decomposition constructor with a non-square matrix
     * @throws Exception
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testCholeskyDecompositionWithNonSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0},
            {1.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the Cholesky decomposition constructor with a not symmetric matrix
     * @throws Exception
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionWithNotSymmetricMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 3.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the Cholesky decomposition constructor with a not positive definite matrix
     * @throws Exception
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionWithNotPositiveDefiniteMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, -1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the solve method with a valid matrix
     * @throws Exception
     */
    @Test
    public void testSolveWithValidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {2.0, 1.0},
            {1.0, 2.0}
        });
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVectorImpl(new double[] {1.0, 2.0});
        RealVector x = decomp.getSolver().solve(b);
        double[] expected = {-0.3333333333333333, 0.6666666666666667};
        Assert.assertArrayEquals(expected, x.toArray(), 1.0e-10);
    }

    /**
     * Test the solve method with a singular matrix
     * @throws Exception
     */
    @Test(expected = InvalidMatrixException.class)
    public void testSolveWithSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {0.0, 1.0},
            {1.0, 0.0}
        });
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVectorImpl(new double[] {1.0, 2.0});
        decomp.getSolver().solve(b);
    }
}