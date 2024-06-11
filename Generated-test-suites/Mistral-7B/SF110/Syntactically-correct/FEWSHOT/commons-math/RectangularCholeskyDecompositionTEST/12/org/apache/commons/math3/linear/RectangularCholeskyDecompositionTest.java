package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();

            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());

            // Check that the root matrix is correct
            RealMatrix expected = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 1.0, 0.0}, {0.11764705882353, 0.32601136669253, 1.0}});
            assertMatrixEquals(expected, root, 1e-13);

        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Matrix should be positive definite");
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionSingular() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 0.0}});

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-12);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionNonSquare() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testRectangularCholeskyDecompositionIdentity() {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(4);

        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();

            assertEquals(4, rank);
            assertEquals(4, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());

            // Check that the root matrix is correct
            RealMatrix expected = RealMatrixUtils.createIdentityMatrix(4);
            assertMatrixEquals(expected, root, 1e-13);

        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Matrix should be positive definite");
        }
    }

    private void assertMatrixEquals(RealMatrix expected, RealMatrix actual, double tolerance) {
        for (int i = 0; i < expected.getRowDimension(); i++) {
            for (int j = 0; j < expected.getColumnDimension(); j++) {
                double expectedEntry = expected.getEntry(i, j);
                double actualEntry = actual.getEntry(i, j);
                assertEquals(expectedEntry, actualEntry, tolerance);
            }
        }
    }
}