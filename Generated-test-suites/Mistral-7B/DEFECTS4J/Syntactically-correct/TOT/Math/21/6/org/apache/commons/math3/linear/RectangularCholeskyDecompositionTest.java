package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link RectangularCholeskyDecomposition} class.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with positive definite symmetric matrix.
     */
    @Test
    public void testDecompositionPositiveDefinite() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 4.0},
            {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        RectangularCholeskyDecomposition decomposition;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown for positive definite matrix.");
            return;
        }

        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(3, rank);
        assertArrayEquals(new double[][]{
                {1.0 / Math.sqrt(2.0), 0.0, -0.5 * Math.sqrt(2.0 / 2.0)},
                {0.0, 1.0 / Math.sqrt(5.0), 0.3},
                {-0.5 * Math.sqrt(2.0 / 2.0), 0.3, 1.0 / Math.sqrt(6.0)}},
            root.getData(), 0.001);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with positive semidefinite matrix.
     */
    @Test
    public void testDecompositionPositiveSemidefinite() {
        double[][] data = {
            {1.0, 0.1, 3.0},
            {0.1, 1.0, 4.0},
            {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        RectangularCholeskyDecomposition decomposition;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected result for positive semidefinite matrix
            return;
        }

        fail("Exception should be thrown for positive semidefinite matrix.");
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#RectangularCholeskyDecomposition(RealMatrix, double)}
     * with non-positive definite matrix.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionNonPositiveDefinite() {
        double[][] data = {
            {1.0, -0.1, 3.0},
            {-0.1, 1.0, 4.0},
            {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        new RectangularCholeskyDecomposition(matrix, 1e-12);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRank()}
     * with positive definite matrix.
     */
    @Test
    public void testRankPositiveDefinite() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 4.0},
            {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        RectangularCholeskyDecomposition decomposition;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown for positive definite matrix.");
            return;
        }

        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRank()}
     * with positive semidefinite matrix.
     */
    @Test
    public void testRankPositiveSemidefinite() {
        double[][] data = {
            {1.0, 0.1, 3.0},
            {0.1, 1.0, 4.0},
            {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        RectangularCholeskyDecomposition decomposition;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected result for positive semidefinite matrix
            return;
        }

        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRootMatrix()}
     * with positive definite matrix.
     */
    @Test
    public void testRootMatrixPositiveDefinite() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 4.0},
            {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        RectangularCholeskyDecomposition decomposition;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown for positive definite matrix.");
            return;
        }

        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(3, rank);
        assertArrayEquals(new double[][]{
                {1.0 / Math.sqrt(2.0), 0.0, -0.5 * Math.sqrt(2.0 / 2.0)},
                {0.0, 1.0 / Math.sqrt(5.0), 0.3},
                {-0.5 * Math.sqrt(2.0 / 2.0), 0.3, 1.0 / Math.sqrt(6.0)}},
            root.getData(), 0.001);
    }

    /**
     * Test method for {@link RectangularCholeskyDecomposition#getRootMatrix()}
     * with positive semidefinite matrix.
     */
    @Test
    public void testRootMatrixPositiveSemidefinite() {
        double[][] data = {
            {1.0, 0.1, 3.0},
            {0.1, 1.0, 4.0},
            {3.0, 4.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        RectangularCholeskyDecomposition decomposition;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected result for positive semidefinite matrix
            return;
        }

        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        assertEquals(2, rank);
        assertArrayEquals(new double[][]{
                {1.0 / Math.sqrt(1.01), 0.0},
                {0.0, 1.0 / Math.sqrt(5.0)},
                {-0.5 * Math.sqrt(1.01 / 2.0), 0.3}}
                , root.getData(), 0.001);
    }
}