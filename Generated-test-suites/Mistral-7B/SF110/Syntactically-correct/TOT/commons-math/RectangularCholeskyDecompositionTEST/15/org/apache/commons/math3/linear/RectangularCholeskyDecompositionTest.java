package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testRectangularCholeskyDecomposition() {
        // Positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(6.0, rootMatrix.getEntry(0, 0), 1e-12);
        // ...
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmallValue() {
        // Positive definite matrix with a small value
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-15);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        // Check that the decomposed matrix is the same as the original one up to a given precision
        for (int i = 0; i < rootMatrix.getRowDimension(); i++) {
            RealVector row = rootMatrix.getColumnVector(i);
            RealVector originalRow = matrix.getColumnVector(i);
            for (int j = 0; j < rootMatrix.getColumnDimension(); j++) {
                assertEquals(originalRow.getEntry(j), row.getEntry(j), 1e-15);
            }
        }
        // Check that the rank is correct
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        // Non-positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("NonPositiveDefiniteMatrixException expected");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    // Add any other test cases here
}