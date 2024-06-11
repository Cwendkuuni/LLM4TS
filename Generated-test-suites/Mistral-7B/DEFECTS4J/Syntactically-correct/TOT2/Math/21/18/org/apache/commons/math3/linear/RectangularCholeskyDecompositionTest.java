package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithValidInput() throws NonPositiveDefiniteMatrixException {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        double small = 0.1;

        // When
        RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        assertNotNull(rectangularCholeskyDecomposition);
        assertEquals(3, rectangularCholeskyDecomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNegativeDiagonalElements() throws NonPositiveDefiniteMatrixException {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, -1.0, 3.0}, {2.0, 3.0, 4.0}});
        double small = 0.1;

        // When & Then (Expected exception)
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        double small = 0.1;
        RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);

        // When
        RealMatrix rootMatrix = rectangularCholeskyDecomposition.getRootMatrix();

        // Then
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 4.0}});
        double small = 0.1;
        RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);

        // When
        int rank = rectangularCholeskyDecomposition.getRank();

        // Then
        assertEquals(3, rank);
    }

    // Add tests for the identified edge cases and exceptions here

}