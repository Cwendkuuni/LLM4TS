package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.special.Constant;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorThrowsExceptionWithInvalidMatrix() {
        try {
            new RectangularCholeskyDecomposition(Array2DRowRealMatrix.identity(3).subtract(Array2DRowRealMatrix.eye(3, 3).multiply(2)));
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithSmallValue() {
        final double small = Constant.EPSILON * 100;
        RealMatrix matrix = Array2DRowRealMatrix.eye(4, 4);
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        RealMatrix matrix = Array2DRowRealMatrix.eye(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(rank, rootMatrix.getColumnDimension());
        for (int i = 0; i < rank; ++i) {
            for (int j = 0; j < rank; ++j) {
                assertEquals(0.0, rootMatrix.getEntry(i, j), Constant.EPSILON);
                if (i != j) {
                    assertEquals(0.0, rootMatrix.getEntry(i, j), Constant.EPSILON);
                }
                assertEquals(1.0, rootMatrix.getEntry(i, i), Constant.EPSILON);
            }
        }
    }

    @Test
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = Array2DRowRealMatrix.eye(3).add(Array2DRowRealMatrix.eye(3).multiply(-1));
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithSingularMatrix() {
        RealMatrix matrix = Array2DRowRealMatrix.eye(3).add(Array2DRowRealMatrix.eye(3).multiply(-1).multiply(0.5));
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = Array2DRowRealMatrix.eye(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = Array2DRowRealMatrix.eye(3);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assertEquals(0.0, rootMatrix.getEntry(i, j), Constant.EPSILON);
                if (i != j) {
                    assertEquals(0.0, rootMatrix.getEntry(i, j), Constant.EPSILON);
                }
                assertEquals(1.0, rootMatrix.getEntry(i, i), Constant.EPSILON);
            }
        }
    }
}