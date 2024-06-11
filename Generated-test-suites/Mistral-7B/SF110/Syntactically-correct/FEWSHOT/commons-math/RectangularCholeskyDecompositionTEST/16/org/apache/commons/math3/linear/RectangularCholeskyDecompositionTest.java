package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        for (int i = 0; i < rank; ++i) {
            for (int j = 0; j < rank; ++j) {
                if (i == j) {
                    assertEquals(1.0, root.getEntry(i, j), Precision.SAFE_EPSILON);
                } else {
                    assertEquals(0.0, root.getEntry(i, j), Precision.SAFE_EPSILON);
                }
            }
        }
    }

    @Test
    public void testNegativeMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -3.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 0.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(decomposition);
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive definite matrix");
        }
        assertNotNull(decomposition);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        for (int i = 0; i < rank; ++i) {
            for (int j = 0; j < rank; ++j) {
                assertEquals(0.0, root.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
    }

    @Test
    public void testPositiveDefiniteMatrixWithSmallValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 1e-12}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive definite matrix");
        }
        assertNotNull(decomposition);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertEquals(3, root.getRowDimension());
        assertEquals(rank, root.getColumnDimension());
        for (int i = 0; i < rank; ++i) {
            for (int j = 0; j < rank; ++j) {
                if (i == j) {
                    assertEquals(1.0, root.getEntry(i, j), Precision.SAFE_EPSILON);
                } else {
                    assertEquals(0.0, root.getEntry(i, j), Precision.SAFE_EPSILON);
                }
            }
        }
    }
}