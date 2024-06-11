package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.apache.commons.math3.MatrixUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompositionPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 9.0}
        });

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomposition.getRootMatrix();

        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());

        assertEquals(0.0, Precision.doubleValue(rootMatrix.getEntry(0, 0), 1e-13), 0.0);
        assertEquals(1.4142135625, Precision.doubleValue(rootMatrix.getEntry(1, 1), 1e-13), 1.4142135625);
        assertEquals(1.0 / 1.4142135625, Precision.doubleValue(rootMatrix.getEntry(1, 0), 1e-13), 1.0 / 1.4142135625);
        assertEquals(0.7071067811865475244, Precision.doubleValue(rootMatrix.getEntry(2, 1), 1e-13), 0.7071067811865475244);
        assertEquals(1.0 / 0.7071067811865475244, Precision.doubleValue(rootMatrix.getEntry(2, 2), 1e-13), 1.0 / 0.7071067811865475244);

        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 2.0, 3.0},
                {2.0, -5.0, 6.0},
                {3.0, 6.0, -9.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Should have thrown NonPositiveDefiniteMatrixException.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSmallEigenvalues() {
        double small = 1e-15;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 6.0},
                {3.0, 6.0, 1.0}
        });

        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, small);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Ignore
        }

        assertNull(decomposition);
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(5);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(5, rootMatrix.getRowDimension());
        assertEquals(5, rootMatrix.getColumnDimension());

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(0.0, PrecisionUtils.isClose(rootMatrix.getEntry(i, j), 1.0, 1e-15), 0.0);
            }
        }
    }
}