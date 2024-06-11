
package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0.5, 0.5}, {0.5, 1, 0.5}, {0.5, 0.5, 1}});
        double small = 0.01;

        // When
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1, 0.5, 0.5}, {0.5, 1, 0.5}, {0, 0, 1}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        assertEquals(expectedRank, actualRank);

        // Additional test - check decomposition
        RealMatrix expectedDecomposed = new Array2DRowRealMatrix(new double[][]{{1, 0, 0.5}, {0.5, 1, 0}, {0.5, 0, 1}});
        RealMatrix actualDecomposed = MatrixUtils.multiply(decomposition.getRootMatrix(), decomposition.getRootMatrix().transpose());
        assertEquals(expectedDecomposed, actualDecomposed);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionWithSmallDiagonal() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0.1, 0.1}, {0.1, 1, 0.1}, {0.1, 0.1, 1}});
        double small = 0.01;

        // When
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then (should throw an exception)

    }

    @Test
    public void testNonSymmetricMatrix() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {2, 3}});
        double small = 0.01;

        // When
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // Then (expected exception)
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullRealMatrix() {
        RealMatrix matrix = null;
        double small = 0.01;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail();
        } catch (NullPointerException e) {
            // Then (expected exception)
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeSmallThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        double small = -0.01;

        try {
            new RectangularCholeskyDecomposition(matrix, small);
            fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // Then (expected exception)
        }
    }

    @Test
    public void testEdgeCaseWithSmallDiagonalAndLargeRowsColumns() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.00001, 0.0001, 0.001}, {0.0001, 0.01, 0.1}, {0.001, 0.1, 1}});
        double small = 0.0001;

        // When
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1, 0.0001, 0.001}, {0.0001, 1, 0.1}, {0, 0, 1}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        assertEquals(expectedRank, actualRank);
    }

    @Test
    public void testLargeMatrix() {
        // Given
        int rows = 100;
        int cols = 100;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[rows * cols][cols]);
        for (int i = 0; i < rows * cols; i++) {
            matrix.setEntry(i / cols, i % cols, Math.random());
        }

        double small = 0.001;

        // When
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[rows][decomposition.getRank()]);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < decomposition.getRank(); j++) {
                expectedRoot.setEntry(i, j, Math.sqrt(matrix.getEntry(i, i)) / Math.sqrt(small));
            }
        }
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
        int expectedRank = (int) Math.min(rows, cols);
        int actualRank = decomposition.getRank();
        assertEquals(expectedRank, actualRank);
    }

}
