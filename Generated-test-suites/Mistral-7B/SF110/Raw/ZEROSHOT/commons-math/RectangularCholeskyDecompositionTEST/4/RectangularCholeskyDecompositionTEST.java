package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.MatrixUtils.eye;
import static org.apache.commons.math3.linear.RealMatrixUtils. roundedEquals;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionOfNonPositiveDefiniteMatrix() {
        RealMatrix matrix = createRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testDecompositionOfIdentityMatrix() {
        RealMatrix matrix = eye(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(matrix.getRowDimension(), root.getRowDimension());
        Assert.assertEquals(matrix.getColumnDimension(), root.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < decomposition.getRank(); j++) {
                assertEquals("Row " + i + " Column " + j, 0.0, root.getEntry(i, j), 1e-14);
            }
        }
    }

    @Test
    public void testDecompositionOfSingularMatrix() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        Assert.assertNull(decomposition);
    }

    @Test
    public void testDecompositionOfSymmetricMatrix() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        Assert.assertNull(decomposition);
    }

    @Test
    public void testDecompositionOfPositiveDefiniteMatrix() {
        RealMatrix matrix = createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
            fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix root = decomposition.getRootMatrix();

        Assert.assertEquals(matrix.getRowDimension(), root.getRowDimension());
        Assert.assertEquals(matrix.getColumnDimension(), root.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < decomposition.getRank(); j++) {
                assertEquals("Row " + i + " Column " + j, 0.0, root.getEntry(i, j), 1e-14);
                if (i == j) {
                    assertTrue("Row " + i + " Diagonal element is positive: " + root.getEntry(i, j), root.getEntry(i, j) > 0);
                }
            }
        }

        RealMatrix product = matrix.multiply(decomposition.getRootMatrix());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                double expected = matrix.getEntry(i, j);
                double actual = product.getEntry(i, j);
                if (i == j) {
                    // diagonal elements should be identical
                    assertEquals("Row " + i + " Column " + j, expected, actual, 1e-14);
                } else {
                    // off-diagonal elements should be zero
                    assertEquals("Row " + i + " Column " + j, 0.0, actual, 1e-14);
                }
            }
        }
    }

    @Test
    public void testDecompositionOfLargeMatrix() {
        int size = 1000;
        RealMatrix matrix = createRealMatrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.setEntry(i, j, FastMath.random());
            }
        }

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
            fail("Unexpected exception: " + e.getMessage());
        }

        RealMatrix root = decomposition.getRootMatrix();
        RealMatrix product = matrix.multiply(root);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < decomposition.getRank(); j++) {
                double actual = root.getEntry(i, j);
                double expected = 0.0;
                if (i == j) {
                    expected = FastMath.sqrt(matrix.getEntry(i, i));
                }
                assertEquals("Row " + i + " Column " + j, roundedEquals(expected, actual, 1e-14), actual);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double actual = product.getEntry(i, j);
                double expected = matrix.getEntry(i, j);
                if (i == j) {
                    assertTrue("Row " + i + " Diagonal element is identical: " + expected + " vs. " + actual, roundedEquals(expected, actual, 1e-14));
                } else {
                    assertEquals("Row " + i + " Column " + j, 0.0, actual, 1e-14);
                }
            }
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        Assert.assertNull(decomposition);

        matrix = createRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition2 = null;
        try {
            decomposition2 = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            // unexpected
            fail("Unexpected exception: " + e.getMessage());
        }
        int rank = decomposition2.getRank();
        assertEquals("Rank should be 3", 3, rank);
    }
}