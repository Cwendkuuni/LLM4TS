package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testCholeskyDecompositionPositive() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertTrue(Precision.isClose(root.getEntry(0, 0), 1.4142135625, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(1, 0), 0.7071067811865575, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(2, 0), 1.0, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(0, 1), 0.7071067811865575, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(1, 1), 1.4142135625, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(2, 1), 0.7071067811865575, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(0, 2), 0.0, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(1, 2), -1.4142135625, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(2, 2), 1.7320508075688772, 1E-14));
    }

    @Test
    public void testCholeskyDecompositionSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 1.0E-8);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testCholeskyDecompositionSymmetric() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(3, rank);
        assertTrue(Precision.isClose(root.getEntry(0, 0), 1.4142135625, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(1, 0), 0.7071067811865575, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(1, 1), 1.4142135625, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(2, 0), 0.7071067811865575, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(2, 1), 0.7071067811865575, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(2, 2), 1.4142135625, 1E-14));
    }

    @Test
    public void testCholeskyDecompositionIdentity() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();
        assertEquals(2, rank);
        assertTrue(Precision.isClose(root.getEntry(0, 0), 1.0, 1E-14));
        assertTrue(Precision.isClose(root.getEntry(1, 1), 1.0, 1E-14));
    }
}