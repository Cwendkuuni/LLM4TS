package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompositionPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix result = decomposition.getRootMatrix();

        // Check the result
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(1.0, result.getEntry(0, 0), 1e-13);
        assertEquals(2.0, result.getEntry(0, 1), 1e-13);
        assertEquals(1.0, result.getEntry(0, 2), 1e-13);
        assertEquals(-2.0, result.getEntry(1, 0), 1e-13);
        assertEquals(-1.0, result.getEntry(1, 1), 1e-13);
        assertEquals(-1.5, result.getEntry(1, 2), 1e-13);
        assertEquals(-1.5, result.getEntry(2, 1), 1e-13);
        assertEquals(-3.0, result.getEntry(2, 2), 1e-13);

        // Check the rank
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testDecompositionSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 3.0}, {3.0, 3.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testDecompositionSingularMatrixWithSmallValue() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 3.0}, {3.0, 3.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-15);
            fail("Should throw NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testDecompositionDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 5.0, 0.0}, {0.0, 0.0, 10.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix result = decomposition.getRootMatrix();

        // Check the result
        assertEquals(3, result.getRowDimension());
        assertEquals(3, result.getColumnDimension());
        assertEquals(1.0, result.getEntry(0, 0), 1e-13);
        assertEquals(0.0, result.getEntry(1, 0), 1e-13);
        assertEquals(0.0, result.getEntry(2, 0), 1e-13);
        assertEquals(0.0, result.getEntry(0, 1), 1e-13);
        assertEquals(2.2360679774997897d, result.getEntry(1, 1), 1e-13);
        assertEquals(0.0, result.getEntry(2, 1), 1e-13);
        assertEquals(0.0, result.getEntry(0, 2), 1e-13);
        assertEquals(0.0, result.getEntry(1, 2), 1e-13);
        assertEquals(3.1622776601683795d, result.getEntry(2, 2), 1e-13);

        // Check the rank
        assertEquals(3, decomposition.getRank());
    }
}