package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstruct() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3);
        double small = 0.01;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        // check the rank and root matrix
        assertEquals(rcd.getRank(), 3);
        assertTrue(rcd.getRootMatrix().isSymmetric());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3);
        double small = 0.01;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = MatrixUtils.createRealMatrix(3, 3).multiply(rcd.getRootMatrix()).transpose();
        RealMatrix actual = matrix.multiply(rcd.getRootMatrix().transpose());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3);
        double small = 0.01;
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(rcd.getRank(), 3);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSmallThreshold() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3);
        double small = 0.0;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(e.getDiagonalElement(), 0.0);
            assertEquals(e.getRow(), 0);
            assertEquals(e.getThreshold(), small);
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        double small = 0.01;
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 2.0);
        matrix.setEntry(1, 1, 3.0);
        try {
            new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testEmptyMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(0, 0);
        double small = 0.01;
        try {
            new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(2, 2);
        double small = 0.01;
        matrix.setEntry(0, 0, 0.0);
        matrix.setEntry(1, 1, 1.0);
        try {
            new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        double small = 0.01;
        matrix.setEntry(0, 0, -1.0);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(2, 2, 1.0);
        try {
            new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}