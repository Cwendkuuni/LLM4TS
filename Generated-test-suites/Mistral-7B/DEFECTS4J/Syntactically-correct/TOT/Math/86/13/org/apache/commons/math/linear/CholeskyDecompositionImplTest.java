package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    /**
     * Test CholeskyDecompositionImpl with positive definite matrix
     */
    @Test
    public void testCholeskyDecompositionPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 4, 6},
                {2, 6, 10}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0},
                {0.4472, 0.8944, 0},
                {0.2708, 1.7321, 2.5814}
        });
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0},
                {0.4472, 0.8944, 0},
                {0.2708, 1.7321, 2.5814}
        });

        assertEquals("L is not equal to the expected L", expectedL, l);
        assertEquals("LT is not equal to the expected LT", expectedLT, lt);

        RealVector b = new RealVectorImpl(new double[]{1, 2, 2});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{1, -0.4472, -0.2708});
        assertEquals("Solution is not equal to the expected solution", expectedX, x);

        RealMatrix A = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 4, 6},
                {2, 6, 10}
        });
        RealMatrix expectedA = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 4, 6},
                {2, 6, 10}
        });
        RealMatrix X = new Array2DRowRealMatrix(new double[][]{
                {1, -0.4472, -0.2708},
                {-0.4472, 1.3865, -1.1714},
                {-0.2708, -1.1714, 1.8214}
        });
        RealMatrix AX = A.operate(X);

        assertEquals("AX is not equal to the identity matrix", MatrixUtils.createRealIdentityMatrix(3), AX);
    }

    /**
     * Test CholeskyDecompositionImpl with singular matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 2},
                {2, 4, -6},
                {2, 6, 10}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl with non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testCholeskyDecompositionNonSquare() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2},
                {2, 4}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl with non-symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionNonSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2},
                {2, 1}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl with low relative symmetry threshold
     */
    @Test
    public void testCholeskyDecompositionLowRelativeSymmetryThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1.00000001e-16, 2},
                {1.00000001e-16, 1, 2},
                {2, 2, 4}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-20);

        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0},
                {0.4999999876171875, 0.999999962158223, 0},
                {0.4999999876171875, 0.999999962158223, 2}
        });
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0},
                {0.4999999876171875, 0.999999962158223, 0},
                {0.4999999876171875, 0.999999962158223, 2}
        });

        assertEquals("L is not equal to the expected L", expectedL, l, 1e-12);
        assertEquals("LT is not equal to the expected LT", expectedLT, lt, 1e-12);

        RealVector b = new RealVectorImpl(new double[]{1, 1, 1});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{1, 1, 1});
        assertEquals("Solution is not equal to the expected solution", expectedX, x, 1e-12);

        RealMatrix A = new Array2DRowRealMatrix(new double[][]{
                {1, 1.00000001e-16, 2},
                {1.00000001e-16, 1, 2},
                {2, 2, 4}
        });
        RealMatrix expectedA = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix X = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        });
        RealMatrix AX = A.operate(X);

        assertEquals("AX is not equal to the identity matrix", expectedA, AX, 1e-12);
    }

    /**
     * Test CholeskyDecompositionImpl with high relative symmetry threshold
     */
    @Test
    public void testCholeskyDecompositionHighRelativeSymmetryThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1, 1e-15, 2},
                {1e-15, 1, 2},
                {2, 2, 4}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-8);

        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0},
                {0.9999999999999996, 0.9999999999999997, 0},
                {0.9999999999999996, 0.9999999999999997, 2}
        });
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0},
                {0.9999999999999996, 0.9999999999999997, 0},
                {0.9999999999999996, 0.9999999999999997, 2}
        });

        assertEquals("L is not equal to the expected L", expectedL, l, 1e-10);
        assertEquals("LT is not equal to the expected LT", expectedLT, lt, 1e-10);

        RealVector b = new RealVectorImpl(new double[]{1, 1, 1});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{1, 1, 1});
        assertEquals("Solution is not equal to the expected solution", expectedX, x, 1e-10);

        RealMatrix A = new Array2DRowRealMatrix(new double[][]{
                {1, 1e-15, 2},
                {1e-15, 1, 2},
                {2, 2, 4}
        });
        RealMatrix expectedA = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix X = new Array2DRowRealMatrix(new double[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        });
        RealMatrix AX = A.operate(X);

        assertEquals("AX is not equal to the identity matrix", expectedA, AX, 1e-10);
    }

}