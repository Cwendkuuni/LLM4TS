package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import org.apache.commons.math3.MatrixUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testCholeskyDecompositionPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 2.0}});

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            assertEquals(3, rank);
            assertEquals(3, rootMatrix.getRowDimension());
            assertEquals(rank, rootMatrix.getColumnDimension());

            // Verify the decomposed matrix
            for (int i = 0; i < rank; i++) {
                for (int j = 0; j < rank; j++) {
                    assertEquals(Precision.VERY_SMALL_DOUBLON, rootMatrix.getEntry(i, j), 0.0, 0.0);
                }
            }

            // Verify the remaining elements are zero
            for (int i = 0; i < 3; i++) {
                for (int j = rank; j < 3; j++) {
                    assertEquals(0.0, rootMatrix.getEntry(i, j), 0.0);
                }
            }

        } catch (NonPositiveDefiniteMatrixException e) {
            // This should not be thrown
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testCholeskyDecompositionSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertNull(decomposition.getRootMatrix());
            assertEquals(0, decomposition.getRank());

        } catch (NonPositiveDefiniteMatrixException e) {
            // This is expected
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, -1.0, 0.0}, {-1.0, 2.0, 1.0}, {0.0, 1.0, -1.0}});

        new RectangularCholeskyDecomposition(matrix);
    }
}