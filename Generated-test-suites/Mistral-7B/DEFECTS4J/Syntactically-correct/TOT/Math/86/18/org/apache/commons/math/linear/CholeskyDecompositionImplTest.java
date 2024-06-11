package org.apache.commons.math.linear.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the decomposition of a positive definite matrix.
     */
    @Test
    public void testDecompositionOfPositiveDefiniteMatrix() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        });

        try {
            final CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            final RealMatrix l = choleskyDecomposition.getL();
            final RealMatrix lt = choleskyDecomposition.getLT();
            final RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.7071067811865476, 0.4226185543080189, 0.0},
                    {-0.4226185543080189, 1.4142137413226387, 1.2247448713915894},
            });
            final RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 0.7071067811865476, -0.4226185543080189},
                    {0.7071067811865476, 2.416425356138346, 1.1547005383792593},
                    {-0.4226185543080189, 1.1547005383792593, 3.605675292994239},
            });
            assertEquals("The L matrix is not as expected.", expectedL, l);
            assertEquals("The LT matrix is not as expected.", expectedLT, lt);
            final DecompositionSolver solver = choleskyDecomposition.getSolver();
            final RealVector x = solver.solve(MatrixUtils.createColumnVector(new double[]{1.0, 2.0, 3.0}));
            assertEquals("The solution is not as expected.", new Array2DRowRealMatrix(new double[][]{
                    {-1.4161468365471423},
                    {0.8574266241758634},
                    {0.14285714285714285},
            }).getColumnVector(0), x, 1.0e-14);
        } catch (Exception e) {
            fail("An unexpected exception was thrown: " + e);
        }
    }

    /**
     * Test the decomposition of a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testDecompositionOfNonSquareMatrix() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0, 5.0},
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the decomposition of a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testDecompositionOfNonSymmetricMatrix() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 1.0},
            {0.0, 1.0, 1.0},
        });
        new CholeskyDecompositionImpl(matrix, 1.0e-13, 1.0e-13);
    }

    /**
     * Test the decomposition of a non-positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testDecompositionOfNonPositiveDefiniteMatrix() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, -1.0},
        });
        new CholeskyDecompositionImpl(matrix, 1.0e-13, 1.0e-13);
    }

}