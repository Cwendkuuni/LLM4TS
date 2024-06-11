package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test decomposition of a simple 2x2 matrix.
     */
    @Test
    public void testDecomposeSimple() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
            Assert.assertEquals(expected, decomp.getRootMatrix());
            Assert.assertEquals(2, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // this test should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test decomposition of a 2x2 matrix with a negative diagonal element.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecomposeNegativeDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, -1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test decomposition of a 3x3 matrix with a small diagonal element.
     */
    @Test
    public void testDecomposeSmallDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.1}, {0.5, 1.0, 0.5}, {0.1, 0.5, 1.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.1}, {0.5, 1.0, 0.5}, {0.1, 0.0, 1.0}});
            Assert.assertEquals(expected, decomp.getRootMatrix(), 1e-12);
            Assert.assertEquals(3, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // this test should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test decomposition of a non-symmetric matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecomposeNonSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {-0.5, 1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test decomposition of a singular matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecomposeSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    /**
     * Test getRank method with a simple 2x2 matrix.
     */
    @Test
    public void testGetRankSimple() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            Assert.assertEquals(2, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // this test should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getRank method with a 2x2 matrix with a negative diagonal element.
     */
    @Test
    public void testGetRankNegativeDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, -1.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            Assert.assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // this test should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getRank method with a 2x2 matrix with a small diagonal element.
     */
    @Test
    public void testGetRankSmallDiagonal() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0e-10}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            Assert.assertEquals(2, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // this test should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getRank method with a non-symmetric matrix.
     */
    @Test
    public void testGetRankNonSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {-0.5, 1.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            Assert.assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // this test should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getRank method with a singular matrix.
     */
    @Test
    public void testGetRankSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            Assert.assertEquals(0, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            // this test should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}