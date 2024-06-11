
package org.apache.commons.math3.linear;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.util.MatrixUtils;
import org.junit.jupiter.api.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithInvalidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}}); // Non-positive definite matrix
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmallArgument() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 5}, {3, 5, 6}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix rootMatrix = rcd.getRootMatrix();
        int rank = rcd.getRank();

        // Assertions for rootMatrix and rank
        assertArrayEquals(new double[][]{{1.0 / Math.sqrt(2.0), 0.0, 0.0},
                {-0.5 * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0), 0.0},
                {-0.5 * (1.0 / Math.sqrt(2.0)), -0.5 * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0)}},
                rootMatrix.getData(), 0.001);
        assertEquals(3, rank);

        // Edge cases
        RealMatrix smallMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 5}, {3, 5, 6 + 1e-308}});
        RectangularCholeskyDecomposition rcdSmall = new RectangularCholeskyDecomposition(smallMatrix, 0.0);
        RealMatrix rootMatrixSmall = rcdSmall.getRootMatrix();
        int rankSmall = rcdSmall.getRank();

        // Assertions for rootMatrix (small) and rank (small)
        assertArrayEquals(new double[][]{{1.0 / Math.sqrt(2.0), 0.0, 0.0},
                {-0.5 * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0), 0.0},
                {-0.5 * (1.0 / Math.sqrt(2.0)), -0.5 * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0)}},
                rootMatrixSmall.getData(), 0.001);
        assertEquals(3, rankSmall);

        // Error scenarios
        RealMatrix errorMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}, {3, 4}}); // Singular matrix
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(errorMatrix));

        RealMatrix errorMatrix2 = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}, {3, 4}, {4, 1}}); // Rectangular but not square matrix
        assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(errorMatrix2));
    }

    @Test
    public void testRectangularCholeskyDecompositionWithValidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 4, 5}, {3, 5, 6}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = rcd.getRootMatrix();
        int rank = rcd.getRank();

        // Assertions for rootMatrix and rank
        assertArrayEquals(new double[][]{{1.0 / Math.sqrt(2.0), 0.0, 0.0},
                {-0.5 * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0), 0.0},
                {-0.5 * (1.0 / Math.sqrt(2.0)), -0.5 * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0)}},
                rootMatrix.getData(), 0.001);
        assertEquals(3, rank);
    }
}
