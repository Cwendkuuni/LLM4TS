package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.special.SpecialFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a positive semi-definite matrix.
     * <p>This test checks that the method correctly computes the rectangular
     * Cholesky decomposition of a positive semi-definite matrix.</p>
     */
    @Test
    public void testPositiveSemiDefiniteDecomposition() {
        // create test matrix
        double[][] matrixData = {
                {2.0, 1.0, 0.5},
                {1.0, 2.0, 1.0},
                {0.5, 1.0, 1.5},
                {0.1, 0.1, 0.1}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        // perform the decomposition
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);

            // compare the results with the expected ones
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.4142135625, 0.7071067812, 0.3535533906},
                    {-0.7071067812, 1.4142135625, 0.7071067812},
                    {0.3535533906, -0.7071067812, 1.1615947552}}, false);
            RealMatrix expectedRootTranspose = RealMatrixUtils.transpose(expectedRoot);
            RealMatrix expectedResult = RealMatrixUtils.multiply(expectedRoot, expectedRootTranspose);
            RealMatrix result = RealMatrixUtils.multiply(decomposition.getRootMatrix(), decomposition.getRootMatrix());

            assertEquals(SpecialFunctions.MATRIX_EQUIVALENT, expectedResult, result);
            assertEquals(decomposition.getRank(), 3);

        } catch (NonPositiveDefiniteMatrixException e) {
            fail("The matrix should be positive semi-definite.");
        }
    }

    /**
     * Test the decomposition of a non-positive definite matrix.
     * <p>This test checks that the method throws an exception when given a non-positive definite matrix.</p>
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteDecomposition() {
        // create test matrix
        double[][] matrixData = {
                {-1.0, 1.0, 0.5},
                {1.0, -1.0, 1.0},
                {0.5, 1.0, -1.5},
                {0.1, 0.1, 0.1}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        // perform the decomposition
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("The matrix should not be positive semi-definite.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected exception
        }
    }

    /**
     * Test the decomposition of an empty matrix.
     * <p>This test checks that the method throws an exception when given an empty matrix.</p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDecomposition() {
        // create empty matrix
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(0);

        // perform the decomposition
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("The matrix should be non-empty.");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

}