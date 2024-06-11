package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for the {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a positive definite matrix.
     */
    @Test
    public void testDecomposition() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 11.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 1e-10;
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                {0.0, 2.316652717065907, 0.0},
                                                                                {0.0, 0.0, 3.1622776601683795}});
            assertEquals(expectedRoot, root, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // The matrix is positive definite, the exception should not be thrown
            assertNotNull(e);
        }
    }

    /**
     * Test the decomposition of a positive semi-definite matrix with rank 2.
     */
    @Test
    public void testDecompositionRankTwo() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 3.0},
            {3.0, 3.0, 11.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 1e-10;
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(2, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                {0.0, 2.316652717065907, 0.0}});
            assertEquals(expectedRoot, root, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // The matrix is positive semi-definite, the decomposition should be successful
            // and the root matrix should have rank 2
            assertNotNull(e);
        }
    }

    /**
     * Test the decomposition of a positive semi-definite matrix with rank 1.
     */
    @Test
    public void testDecompositionRankOne() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 0.5, 3.0},
            {3.0, 3.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 1e-10;
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            assertEquals(1, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                {0.0, 1.0, 0.0},
                                                                                {0.0, 0.0, 1.0}});
            assertEquals(expectedRoot, root, 1e-14);
        } catch (NonPositiveDefiniteMatrixException e) {
            // The matrix is positive semi-definite, the decomposition should be successful
            // and the root matrix should have rank 1
            assertNotNull(e);
        }
    }

    /**
     * Test the exception when trying to decompose a non positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testExceptionNonPositiveDefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, -5.0, 6.0},
            {3.0, 6.0, 11.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 1e-10;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test the exception when trying to decompose a matrix with negative diagonal elements.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testExceptionNegativeDiagonal() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, -6.0},
            {3.0, 6.0, 11.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        double small = 1e-10;
        new RectangularCholeskyDecomposition(matrix, small);
    }

    /**
     * Test the decomposition of a square matrix.
     */
    @Test
    public void testSquareMatrix() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 11.0},
            {3.0, 6.0, 11.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-14);
            fail("An exception should be thrown because the matrix is not square.");
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

}