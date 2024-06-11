package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.Epsilon;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(0, 0));
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(5);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(5, rank);
        for (int i = 0; i < rank; i++) {
            RealVector row = root.getColumnVector(i);
            assertTrue(Precision.isClose(row.getEntry(i), 1.0, Epsilon.CONTINUOUS_MACHINE_EPSILON));
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, Epsilon.MACHINE_EPSILON);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, -1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, Epsilon.MACHINE_EPSILON);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        });
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, Epsilon.MACHINE_EPSILON);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Expected positive definite matrix");
        }
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        for (int i = 0; i < rank; i++) {
            RealVector row = root.getColumnVector(i);
            for (int j = 0; j < rank; j++) {
                double entry = row.getEntry(j);
                if (i == j) {
                    assertTrue(Precision.isClose(entry, Precision.sqrt(matrix.getEntry(i, i)), Epsilon.CONTINUOUS_MACHINE_EPSILON));
                } else {
                    assertTrue(Precision.isClose(entry, 0.0, Epsilon.CONTINUOUS_MACHINE_EPSILON));
                }
            }
        }
    }
}