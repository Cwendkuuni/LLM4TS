package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Solver;

import org.junit.Test;

/**
 * Tests for {@link CholeskyDecompositionImpl} class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test of CholeskyDecompositionImpl constructor.
     * @throws Exception - any exception
     */
    @Test
    public void testCholeskyDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 2.5, 3.0},
            {2.0, 3.0, 4.0}
        });
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        // Basic functionality test
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(3);
        assertEquals(lt, l.transpose());
        RealMatrix product = l.multiply(lt);
        assertEquals(identity, product, 0.0);
        // Use case test
        RealVector b = new RealVectorImpl(new double[] {1.0, 2.0, 3.0}, false);
        RealVector x = decomposition.getSolver().solve(b);
        RealVector expected = new RealVectorImpl(new double[] {-0.3333333333333333,
                                                               1.0,
                                                               -0.6666666666666667},
                                                       false);
        assertArrayEquals(expected.getDataRef(), x.getDataRef(), 0.0);
        RealMatrix y = new Array2DRowRealMatrix(new double[][] {
            {4.0, 2.0},
            {2.0, 1.0}
        });
        RealMatrix expectedY = new Array2DRowRealMatrix(new double[][] {
            {3.0, 2.0},
            {0.0, 1.0}
        });
        RealMatrix xY = decomposition.getSolver().solve(y);
        assertEquals(expectedY, xY);
        // Edge case test
        matrix = new Array2DRowRealMatrix(new double[][] {
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 1.0},
            {1.0, 1.0, 0.0}
        });
        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // Expected
        }
        // Error case test
        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3).subtract(
                    new Array2DRowRealMatrix(new double[][] {
                        {-1.0, 0.0, 0.0},
                        {0.0, 0.0, 0.0},
                        {0.0, 0.0, -1.0}
                    })));
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

}