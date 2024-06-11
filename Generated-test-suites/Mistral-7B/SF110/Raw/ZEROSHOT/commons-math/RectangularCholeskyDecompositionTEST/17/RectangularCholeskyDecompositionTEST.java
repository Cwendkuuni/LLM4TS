package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Tests the constructor with a positive definite matrix.
     */
    @Test
    public void testPositiveDefinite() {
        final double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(3, rank);
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                    {0.0, 2.3166247688465676, 0.0},
                    {0.0, 0.0, 3.1622776601683796}});
            assertEquals(expectedRoot, rootMatrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Tests the constructor with a singular matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingular() {
        final double[][] matrixData = {
                {1.0, 2.0, 3.0, 4.0},
                {2.0, 5.0, 6.0, 7.0},
                {3.0, 6.0, 10.0, 11.0},
                {4.0, 7.0, 11.0, 14.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    /**
     * Tests the constructor with a non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefinite() {
        final double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, -1.0, 0.0},
                {3.0, 0.0, -1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix);
    }

    /**
     * Tests the constructor with a very small negative eigenvalue.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testVerySmallNegativeEigenvalue() {
        final double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 9.99999e-12}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix);
    }

    /**
     * Tests the constructor with a very large negative eigenvalue.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testVeryLargeNegativeEigenvalue() {
        final double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, -1e10}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix);
    }

    /**
     * Tests the rank.
     */
    @Test
    public void testRank() {
        final double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            int rank = decomposition.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}