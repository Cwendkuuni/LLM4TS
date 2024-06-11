package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    /** Test matrix for positive definite case. */
    private static final double[][] TEST_MATRIX_PD = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 7.0},
            {3.0, 7.0, 10.0}
    };

    /** Test matrix for non-symmetric case. */
    private static final double[][] TEST_MATRIX_NS = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 7.0},
            {3.0, 7.0, 1.0}
    };

    /** Test matrix for non-positive definite case. */
    private static final double[][] TEST_MATRIX_NP = {
            {1.0, -1.0, 0.0},
            {-1.0, 1.0, 1.0},
            {0.0, 1.0, 1.0}
    };

    /** Test matrix for singular case. */
    private static final double[][] TEST_MATRIX_S = {
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0}
    };

    /** Test matrix for identity case. */
    private static final double[][] TEST_MATRIX_ID = {
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
    };

    /** Test vector for solving system A &times; X = B. */
    private static final double[] TEST_VECTOR_SOLVE = {1.0, 2.0, 3.0};

    /** Test matrix for solving system A &times; X = B. */
    private static final RealMatrix TEST_MATRIX_SOLVE = new Array2DRowRealMatrix(TEST_MATRIX_PD);

    /** Test vector for checking determinant. */
    private static final double[] TEST_VECTOR_DET = {1.0, 1.0, 1.0};

    /** Test matrix for checking determinant. */
    private RealMatrix testMatrix;

    /** Test Cholesky decomposition object. */
    private CholeskyDecompositionImpl choleskyDecomposition;

    /** Test solver object. */
    private CholeskyDecompositionImpl.Solver solver;

    /**
     * Initialize test objects.
     */
    @Before
    public void setUp() {
        testMatrix = new Array2DRowRealMatrix(TEST_MATRIX_PD);
        choleskyDecomposition = new CholeskyDecompositionImpl(testMatrix);
        solver = choleskyDecomposition.getSolver();
    }

    /**
     * Test positive definite matrix decomposition.
     */
    @Test
    public void testPositiveDefiniteDecomposition() {
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lT = choleskyDecomposition.getLT();
        SingularValueDecomposition svd = new SingularValueDecomposition(l);
        RealMatrix lProduct = l.multiply(lT);
        Assert.assertEquals(svd.getMatrixA(), lProduct.transpose());
    }

    /**
     * Test non-symmetric matrix decomposition.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricDecomposition() {
        RealMatrix nonSymmetricMatrix = new Array2DRowRealMatrix(TEST_MATRIX_NS);
        new CholeskyDecompositionImpl(nonSymmetricMatrix);
    }

    /**
     * Test non-positive definite matrix decomposition.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteDecomposition() {
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(TEST_MATRIX_NP));
    }

    /**
     * Test singular matrix decomposition.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularDecomposition() {
        new CholeskyDecompositionImpl(new Array2DRowRealMatrix(TEST_MATRIX_S));
    }

    /**
     * Test solving system A &times; X = B.
     */
    @Test
    public void testSolveSystem() {
        double[] result = solver.solve(TEST_VECTOR_SOLVE);
        RealVector expected = new RealVectorImpl(TEST_VECTOR_SOLVE.clone(), false);
        Assert.assertEquals(expected, new RealVectorImpl(result, false));
    }

    /**
     * Test checking determinant.
     */
    @Test
    public void testDeterminant() {
        double determinant = choleskyDecomposition.getDeterminant();
        Assert.assertEquals(determinant, 15.0, 1.0e-15);
    }
}