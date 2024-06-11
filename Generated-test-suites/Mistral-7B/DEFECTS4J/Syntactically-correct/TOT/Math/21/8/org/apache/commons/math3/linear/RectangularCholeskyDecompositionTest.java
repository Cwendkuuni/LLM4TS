package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test the decomposition of a positive definite matrix.
     * @throws NonPositiveDefiniteMatrixException if the matrix is not positive semidefinite.
     */
    @Test
    public void testPositiveDefinite() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = { {1.0, 2.0, 3.0}, {2.0, 5.0, 4.0}, {3.0, 4.0, 9.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 1e-12);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix expectedRoot = RealMatrixUtils.createColumnRealMatrix(new double[]{1.0, 1.0, 1.0}, false);
        RealMatrix expectedRootTranspose = RealMatrixUtils.createColumnRealMatrix(new double[]{1.0, 2.0, 3.0}, false);
        RealMatrix expectedProduct = RealMatrixUtils.multiply(root, expectedRootTranspose);
        RealMatrix actualProduct = RealMatrixUtils.multiply(root, expectedRootTranspose);
        Assert.assertEquals("Failed to decompose positive definite matrix", expectedProduct, actualProduct, 1e-12);
    }

    /**
     * Test the decomposition of a positive semidefinite matrix.
     * @throws NonPositiveDefiniteMatrixException if the matrix is not positive semidefinite.
     */
    @Test
    public void testPositiveSemidefinite() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = { {1.0, 2.0, 0.0}, {2.0, 5.0, 4.0}, {0.0, 4.0, 9.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 1e-12);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix expectedRoot = RealMatrixUtils.createColumnRealMatrix(new double[]{1.0, 1.0, 0.0}, false);
        RealMatrix expectedRootTranspose = RealMatrixUtils.createColumnRealMatrix(new double[]{1.0, 1.0, 1.0}, false);
        RealMatrix expectedProduct = RealMatrixUtils.multiply(root, expectedRootTranspose);
        RealMatrix actualProduct = RealMatrixUtils.multiply(root, expectedRootTranspose);
        Assert.assertEquals("Failed to decompose positive semidefinite matrix", expectedProduct, actualProduct, 1e-12);
    }

    /**
     * Test the decomposition of a singular matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingular() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = { {1.0, 2.0, 0.0}, {2.0, 0.0, 4.0}, {0.0, 4.0, 0.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    /**
     * Test the decomposition of a negative definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeDefinite() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = { {1.0, 2.0, 3.0}, {2.0, -5.0, 4.0}, {3.0, 4.0, -9.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    /**
     * Test the decomposition of a matrix with small diagonal elements.
     */
    @Test
    public void testSmallDiagonal() {
        double[][] matrixData = { {1.0, 2.0, 0.0}, {2.0, 0.5, 4.0}, {0.0, 4.0, 0.5} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.1);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    /**
     * Test the decomposition of a matrix with negative off-diagonal elements.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeOffDiagonal() throws NonPositiveDefiniteMatrixException {
        double[][] matrixData = { {1.0, 2.0, 0.0}, {2.0, 5.0, -4.0}, {0.0, 4.0, 9.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

}