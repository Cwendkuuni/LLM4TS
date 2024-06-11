package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link CholeskyDecompositionImpl}.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test Cholesky decomposition for a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 1.0},
                                                                                      {1.0, 2.0}});
        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = decomposition.getL();
            RealMatrix lt = decomposition.getLT();
            RealMatrix product = l.multiply(lt);
            assertEquals(MatrixUtils.createRealIdentityMatrix(2), product, 1e-9);
        } catch (NotPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Cholesky decomposition for a non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 1.0},
                                                                                      {1.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test Cholesky decomposition for a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetric() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0},
                                                                         {1.0, 2.0},
                                                                         {0.0, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test Cholesky decomposition with threshold values.
     */
    @Test
    public void testThresholdValues() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0e-1, 1.0e-1},
                                                                                      {1.0e-1, 1.0e-2}});
        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix,
                                                                                     1.0e-14, 1.0e-12);
            RealMatrix l = decomposition.getL();
            RealMatrix lt = decomposition.getLT();
            RealMatrix product = l.multiply(lt);
            assertEquals(MatrixUtils.createRealIdentityMatrix(2), product, 1e-13);
        } catch (NotPositiveDefiniteMatrixException | NotSymmetricMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test solving a linear equation using Cholesky decomposition.
     */
    @Test
    public void testSolve() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{3.0, 2.0},
                                                                                      {2.0, 1.0}});
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0}, false);
        RealVector x = new CholeskyDecompositionImpl(matrix).getSolver().solve(b);
        RealVector expected = new RealVectorImpl(new double[]{2.0 / 3.0, 1.0 / 3.0}, false);
        assertEquals(expected, x, 1e-9);
    }

    /**
     * Test singular value decomposition for comparison.
     */
    @Test
    public void testSingularValueDecomposition() throws SingularValueDecompositionException {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{3.0, 2.0},
                                                                                      {2.0, 1.0}});
        SingularValueDecomposition svd = matrix.getSingularValueDecomposition();
        RealMatrix l = svd.getL();
        RealMatrix lt = svd.getLt();
        RealMatrix product = l.multiply(lt);
        assertEquals(MatrixUtils.createRealIdentityMatrix(2), product, 1e-9);
    }

}