package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(3, rank);
            assertTrue(Precision.isClose(root.getEntry(0, 0), 1.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(1, 0), 0.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(2, 0), 0.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(0, 1), 2.0 / 3.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(1, 1), (5.0 - (2.0 * 2.0 / 3.0)) / 3.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(2, 1), (6.0 - (3.0 * 2.0 / 3.0)) / 3.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(0, 2), 3.0 / 3.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(1, 2), (6.0 - (2.0 * 2.0 / 3.0) - (3.0 * 2.0 / 3.0)) / 3.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(2, 2), (10.0 - (3.0 * 3.0 / 3.0) - (6.0 * 2.0 / 3.0) - (3.0 * 2.0 / 3.0)) / 3.0, 1e-10));
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, -1.0, 6.0},
                {3.0, 6.0, -10.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 0.0, 6.0},
                {3.0, 6.0, 0.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);

        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(2, rank);
            assertTrue(Precision.isClose(root.getEntry(0, 0), 1.0, 1e-10));
            assertTrue(Precision.isClose(root.getEntry(1, 1), 1.0, 1e-10));
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }
}