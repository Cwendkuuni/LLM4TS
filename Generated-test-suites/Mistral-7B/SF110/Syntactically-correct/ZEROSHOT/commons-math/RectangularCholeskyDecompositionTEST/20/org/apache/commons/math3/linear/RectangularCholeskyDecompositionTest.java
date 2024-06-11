package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrixFromArrays;
import static org.apache.commons.math3.linear.MatrixUtils.round;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = createRealMatrixFromArrays(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testDecomposition() {
        RealMatrix matrix = createRealMatrixFromArrays(new double[][]{{4.0, 1.0, 2.0}, {1.0, 5.0, 3.0}, {2.0, 3.0, 7.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        RealMatrix expectedRoot = createRealMatrixFromArrays(new double[][]{{1.0 / FastMath.sqrt(4.0), 0.0, 0.0},
                {-0.25 * FastMath.sqrt(2.0 / 5.0), 1.0 / FastMath.sqrt(5.0), 0.0},
                {-0.5 * FastMath.sqrt(2.0 / 7.0), -0.25 * FastMath.sqrt(2.0 / 5.0), 1.0 / FastMath.sqrt(7.0)}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        for (int i = 0; i < expectedRoot.getRowDimension(); i++) {
            for (int j = 0; j < expectedRoot.getColumnDimension(); j++) {
                double expected = expectedRoot.getEntry(i, j);
                double actual = actualRoot.getEntry(i, j);
                assertEquals("i=" + i + ", j=" + j, expected, actual, 1e-12);
            }
        }
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        assertEquals("Expected rank: " + expectedRank, expectedRank, actualRank);
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = createRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        RealMatrix expectedRoot = matrix;
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        for (int i = 0; i < expectedRoot.getRowDimension(); i++) {
            for (int j = 0; j < expectedRoot.getColumnDimension(); j++) {
                double expected = expectedRoot.getEntry(i, j);
                double actual = actualRoot.getEntry(i, j);
                assertEquals("i=" + i + ", j=" + j, expected, actual, 1e-14);
            }
        }
        int expectedRank = 2;
        int actualRank = decomposition.getRank();
        assertEquals("Expected rank: " + expectedRank, expectedRank, actualRank);
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = createRealMatrixFromArrays(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-12);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testRounding() {
        RealMatrix matrix = createRealMatrixFromArrays(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
        RealMatrix expectedRoot = createRealMatrixFromArrays(new double[][]{{0.447213602, 0.0}, {-0.447213602, 0.447213602}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals(expectedRoot.getRowDimension(), actualRoot.getRowDimension());
        assertEquals(expectedRoot.getColumnDimension(), actualRoot.getColumnDimension());
        for (int i = 0; i < expectedRoot.getRowDimension(); i++) {
            for (int j = 0; j < expectedRoot.getColumnDimension(); j++) {
                double expected = expectedRoot.getEntry(i, j);
                double actual = actualRoot.getEntry(i, j);
                assertEquals("i=" + i + ", j=" + j, expected, round(actual, 6), 1e-6);
            }
        }
        int expectedRank = 2;
        int actualRank = decomposition.getRank();
        assertEquals("Expected rank: " + expectedRank, expectedRank, actualRank);
    }
}